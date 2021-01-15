package VForPhotoLC.VForPhotoLC.dao;

import VForPhotoLC.VForPhotoLC.Entities.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<LoginHistory, String> {
}
