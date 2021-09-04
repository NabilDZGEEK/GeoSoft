package controller;

import javafx.scene.layout.GridPane;

public class Chome extends Controller{
    public static Chome instance = null;


    public static Chome getInstance() {
        if(instance==null){
            instance = new Chome();
        }
        return instance;
    }

    Chome(){
        super("home.fxml",0);
        GridPane tab= (GridPane) scene.lookup("#dashboard");
        tab.getChildren().get(0).setOnMouseClicked(mouseEvent -> Cnvprojet.getInstance().nvprojet.show());
        tab.getChildren().get(1).setOnMouseClicked(mouseEvent -> Couvrirprojet.getInstance().afficher());
        tab.getChildren().get(2).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Cpoussee.getInstance().scene));
        tab.getChildren().get(3).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Cglissement.getInstance().scene));
        tab.getChildren().get(4).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Cpoiconnement.getInstance().scene));
        tab.getChildren().get(5).setOnMouseClicked(mouseEvent -> {
            Cprincipale.primaryStage.setScene(Crenversement.getInstance().scene);
            Crenversement.getInstance().calcul();
        });
        tab.getChildren().get(6).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Cinterne.getInstance().scene));
        tab.getChildren().get(7).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Cinterne2.getInstance().scene));
        tab.getChildren().get(8).setOnMouseClicked(mouseEvent -> Cprincipale.primaryStage.setScene(Capropos.getInstance().scene));
    }

}