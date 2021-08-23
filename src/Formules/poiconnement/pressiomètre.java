package Formules.poiconnement;

import Formules.formule;
import controller.Cglissement;
import controller.Cpoussee;

public class pressiomètre extends formule {
    public static double e=0;
    public static void calcul_e(){
        Cpoussee cp=Cpoussee.getInstance();
        if(Cglissement.getInstance().vk!=0){
            e= (cp.pa_non_elu*cos(cp.sigma)*cp.h)/(2* Cglissement.getInstance().vk);
        }

    }
   public static double kp(double a,double b,double c,double k0,double db){
       if(e==0){
           return k0+(a+b*db);
       }
        return k0+(a+b*db)*(1-Math.pow(e,-c*db));
    }
    public static double isigma_drainnee(double sd,double db){
       if(sd<Math.PI/4){
           return carré(1-2*sd/Math.PI)-(2*sd/Math.PI)*(2-6*sd/Math.PI)*Math.pow(e,-db);
       }
       return carré(1-2*sd/Math.PI)*(1-Math.pow(e,-db));
    }
    public static double isigma_non_drainnee(double sd){
       return carré(1-2*sd/Math.PI);
    }
    public static double isigma_inter(double sd,double db,double c,double y,double b,double phi){
       double dr=isigma_drainnee(sd,db);
       double e=0.6*c/(y*b*tan(phi));
        return dr+(dr-isigma_non_drainnee(sd))*Math.pow(e,-e);
    }
    public static double ibeta_drainnee(double beta,double d){
       return 1-beta/Math.PI*carré(1-d/(8*beta));
    }
    public static double ibeta_non_drainnee(double beta,double d,double b,double de){
        return 1-0.9*tan(beta)*(2-tan(beta))*carré(1-(d+de/tan(beta))/(8*b));
    }
    public static double ibeta_inter(double beta,double d,double b,double de,double c,double y,double phi){
        System.out.println("inter : "+beta+" "+d+" "+b+" "+de+" "+c+" "+y+" "+phi);
        double ibd=ibeta_drainnee(beta,d);
        double e=0.6*c/(y*b*tan(phi));
        return ibd+(ibd-ibeta_non_drainnee(beta,d,b,de))*Math.pow(e,-e);
    }
    public static double calcul_rvd(double qnet, double a,double y){
        return qnet*a/y;
    }

}
