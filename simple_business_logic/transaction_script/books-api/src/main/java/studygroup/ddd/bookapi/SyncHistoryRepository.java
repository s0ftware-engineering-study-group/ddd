package studygroup.ddd.bookapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SyncHistoryRepository extends JpaRepository<SyncHistoryEntity, Long> {
}
