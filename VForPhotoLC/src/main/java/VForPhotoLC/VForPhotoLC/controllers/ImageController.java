package VForPhotoLC.VForPhotoLC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImageController {
    @RequestMapping(value = "/greece")
    public ModelAndView showGreece(){return new ModelAndView("greece");}
    @RequestMapping(value = "/uk")
    public ModelAndView showUk(){ return new ModelAndView("uk"); }
    @RequestMapping(value = "/spain")
    public ModelAndView showSpain(){
        return new ModelAndView("spain");
    }

}






