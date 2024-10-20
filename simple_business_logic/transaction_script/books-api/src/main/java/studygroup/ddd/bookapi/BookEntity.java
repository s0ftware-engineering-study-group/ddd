package studygroup.ddd.bookapi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "book")
public class BookEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String authorName;
    private String publisherName;
    private Date publishedAt;
}
