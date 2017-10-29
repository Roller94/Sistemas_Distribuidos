
package ac.cr.una.ManejadorArchivos;

import ac.cr.una.modelo.Archivo;
import ac.cr.una.sincronizador.MD5;
import java.io.File;
import java.util.ArrayList;

public class VerificadorDirectorio {
    private ArrayList<Archivo> archivos;
    private MD5 md5 = new MD5();
    
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
                archivos.add(new Archivo(file.getName(), file.lastModified(),file.length(), md5.getMD5(file.getAbsolutePath())));
            }
        }
    }

     
    
}
