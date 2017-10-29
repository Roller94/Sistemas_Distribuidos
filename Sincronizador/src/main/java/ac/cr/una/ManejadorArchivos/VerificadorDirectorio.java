
package ac.cr.una.ManejadorArchivos;

import ac.cr.una.modelo.Archivo;
import ac.cr.una.sincronizador.MD5;
import java.io.File;
import java.util.ArrayList;

public class VerificadorDirectorio {
    private ArrayList<Archivo> archivos;
    private MD5 md5;
    
    public VerificadorDirectorio() {
        archivos = new ArrayList();
        md5 = new MD5();
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
                archivos.add(new Archivo(file, md5.getMD5(file.getAbsolutePath())));
            }
        }
    }
    
    public boolean esEliminado(File file, ArrayList<Archivo> archivosGuardados){
        boolean estaEliminado = true;
        int tamano = archivosGuardados.size();
        for(int indice = 0; indice < tamano; indice++){
            if(archivosGuardados.get(indice).getFile().getName().equals(file.getName())){
                estaEliminado = false;
                indice = tamano;
            }
        }
        
        return estaEliminado;
    }
    
    public boolean esNuevo(File file, ArrayList<Archivo> archivosGuardados){
        boolean estaNuevo = false;
        int tamano = archivosGuardados.size();
        for(int indice = 0; indice < tamano; indice++){
            if(archivosGuardados.get(indice).getFile().getName().equals(file.getName())){
                estaNuevo = true;
                indice = tamano;
            }
        }
        
        return estaNuevo;
    }
    
    public boolean esModificado(String md5Key, File file, ArrayList<Archivo> archivosGuardados){
        boolean estaModificado = false;
        int tamano = archivosGuardados.size();
        for(int indice = 0; indice < tamano; indice++){
            if(archivosGuardados.get(indice).getFile().getName().equals(file.getName())){
                if(!archivosGuardados.get(indice).getMd5().equals(md5)){
                    estaModificado = true;
                }
            }
        }
        
        return estaModificado;
    }
    
    public void verifiqueCambiosDelDirectorio(String ruta){
        File[] arhivos = listeArchivosDelDirectorio(ruta);
        for (File file : arhivos) {
            if (file.isFile()) {
                String md5Key =  md5.getMD5(file.getAbsolutePath());
                boolean eliminado = esEliminado(file, this.archivos);
                boolean creado = esNuevo(file, this.archivos);
                boolean modificado = esModificado(md5Key, file, this.archivos);
            }
        }
    }
    
}
