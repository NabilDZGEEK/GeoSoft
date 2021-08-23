package Formules;

public class Poncelet extends formule{
    public static double calculer(double phi,double sigma,double lambda,double beta) throws NumberFormatException{
        double gauche=carré(sin(lambda-phi))/((carré(sin(lambda))*sin(lambda+sigma)));
        double racine=0;
        if(phi>beta){
            racine=(sin(phi+sigma)*sin(phi-beta))/(sin(lambda+sigma)*sin(lambda-beta));
        }
        return gauche*Math.pow(1+Math.sqrt(racine),-2);
    }

}
