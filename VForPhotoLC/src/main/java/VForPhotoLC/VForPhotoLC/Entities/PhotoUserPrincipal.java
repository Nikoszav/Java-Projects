package VForPhotoLC.VForPhotoLC.Entities;



import java.nio.file.attribute.UserPrincipal;

public class PhotoUserPrincipal implements UserPrincipal {
    private PhotoUser photoUser;

    public PhotoUserPrincipal(PhotoUser photoUser){this.photoUser=photoUser;}
    @Override
    public String getName() {
        return photoUser.getEmail();
    }
}
