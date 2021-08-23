package controller;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class menu extends HBox {
    Button[] b=new Button[9];



    menu() {
        b[0]=new Button("Accueil");
        b[1]=new Button("Poussée");
        b[2]=new Button("Glissement");
        b[3]=new Button("Poiçonnement");
        b[4]=new Button("Renversement");
        b[5]=new Button("Interne");
        b[6]=new Button("interne2");
        b[7]=new Button("à propos");
        b[8]=new Button("Aide");

        this.getChildren().addAll(b);
    }
}
