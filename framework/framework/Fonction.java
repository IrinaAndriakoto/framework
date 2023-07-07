package etu1924.framework.utils;

import etu1924.framework.*;
import etu1924.modelView.*;
import etu1924.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import etu1924.framework.Mapping;

public class Fonction {
    public static Object getInputData(Object ob,HttpServletRequest req , HttpServletResponse rep) 
    throws ClassNotFoundException, IOException,NoSuchMethodException,IllegalAccessException,InvocationTargetException{
        PrintWriter out = rep.getWriter();
        Field[] fields = ob.getClass().getDeclaredFields();
        Enumeration<String> paramNames = req.getParameterNames();
        while(paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            for(int i=0;i<fields.length;i++){
                Field field = fields[i];
                if(field.getName().equals(paramName)){
                    String methodName = "set" + capitalizeFirstLetter(field.getName());
                    Method method = ob.getClass().getMethod(methodName, field.getType());
                    Object paramValue = req.getParameter(paramName);
                    method.invoke(ob,paramValue);
                }
            }
        }
        Field[] newFields = ob.getClass().getDeclaredFields();
        for(Field field : newFields) {
            field.setAccessible(true);
            String key = field.getName();
            Object value = field.get(ob);
            if(req.getAttribute(key) == null) {
                req.setAttribute(key, value);
            }
        }
        req.setAttribute((String) ob.getClass().getName(), ob);
        return ob;
    }

    public static String capitalizeFirstLetter(String mot){
        if(mot == null || mot.isEmpty()) return mot;
        else{
            char firstChar = Character.toUpperCase(mot.charAt(0));
            return firstChar+mot.substring(1);
        }
    }

