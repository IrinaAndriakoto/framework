package utile;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public static Object getTheObject(HashMap<String,Mapping>mappingUrls,HashMap<String,Object>singleton,HttpServletRequest req)
    throws ClassNotFoundException,InstantiationException,IllegalAccessException
    {
        Object ob = new Object();

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
}
