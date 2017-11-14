package controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(Model model) {
        return ("index");
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HomeController.class, args);
    }
}
