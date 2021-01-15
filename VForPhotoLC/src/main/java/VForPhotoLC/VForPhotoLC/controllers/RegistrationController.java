package VForPhotoLC.VForPhotoLC.controllers;


import VForPhotoLC.VForPhotoLC.Entities.PhotoUser;
import VForPhotoLC.VForPhotoLC.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
        public ModelAndView showRegistration(){
            PhotoUser photoUser =new PhotoUser();
            return new ModelAndView("register", "photoUser",photoUser);
        }
        @RequestMapping(value = "/register", method = RequestMethod.POST)
        public String processRegistration(PhotoUser photoUser){
            photoUser.setEnabled(Boolean.TRUE);
            photoUser.setAuthoritiy("ROLE USER");
            registrationService.registerUser(photoUser);
            return "redirect:/profile";
        }


}
