/**
 * @author nabil AIT SAID
 * Date : 04/09/2021
 */

import javafx.application.Application;
import controller.Cprincipale;
import controller.Chome;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        primaryStage.setTitle("GeoSoft");
        primaryStage.getIcons().add(new Image("/images/logo.png"));
        //creation d'un controlleur pour le menu
        new Cprincipale();
        Cprincipale.primaryStage=primaryStage;
        primaryStage.setScene(Chome.getInstance().scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
