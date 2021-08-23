package Formules;

public class Mononobe_okabe extends formule{
    public static double calculer(double phi,double sigma,double lambda,double beta,double teta)throws NumberFormatException{
        lambda=180-lambda;
        System.out.println(phi);
        System.out.println(sigma);
        System.out.println(lambda);
        System.out.println(beta);
        System.out.println(teta);
        double s1=carré(sin(lambda+phi-teta));
        System.out.println(s1);
        double gauche=cos(teta)*carré(sin(lambda))*sin(lambda-teta-sigma);
        System.out.println(gauche);
        double racine=0;
        if(phi>beta+teta){
            racine=(sin(phi+sigma)*sin(phi-beta-teta))/(sin(lambda-teta-sigma)*sin(lambda+beta));
        }
        System.out.println(racine);
        double s2=gauche*(Math.pow(1+Math.sqrt(racine),2));
        return s1/s2;
    }
}
