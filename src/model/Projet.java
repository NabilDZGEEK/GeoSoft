package model;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Projet {
    static Projet instance;

    public File fichier;
    public Document dom;
    public DocumentBuilder builder;
    Projet(){
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
    public static Projet getInstance() {
        if(instance==null){
            instance=new Projet();
        }
        return instance;
    }

    public void ecrire() {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(dom), new StreamResult(fichier));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
