package controller;

import Formules.*;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Projet;
import notification.Notification;
import org.w3c.dom.Element;
import model.Imprimante;


public class Cpoussee extends Controller{

    public static Cpoussee instance;


    public static Cpoussee getInstance() {
        if(instance==null){
            instance = new Cpoussee();
        }
        return instance;
    }
    public double phi=0,sigma=0,c=0,gama=0,h=0,q=0,pa_non_elu=0,pa=0;

    Button enregistrer;

    char etat;//"statique ou dynamique
    private int rang;//la methode séléctionnée
    final TextArea info;
    private final CheckBox c1,c2;//Les checkbox
    private final RadioButton radio1,radio2;//static ou dynamique

    private final TextField[]t=new TextField[11];//les champs de données
    private final TextField[] res=new TextField[5];

    private final ComboBox<String> combo;//selection methode

    private final String[] static_methods={"RANKINE","PONCELET","COULOMB"};
    private final String[]dynamic_methods={"RPA","MONONOBE OKABE"};
    private final String[] static_infos={"RANKINE","PONCELET","COULOMB"};
    private final String[]dynamic_infos={"RPA","MONONOBE OKABE"};
    private void desactiver(boolean[] vect){//activation des entrées t[1,2,3]
        for (int i=0;i< vect.length;i++){
            t[i+1].setDisable(vect[i]);//vect[0]=t[1]...
        }
    }
    ToggleGroup toggleGroup;
    Cpoussee(){
        super("poussee.fxml",1);
        final Button calculer,effacer,imprimer;
        /*récupération des éléments a partir de la vue*/
        GridPane resultats = (GridPane) scene.lookup("#resultats");
        GridPane donnees = (GridPane) scene.lookup("#donnees");

        calculer= (Button) scene.lookup("#calculer");
        enregistrer=(Button) scene.lookup("#enregistrer");
        effacer=(Button) scene.lookup("#effacer");
        imprimer=(Button) scene.lookup("#imprimer");

        info=(TextArea)scene.lookup("#info");
        combo=(ComboBox<String>)scene.lookup("#combo");
        c1=(CheckBox) scene.lookup("#c1");
        c2=(CheckBox) scene.lookup("#c2");
        radio1=(RadioButton)scene.lookup("#radio1");
        radio2=(RadioButton)scene.lookup("#radio2");
        toggleGroup = new ToggleGroup();

        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);

        Label[] l = new Label[11];
        for(int i = 0; i<11; i++){
            l[i]=(Label) donnees.getChildren().get(i);
            t[i]=(TextField) donnees.getChildren().get(i+11);
        }
        for(int i=0;i<5;i++){
            res[i]= (TextField) resultats.getChildren().get(i+5);
        }

        t[8].disableProperty().bind(Bindings.not(c1.selectedProperty()));
        t[9].disableProperty().bind(Bindings.not(c1.selectedProperty()));
        t[10].disableProperty().bind(Bindings.not(c2.selectedProperty()));


