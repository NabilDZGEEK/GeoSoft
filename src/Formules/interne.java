package Formules;

public class interne extends formule{
    public static Double calcul(Double pend,Double pflu,Double pdeg,Double rtk) throws NumberFormatException{
        return pend*pflu*pdeg*rtk/1.25;
    }
    public  static Double calcul2(Double y3k, Double z, Double n, Double b, Double ls, Double phi3k, Double cg) throws NumberFormatException{
        return cg*tan(phi3k)*y3k*z*2*n*b*ls/1.25;
    }
}
