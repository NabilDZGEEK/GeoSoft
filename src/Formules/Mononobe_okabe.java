package Formules;

public class Mononobe_okabe extends formule{
    public static double calculer(double phi,double sigma,double lambda,double beta,double teta)throws NumberFormatException{
        lambda=180-lambda;
        double s1=carré(sin(lambda+phi-teta));
        double gauche=cos(teta)*carré(sin(lambda))*sin(lambda-teta-sigma);

        double racine=0;
        if(phi>beta+teta){
            racine=(sin(phi+sigma)*sin(phi-beta-teta))/(sin(lambda-teta-sigma)*sin(lambda+beta));
        }
        double s2=gauche*(Math.pow(1+Math.sqrt(racine),2));
        return s1/s2;
    }
}
