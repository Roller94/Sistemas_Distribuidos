
package ac.cr.una.ManejadorArchivos;

import ac.cr.una.modelo.Archivo;
import java.util.ArrayList;
import java.io.File;

public class VerificadorDirectorio {
    private ArrayList<Archivo> archivos;

    public VerificadorDirectorio() {
    }

    public VerificadorDirectorio(ArrayList<Archivo> archivos) {
        this.archivos = archivos;
    }
    
    public File[] listeArchivosDelDirectorio(String ruta){
        File folder = new File(ruta);
        return folder.listFiles();
    }
    
    public void almaceneArchivosDelDirectorioEnMemoria(String ruta){
        File[] listOfFiles = listeArchivosDelDirectorio(ruta);

        for (File file : listOfFiles) {
            if (file.isFile()) {
                //Llamar al metodo que obtiene el md5 y guardarlo ;
                archivos.add(new Archivo(file.getName(),file.lastModified(),file.length(),"md5 key"));
            }
        }
    }

     
    
}
