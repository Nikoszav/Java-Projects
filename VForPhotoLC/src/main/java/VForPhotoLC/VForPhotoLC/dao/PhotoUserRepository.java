package VForPhotoLC.VForPhotoLC.dao;

import VForPhotoLC.VForPhotoLC.Entities.PhotoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoUserRepository extends JpaRepository<PhotoUser, String> {
}