    public static Object getTheObject(HashMap<String,Mapping>mappingUrls,HashMap<String,Object>singleton,HttpServletRequest req,HttpServletResponse rep)
    throws ClassNotFoundException,InstantiationException,IllegalAccessException
    {
        Object ob = new Object();
        ModelView retour = new ModelView();

        String stringUrl = req.getRequestURI();
        String[] arrayPath = stringUrl.split("/");
        String keyUrl = arrayPath[arrayPath.length - 1];

        for(String keyMethod : mappingUrls.keySet()){
            Mapping map = mappingUrls.get(keyMethod);
            if(keyUrl.equals(keyMethod)){
                Class<?> classe = Class.forName(map.getClassName());
                ob = classe.newInstance();
            }
        }

        return ob;
    }
     public static Object setMethodsParameters(Method methode, Object objet, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        
        Enumeration<String> paramNames_1 = request.getParameterNames();
        HashMap<String, Object[]> parametres = new HashMap<>();
        while (paramNames_1.hasMoreElements()) {
            String paramName = paramNames_1.nextElement();
            Object[] paramValues = request.getParameterValues(paramName);
            parametres.put(paramName, paramValues);
        }
        
        Parameter[] parametresfonction = methode.getParameters();
        Object[] values = new Object[parametresfonction.length];

        // Création du tableau de noms de paramètres
        String[] parametersclasses = new String[parametresfonction.length];
        // Parcours des paramètres et récupération des noms
        for (int i = 0; i < parametresfonction.length; i++) {
            parametersclasses[i] = parametresfonction[i].getName();
        }
        
        for (Map.Entry<String, Object[]> entry : parametres.entrySet()) { //pour chaque clé / clé 
            String key = entry.getKey();
            for(int i = 0; i < parametersclasses.length; i++){
                if(parametersclasses[i].equals(key)){
                    Object[] params = parametres.get(key);
                    if(params.length == 1){
                        values[i] = convertToPrimitive(params[0], parametresfonction[i].getType());
                        i += 1;
                    }
                    else if(params.length > 1){
                        int[] array_object_to_set = new int[params.length];
                        int j = 0;
                        for(Object p : params){
                            System.out.println("p: "+p);
                            array_object_to_set[j] = Integer.parseInt((String) p);//Utile.convertToPrimitive(p, int.class);
                            j += 1;
                        }
                        values[i] = array_object_to_set;
                        i += 1;
                    }

                }
            }
        }
        Object retour = null;
        retour = methode.invoke(objet, values);
        return retour;
   }
   private static Object convertToPrimitive(Object value, Class<?> type) {
    if (type.equals(byte.class)) {
        return Byte.valueOf(value.toString());
    } else if (type.equals(short.class)) {
        return Short.valueOf(value.toString());
    } else if (type.equals(int.class)) {
        return Integer.valueOf(value.toString());
    } else if (type.equals(long.class)) {
        return Long.valueOf(value.toString());
    } else if (type.equals(float.class)) {
        return Float.valueOf(value.toString());
    } else if (type.equals(double.class)) {
        return Double.valueOf(value.toString());
    } else if (type.equals(boolean.class)) {
        return Boolean.valueOf(value.toString());
    } else if (type.equals(char.class)) {
        return value.toString().charAt(0);
    } else {
        throw new IllegalArgumentException("Type non supporté : " + type.getName());
    }
}
    public static Object recuperationFileData(Object object, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        try {
            PrintWriter out = response.getWriter();
            String methodd = request.getMethod();
            out.println("-----------FILE---------------");
            Collection<Part> parts = request.getParts();
            Field[] fields = object.getClass().getDeclaredFields();
            int size = parts.size();
            out.println("La taille de la collection parts est : " + size);
            for (Part part : parts) {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    if (field.getName().equals(part.getName())) {
                        if (field.getType().getName().equals("etu1924.framework.FileUpload")) {
                            String methodName = "set" + capitalizeFirstLetter(field.getName());
                            Method method = object.getClass().getMethod(methodName, FileUpload.class);
                            FileUpload paramValue = traitementFile(field.getName(), request);
                            method.invoke(object, paramValue);
                        }
                    }
                }
            }   
        } catch (Exception e) {
            // TODO: handle exception
        }
        return object;
    }

    public static ModelView recup_ModelView(HashMap<String, Mapping> mappingUrls,HashMap<String, Object> singleton, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out =response.getWriter();
        
        ModelView retour = new ModelView();
        // sprint5
        String stringUri = request.getRequestURI();
        String[] arrayPath = stringUri.split("/");
        String cle = arrayPath[arrayPath.length - 1];

        try{
            Mapping mapping = new Mapping();
            for(String key : mappingUrls.keySet()){
                if(key.equals(cle)){
                    mapping = mappingUrls.get(key);
                    break;
                }
            }
            if(!mapping.getClassName().equals("default")){
                // out.println("tsy null tsony ilay mapping");
                String nomMethode = cle;
                String nomDeClasse = (String) mapping.getClassName();
                Method methode = null;
                Object object = null;
                String method = "";
                
                // SPRINT-10
                if (singleton.containsKey(nomDeClasse)) {
                    object = singleton.get(nomDeClasse);
                    out.println("Singleton");
                    out.println("<br>");
                }
                else{
                    java.lang.Class cl = java.lang.Class.forName(nomDeClasse);
                    object = cl.newInstance();
                    out.println("TSY Singleton");
                    out.println("<br>");
                }
                
                method = (String) mapping.getMethod();
                methode = obtenirMethode(object, method);

                // ty niova sprint 8
                

                // retour = (ModelView) methode.invoke(object);
                retour = (ModelView) setMethodsParameters(methode, object, request);
                

            }else{
                // out.println("Null ilay mapping ehh");
            }

        } catch(Exception e){
            e.printStackTrace(out);
        }
        return retour;
    }

    public static FileUpload traitementFile(String nomAttribut, HttpServletRequest request) throws IOException, ServletException{
        FileUpload fu = new FileUpload();

        Part filePart = request.getPart(nomAttribut);
        String fileName = filePart.getSubmittedFileName();
        InputStream fileContent = filePart.getInputStream();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int bytesRead;
        byte[] data = new byte[4096];
        while ((bytesRead = fileContent.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        byte[] fileBytes = buffer.toByteArray();
        Byte[] fileBytesWrapper = new Byte[fileBytes.length];
        for (int i = 0; i < fileBytes.length; i++) {
            fileBytesWrapper[i] = fileBytes[i];
        }
        fu = new FileUpload(fileName, fileBytesWrapper);
        return fu;
    }    

    public static void resetObjectsToDefault(HashMap<String, Object> hashMap) {
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            Object object = entry.getValue();
            resetFields(object);
        }
    }
    public static void resetFields(Object object) {
        Class<?> clazz = object.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                try {
                    // Remet le champ à sa valeur par défaut en fonction de son type
                    field.set(object, getDefaultValue(field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            clazz = clazz.getSuperclass();
        }
    }
    private static Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return false;
            } else if (type == byte.class || type == short.class || type == int.class || type == long.class) {
                return 0;
            } else if (type == float.class || type == double.class) {
                return 0.0;
            } else if (type == char.class) {
                return '\u0000';
            } else if (type == FileUpload.class) {
                return new FileUpload();
            }
        }

        return null;
    }
    public static Method obtenirMethode(Object object, String nomMethode){
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if(method.getName().equals(nomMethode)){
                return method;
            }
        }
        return null;
    }   
    public static HashMap<String, Object> recuperationSingleton(HashMap<String, Object> singleton, String packageName){
        singleton = new HashMap<String, Object>();
        // Obtenez le nom du répertoire correspondant au package
        String packagePath = packageName.replace('.', '/');
        try {
            // Obtenez tous les fichiers dans le package
            URL packageURL = Thread.currentThread().getContextClassLoader().getResource(packagePath);
            File packageDirectory = new File(packageURL.toURI());
            File[] files = packageDirectory.listFiles();
            
            // Parcourez tous les fichiers
            for (File file : files) {
                // Vérifiez s'il s'agit d'un fichier .class
                if (file.isFile() && file.getName().endsWith(".class")) {
                    // Obtenez le nom de classe correspondant
                    String className = packageName + '.' + file.getName().substring(0, file.getName().lastIndexOf('.'));
                    
                    // Chargez la classe correspondante
                    Class<?> clazz = Class.forName(className);
                    if( clazz.isAnnotationPresent(Scope.class) ){
                        Scope scope = clazz.getAnnotation(Scope.class);
                        if(scope.isSingleton()){
                            singleton.put(className , clazz.newInstance());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singleton;
    }

}
