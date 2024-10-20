package studygroup.ddd.bookapi;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "book")
@Builder
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String authorName;
    private String publisherName;
    private Date publishedAt;

    @Override
    public String toString() {
        return "BookEntity[title=%s, author=%s]".formatted(this.title, this.authorName);
    }
}
