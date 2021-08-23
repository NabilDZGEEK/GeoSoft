package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Projet;
import notification.Notification;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;

public class Cnvprojet {
    public static Cnvprojet instance;

    public static Cnvprojet getInstance(){
        if(instance==null){
            instance=new Cnvprojet();
        }
        return instance;
    }

    Parent root;
    public Scene scene;
    Stage nvprojet;
    Cnvprojet() {
        /*chargement de l'interface*/
        try {
            root = FXMLLoader.load(getClass().getResource("/view/nvprojet.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene=new Scene(root,800,500);
        creerFenetre();
        Button enregistrer= (Button) scene.lookup("#enregistrer");
        Button annuler= (Button) scene.lookup("#annuler");
        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GridPane donnees=(GridPane) scene.lookup("#donnees");
                File f=new FileChooser().showSaveDialog(scene.getWindow());
                if(f!=null){
                    Projet.getInstance().fichier=f;
                    try {
                        Projet.getInstance().dom = Projet.getInstance().builder.newDocument();
                        Element root = Projet.getInstance().dom.createElement("projet");
                        String[]values=new String[9];
                        for(int i=0;i<8;i++){
                            TextField tf= (TextField) donnees.getChildren().get(9+i);
                            values[i]=tf.getText();
                        }
                        TextArea ta=(TextArea)donnees.getChildren().get(17);
                        values[8]=ta.getText();

                        root.setAttribute("num_affaire",values[0]);
                        root.setAttribute("titre_projet",values[1]);
                        root.setAttribute("lieu_projet",values[2]);
                        root.setAttribute("date_etude",values[3]);
                        root.setAttribute("maitre_ouvrage",values[4]);
                        root.setAttribute("maitre_oeuvre",values[5]);
                        root.setAttribute("client",values[6]);
                        root.setAttribute("bureau_etude",values[7]);
                        root.setAttribute("commentaire",values[8]);

                        Element newElement;
                        newElement=Projet.getInstance().dom.createElement("poussee");root.appendChild(newElement);
                        newElement=Projet.getInstance().dom.createElement("glissement");root.appendChild(newElement);
                        newElement=Projet.getInstance().dom.createElement("poiconnement");root.appendChild(newElement);
                        /*création de l'élémen tableau*/
                        Element tableau=Projet.getInstance().dom.createElement("tableau");

                        for(byte i=0;i<14;i++){
                            tableau.appendChild(Projet.getInstance().dom.createElement("ligne"));
                        }

                        /*insertion du tableau dans pressiometre et penetrometre */
                        Element el2=Projet.getInstance().dom.createElement("pressiometre");
                        el2.appendChild(tableau);
                        newElement.appendChild(el2);
                        el2=Projet.getInstance().dom.createElement("penetrometre");
                        el2.appendChild(tableau.cloneNode(true));
                        newElement.appendChild(el2);

                        newElement.appendChild(Projet.getInstance().dom.createElement("cisaillement"));
                        newElement=Projet.getInstance().dom.createElement("renversement");root.appendChild(newElement);
                        newElement=Projet.getInstance().dom.createElement("interne1");root.appendChild(newElement);
                        newElement=Projet.getInstance().dom.createElement("interne2");root.appendChild(newElement);
                        Projet.getInstance().dom.appendChild(root);

                        Projet.getInstance().ecrire();

                        Notification.Notifier.INSTANCE.notifySuccess("succès","Projet crée avec succès");
                        nvprojet.close();
                        /*vider les champs*/
                        Cpoussee.getInstance().réinitialiser();
                        Cglissement.getInstance().réinitialiser();
                        Cpoiconnement.getInstance().réinitialiser();
                        Cinterne.getInstance().réinitialiser();
                        Cinterne2.getInstance().réinitialiser();
                        /*activation des champs enregistrer dans toutes les verifications*/
                        Cpoussee.getInstance().enregistrer.setDisable(false);
                        Cglissement.getInstance().enregistrer.setDisable(false);
                        Cpoiconnement.getInstance().enregistrer.setDisable(false);
                        Crenversement.getInstance().enregistrer.setDisable(false);
                        Cinterne.getInstance().enregistrer.setDisable(false);
                        Cinterne2.getInstance().enregistrer.setDisable(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        annuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                nvprojet.close();
            }
        });
    }
    private void creerFenetre(){
        nvprojet=new Stage();
        nvprojet.setTitle("Nouveau projet");
        nvprojet.setScene(scene);
        nvprojet.initModality(Modality.APPLICATION_MODAL);
    }
}
