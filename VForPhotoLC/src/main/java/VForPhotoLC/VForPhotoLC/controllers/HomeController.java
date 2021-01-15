package VForPhotoLC.VForPhotoLC.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class HomeController {

        @RequestMapping(value = "/about")
        public ModelAndView showAbout(){
            return new ModelAndView("about");
        }
        @RequestMapping(value = "/contact_us")
        public ModelAndView showContact(){
            return new ModelAndView("contact_us");
        }
        @RequestMapping(value = "/index")
        public ModelAndView showHome(){return new ModelAndView("index");}


}
