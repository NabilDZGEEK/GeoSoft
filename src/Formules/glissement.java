package Formules;

import controller.Cpoussee;

public class glissement extends formule{
    static public Double[] drainee(double b, double phi3, double c, double y, double yrh,double h)throws NumberFormatException{
        Cpoussee cp=Cpoussee.getInstance();
        double vk=b*h*y+cp.pa_non_elu*sin(cp.sigma);
        Double[] res=new Double[2];
        res[0]=1/yrh*Math.min(vk*tan(cp.phi)+b*cp.c,vk*tan(phi3)+b*c);
        res[1]=vk;
        return res;

    }
    static public double non_drainee(double b,double cu,double yrh)throws NumberFormatException{
        return 1/yrh*b*cu;
    }
}
