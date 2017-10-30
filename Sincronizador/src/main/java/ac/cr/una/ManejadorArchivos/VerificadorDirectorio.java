
package ac.cr.una.ManejadorArchivos;

import ac.cr.una.modelo.Archivo;
import ac.cr.una.modelo.ArchivoControl;
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
    
    public void agregueNuevoArchivo(Archivo archivo){
        archivos.add(archivo);
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
    
    public ArrayList<Archivo> obtengaArhivosEliminados(File[] arhivosActuales, ArrayList<Archivo> archivosGuardados){
        ArrayList<Archivo> eliminados= new ArrayList<>();
        boolean estaEliminado = false;
        int tamano = archivosGuardados.size();
        for(int indice = 0; indice < tamano; indice++){
            estaEliminado = false;
            for (File file : arhivosActuales) {
                if(archivosGuardados.get(indice).getFile().getName().equals(file.getName())){
                    estaEliminado = true;
                }
            }
            if(!estaEliminado){
                eliminados.add(new Archivo(archivosGuardados.ge, md5));
            }
        }
        
        return eliminados;
    }
    
    public boolean esNuevo(File file, ArrayList<Archivo> archivosGuardados){
        boolean estaNuevo = true;
        int tamano = archivosGuardados.size();
        for(int indice = 0; indice < tamano; indice++){
            if(archivosGuardados.get(indice).getFile().getName().equals(file.getName())){
                estaNuevo = false;
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
                if(!archivosGuardados.get(indice).getMd5().equals(md5Key)){
                    estaModificado = true;
                }
            }
        }
        
        return estaModificado;
    }
    
    public ArrayList<ArchivoControl> obtengaCambiosDelDirectorio(String ruta){
        File[] arhivos = listeArchivosDelDirectorio(ruta);
        ArrayList<ArchivoControl> control = new ArrayList<>();
        for (File file : arhivos) {
            if (file.isFile()) {
                String md5Key =  md5.getMD5(file.getAbsolutePath());
                boolean nuevo = esNuevo(file, this.archivos);
                if(nuevo){
                    agregueNuevoArchivo(new Archivo(file, md5Key));
                    control.add(new ArchivoControl(false, true, false, file, md5Key));
                } else{
                    boolean modificado = esModificado(md5Key, file, this.archivos);
                    control.add(new ArchivoControl(false, nuevo, modificado, file, md5Key));
                }               
            }
        }
        
        
        
        return control;
    }
    
}
