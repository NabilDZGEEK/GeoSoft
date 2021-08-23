package controller.poiconnement;

import Formules.poiconnement.cisaillement;
import Formules.poiconnement.penetrometre;
import Formules.poiconnement.pressiomètre;
import controller.Cglissement;
import controller.Cpoiconnement;
import controller.Cpoussee;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import model.Projet;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Cpenetrometre  {
    public static Cpenetrometre instance;


    public static Cpenetrometre getInstance() {
        if(instance==null){
            instance = new Cpenetrometre();
        }
        return instance;
    }
    NumberFormat formatter = new DecimalFormat("#0.00");
    double kc=0,qce=0,de=0,isigma=1,ibeta=1;
    Button calculer,enregistrer,effacer;
    GridPane table,données;
    double[][] values;
    TextField[] t;
    ComboBox sol,fondation,inclin_charge;
    Boolean condition;
    ToggleGroup toggleGroup = new ToggleGroup();
    Cpenetrometre(){
        Scene scene=Cpoiconnement.getInstance().scene;
        calculer =(Button) scene.lookup("#calculer");
        enregistrer =(Button) scene.lookup("#enregistrer");
        effacer =(Button) scene.lookup("#effacer");
        table=(GridPane) scene.lookup("#table");
        values=new double[][]{{0.07,0.007,1.3,0.27},{0.1,0.007,1.5,0.27},{0.04,0.006,2,0.09},{0.03,0.02,5,0.09},{0.04,0.03,3,0.11},{0.05,0.04,3,0.11},{0.04,0.03,3,0.11},{0.05,0.04,3,0.11}};

        fondation=(ComboBox) scene.lookup("#fondation");
        sol=(ComboBox) scene.lookup("#sol");
        inclin_charge=(ComboBox) scene.lookup("#inclin_charge");
        RadioButton radio1,radio2,radio3;
        radio1=(RadioButton)scene.lookup("#radio1");
        radio2=(RadioButton)scene.lookup("#radio2");
        radio3=(RadioButton)scene.lookup("#radio3");

        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        radio3.setToggleGroup(toggleGroup);
        données=(GridPane) scene.lookup("#données");
        Label[] lab=new Label[5];
        lab[0]=(Label) données.getChildren().get(0);
        lab[1]=(Label) données.getChildren().get(1);
        lab[2]=(Label) données.getChildren().get(2);
        lab[3]=(Label) données.getChildren().get(3);
        lab[4]=(Label) données.getChildren().get(4);

        lab[0].setTooltip(new Tooltip("Largeur du mur"));
        lab[1].setTooltip(new Tooltip("Longeur du mur"));
        lab[2].setTooltip(new Tooltip("Angle du talus"));
        lab[3].setTooltip(new Tooltip("Distance entre talus et fondation"));
        lab[4].setTooltip(new Tooltip("Densité déjaugée du sol"));

        t=new TextField[5];
        t[0]=(TextField) données.getChildren().get(5);
        t[1]=(TextField) données.getChildren().get(6);
        t[2]=(TextField) données.getChildren().get(7);
        t[3]=(TextField) données.getChildren().get(8);
        t[4]=(TextField) données.getChildren().get(9);
    }

    public void config(){
        calculer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double somme=0,produit=1,cpt=0;//compteur de lignes saisies
                for(int i=1;i<14;i++){
                    TextField plit=(TextField) table.getChildren().get(i);
                    TextField hit=(TextField) table.getChildren().get(14+i);//17->31
                    TextField plit_hit=(TextField) table.getChildren().get(28+i);//33->47
                    if(plit.getText()!=""&hit.getText()!=""){
                        double pli=Double.parseDouble(plit.getText());
                        double hi=Double.parseDouble(hit.getText());
                        double pli_hi=pli*hi;
                        plit_hit.setText(formatter.format(pli_hi));
                        somme+=pli_hi;produit*=pli_hi;
                        cpt++;
                    }
                }
                qce=Math.pow(produit,1/cpt);
                TextField qcet=(TextField) Cpoiconnement.instance.scene.lookup("#qce");
                qcet.setText(formatter.format(qce));
                de=somme/qce;
                String fonds=fondation.getSelectionModel().getSelectedItem().toString();
                int sols=sol.getSelectionModel().getSelectedIndex();
                int ligne;

                penetrometre.calcul_e();
                double b_prime,l_prime,a_prime=0;
                double b=Double.parseDouble(t[0].getText());
                double l=Double.parseDouble(t[1].getText());
                b_prime=b-2*pressiomètre.e;
                l_prime=l-2*pressiomètre.e;
                double db=de/b;
                switch (fonds){
                    case "carré":ligne=sols*2+1;
                        kc=penetrometre.ka(values[ligne][0],values[ligne][1],values[ligne][2],values[ligne][3],db);
                        a_prime=l_prime*b_prime;
                        break;
                    case "filante":ligne=sols*2;
                        a_prime=b*l*(1-2* cisaillement.e/b);
                        kc=penetrometre.ka(values[ligne][0],values[ligne][1],values[ligne][2],values[ligne][3],db);
                        break;
                    case "rectangulaire":
                        ligne=sols*2+1;
                        double kcc=penetrometre.ka(values[ligne][0],values[ligne][1],values[ligne][2],values[ligne][3],db);
                        ligne=sols*2;
                        double kcf= penetrometre.ka(values[ligne][0],values[ligne][1],values[ligne][2],values[ligne][3],db);
                        double bl=b/(Double.parseDouble(t[1].getText())-2*pressiomètre.e);//B/L
                        kc=kcf*(1-bl)+kcc*bl;
                        a_prime=l_prime*b_prime;
                }
                double sigmad=Math.atan(Cpoussee.getInstance().pa*Math.cos(Cpoussee.instance.sigma*Math.PI/180)/ Cglissement.getInstance().vk*Math.PI/180);

                double beta=Double.parseDouble(t[2].getText());
                double d=Double.parseDouble(t[3].getText());
                RadioButton selected= (RadioButton) toggleGroup.getSelectedToggle();
                double qnetv;

                if(d>8*beta){//fondation à proximité
                    switch (selected.getId()){
                        case "radio1":ibeta=pressiomètre.ibeta_drainnee(beta,d);condition=true;break;
                        case "radio2":ibeta=pressiomètre.ibeta_non_drainnee(beta,d,b,de);condition=false;break;
                        case "radio3":ibeta=pressiomètre.ibeta_inter(beta,d,b,de,Cpoussee.getInstance().c,Cpoussee.getInstance().gama,Cpoussee.getInstance().phi);condition=true;
                    }
                    if(!inclin_charge.getSelectionModel().isSelected(0)) {//!=0
                        switch (selected.getId()){
                            case "radio1": isigma=pressiomètre.isigma_drainnee(sigmad,db);break;
                            case "radio2":isigma=pressiomètre.isigma_non_drainnee(sigmad);break;
                            case "radio3":isigma=pressiomètre.isigma_inter(sigmad,db,Cpoussee.getInstance().c, Cpoussee.getInstance().gama,b,Cpoussee.getInstance().phi);
                        }
                    }
                }else{
                    switch (selected.getId()){
                        case "radio1":ibeta=pressiomètre.ibeta_drainnee(beta,d);condition=true;break;
                        case "radio2":ibeta=pressiomètre.ibeta_non_drainnee(beta,d,b,de);condition=false;break;
                        case "radio3":ibeta=pressiomètre.ibeta_inter(beta,d,b,de,Cpoussee.getInstance().c,Cpoussee.getInstance().gama,Cpoussee.getInstance().phi);condition=true;
                    }
                }


                if(inclin_charge.getSelectionModel().isSelected(2)&d>8*beta) {
                    qnetv=qce*Math.min(ibeta/isigma,isigma);
                }else{
                    qnetv=qce*isigma*ibeta;
                }
                double rvdv;
                if(condition){
                    rvdv=pressiomètre.calcul_rvd(qnetv,a_prime,2.8);
                }else{
                    rvdv= pressiomètre.calcul_rvd(qnetv,a_prime,1.68);
                }
                Cpoiconnement.getInstance().qnet.setText(formatter.format(qnetv));
                Cpoiconnement.getInstance().rvd.setText(formatter.format(rvdv));

                TextField vkt=(TextField) Cpoiconnement.instance.scene.lookup("#vk");
                vkt.setText(formatter.format(Cglissement.getInstance().vk));
                Label res=(Label) Cpoiconnement.instance.scene.lookup("#res");
                if(Cglissement.getInstance().vk>b*l*Double.parseDouble(t[4].getText())*d+rvdv){
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
                Element penetrometre= (Element) Projet.getInstance().dom.getElementsByTagName("penetrometre").item(0);
                penetrometre.setAttribute("sol",sol.getSelectionModel().getSelectedItem().toString());
                penetrometre.setAttribute("fondation",fondation.getSelectionModel().getSelectedItem().toString());
                RadioButton rb;
                for(int i=0;i<3;i++){
                    rb= (RadioButton) toggleGroup.getToggles().get(i);
                    if(rb.equals(toggleGroup.getSelectedToggle())){
                        penetrometre.setAttribute("g",String.valueOf(i));
                        break;
                    }
                }
                /* remplissage du tableau */
                /* remplissage du tableau */
                Element tableau= (Element) Projet.getInstance().dom.getElementsByTagName("tableau").item(1);
                NodeList lignes=Projet.getInstance().dom.getElementsByTagName("ligne");
                for(byte i=1;i<14;i++){
                    TextField plit=(TextField) table.getChildren().get(i);
                    TextField hit=(TextField) table.getChildren().get(14+i);
                    TextField plit_hit=(TextField) table.getChildren().get(28+i);
                    Element ligne= (Element) lignes.item(14+i);

                    ligne.setAttribute("plit",plit.getText());
                    ligne.setAttribute("hit",hit.getText());
                    ligne.setAttribute("plit_hit",plit_hit.getText());
                }
                TextField qcet=(TextField) Cpoiconnement.instance.scene.lookup("#qce");
                tableau.setAttribute("qce",qcet.getText());


                penetrometre.setAttribute("inclinaison",inclin_charge.getSelectionModel().getSelectedItem().toString());
                penetrometre.setAttribute("b",t[0].getText());
                penetrometre.setAttribute("l",t[1].getText());
                penetrometre.setAttribute("beta",t[2].getText());
                penetrometre.setAttribute("d",t[3].getText());
                penetrometre.setAttribute("y1",t[4].getText());

                penetrometre.setAttribute("vk",Cpoiconnement.getInstance().vk.getText());
                penetrometre.setAttribute("qnet",Cpoiconnement.getInstance().qnet.getText());
                penetrometre.setAttribute("rvd",Cpoiconnement.getInstance().rvd.getText());
                Projet.getInstance().ecrire();
            }
        });
        effacer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i=5;i<10;i++){
                    TextField t= (TextField) données.getChildren().get(i);
                    t.setText("");
                }
            }
        });
    }
    public void remplire(){
        Element penetrometre= (Element) Projet.getInstance().dom.getElementsByTagName("penetrometre").item(0);
        if(penetrometre.hasAttributes()){
            sol.setValue(penetrometre.getAttribute("sol"));
            fondation.setValue(penetrometre.getAttribute("fondation"));
            inclin_charge.setValue(penetrometre.getAttribute("inclinaison"));
            toggleGroup.selectToggle(toggleGroup.getToggles().get(Integer.parseInt(penetrometre.getAttribute("g"))));

            t[0].setText(penetrometre.getAttribute("b"));
            t[1].setText(penetrometre.getAttribute("l"));
            t[2].setText(penetrometre.getAttribute("beta"));
            t[3].setText(penetrometre.getAttribute("d"));
            t[4].setText(penetrometre.getAttribute("y1"));

            /*remplissage du tableau*/
            Element tableau= (Element) Projet.getInstance().dom.getElementsByTagName("tableau").item(1);
            NodeList lignes=Projet.getInstance().dom.getElementsByTagName("ligne");
            for(byte i=1;i<14;i++){
                TextField plit=(TextField) table.getChildren().get(i);
                TextField hit=(TextField) table.getChildren().get(14+i);
                TextField plit_hit=(TextField) table.getChildren().get(28+i);
                Element ligne= (Element) lignes.item(i+14);
                plit.setText(ligne.getAttribute("plit"));
                hit.setText(ligne.getAttribute("hit"));
                plit_hit.setText(ligne.getAttribute("plit_hit"));
            }
            TextField qcet=(TextField) Cpoiconnement.instance.scene.lookup("#qce");
            qcet.setText(tableau.getAttribute("qce"));

            Cpoiconnement.getInstance().vk.setText(penetrometre.getAttribute("vk"));
            Cpoiconnement.getInstance().qnet.setText(penetrometre.getAttribute("qnet"));
            Cpoiconnement.getInstance().rvd.setText(penetrometre.getAttribute("rvd"));
        }else{
            sol.setValue("choisir");
            fondation.setValue("choisir");
            inclin_charge.setValue("choisir");
            toggleGroup.selectToggle(null);
            for(byte i=0;i<5;i++){
                t[i].setText("");
            }
            /* vider le tableau */
            for(byte i=1;i<14;i++){
                ((TextField)(table.getChildren().get(i))).setText("");
            }
            for(byte i=15;i<28;i++){
                ((TextField)(table.getChildren().get(i))).setText("");
            }
            for(byte i=29;i<42;i++){
                ((TextField)(table.getChildren().get(i))).setText("");
            }
            TextField qcet=(TextField) Cpoiconnement.instance.scene.lookup("#qce");
            qcet.setText("");

            Cpoiconnement.getInstance().vk.setText("");
            Cpoiconnement.getInstance().qnet.setText("");
            Cpoiconnement.getInstance().rvd.setText("");
        }

    }
}
