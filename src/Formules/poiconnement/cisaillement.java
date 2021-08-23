package Formules.poiconnement;

import Formules.formule;
import controller.Cglissement;
import controller.Cpoussee;
import controller.poiconnement.Ccisaillement;

public class cisaillement extends formule {
    public static double e;
    public static void calcul_e(){
        Cpoussee cp=Cpoussee.getInstance();
        if(cp.pa_non_elu==0){
            e=0;
        }else{
            e= (cp.pa_non_elu*cos(cp.sigma)*cp.h)/(2* Cglissement.instance.vk);
        }

    }
    final static double E=Math.E;
    public static double[] cofs=new double[12];
    public static double calcul_drainnee(double phi,double c,double h,double q,double y,double a, double b, double l, double yd,double d){
        double bl=b/l;
        double m=(2+bl)/(1+bl);

        double nq=Math.pow(E,Math.PI*tan(phi))*carré(tan(180/4+phi/2));
        double nc=(nq-1)/tan(phi);
        double ny=2*(nq-1)*Math.pow(tan(phi),E);
        double sq=1,sc=1,sy=1;
        if(!Ccisaillement.etat.equals("filante")){
            sq=1+bl*sin(phi);
            sc=(sq*nq-1)/(nq-1);
            sy=1-0.3*bl;
        }

        double bq=carré(1-a*tan(phi));
        double bc=bq-(1-bq)/(nc*tan(phi));
        double by=bq;
        double H=Cpoussee.instance.pa_non_elu*cos(Cpoussee.instance.sigma);
        double v= b* h*y+Cpoussee.getInstance().pa_non_elu*sin(Cpoussee.getInstance().sigma)+q;
        double iq=Math.pow(1-H/(v+b*l*c/tan(phi)),m);
        double ic=iq-(1-iq)/(nc*tan(phi));
        double iy=iq*iq;
        double q0=d*yd;
        cofs[0]=nc;cofs[1]=nq;cofs[2]=ny;
        cofs[3]=bc;cofs[4]=bq;cofs[5]=by;
        cofs[6]=sc;cofs[7]=sq;cofs[8]=sy;
        cofs[9]=ic;cofs[10]=iq;cofs[11]=iy;

        return c*nc*bc*sc*ic+q0*nq*bq*sq*iq+q*nq*bq*sq*iq+0.5*y*b*ny*by*sy*iy-q0;
    }
    public static double calcul_non_drainnee_coherant(double cu1,double q, double a, double b, double l){
        double bc=1-2*a/(Math.PI+2);
        double sc=1+0.2*b/l;
        double H=Cpoussee.instance.pa_non_elu*cos(Cpoussee.instance.sigma);
        double ic=1;
        if(H<=b*l*cu1){
            ic=(1+Math.sqrt(1-H/(b*l*cu1)))/2;
        }
        return cu1*(Math.PI+2)*bc*sc*ic+q;
    }
    public static double calcul_non_drainnee_non_coherant(double phi,double b, double y){

        double nq=Math.pow(E,Math.PI*tan(phi))*carré(tan(180/4+phi/2));
        double ny=2*(nq-1)*tan(Math.pow(phi,E));
        return 0.5*y*b*ny;
    }
    public static double calcul_non_drainnee_heterogene(double cu1,double q,double phi,double a,double b,double l,double y){

        return Math.min(calcul_non_drainnee_coherant(cu1,q,a,b,l),calcul_non_drainnee_non_coherant(y,b,phi));
    }
    public static double calcul_rvd(double qnet, double a,double y){
        return qnet*a/y;
    }
    public static double calculer_a_circulaire(double r){
        return Math.PI*r*r*((2*Math.acos(e/r)/Math.PI)-2*e/(Math.PI*r)*Math.sqrt(1-carré(e/r)));
    }
}
