package VForPhotoLC.VForPhotoLC.services;

import VForPhotoLC.VForPhotoLC.Entities.CollatedUser;
import VForPhotoLC.VForPhotoLC.Entities.LoginHistory;
import VForPhotoLC.VForPhotoLC.dao.HistoryRepository;
import VForPhotoLC.VForPhotoLC.dao.PhotoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProfileServiceImpl implements ProfileService {
        private PhotoUserRepository photoUserRepository;
        private HistoryRepository historyRepository;

        @Autowired
        public ProfileServiceImpl(PhotoUserRepository photoUserRepository, HistoryRepository historyRepository){
                this.photoUserRepository=photoUserRepository;
                this.historyRepository=historyRepository;

        }

    @Override
    public CollatedUser getProfile(String email) {
            CollatedUser user = new CollatedUser();
            user.setPhotoUser(photoUserRepository.findById(email).orElseThrow(()-> new UsernameNotFoundException("No matching user found")));
            List<LoginHistory> history = historyRepository.findAll()
                .stream().filter(loginHistory -> loginHistory.getEmail().equals(email))
                .collect(Collectors.toList());
            user.setLogins(history);

            return user;
    }
}
