package VForPhotoLC.VForPhotoLC.Entities;

import java.util.List;

public class CollatedUser {
    private PhotoUser photoUser;

    private List<LoginHistory> logins;

    public PhotoUser getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(PhotoUser photoUser) {
        this.photoUser = photoUser;
    }

    public List<LoginHistory> getLogins() {
        return logins;
    }

    public void setLogins(List<LoginHistory> logins) {
        this.logins = logins;
    }
}
