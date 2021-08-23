package controller;
import Formules.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import model.Projet;
import notification.Notification;
import org.w3c.dom.Element;


public class Cpoussee extends Controller{

    public static Cpoussee instance;


    public static Cpoussee getInstance() {
        if(instance==null){
            instance = new Cpoussee();
        }
        return instance;
    }
    public double phi=0,sigma=0,c=0,gama=0,h=0,q=0,pa_non_elu=0,pa=0;
    private Button calculer,effacer,imprimer;
    Button enregistrer;

    private char etat;//"statique ou dynamique
    private int rang;//la methode séléctionnée
    private TextArea info;
    private CheckBox c1,c2;//Les checkbox
    private RadioButton radio1,radio2;//static ou dynamique

    private TextField[]t=new TextField[11];//les champs de données
    private Label[] l=new Label[11];//leurs labels
    private TextField[] res=new TextField[5];

    private ComboBox combo;//selection methode

    private GridPane donnees,resultats;
    private String[] static_methods={"RANKINE","PONCELET","COULOMB"};
    private String[]dynamic_methods={"RPA","MONONOBE OKABE"};
    private String[] static_infos={"RANKINE","PONCELET","COULOMB"};
    private  String[]dynamic_infos={"RPA","MONONOBE OKABE"};
    private void désactiver(boolean[] vect){//activation des entrées t[1,2,3]
        for (int i=0;i< vect.length;i++){
            t[i+1].setDisable(vect[i]);//vect[0]=t[1]...
        }
    }
    ToggleGroup toggleGroup;
    Cpoussee(){
        super("poussee.fxml",1);
        resultats= (GridPane) scene.lookup("#resultats");
        donnees= (GridPane) scene.lookup("#donnees");

        calculer= (Button) scene.lookup("#calculer");
        enregistrer=(Button) scene.lookup("#enregistrer");
        effacer=(Button) scene.lookup("#effacer");

        info=(TextArea)scene.lookup("#info");
        combo=(ComboBox)scene.lookup("#combo");
        c1=(CheckBox) scene.lookup("#c1");
        c2=(CheckBox) scene.lookup("#c2");
        radio1=(RadioButton)scene.lookup("#radio1");
        radio2=(RadioButton)scene.lookup("#radio2");
       toggleGroup = new ToggleGroup();

        radio1.setToggleGroup(toggleGroup);
        radio2.setToggleGroup(toggleGroup);
        for(int i=0;i<11;i++){
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

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if(t1!=null){
                    t[0].setDisable(false);
                    t[4].setDisable(false);
                    combo.setDisable(false);
                    t[6].setDisable(false);
                    t[7].setDisable(false);
                    switch (((RadioButton)t1).getId()){
                        case "radio1":etat='s';combo.getItems().clear();combo.getItems().addAll(static_methods);t[5].setDisable(true);break;//actif
                        case "radio2":etat='d';combo.getItems().clear();combo.getItems().addAll((dynamic_methods));t[5].setDisable(false);//dynamic
                    }
                }
            }
        });
        combo.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {

                try{
                    String res=combo.getSelectionModel().getSelectedItem().toString();
                    if(etat=='s'){

                        if(res.equals(static_methods[0])){
                            désactiver(new boolean[]{true, true, true});
                            info.setText(static_infos[0]);rang=0;

                        }
                        else if(res.equals(static_methods[1])){
                            désactiver(new boolean[]{false, false,false});
                            info.setText(static_infos[1]);rang=1;
                        }
                        else{
                            désactiver(new boolean[]{false, false,false});
                            info.setText(static_infos[2]);rang=2;
                        }
                    }
                    if(etat=='d'){
                        désactiver(new boolean[]{true,true,false});
                        if(res.equals(dynamic_methods[0])){info.setText(dynamic_infos[0]);rang=0;}
                        else{
                            info.setText(dynamic_infos[1]);rang=1;
                            désactiver(new boolean[]{false,false,false});
                        }
                    }
                }catch(NullPointerException e){
                    System.err.println(e);
                }
            }
        });


        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Element poussee= (Element) Projet.getInstance().dom.getElementsByTagName("poussee").item(0);
                poussee.setAttribute("type_calcul",String.valueOf(etat));
                poussee.setAttribute("methode",combo.getSelectionModel().getSelectedItem().toString());
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
            }
        });
        effacer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(int i=0;i<11;i++){
                    t[i].setText("");
                }
            }
        });
        calculer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double[] tt= new double[11];//valeurs de données
                double kapv=0,kamv = 0;//resultat ka
                double papv,pamv;//resultat pa
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
                        kamv=kapv;
                    }else{
                        double teta_pos,teta_neg;
                        tt[3]=Double.parseDouble(t[3].getText());
                        tt[5]=Double.parseDouble(t[5].getText());
                        teta_pos=Math.atan(tt[5]/(1+1/3*tt[5]))*180/Math.PI;
                        teta_neg=Math.atan(tt[5]/(1-1/3*tt[5]))*180/Math.PI;
                        switch (rang){
                            case 0:kapv=Rpa.calculer(tt[0],tt[3],teta_pos);
                                kamv=Rpa.calculer(tt[0],tt[3],teta_neg);
                                break;
                            case 1: tt[1]=Double.parseDouble(t[1].getText());sigma=tt[1];
                                tt[2]=Double.parseDouble(t[2].getText());
                                kapv=Mononobe_okabe.calculer(tt[0],tt[1],tt[2],tt[3],teta_pos);
                                kamv=Mononobe_okabe.calculer(tt[0],tt[1],tt[2],tt[3],teta_neg);
                        }
                    }
                    res[0].setText(formatter.format(kapv));
                    res[1].setText(formatter.format(kamv));
                    double paeluvp=0,paeluvm=0;//pa(elu)+,pa(elu)-
                    double cp=(tt[4]/Math.tan(tt[0]*Math.PI/180))*(1-kapv*Math.cos(tt[1]*Math.PI/180));
                    double cm=(tt[4]/Math.tan(tt[0]*Math.PI/180))*(1-kamv*Math.cos(tt[1]*Math.PI/180));
                    papv=0.5*kapv*tt[6]*tt[7]*tt[7]-cp;
                    pamv=0.5*kamv*tt[6]*tt[7]*tt[7]-cm;
                    if(t[8].getText()!=""&t[9].getText()!="") {
                        tt[8] = Double.parseDouble(t[8].getText());
                        tt[9] = Double.parseDouble(t[9].getText());
                        papv+=tt[8]*tt[9];pamv+=tt[8]*tt[9];
                    }
                    pa_non_elu=Math.max(papv,pamv);
                    paeluvp=papv*1.35;
                    paeluvm=pamv*1.35;
                    if(t[10].getText()!=""){
                        q=Double.parseDouble(t[10].getText());
                            papv+=q*kapv;
                            pamv+=q*kamv;
                            paeluvp+=kapv*q*1.5;
                            paeluvm+=kamv*q*1.5;

                    }
                    res[2].setText(formatter.format(papv));
                    res[3].setText(formatter.format(pamv));
                    paeluv=Math.max(paeluvp,paeluvm);//max(pa(elu)+,pa(elu)-)
                    res[4].setText(formatter.format(paeluv));
                    pa=paeluv;
                }catch (NumberFormatException e){

                    Notification.Notifier.INSTANCE.notifyError("Erreur","Champs manquants ou érronés");
                }

            }
        });

    }
    public void réinitialiser(){

        Element poussee= (Element) Projet.getInstance().dom.getElementsByTagName("poussee").item(0);

        if(poussee.hasAttributes()){
            if(poussee.getAttribute("type_calcul").equals("s")){
                radio1.setSelected(true);
            }else  if(poussee.getAttribute("type_calcul").equals("d")){
                radio2.setSelected(true);
            }
            System.out.println(((RadioButton) (toggleGroup.getSelectedToggle())).getText());
            combo.setValue(poussee.getAttribute("methode"));
            if(poussee.getAttribute("c1").equals("oui")){
                c1.setSelected(true);
            }else{
                c1.setSelected(false);
            }
            if(poussee.getAttribute("c2").equals("oui")){
                c2.setSelected(true);
            }else{
                c2.setSelected(false);
            }
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

            if(t[0].getText()!="") phi=Double.parseDouble(t[0].getText());
            if(t[1].getText()!="") sigma=Double.parseDouble(t[1].getText());
            if(t[4].getText()!="") c=Double.parseDouble(t[4].getText());
            if(t[6].getText()!="") gama=Double.parseDouble(t[6].getText());
            if(t[7].getText()!="") h=Double.parseDouble(t[7].getText());
            if(t[10].getText()!="") q=Double.parseDouble(t[10].getText());
            if(res[4].getText()!="") pa=Double.parseDouble(res[4].getText());

        }else{
            phi=0;sigma=0;c=0;gama=0;h=0;q=0;pa=0;
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
