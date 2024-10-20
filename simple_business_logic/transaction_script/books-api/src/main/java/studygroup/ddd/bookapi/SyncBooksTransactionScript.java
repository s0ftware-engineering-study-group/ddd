package studygroup.ddd.bookapi;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Service
@Slf4j
public class SyncBooksTransactionScript {

    private final String externalApiUrl;
    // I'm not sure if a JPA repository can be considered a thin database wrapper, but I'll consider it as such.
    private final BookRepository bookRepository;
    private final SyncHistoryRepository syncHistoryRepository;

    public SyncBooksTransactionScript(
            @Value("${external.api.url}") String externalApiUrl,
            BookRepository bookRepository,
            SyncHistoryRepository syncHistoryRepository
    ) {
        this.externalApiUrl = externalApiUrl;
        this.bookRepository = bookRepository;
        this.syncHistoryRepository = syncHistoryRepository;
    }

    /*
        Single procedure that implements everything from fetching the books to persisting to the database.
        I chose to not extract methods and/or classes to make nature of the transaction script more explicit.

        The @Transactional annotation below ensures the transactional behavior for the script.
     */
    @Transactional
    public void syncBooks() {
        var restTemplate = new RestTemplate();
        var booksUrl = externalApiUrl + "/books";

        log.info("Fetching books from external API: {}", booksUrl);

        ResponseEntity<ExternalApiBookResponse[]> response = restTemplate.getForEntity(
                booksUrl, ExternalApiBookResponse[].class);

        if (!response.hasBody()) {
            return;
        }

        var dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        var parsedBooks = Arrays.stream(response.getBody()).map(externalBook -> {
            try {
                return BookEntity.builder()
                        .authorName(externalBook.getAuthor())
                        .title(externalBook.getTitle().toLowerCase())
                        .publisherName(externalBook.getPublisher())
                        .publishedAt(dateFormat.parse(externalBook.getPublishedAt()))
                        .build();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        log.debug("Fetched {} books from external API", parsedBooks.size());

        parsedBooks.forEach(book -> {
            // This validation grants idempotency to the transaction script
            var existingBook = bookRepository.findByTitle(book.getTitle());
            if (existingBook.isEmpty()) {
                log.info("Saving book: {}", book);
                bookRepository.save(book);
            }
        });

        syncHistoryRepository.save(SyncHistoryEntity.builder()
                .numberOfBooks(parsedBooks.size())
                .syncAt(new Date())
                .build());
        log.debug("Sync finished successfully");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class ExternalApiBookResponse {
        private long id;
        private String title;
        private String author;
        private String publisher;
        private String publishedAt;
    }
}
