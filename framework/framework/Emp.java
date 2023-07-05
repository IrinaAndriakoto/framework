package object;

import java.util.ArrayList;
import java.util.Vector;

// import utilitaire.FileUpload;
// import utilitaire.ModelView;
import utile.Annotation;

@Annotation(isSegleton = true)
public final class Emp {
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
}
