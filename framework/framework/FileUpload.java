package etu1924.framework;

public class FileUpload {
    String name;
    String path;
    Byte[] bytes;

    public String getName(){
        return this.name;
    }
    public String getPath(){
        return this.path;      
    }
    public Byte[] getBytes(){
        return this.bytes;
    }
    public void setName(String n){
        this.name = n;
    }
    public void setPath(String p){
        this.path = p;      
    }
    public void setBytes(Byte[] b){
        this.bytes = b;
    }

    public FileUpload(String name , Byte[] bytes){
        this.setName(name);
        this.setBytes(bytes);
    }
    public FileUpload(){

    }

}