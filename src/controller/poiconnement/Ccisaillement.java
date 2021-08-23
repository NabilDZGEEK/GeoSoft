package controller.poiconnement;

import Formules.poiconnement.cisaillement;
import controller.Cglissement;
import controller.Cpoiconnement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import model.Projet;
import org.w3c.dom.Element;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Ccisaillement{
    public static Ccisaillement instance;

    public static Ccisaillement getInstance() {
        if(instance==null){
            instance = new Ccisaillement();
        }
        return instance;
    }

    TextField[] t=new TextField[11];//données
    TextField[] sr=new TextField[4];
    ToggleGroup gtype=new ToggleGroup();
    ComboBox fondation;
    GridPane donnees,coefs,sous_resultat;
    boolean condition;
    Button calculer,enregistrer,effacer;
    ToggleGroup toggleGroup;
    public static String etat;
    NumberFormat formatter = new DecimalFormat("#0.00");
    Ccisaillement(){

        Scene scene=Cpoiconnement.getInstance().scene;

        donnees= (GridPane) scene.lookup("#données");
        coefs= (GridPane) scene.lookup("#coefs");
        fondation=(ComboBox) scene.lookup("#fondation");
        sous_resultat=(GridPane) scene.lookup("#sous_resultat");
        for(int i=0;i<11;i++){
            t[i]=(TextField) donnees.getChildren().get(11+i);
        }

        sr[0]=(TextField) sous_resultat.getChildren().get(4);
        sr[1]=(TextField) sous_resultat.getChildren().get(5);
        sr[2]=(TextField) sous_resultat.getChildren().get(6);
        sr[3]=(TextField) sous_resultat.getChildren().get(7);

        RadioButton radio1,radio2;
        radio1=(RadioButton)scene.lookup("#radio1");
        radio2=(RadioButton)scene.lookup("#radio2");
        toggleGroup = new ToggleGroup();
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        VBox type_sol=(VBox) scene.lookup("#type");
        HBox types= (HBox) type_sol.getChildren().get(1);
        RadioButton b=(RadioButton)types.getChildren().get(0);b.setToggleGroup(gtype);
        b=(RadioButton)types.getChildren().get(1);b.setToggleGroup(gtype);
        b=(RadioButton)types.getChildren().get(2);b.setToggleGroup(gtype);

        Label label;

        label=(Label) donnees.getChildren().get(0);label.setTooltip(new Tooltip("inclinaison de la base de fondation par rapport à l'horizontal"));
        label=(Label) donnees.getChildren().get(1);label.setTooltip(new Tooltip("Largeur du mur"));
        label=(Label) donnees.getChildren().get(2);label.setTooltip(new Tooltip("Longueur du mur"));
        label=(Label) donnees.getChildren().get(3);label.setTooltip(new Tooltip("Densité déjaugée du sol"));
        label=(Label) donnees.getChildren().get(4);label.setTooltip(new Tooltip("Profendeur d'encastrement"));
        label=(Label) donnees.getChildren().get(5);label.setTooltip(new Tooltip("Cohésion non drainnée"));
        label=(Label) donnees.getChildren().get(6);label.setTooltip(new Tooltip("Angle de frottement"));
        label=(Label) donnees.getChildren().get(7);label.setTooltip(new Tooltip("Cohesion du sol"));
        label=(Label) donnees.getChildren().get(8);label.setTooltip(new Tooltip("surcharges"));
        label=(Label) donnees.getChildren().get(9);label.setTooltip(new Tooltip("densité du mur"));
        label=(Label) donnees.getChildren().get(10);label.setTooltip(new Tooltip("Hauteur du mur"));

        fondation.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                etat=t1.toString();Label lab;
                switch (etat){
                    case "carré":t[2].setDisable(true);lab=(Label) donnees.getChildren().get(1);lab.setText("B");break;
                    case "circulaire": lab=(Label) donnees.getChildren().get(1);lab.setText("R");t[2].setDisable(true);break;
                    default:t[2].setDisable(false);lab=(Label) donnees.getChildren().get(1);lab.setText("B");
                }
            }
        });
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if(t1!=null){
                    //Button radio sélectionné
                    if(((RadioButton)t1).getId().equals("radio1")){//condition drainnee
                        condition=true;
                        type_sol.setVisible(false);
                        t[5].setDisable(true);
                    }else{//condition non drainnee
                        condition=false;
                        type_sol.setVisible(true);
                        t[5].setDisable(false);
                    }
                }
            }
        });
        calculer =(Button) scene.lookup("#calculer");
        enregistrer =(Button) scene.lookup("#enregistrer");
        effacer =(Button) scene.lookup("#effacer");
        effacer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i=11;i<22;i++){
                    TextField t= (TextField) donnees.getChildren().get(i);
                    t.setText("");
                }
            }
        });

    }
    public void config(){
        calculer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                double[] tt=new double[11];

                tt[0]=Double.parseDouble(t[0].getText());
                tt[1]=Double.parseDouble(t[1].getText());
                if(etat.equals("carré")){
                    tt[2]=tt[1];
                }else{
                    tt[2]=Double.parseDouble(t[2].getText());
                }

                tt[3]=Double.parseDouble(t[3].getText());
                tt[6]=Double.parseDouble(t[6].getText());
                tt[7]=Double.parseDouble(t[7].getText());
                tt[8]=Double.parseDouble(t[8].getText());
                tt[9]=Double.parseDouble(t[9].getText());
                tt[10]=Double.parseDouble(t[10].getText());


                double qnetv=0,rvdv = 0;
                double b_prime,l_prime,a_prime;

                cisaillement.calcul_e();

                b_prime=tt[1]-2*cisaillement.e;
                l_prime=tt[2]-2*cisaillement.e;

                TextField f;
                f=(TextField) sous_resultat.getChildren().get(4);f.setText(String.valueOf(b_prime));
                f=(TextField) sous_resultat.getChildren().get(5);f.setText(String.valueOf(l_prime));
                if(etat.equals("filante")){
                    a_prime=tt[1]*tt[2]*(1-2*cisaillement.e/tt[1]);
                }else{
                    a_prime=l_prime*b_prime;
                }
                if(etat.equals("circulaire")){
                    a_prime=cisaillement.calculer_a_circulaire(tt[1]);
                }
                f=(TextField) sous_resultat.getChildren().get(6);f.setText(String.valueOf(a_prime));
                f=(TextField) sous_resultat.getChildren().get(7);f.setText(String.valueOf(cisaillement.e));
                if(condition){
                    tt[4]=Double.parseDouble(t[4].getText());

                    qnetv= cisaillement.calcul_drainnee(tt[6],tt[7],tt[10],tt[8],tt[9],tt[0],tt[1],tt[2],tt[3],tt[4]);
                    rvdv=cisaillement.calcul_rvd(qnetv,a_prime,2.8);
                }else {
                    RadioButton rb=(RadioButton) gtype.getSelectedToggle();
                    switch (rb.getText()){
                        case "coherent":tt[5]=Double.parseDouble(t[5].getText());
                            qnetv=cisaillement.calcul_non_drainnee_coherant(tt[5],tt[8],tt[0],tt[1],tt[3]);break;
                        case "non coherent":qnetv=cisaillement.calcul_non_drainnee_non_coherant(tt[6],tt[1],tt[3]);break;
                        case "heterogène" :tt[5]=Double.parseDouble(t[5].getText());
                            qnetv=cisaillement.calcul_non_drainnee_heterogene(tt[5],tt[8],tt[6],tt[0],tt[1],tt[2],tt[3]);
                    }
                    rvdv=cisaillement.calcul_rvd(qnetv,a_prime,1.68);
                }

                for(int i=0;i<12;i++){
                    Label lab=(Label) coefs.getChildren().get(12+i);
                    lab.setText(formatter.format(cisaillement.cofs[i]));
                }
                TextField vkt=(TextField) Cpoiconnement.instance.scene.lookup("#vk");
                vkt.setText(formatter.format(Cglissement.getInstance().vk));
                Label res=(Label) Cpoiconnement.instance.scene.lookup("#res");
                Cpoiconnement.getInstance().qnet.setText(formatter.format(qnetv));
                Cpoiconnement.getInstance().rvd.setText(formatter.format(rvdv));
                if(Cglissement.getInstance().vk>tt[1]*tt[2]*Double.parseDouble(t[4].getText())*tt[4]+rvdv){
                    res.setText("INSTABLE");
                    res.setTextFill(Color.web("#A1170E"));
                }else {
                    res.setText("STABLE");
                    res.setTextFill(Color.web("#19A112"));
                }
            }
        });
        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Element cisaillement= (Element) Projet.getInstance().dom.getElementsByTagName("cisaillement").item(0);

                cisaillement.setAttribute("condition",((RadioButton)(toggleGroup.getSelectedToggle())).getText());
                cisaillement.setAttribute("fondation",fondation.getSelectionModel().getSelectedItem().toString());
                if(!condition){
                    RadioButton rb=(RadioButton) gtype.getSelectedToggle();
                    switch (rb.getText()){
                        case "coherent":cisaillement.setAttribute("type","0");break;
                        case "non coherent":cisaillement.setAttribute("type","1");break;
                        case "heterogène" :cisaillement.setAttribute("type","2");
                    }
                }
                cisaillement.setAttribute("a",t[0].getText());
                cisaillement.setAttribute("b",t[1].getText());
                cisaillement.setAttribute("l",t[2].getText());
                cisaillement.setAttribute("y1",t[3].getText());
                cisaillement.setAttribute("d",t[4].getText());
                cisaillement.setAttribute("cu",t[5].getText());
                cisaillement.setAttribute("phi",t[6].getText());
                cisaillement.setAttribute("c",t[7].getText());
                cisaillement.setAttribute("q",t[8].getText());
                cisaillement.setAttribute("y3",t[9].getText());
                cisaillement.setAttribute("h",t[10].getText());

                cisaillement.setAttribute("bp",sr[0].getText());
                cisaillement.setAttribute("lp",sr[1].getText());
                cisaillement.setAttribute("ap",sr[2].getText());
                cisaillement.setAttribute("e",sr[3].getText());

                cisaillement.setAttribute("vk",Cpoiconnement.getInstance().vk.getText());
                cisaillement.setAttribute("qnet",Cpoiconnement.getInstance().qnet.getText());
                cisaillement.setAttribute("rvd",Cpoiconnement.getInstance().rvd.getText());
                Projet.getInstance().ecrire();
            }
        });
    }
    public void réinitialiser(){
        Element cisaillement= (Element) Projet.getInstance().dom.getElementsByTagName("cisaillement").item(0);
        if(cisaillement.hasAttributes()){
            switch (cisaillement.getAttribute("condition")){
                case "condition drainée":toggleGroup.selectToggle(toggleGroup.getToggles().get(0));break;
                case "condition non drainée": toggleGroup.selectToggle(toggleGroup.getToggles().get(1));
                    gtype.selectToggle(gtype.getToggles().get(Integer.parseInt(cisaillement.getAttribute("type"))));
            }
            fondation.setValue(cisaillement.getAttribute("fondation"));
            t[0].setText(cisaillement.getAttribute("a"));
            t[1].setText(cisaillement.getAttribute("b"));
            t[2].setText(cisaillement.getAttribute("l"));
            t[3].setText(cisaillement.getAttribute("y1"));
            t[4].setText(cisaillement.getAttribute("d"));
            t[5].setText(cisaillement.getAttribute("cu"));
            t[6].setText(cisaillement.getAttribute("phi"));
            t[7].setText(cisaillement.getAttribute("c"));
            t[8].setText(cisaillement.getAttribute("q"));
            t[9].setText(cisaillement.getAttribute("y3"));
            t[10].setText(cisaillement.getAttribute("h"));

            sr[0].setText(cisaillement.getAttribute("bp"));
            sr[1].setText(cisaillement.getAttribute("lp"));
            sr[2].setText(cisaillement.getAttribute("ap"));
            sr[3].setText(cisaillement.getAttribute("e"));
            Cpoiconnement.getInstance().vk.setText(cisaillement.getAttribute("vk"));
            Cpoiconnement.getInstance().qnet.setText(cisaillement.getAttribute("qnet"));
            Cpoiconnement.getInstance().rvd.setText(cisaillement.getAttribute("rvd"));
        }else{
            toggleGroup.selectToggle(null);
            fondation.setValue("choisir");
            gtype.selectToggle(null);
            for(int i=0;i<11;i++){
                t[i].setText("");
            }
            for(int i=0;i<4;i++){
                sr[i].setText("");
            }
            Cpoiconnement.getInstance().vk.setText("");
            Cpoiconnement.getInstance().qnet.setText("");
            Cpoiconnement.getInstance().rvd.setText("");
        }

    }
}
