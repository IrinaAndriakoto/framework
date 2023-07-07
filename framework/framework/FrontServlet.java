package etu1924.framework.servlet;

import etu1924.framework.Mapping;
import etu1924.modelView.ModelView;
import etu1924.framework.utils.Fonction;
import etu1924.annotation.url;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import java.util.HashMap;
import java.lang.reflect.*;

import org.apache.commons.io.FilenameUtils;

public class FrontServlet extends HttpServlet{ 
    HashMap<String,Mapping> mappingUrls;
    HashMap<String, Object> singleton;

    public void init() throws ServletException {
        try {
        mappingUrls = new HashMap<String, Mapping>();
        String packageName = getServletContext().getInitParameter("packageName");
        singleton = Fonction.recuperationSingleton(singleton, packageName);
        URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "//")); 
            for (File file : new File(root.getFile().replaceAll("%20", " ")).listFiles()) {
                if (file.getName().contains(".class")) {
                    String className = file.getName().replaceAll(".class$", "");
                    Class<?> cls = Class.forName(packageName + "." + className);
                    for (Method method : cls.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(url.class)) {
                            mappingUrls.put(method.getAnnotation(url.class).value(), new Mapping(cls.getName(), method.getName()));
                        }
                    }
                }
            }
            } catch (Exception e) {
                throw new ServletException(e);
            }
    } 

    public String getUrl(HttpServletRequest req){
        return req.getRequestURI().substring(req.getContextPath().length()+1);
    }

    private Mapping getMappingUrls(String key){
        Mapping map = mappingUrls.get(key);
        return map;
    }

    private Class getClass(Mapping map) throws ClassNotFoundException {
        String className = "etu1924.model." + map.getClassName();
        return Class.forName(className);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse rep) throws ServletException , IOException {
        PrintWriter out = rep.getWriter();
        try{
            String url = getUrl(req);
            Mapping map = getMappingUrls(url);
            String directory = getServletContext().getRealPath("C:\\Users\\USER\\Documents\\GitHub\\framework\\framework\\test-framework\\etu1924\\model");
            
            String[] classe = reset(directory);
            out.println(directory);

            if(map != null){
                getDataNameView(url, req, rep);
            }
            out.println(mappingUrls.size());
            
            Object o=Fonction.getTheObject(mappingUrls, singleton, req, rep);
            Object obj = Fonction.getInputData(o, req, rep);
            
            out.close();
        }
        catch(Exception e){
            e.printStackTrace();
            // throw new Exception(e);
        }
    }

    private void getDataNameView(String key,HttpServletRequest req , HttpServletResponse rep) 
        throws ServletException , IOException , NoSuchMethodException , InstantiationException , IllegalAccessException , 
        ClassNotFoundException , IllegalAccessException , InvocationTargetException
    {
        PrintWriter out = rep.getWriter();
        try{
            Enumeration<String> parameterNames = req.getParameterNames();
            Mapping map = getMappingUrls(key);
            String className = "etu1924.model." + map.getClassName();
            Class<?> classe = getClass(map);
            Object ob = classe.getConstructor().newInstance();
            Method[] methodes = classe.getDeclaredMethods();
            Method methode = null ;

            for(Method mtd : methodes){
                out.println(mtd.getName());
                if(mtd.getName().equals(map.getMethod())) {
                    methode = mtd;
                    break;
                }
            }

            //obtention des types de parametres de la methode 
            Class<?>[] parameterTypes = methode.getParameterTypes();
            //obtention des nom des parametres sur ;es types de parametres
            Parameter[] parameters = methode.getParameters();
            Object paramValues = new Object[parameterTypes.length];
            int paramCount = methode.getParameterCount();
            out.println(paramCount);

            if(methode.getReturnType()== ModelView.class) {
                if(paramCount == 0) {
                    ob = classe.getDeclaredMethod(methode.getName()).invoke(ob,(Object[]) null);
                }
                else {
                    ob = classe.getDeclaredMethod(methode.getName(), parameterTypes).invoke(ob, paramValues);
                }
                loadView((ModelView) ob,req,rep);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void loadView(ModelView view , HttpServletRequest req , HttpServletResponse rep) 
            throws ServletException , IOException , NoSuchMethodException , InstantiationException , IllegalAccessException,
            ClassNotFoundException , IllegalArgumentException  , InvocationTargetException {
                PrintWriter out = rep.getWriter();
                try{
                    if(view != null) {
                        if(view.getData().size() != 0) {
                            for(Map.Entry<String,Object> entry : view.getData().entrySet()){
                                String key1 = entry.getKey();
                                Object value = entry.getValue();
                                req.setAttribute(key1, value);
                            }
                        }
                        RequestDispatcher dispat = req.getRequestDispatcher(view.getUrl());
                        dispat.forward(req , rep);
                    }
                    else{
                        processRequest(req,rep);
                    }
                }
                catch ( Exception e){
                    e.printStackTrace(out);
                }
    }

    public String[] reset(String Directory){
        ArrayList<String> rar = new ArrayList<>();
        File dossier = new File(Directory);
        String[] contenu = dossier.list();
        for(int i=0;i<contenu.length ; i++){
            String fe = FilenameUtils.getExtension(contenu[i]);
            if(fe.equalsIgnoreCase("class")){
                String[] value = contenu[i].split("[.]");
                rar.add(value[0]);
            }
        }
        return rar.toArray(new String[rar.size()]);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            processRequest(request, response);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace(out);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            processRequest(request, response);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace(out);
        }
    }

}

        