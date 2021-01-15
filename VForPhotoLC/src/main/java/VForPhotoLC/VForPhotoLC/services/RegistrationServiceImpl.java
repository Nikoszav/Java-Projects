package VForPhotoLC.VForPhotoLC.services;

import VForPhotoLC.VForPhotoLC.Entities.PhotoUser;
import VForPhotoLC.VForPhotoLC.dao.PhotoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
        private PhotoUserRepository photoUserRepository;

        @Autowired
        public RegistrationServiceImpl (PhotoUserRepository photoUserRepository){
            this.photoUserRepository=photoUserRepository;
        }

        @Override
        public void registerUser(PhotoUser photoUser) {
                photoUserRepository.save(photoUser);
        }
}