        l[0].setTooltip(new Tooltip("angle de frotement du sol"));
        l[1].setTooltip(new Tooltip("frotement sol/structure"));
        l[2].setTooltip(new Tooltip("fruit du mur"));
        l[3].setTooltip(new Tooltip("angle de surface"));
        l[4].setTooltip(new Tooltip("Cohesion du sol"));
        l[5].setTooltip(new Tooltip("coeff accélération horizontale"));
        l[6].setTooltip(new Tooltip("densité du sol"));
        l[7].setTooltip(new Tooltip("hauteur"));
        l[8].setTooltip(new Tooltip("densité d'eau"));
        l[9].setTooltip(new Tooltip("niveau piézomètrique"));
        l[10].setTooltip(new Tooltip("surcharges"));
        combo.setDisable(false);
        toggleGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {

            if(t1!=null){
                t[0].setDisable(false);
                t[4].setDisable(false);

                t[6].setDisable(false);
                t[7].setDisable(false);
                combo.getItems().clear();
                switch (((RadioButton)t1).getId()){
                    case "radio1":etat='s';combo.getItems().addAll(static_methods);t[5].setDisable(true);break;//actif
                    case "radio2":etat='d';combo.getItems().addAll((dynamic_methods));t[5].setDisable(false);//dynamic
                }
            }
        });

        combo.valueProperty().addListener((observableValue, o, t1) -> {

            if(t1!=null && !t1.equals("choisir")){
                ImageView im=(ImageView)scene.lookup("#im");
                if(etat=='s'){

                    if(t1.equals(static_methods[0])){
                        desactiver(new boolean[]{true, true, true});
                        info.setText(static_infos[0]);rang=0;
                        im.setImage(new Image("/images/poussee/rankine.png"));
                    }
                    else if(t1.equals(static_methods[1])){
                        desactiver(new boolean[]{false, false,false});
                        info.setText(static_infos[1]);rang=1;
                        im.setImage(new Image("/images/poussee/poncelet.png"));
                    }
                    else{
                        desactiver(new boolean[]{false, false,false});
                        info.setText(static_infos[2]);rang=2;
                        im.setImage(new Image("/images/poussee/coulomb.png"));
                    }
                }
                if(etat=='d'){
                    desactiver(new boolean[]{true,true,false});
                    if(t1.equals(dynamic_methods[0])){
                        info.setText(dynamic_infos[0]);rang=0;
                        im.setImage(new Image("/images/poussee/rpa.png"));
                    }
                    else{
                        info.setText(dynamic_infos[1]);rang=1;
                        im.setImage(new Image("/images/poussee/mononobe.png"));
                        desactiver(new boolean[]{false,false,false});
                    }
                }

            }


        });


        enregistrer.setOnAction(actionEvent -> {

            Element poussee= (Element) Projet.getInstance().dom.getElementsByTagName("poussee").item(0);
            poussee.setAttribute("type_calcul",String.valueOf(etat));
            poussee.setAttribute("methode",combo.getSelectionModel().getSelectedItem());
            if(c1.isSelected()){
                poussee.setAttribute("c1","oui");
            }else {
                poussee.setAttribute("c1","non");
            }
            if(c2.isSelected()){
                poussee.setAttribute("c2","oui");
            }else {
                poussee.setAttribute("c2","non");
            }
            poussee.setAttribute("phi",t[0].getText());
            poussee.setAttribute("sigma",t[1].getText());
            poussee.setAttribute("lambda",t[2].getText());
            poussee.setAttribute("beta",t[3].getText());
            poussee.setAttribute("C1",t[4].getText());
            poussee.setAttribute("kh",t[5].getText());
            poussee.setAttribute("y1",t[6].getText());
            poussee.setAttribute("h",t[7].getText());
            poussee.setAttribute("yw",t[8].getText());
            poussee.setAttribute("z",t[9].getText());
            poussee.setAttribute("q",t[10].getText());
            poussee.setAttribute("kap",res[0].getText());
            poussee.setAttribute("kan",res[1].getText());
            poussee.setAttribute("pap",res[2].getText());
            poussee.setAttribute("pan",res[3].getText());
            poussee.setAttribute("pa_elu",res[4].getText());
            Projet.getInstance().ecrire();
        });
        effacer.setOnMouseClicked(mouseEvent -> {
            /*vider tous les champs*/
            for(int i=0;i<11;i++){
                t[i].setText("");
            }
        });
        calculer.setOnAction(actionEvent -> {
            double[] tt= new double[11];//valeurs de données
            double kapv=0,kanv = 0;//resultat ka
            double papv,panv;//resultat pa
            double paeluv;//resultat pa(elu)

            try{
                tt[0]=Double.parseDouble(t[0].getText());phi=tt[0];
                tt[6]=Double.parseDouble(t[6].getText());gama=tt[6];
                tt[7]=Double.parseDouble(t[7].getText());h=tt[7];
                if(etat=='s'){//static
                    switch (rang){
                        case 0:
                            kapv=rankine.calculer(tt[0]);break;
                        case 1:
                            tt[1]=Double.parseDouble(t[1].getText());sigma=tt[1];
                            tt[2]=Double.parseDouble(t[2].getText());
                            tt[3]=Double.parseDouble(t[3].getText());
                            kapv=Poncelet.calculer(tt[0],tt[1],tt[2],tt[3]);break;
                        case 2:
                            tt[1]=Double.parseDouble(t[1].getText());sigma=tt[1];
                            tt[2]=Double.parseDouble(t[2].getText());
                            tt[3]=Double.parseDouble(t[3].getText());
                            kapv=Coulomb.calculer(tt[0],tt[1],tt[2],tt[3]);
                    }
                    kanv=kapv;
                }else{
                    double teta_pos,teta_neg;
                    tt[3]=Double.parseDouble(t[3].getText());
                    tt[5]=Double.parseDouble(t[5].getText());
                    teta_pos=Math.atan(tt[5]/(1+1/3.0*tt[5]))*180/Math.PI;
                    teta_neg=Math.atan(tt[5]/(1-1/3.0*tt[5]))*180/Math.PI;
                    System.out.println(teta_pos);
                    System.out.println(teta_neg);
                    switch (rang){
                        case 0: kapv=Rpa.calculer(tt[0],tt[3],teta_pos);
                                kanv=Rpa.calculer(tt[0],tt[3],teta_neg);
                                break;
                        case 1: tt[1]=Double.parseDouble(t[1].getText());sigma=tt[1];
                                tt[2]=Double.parseDouble(t[2].getText());
                                kapv=Mononobe_okabe.calculer(tt[0],tt[1],tt[2],tt[3],teta_pos);
                                kanv=Mononobe_okabe.calculer(tt[0],tt[1],tt[2],tt[3],teta_neg);
                    }
                }
                res[0].setText(formatter.format(kapv));
                res[1].setText(formatter.format(kanv));
                double paeluvp=0,paeluvm=0;//pa(elu)+,pa(elu)-
                final double tan = Math.tan(tt[0] * Math.PI / 180);
                final double cos = Math.cos(tt[1] * Math.PI / 180);
                double cp=(tt[4]/tan)*(1-kapv* cos);
                double cm=(tt[4]/tan)*(1-kanv* cos);
                papv=0.5*kapv*tt[6]*tt[7]*tt[7]-cp;
                panv=0.5*kanv*tt[6]*tt[7]*tt[7]-cm;
                if(!t[8].getText().equals("") & !t[9].getText().equals("")) {
                    tt[8] = Double.parseDouble(t[8].getText());
                    tt[9] = Double.parseDouble(t[9].getText());
                    papv+=tt[8]*tt[9];panv+=tt[8]*tt[9];
                }
                pa_non_elu=Math.max(papv,panv);

                if(etat=='s'){
                    paeluvp=papv*1.35;
                    paeluvm=panv*1.35;
                }

                if(!t[10].getText().equals("")){
                    q=Double.parseDouble(t[10].getText());
                        papv+=q*kapv;
                        panv+=q*kanv;
                        if(etat=='s'){
                            paeluvp+=kapv*q*1.5;
                            paeluvm+=kanv*q*1.5;
                        }
                }
                res[2].setText(formatter.format(papv));
                res[3].setText(formatter.format(panv));
                if(etat=='s'){
                    paeluv=Math.max(paeluvp,paeluvm);//max(pa(elu)+,pa(elu)-)
                }else{
                    paeluv=pa_non_elu;
                }

                res[4].setText(formatter.format(paeluv));
                pa=paeluv;
            }catch (NumberFormatException e){

                Notification.Notifier.INSTANCE.notifyError("Erreur","Champs manquants ou érronés");
            }

        });
        imprimer.setOnMouseClicked(mouseEvent -> Imprimante.getInstance().imprimer(root));
    }
    public void reinitialiser(){

        Element poussee= (Element) Projet.getInstance().dom.getElementsByTagName("poussee").item(0);

        if(poussee.hasAttributes()){
            if(poussee.getAttribute("type_calcul").equals("s")){
                etat='s';
                radio1.setSelected(true);
            }else  if(poussee.getAttribute("type_calcul").equals("d")){
                etat='d';
                radio2.setSelected(true);
            }
            System.out.println(((RadioButton) (toggleGroup.getSelectedToggle())).getText());
            combo.setValue(poussee.getAttribute("methode"));
            c1.setSelected(poussee.getAttribute("c1").equals("oui"));
            c2.setSelected(poussee.getAttribute("c2").equals("oui"));
            t[0].setText(poussee.getAttribute("phi"));
            t[1].setText(poussee.getAttribute("sigma"));
            t[2].setText(poussee.getAttribute("lambda"));
            t[3].setText(poussee.getAttribute("beta"));
            t[4].setText(poussee.getAttribute("C1"));
            t[5].setText(poussee.getAttribute("kh"));
            t[6].setText(poussee.getAttribute("y1"));
            t[7].setText(poussee.getAttribute("h"));
            t[8].setText(poussee.getAttribute("yw"));
            t[9].setText(poussee.getAttribute("z"));
            t[10].setText(poussee.getAttribute("q"));
            res[0].setText(poussee.getAttribute("kap"));
            res[1].setText(poussee.getAttribute("kan"));
            res[2].setText(poussee.getAttribute("pap"));
            res[3].setText(poussee.getAttribute("pan"));
            res[4].setText(poussee.getAttribute("pa_elu"));

            if(!t[0].getText().equals("")) phi=Double.parseDouble(t[0].getText());
            if(!t[1].getText().equals("")) sigma=Double.parseDouble(t[1].getText());
            if(!t[4].getText().equals("")) c=Double.parseDouble(t[4].getText());
            if(!t[6].getText().equals("")) gama=Double.parseDouble(t[6].getText());
            if(!t[7].getText().equals("")) h=Double.parseDouble(t[7].getText());
            if(!t[10].getText().equals("")) q=Double.parseDouble(t[10].getText());
            if(!res[4].getText().equals("")) pa=Double.parseDouble(res[4].getText());

        }else{
            phi=0;sigma=0;c=0;gama=0;h=0;q=0;pa_non_elu=0;pa=0;
            toggleGroup.selectToggle(null);
            c1.setSelected(false);
            c2.setSelected(false);
            combo.setValue("choisir");
            for(byte i=0;i<11;i++){
                t[i].setText("");
            }
            for(byte i=0;i<5;i++){
                res[i].setText("");
            }
        }

    }
}
