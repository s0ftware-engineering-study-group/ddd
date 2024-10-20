package studygroup.ddd.bookapi;

import java.util.Date;

public record BookResponse(
        Long id,
        String title,
        String authorName,
        String publisherName,
        Date publishedAt
) {
}
