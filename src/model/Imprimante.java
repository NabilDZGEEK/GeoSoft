/**
 * @author nabil AIT SAID
 * La méthode imprimer() imprime une seule page
 * La méthode imprimerProjet() imprime tous le projet
 */
package model;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import notification.Notification;

import java.io.IOException;

public class Imprimante {
    static  Imprimante instance;
    public static Imprimante getInstance() {
        if(instance==null){
            instance=new Imprimante();
        }
        return instance;
    }

    public void imprimer(Node root){
        if(Printer.getDefaultPrinter()!=null){
            PrinterJob job=PrinterJob.createPrinterJob();
            job.getJobSettings().setPrintQuality(PrintQuality.HIGH);
            if (job.showPrintDialog(null)) {
                Node donnees=((BorderPane)root).getCenter();
                BorderPane racine;
                donnees.getTransforms().add(new Scale(0.85,0.85));
                try {
                    racine = FXMLLoader.load(getClass().getResource("/view/printpage.fxml"));
                    PageLayout pl=job.getPrinter().createPageLayout(Paper.A4,PageOrientation.PORTRAIT,0,0,0,0);
                    job.getJobSettings().setPageLayout(pl);
                    racine.setCenter(donnees);
                    racine.getStylesheets().add("/stylesheet.css");
                    if(job.printPage(racine)){
                        donnees.getTransforms().remove(0);
                        job.endJob();

                        ((BorderPane)root).setCenter(donnees);
                        Notification.Notifier.INSTANCE.notifySuccess("Succès","imprimé avec succès");
                    }else{
                        Notification.Notifier.INSTANCE.notifyError("Echec","échec de l'impression");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }else {
            Notification.Notifier.INSTANCE.notifyError("Erreur","aucune imprimante est installé");
        }
    }
    public void imprimerProjet(){

        BorderPane racine;
        Scale scale=new Scale(0.85,0.85);
        if(Printer.getDefaultPrinter()!=null){
            try{
                racine = FXMLLoader.load(getClass().getResource("/view/printpage.fxml"));
                racine.getStylesheets().add("/stylesheet.css");

                PrinterJob job=PrinterJob.createPrinterJob();
                job.getJobSettings().setPrintQuality(PrintQuality.HIGH);
                PageLayout pl=job.getPrinter().createPageLayout(Paper.A4,PageOrientation.PORTRAIT,0,0,0,0);
                job.getJobSettings().setPageLayout(pl);

                if (job.showPrintDialog(null)) {
                    //impression de la 1ere page
                    BorderPane p=(BorderPane)Couvrirprojet.getInstance().root;
                    VBox v=new VBox(p.getTop(),p.getCenter());
                    racine.setCenter(v);
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    p.setTop(v.getChildren().get(0));
                    p.setCenter(v.getChildren().get(0));
                    System.out.println(racine.getCenter().getTransforms().size());
                    //p.getCenter().getTransforms().remove(0);

                    //impression des vérifications
                    p=(BorderPane)Cpoussee.getInstance().root;
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());


                    p=(BorderPane)Cglissement.getInstance().root;
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());



                    Cpoiconnement cp=Cpoiconnement.getInstance();
                    p=(BorderPane)cp.root;

                    cp.parent.getChildren().set(2,cp.pressiometre);
                    cp.combo.setValue("Pressiomètre");
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());

                    cp.parent.getChildren().set(2,cp.penetrometre);
                    cp.combo.setValue("Penetromètre");
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());

                    cp.parent.getChildren().set(2,cp.cissaiment);
                    cp.combo.setValue("Cisaillemment");
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());

                    cp.reinitialiser();

                    p=(BorderPane)Crenversement.getInstance().root;
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());

                    p=(BorderPane)Cinterne.getInstance().root;
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());

                    p=(BorderPane)Cinterne2.getInstance().root;
                    racine.setCenter(p.getCenter());
                    racine.getCenter().getTransforms().add(scale);
                    job.printPage(racine);
                    racine.getCenter().getTransforms().remove(0);
                    p.setCenter(racine.getCenter());


                    job.endJob();
                    Notification.Notifier.INSTANCE.notifySuccess("Succès","Projet imprimé avec succès");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }



        }else{
            Notification.Notifier.INSTANCE.notifyError("Echec","échec de l'impression");
        }
    }
}
