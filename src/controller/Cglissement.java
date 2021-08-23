package controller;
import Formules.glissement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Projet;
import org.w3c.dom.Element;


public class Cglissement extends Controller{
    public static Cglissement instance = null;


    public static Cglissement getInstance() {
        if(instance==null){
            instance = new Cglissement();
        }
        return instance;
    }
    void désactiver(boolean[] vect){//activation des entrées t[1,2,3]
        for (int i=0;i< vect.length;i++){
            t[i].setDisable(vect[i]);//vect[0]=t[1]...
        }
    }

    public double gama=0;
    private Button calculer,effacer,imprimer;
    Button enregistrer;


    public double vk=0,b=0;
    double cu=0;
    Double rd,fs;

    Double pah;


    boolean dr;

    GridPane donnees,resultats;
    private TextField[]t=new TextField[7];
    private Label[]l=new Label[3];
    private TextField[] res=new TextField[3];
    ToggleGroup toggleGroup;
    Cglissement() {
        super("glissement.fxml",2);
        donnees=(GridPane)scene.lookup("#donnees");
        resultats=(GridPane)scene.lookup("#resultats");

        calculer=(Button) scene.lookup("#calculer");
        enregistrer=(Button) scene.lookup("#enregistrer");
        effacer=(Button) scene.lookup("#effacer");


        for(int i=0;i<7;i++){
            t[i]=(TextField) donnees.getChildren().get(i+7);
        }
        for(int i=0;i<3;i++){
            l[i]=(Label) resultats.getChildren().get(i);
            res[i]=(TextField) resultats.getChildren().get(i+3);
        }

        Label d;
        d= (Label) donnees.getChildren().get(0);d.setTooltip(new Tooltip("Largeur du mur"));
        d= (Label) donnees.getChildren().get(1);d.setTooltip(new Tooltip("Angle de frotement du mur"));
        d= (Label) donnees.getChildren().get(2);d.setTooltip(new Tooltip("Cohesion du mur"));
        d= (Label) donnees.getChildren().get(3);d.setTooltip(new Tooltip("Cohesion non drainée du sol"));
        d= (Label) donnees.getChildren().get(4);d.setTooltip(new Tooltip("Densité du mur"));
        d= (Label) donnees.getChildren().get(5);d.setTooltip(new Tooltip("Coefficient partiel"));
        l[0].setTooltip(new Tooltip("Résistance au glissement"));
        l[1].setTooltip(new Tooltip("Charge vertical"));
        l[2].setTooltip(new Tooltip("Coefficient de sécurité "));

        RadioButton radio1,radio2;
        radio1=(RadioButton)scene.lookup("#radio1");
        radio2=(RadioButton)scene.lookup("#radio2");
        toggleGroup = new ToggleGroup();
        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);


        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {

                if(t1!=null){
                    switch (((RadioButton)t1).getId()){
                        case "radio1":désactiver(new boolean[]{false,false,false,true,false,false});

                            l[1].setVisible(true);res[1].setVisible(true);
                            dr=true;break;
                        case "radio2":désactiver(new boolean[]{false,true,true,false,true,false});

                            dr=false;
                            l[1].setVisible(false);res[1].setVisible(false);
                    }
                }

            }
        });
        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Element glissement= (Element) Projet.getInstance().dom.getElementsByTagName("glissement").item(0);

                glissement.setAttribute("condition",((RadioButton) toggleGroup.getSelectedToggle()).getText());
                glissement.setAttribute("b",t[0].getText());
                glissement.setAttribute("phi3k",t[1].getText());
                glissement.setAttribute("c3k",t[2].getText());
                glissement.setAttribute("cu1",t[3].getText());
                glissement.setAttribute("y",t[4].getText());
                glissement.setAttribute("yrh",t[5].getText());
                glissement.setAttribute("h",t[6].getText());
                glissement.setAttribute("rd",res[0].getText());
                glissement.setAttribute("vk",res[1].getText());
                glissement.setAttribute("fs",res[2].getText());

                Projet.getInstance().ecrire();
            }
        });
        calculer.setOnAction(new EventHandler<ActionEvent>() {


            @Override
            public void handle(ActionEvent actionEvent) {
                Cpoussee cp=Cpoussee.getInstance();
                pah=cp.pa*Math.cos(cp.sigma*Math.PI/180);
                double[]tt=new double[7];

                Double[] r;

                tt[0]=Double.parseDouble(t[0].getText());b=tt[0];
                tt[5]=Double.parseDouble(t[5].getText());
                if(dr){

                    tt[1]=Double.parseDouble(t[1].getText());
                    tt[2]=Double.parseDouble(t[2].getText());
                    tt[4]=Double.parseDouble(t[4].getText());gama=tt[4];
                    tt[6]=Double.parseDouble(t[6].getText());
                    r=glissement.drainee(tt[0],tt[1],tt[2],tt[4],tt[5],tt[6]);
                    rd=r[0];vk=r[1];
                    res[1].setText(formatter.format(vk));
                }else{
                    tt[3]=Double.parseDouble(t[3].getText());cu=tt[3];
                    rd=glissement.non_drainee(tt[0],tt[3],tt[5]);

                }
                res[0].setText(formatter.format(rd));
                fs=rd/pah;
                res[2].setText(formatter.format(fs));

                /* controle label stable */
                Label la= (Label) resultats.getChildren().get(8);
                if(fs>1.1){
                    la.setText("STABLE");
                    la.setTextFill(Color.web("#19A112"));
                }else{
                    la.setText("INSTABLE");
                    la.setTextFill(Color.web("#A1170E"));
                }
            }

        });
        effacer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i=0;i<7;i++){
                    t[i].setText("");
                }
            }
        });
    }
    public void réinitialiser(){
        Element glissement= (Element) Projet.getInstance().dom.getElementsByTagName("glissement").item(0);

        if(glissement.hasAttributes()){
            String cond=glissement.getAttribute("condition");
            if(cond.equals("condition drainée")){
                toggleGroup.getToggles().get(0).setSelected(true);
            }
            if(cond.equals("condition non drainée")){
                toggleGroup.getToggles().get(1).setSelected(true);
            }
            t[0].setText(glissement.getAttribute("b"));
            if(t[0].getText()!="") b=Double.parseDouble(t[0].getText());
            t[1].setText(glissement.getAttribute("phi3k"));
            t[2].setText(glissement.getAttribute("c3k"));
            t[3].setText(glissement.getAttribute("cu1"));
            t[4].setText( glissement.getAttribute("y"));
            t[5].setText(glissement.getAttribute("yrh"));
            t[6].setText(glissement.getAttribute("h"));
            res[0].setText(glissement.getAttribute("rd"));
            res[1].setText(glissement.getAttribute("vk"));
            res[2].setText(glissement.getAttribute("fs"));
            pah=Cpoussee.getInstance().pa*Math.cos(Cpoussee.getInstance().sigma*Math.PI/180);
            if(res[1].getText()!="") vk=Double.parseDouble(res[2].getText());
            Label la= (Label) resultats.getChildren().get(8);
            if(res[2].getText()!="" && Double.parseDouble(res[2].getText())>1.1){
                la.setText("STABLE");
                la.setTextFill(Color.web("#19A112"));
            }else{
                la.setText("INSTABLE");
                la.setTextFill(Color.web("#A1170E"));
            }
        }else{
            vk=0;b=0;
            toggleGroup.selectToggle(null);
            for(byte i=0;i<7;i++){
                t[i].setText("");
            }
            res[0].setText("");
            res[1].setText("");
            res[2].setText("");
            ((Label) resultats.getChildren().get(8)).setText("");
        }

    }
}
