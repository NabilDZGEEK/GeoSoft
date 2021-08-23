

import javafx.application.Application;
import controller.*;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("GeoSoft");
        //creation d'un controlleur pour le menu
        new Cprincipale();
        Cprincipale.primaryStage=primaryStage;

        primaryStage.setScene(Chome.getInstance().scene);
        primaryStage.show();
        //new test().test();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
