package Formules;

public class Coulomb extends formule{
    public static double calculer(double phi,double sigma,double lambda,double beta)throws NumberFormatException{
        double s1=sin(lambda-phi)/sin(lambda);
        double h=0;
        if(phi>=beta){
            h=(sin(sigma+phi)*sin(phi-beta))/sin(lambda-beta);
        }
        double s2=Math.sqrt(sin(lambda+sigma))+Math.sqrt(h);
        return carré(s1/s2);
    }


}