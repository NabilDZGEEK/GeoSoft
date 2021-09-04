package controller;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Cprincipale {
    public static Stage primaryStage;
    static HBox[] menus=new menu[9];

    public Cprincipale(){

        for (int i=0;i<9;i++) {
            menus[i]=new menu();
            menu m= (menu) menus[i];
            m.b[0].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Chome.getInstance().scene));
            m.b[1].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Cpoussee.getInstance().scene));
            m.b[2].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Cglissement.getInstance().scene));
            m.b[3].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Cpoiconnement.getInstance().scene));
            m.b[4].setOnMouseClicked(mouseEvent -> {
                primaryStage.setScene(Crenversement.getInstance().scene);
                Crenversement.getInstance().calcul();
            });

            m.b[5].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Cinterne.getInstance().scene));
            m.b[6].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Cinterne2.getInstance().scene));
            m.b[7].setOnMouseClicked(mouseEvent -> primaryStage.setScene(Capropos.getInstance().scene));

        }

    }
}