package controller;

import feature.GlpkService;
import feature.Prob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
                                     @RequestParam("mat[][]")double[] matriz, Model model){
        //{-1,-1,-1,0,0,0},
        //{0,0,0,-1,-1,-1},
        int variaveis=lojas*clientes;
        int nRestrições= clientes+lojas;
        double[][] matrizRestris= new double[nRestrições][variaveis];
        for(int s=0;s<nRestrições;s++){
            for (int k=0;k<variaveis;k++){
                if(s==0 && k<(variaveis/2))
                    matrizRestris[s][k]=-1;
                else if(s==1 && k>=(variaveis/2))
                    matrizRestris[s][k]=-1;
                else
                    matrizRestris[s][k]=0;
            }
        }
        int count=0;
        for (int i=2;i<nRestrições;i++){
            for(int c=0;c<(clientes-1);c++){
                int teste=(clientes*c)+count;
                if(teste<=variaveis) {
                    matrizRestris[i][teste] = 1;
                }
            }
            count++;
        }

        double[] limits = new double[nRestrições];
        int count2=1;
        for(int i=0;i<matriz.length;i++){
            if(i==0){
                limits[count2-1]=-matriz[clientes];
                count2++;
            }else if(i==(2*clientes)+1){
                limits[count2-1]=-matriz[(2*clientes)+1];
                count2++;
            }else if(i>(matriz.length-(clientes+2))&& i!=matriz.length-1){
                limits[count2-1]=matriz[i];
                count2++;
            }
        }
        double [] coef= new double[variaveis];
        int count3=0;

        for(int j=0;j<matriz.length-(clientes+2);j++){
            if(j<clientes){
                coef[count3]=matriz[j];
                count3++;
            }else if((j-1)%clientes!=0&&j!=clientes || j==clientes+1){
                coef[count3]=matriz[j];
                count3++;
            }
        }

        Prob prob=new Prob(variaveis,nRestrições,matrizRestris,limits,coef);
        GlpkService glpkService = new GlpkService();
        prob=glpkService.serviceProb(prob);
        ModelAndView modelo=new ModelAndView("resultado");
        modelo.addObject("fObj",prob.getResultador()[0]);
        for(int i=1;i<prob.getResultador().length;i++){
            modelo.addObject("c"+i,prob.getResultador()[i]);
        }
        return modelo;
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(HomeController.class, args);
    }
}
