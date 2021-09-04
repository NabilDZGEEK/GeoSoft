package controller;

import controller.poiconnement.Ccisaillement;
import controller.poiconnement.Cpenetrometre;
import controller.poiconnement.Cpressiometre;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Imprimante;
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
    Button enregistrer,imprimer;
    public TextField qnet,rvd,vk;
    public VBox parent;
    public ComboBox combo;
    public HBox penetrometre,pressiometre,cissaiment;
    Cpoiconnement(){
        super("poiconnement.fxml",3);
        enregistrer=(Button) scene.lookup("#enregistrer");
        imprimer=(Button) scene.lookup("#imprimer");
        parent=(VBox)scene.lookup("#issaiparent");


        try {
            cissaiment = FXMLLoader.load(getClass().getResource("/view/poiconnement/cissaiment.fxml"));
            penetrometre=FXMLLoader.load(getClass().getResource("/view/poiconnement/penetromètre.fxml"));
            pressiometre=FXMLLoader.load(getClass().getResource("/view/poiconnement/pressiomètre.fxml"));
            combo= (ComboBox) scene.lookup("#combo");
            combo.valueProperty().addListener((observableValue, o, t1) -> {
                Label res= (Label)scene.lookup("#res");res.setText("");
                TextField vk=(TextField) scene.lookup("#vk");vk.setText("");
                qnet.setText("");rvd.setText("");
                switch ((String) t1){
                    case "Pressiomètre":parent.getChildren().set(2,pressiometre);
                        if(Projet.getInstance().fichier!=null){
                            Cpressiometre.getInstance().remplire();
                        }

                        Cpressiometre.getInstance().config();break;
                    case "Penetromètre":parent.getChildren().set(2,penetrometre);
                        if(Projet.getInstance().fichier!=null){
                            Cpenetrometre.getInstance().remplire();
                        }

                        Cpenetrometre.getInstance().config();break;
                    case "Cisaillemment":parent.getChildren().set(2,cissaiment);
                        if(Projet.getInstance().fichier!=null){
                            Ccisaillement.getInstance().réinitialiser();
                        }

                        Ccisaillement.getInstance().config();
                }

            });
            imprimer.setOnMouseClicked(mouseEvent -> Imprimante.getInstance().imprimer(root));
        } catch (IOException e) {
            e.printStackTrace();
        }


        qnet= (TextField) scene.lookup("#qnet");
        rvd= (TextField) scene.lookup("#rvd");
        vk= (TextField) scene.lookup("#vk");

    }
    public void reinitialiser(){
        combo.setValue("choisir");
        parent.getChildren().set(2,new HBox());
    }
}