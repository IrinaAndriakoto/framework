package etu1924.modelView;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String,Object> data = new HashMap<String,Object>(); 

    public ModelView() {

    }
    public void AddItem(String name, Object value){
        this.getData().put(name, value);
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String newurl) {
        this.url = newurl;
    }
    public HashMap<String,Object> getData() {
        return data;
    }
    public void setData(HashMap<String,Object> newdata) {
        this.data = newdata;
    }
    public ModelView(String url) {
        this.setUrl(url);
    }
}
