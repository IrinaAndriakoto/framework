package etu1924.model;

import java.util.ArrayList;
import java.util.Vector;

import etu1924.annotation.url;
import etu1924.modelView.ModelView;

import etu1924.annotation.Annotation;



@Annotation(isSegleton = true)
public final class Emp {
    String nom;
    
    public String getNom(){
        return this.nom;
    }
    public void setNom(String n){
        this.nom=n;
    }

    @Annotation(url="/Framework/jsp/parler")
    public void parler(){
    }
    
    @Annotation(url="/Framework/jsp/get-emp")
    public Vector getAll(){
        Vector<String> vs = new Vector<>();
        vs.add("ok");
        return vs;
    }
    
    @Annotation(url="/Framework/jsp/add-emp")
    public void insert(){
        
    }

    public ModelView aaa(){
        ModelView m = new ModelView();
        m.setUrl("page.jsp");
        return m;
    }

    public Emp(){

    }

    @Annotation(url = "findAllEmp")
    public ModelView findAll(){
        Object[] all = new Object[]{"1","Irina",6,0.2};
        ModelView mv = new ModelView("/page/all.jsp");
        mv.AddItem("list", all);
        return mv;
    }

    @Annotation(url="maka_input")
    public ModelView getInput(){
        ModelView md = new ModelView();
        md.setUrl("C:\\Users\\USER\\Documents\\GitHub\\framework\\framework\\test-framework\\page.jsp");
        return md;
    }
    
    @url(value="empParam")
    public ModelView getBoucle(int arg0){
        ModelView modelView = new ModelView();
        String nb = "0";
        for(int i=1; i<arg0 ; i++){
            nb = nb +"-"+String.valueOf(i);
        }
        modelView.AddItem("boucle",nb);
        modelView.setUrl("param.jsp");
        return modelView;
    }

}
