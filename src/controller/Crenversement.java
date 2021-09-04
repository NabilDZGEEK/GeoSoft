package controller;

import Formules.Renversement;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Imprimante;

import static java.lang.Math.sin;

public class Crenversement extends Controller{
    public static Crenversement instance = null;


    public static Crenversement getInstance() {
        if(instance==null){
            instance=new Crenversement();
        }
        return instance;
    }
    Button enregistrer,imprimer;
    TextField fst;
    Label res;
    void calcul(){

        Cpoussee cp=Cpoussee.getInstance();
        Cglissement cg=Cglissement.getInstance();
        TextField vkt=(TextField) scene.lookup("#vk");
        TextField pat=(TextField) scene.lookup("#pa");
        double vk=cg.b* cp.h* Cglissement.getInstance().gama+cp.pa_non_elu*sin(cp.sigma*Math.PI/180);
        double fs=0;
        fs=Renversement.calculer(cp.pa,cg.b,vk);
        vkt.setText(formatter.format(vk));
        pat.setText(formatter.format(cp.pa));
        if(fs==0){
            fst.setText("");
        }else{
            fst.setText(Double.toString(fs));
            if(fs>1.5){
                res.setText("STABLE");
                res.setTextFill(Color.web("#19a112"));
            }else{
                res.setText("INSTABLE");
                res.setTextFill(Color.web("#A1170E"));
            }
        }

    }
    Crenversement(){
        super("renversement.fxml",4);
        enregistrer=(Button) scene.lookup("#enregistrer");
        imprimer=(Button) scene.lookup("#imprimer");
        fst=(TextField)scene.lookup("#fs");
        res=(Label) scene.lookup("#res");
        imprimer.setOnMouseClicked(mouseEvent -> Imprimante.getInstance().imprimer(root));
    }
}
