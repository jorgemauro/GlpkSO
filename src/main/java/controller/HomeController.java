package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.portlet.ModelAndView;

@Controller
@EnableAutoConfiguration
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execute(Model model) {
        return ("index");
    }
    @RequestMapping(value ="/", method = RequestMethod.POST)
    public ModelAndView getResultado(@RequestParam("origens")int lojas,
                                     @RequestParam("destinos")int clientes,
                                     @RequestParam("oferta")int estoque,
                                     @RequestParam("mat[][]")double[][] matriz, Model model){
        System.out.println("cheguei");
        return new ModelAndView("resultado");
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HomeController.class, args);
    }
}
