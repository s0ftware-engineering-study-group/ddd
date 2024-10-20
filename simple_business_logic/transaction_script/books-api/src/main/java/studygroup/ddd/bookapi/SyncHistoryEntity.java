package studygroup.ddd.bookapi;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sync_history")
@Builder
@AllArgsConstructor
public class SyncHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfBooks;
    private Date syncAt;
}
