package studygroup.ddd.bookapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class BooksController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SyncBooksTransactionScript syncBooksTransactionScript;

    @PostMapping("/books/sync")
    public void syncBooks() {
        syncBooksTransactionScript.syncBooks();
    }

    @GetMapping("/books")
    public List<BookResponse> getBooks() {
        var allBooks = bookRepository.findAll();
        return convertAllToBookResponse(allBooks);
    }

    private List<BookResponse> convertAllToBookResponse(List<BookEntity> allBooks) {
        return allBooks.stream()
                .map(bookEntity -> new BookResponse(
                        bookEntity.getId(),
                        bookEntity.getTitle(),
                        bookEntity.getAuthorName(),
                        bookEntity.getPublisherName(),
                        bookEntity.getPublishedAt()
                ))
                .toList();
    }
}
