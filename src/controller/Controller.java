package controller;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Controller {

    public Parent root;
    public Scene scene;
    public NumberFormat formatter;
    Controller(String fichier,int numMenu){

        try {
            root = FXMLLoader.load(getClass().getResource("/view/"+fichier));
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene=new Scene(root,1250,700);
        scene.getStylesheets().add("/stylesheet.css");
        formatter = new DecimalFormat("#0.00");
        BorderPane rootpane= (BorderPane) root;
        rootpane.setTop(Cprincipale.menus[numMenu]);


    }

}
