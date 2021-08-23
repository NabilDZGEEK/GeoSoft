package controller;

public class Capropos extends Controller{
    public static Capropos instance = null;


    public static Capropos getInstance() {
        if(instance==null){
            instance = new Capropos();
        }
        return instance;
    }
    Capropos(){
        super("apropos.fxml",7);
    }
}
