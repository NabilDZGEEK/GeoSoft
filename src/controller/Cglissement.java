package controller;
import Formules.glissement;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Imprimante;
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
    void desactiver(boolean[] vect){//activation des entrées t[1,2,3]
        for (int i=0;i< vect.length;i++){
            t[i].setDisable(vect[i]);//vect[0]=t[1]...
        }
    }

    public double gama=0;

    Button enregistrer;


    public double vk=0,b=0;
    double cu=0;
    Double rd,fs;

    Double pah;
    boolean dr;

    GridPane donnees,resultats;
    private final TextField[]t=new TextField[7];
    private final Label[]l=new Label[3];
    private final TextField[] res=new TextField[3];
    ToggleGroup toggleGroup;
    Cglissement() {
        super("glissement.fxml",2);
        donnees=(GridPane)scene.lookup("#donnees");
        resultats=(GridPane)scene.lookup("#resultats");
        final Button calculer,effacer,imprimer;
        calculer=(Button) scene.lookup("#calculer");
        enregistrer=(Button) scene.lookup("#enregistrer");
        effacer=(Button) scene.lookup("#effacer");
        imprimer=(Button) scene.lookup("#imprimer");

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


        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            ImageView im=(ImageView)scene.lookup("#form_img");
            if(t1!=null){
                switch (((RadioButton)t1).getId()){
                    case "radio1":desactiver(new boolean[]{false,false,false,true,false,false});
                        im.setImage(new Image("/images/drainnée.png"));
                        l[1].setVisible(true);res[1].setVisible(true);
                        dr=true;break;
                    case "radio2":desactiver(new boolean[]{false,true,true,false,true,false});
                        im.setImage(new Image("/images/nondr.png"));
                        dr=false;
                        l[1].setVisible(false);res[1].setVisible(false);
                }
            }

        });
        enregistrer.setOnAction(actionEvent -> {
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
        });
        calculer.setOnAction(actionEvent -> {
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
            double val;
            if(cp.etat=='s') val=1.1; else val=1;
            if(fs>val){
                la.setText("STABLE");
                la.setTextFill(Color.web("#19A112"));
            }else{
                la.setText("INSTABLE");
                la.setTextFill(Color.web("#A1170E"));
            }
        });
        effacer.setOnMouseClicked(mouseEvent -> {
            for(int i=0;i<7;i++){
                t[i].setText("");
            }
        });
        imprimer.setOnMouseClicked(mouseEvent -> Imprimante.getInstance().imprimer(root));
    }
    public void reinitialiser(){
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
            if(!t[0].getText().equals("")) b=Double.parseDouble(t[0].getText());
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
            if(!res[1].getText().equals("")) vk=Double.parseDouble(res[2].getText());
            Label la= (Label) resultats.getChildren().get(8);
            double val;
            if(Cpoussee.getInstance().etat=='s') val=1.1; else val=1;
            if(!res[2].getText().equals("") && Double.parseDouble(res[2].getText())>val){
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