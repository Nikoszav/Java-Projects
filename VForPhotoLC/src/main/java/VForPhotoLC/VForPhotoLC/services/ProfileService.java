package VForPhotoLC.VForPhotoLC.services;

import VForPhotoLC.VForPhotoLC.Entities.CollatedUser;

public interface ProfileService {
        CollatedUser getProfile(String email);
}
