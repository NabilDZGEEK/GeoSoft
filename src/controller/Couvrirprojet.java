package controller;

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
import model.Imprimante;
import model.Projet;
import notification.Notification;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;

public class Couvrirprojet {
    public static Couvrirprojet instance;

    public static Couvrirprojet getInstance(){
        if(instance==null){
            instance=new Couvrirprojet();
        }
        return instance;
    }

    public Parent root;
    public Scene scene;
    Stage projet;
    Couvrirprojet() {
        /* chargement la vue de ouvrir projet vers la scene  scene*/
        try {
            root = FXMLLoader.load(getClass().getResource("/view/ouvrirprojet.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene=new Scene(root,800,500);

        /*creation de la fenetre */
        creerFenetre();
        Button modifier= (Button) scene.lookup("#modifier");
        Button passer= (Button) scene.lookup("#passer");
        Button imprimer= (Button) scene.lookup("#imprimer");
        Button annuler= (Button) scene.lookup("#annuler");
        modifier.setOnAction(actionEvent -> {
            GridPane donnees=(GridPane) scene.lookup("#donnees");
            try {
                Element root = Projet.getInstance().dom.getDocumentElement();
                /*mettre les valeurs des champs dans la variable values */
                String[]values=new String[9];
                for(int i=0;i<8;i++){
                    TextField tf= (TextField) donnees.getChildren().get(9+i);
                    values[i]=tf.getText();
                }
                TextArea ta=(TextArea)donnees.getChildren().get(17);
                values[8]=ta.getText();
                /*ecriture des données dans le fichier xml */
                root.setAttribute("num_affaire",values[0]);
                root.setAttribute("titre_projet",values[1]);
                root.setAttribute("lieu_projet",values[2]);
                root.setAttribute("date_etude",values[3]);
                root.setAttribute("maitre_ouvrage",values[4]);
                root.setAttribute("maitre_oeuvre",values[5]);
                root.setAttribute("client",values[6]);
                root.setAttribute("bureau_etude",values[7]);
                root.setAttribute("commentaire",values[8]);

                Projet.getInstance().ecrire();

                Notification.Notifier.INSTANCE.notifySuccess("succès","Projet modifié avec succès");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        passer.setOnAction(actionEvent -> {
            projet.close();
            Cprincipale.primaryStage.setScene(Cpoussee.getInstance().scene);
            Cpoussee.getInstance().reinitialiser();
            Cglissement.getInstance().reinitialiser();
            Cpoiconnement.getInstance().reinitialiser();
            Cinterne.getInstance().reinitialiser();
            Cinterne2.getInstance().reinitialiser();

        });
        imprimer.setOnMouseClicked(mouseEvent -> Imprimante.getInstance().imprimerProjet());
        annuler.setOnAction(actionEvent -> projet.close());

    }
    void afficher(){
        File fic=new FileChooser().showOpenDialog(scene.getWindow());
        if(fic!=null){
            Projet.getInstance().fichier=fic;
            /*activation des champs enregistrer dans toutes les verifications*/
            Cpoussee.getInstance().enregistrer.setDisable(false);
            Cglissement.getInstance().enregistrer.setDisable(false);
            Cpoiconnement.getInstance().enregistrer.setDisable(false);
            Crenversement.getInstance().enregistrer.setDisable(false);
            Cinterne.getInstance().enregistrer.setDisable(false);
            Cinterne2.getInstance().enregistrer.setDisable(false);
            try {
                projet.show();
                charger_les_donnees();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    private void charger_les_donnees() throws IOException, SAXException {
        GridPane donnees=(GridPane) scene.lookup("#donnees");
        Projet.getInstance().dom = Projet.getInstance().builder.parse(Projet.getInstance().fichier);
        Element eltprojet=Projet.getInstance().dom.getDocumentElement();
        TextField t;
        t= (TextField) donnees.getChildren().get(9);t.setText(eltprojet.getAttribute("num_affaire"));
        t= (TextField) donnees.getChildren().get(10);t.setText(eltprojet.getAttribute("titre_projet"));
        t= (TextField) donnees.getChildren().get(11);t.setText(eltprojet.getAttribute("lieu_projet"));
        t= (TextField) donnees.getChildren().get(12);t.setText(eltprojet.getAttribute("date_etude"));
        t= (TextField) donnees.getChildren().get(13);t.setText(eltprojet.getAttribute("maitre_ouvrage"));
        t= (TextField) donnees.getChildren().get(14);t.setText(eltprojet.getAttribute("maitre_oeuvre"));
        t= (TextField) donnees.getChildren().get(15);t.setText(eltprojet.getAttribute("client"));
        t= (TextField) donnees.getChildren().get(16);t.setText(eltprojet.getAttribute("bureau_etude"));
        TextArea ta= (TextArea) donnees.getChildren().get(17);ta.setText(eltprojet.getAttribute("commentaire"));
    }
    private void creerFenetre(){
        projet=new Stage();
        projet.setScene(scene);
        projet.initModality(Modality.APPLICATION_MODAL);
    }
}