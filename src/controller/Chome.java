package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
        tab.getChildren().get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cnvprojet.getInstance().nvprojet.show();
            }
        });
        tab.getChildren().get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Couvrirprojet.getInstance().afficher();

            }
        });
        tab.getChildren().get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Cpoussee.getInstance().scene);
            }
        });
        tab.getChildren().get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Cglissement.getInstance().scene);
            }
        });
        tab.getChildren().get(4).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Cpoiconnement.getInstance().scene);
            }
        });
        tab.getChildren().get(5).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Crenversement.getInstance().scene);
                Crenversement.getInstance().calcul();
            }
        });
        tab.getChildren().get(6).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Cinterne.getInstance().scene);
            }
        });
        tab.getChildren().get(7).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Cinterne2.getInstance().scene);
            }
        });
        tab.getChildren().get(8).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Cprincipale.primaryStage.setScene(Capropos.getInstance().scene);
            }
        });
    }

}
