package VForPhotoLC.VForPhotoLC.services;


import VForPhotoLC.VForPhotoLC.Entities.LoginHistory;
import VForPhotoLC.VForPhotoLC.Entities.PhotoUser;
import VForPhotoLC.VForPhotoLC.dao.HistoryRepository;
import VForPhotoLC.VForPhotoLC.dao.PhotoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PhotoUserDetail implements UserDetailsService {
    private PhotoUserRepository photoUserRepository;
    private HistoryRepository historyRepository;

    @Autowired
    PhotoUserDetail(PhotoUserRepository photoUserRepository, HistoryRepository historyRepository){
        this.photoUserRepository=photoUserRepository;
        this.historyRepository=historyRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
        PhotoUser photoUser = photoUserRepository.findById(s).orElseThrow(()-> new UsernameNotFoundException("No matching user."));
        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setEmail(photoUser.getEmail());
        loginHistory.setTime(LocalDateTime.now());
        historyRepository.save(loginHistory);
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(photoUser.getAuthoritiy()));
        return new  User
                (photoUser.getEmail(),photoUser.getPassword(),photoUser.getEnabled(),true, true, true, authorities);
    }


}
