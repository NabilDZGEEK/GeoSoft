package controller;

import controller.poiconnement.Ccisaillement;
import controller.poiconnement.Cpenetrometre;
import controller.poiconnement.Cpressiometre;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Projet;

import java.io.IOException;

public class Cpoiconnement extends Controller{
    public static Cpoiconnement instance;


    public static Cpoiconnement getInstance() {
        if(instance==null){
            instance=new Cpoiconnement();
        }
        return instance;
    }
    Button enregistrer;
    public TextField qnet,rvd,vk;
    VBox parent;
    ComboBox combo;
    Cpoiconnement(){
        super("poiconnement.fxml",3);
        enregistrer=(Button) scene.lookup("#enregistrer");
        VBox essai=(VBox)scene.lookup("#essai");
        parent= (VBox) essai.getParent();

        final VBox cissaiment;
        final HBox penetrometre,pressiometre;
        try {
            cissaiment = FXMLLoader.load(getClass().getResource("/view/poiconnement/cissaiment.fxml"));
            penetrometre=FXMLLoader.load(getClass().getResource("/view/poiconnement/penetromètre.fxml"));
            pressiometre=FXMLLoader.load(getClass().getResource("/view/poiconnement/pressiomètre.fxml"));
            combo= (ComboBox) scene.lookup("#combo");
            combo.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observableValue, Object o, Object t1) {
                    Label res= (Label)scene.lookup("#res");res.setText("");
                    TextField vk=(TextField) scene.lookup("#vk");vk.setText("");
                    qnet.setText("");rvd.setText("");
                    switch ((String) t1){
                        case "pressiomètre":parent.getChildren().set(1,pressiometre);
                            if(Projet.getInstance().fichier!=null){
                                Cpressiometre.getInstance().remplire();
                            }

                            Cpressiometre.getInstance().config();break;
                        case "penetromètre":parent.getChildren().set(1,penetrometre);
                            if(Projet.getInstance().fichier!=null){
                                Cpenetrometre.getInstance().remplire();
                            }

                            Cpenetrometre.getInstance().config();break;
                        case "cisaillemment":parent.getChildren().set(1,cissaiment);
                            if(Projet.getInstance().fichier!=null){
                                Ccisaillement.getInstance().réinitialiser();
                            }

                            Ccisaillement.getInstance().config();
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        qnet= (TextField) scene.lookup("#qnet");
        rvd= (TextField) scene.lookup("#rvd");
        vk= (TextField) scene.lookup("#vk");

    }
    void réinitialiser(){

        combo.setValue("choisir");
        parent.getChildren().set(1,new HBox());
    }
}
