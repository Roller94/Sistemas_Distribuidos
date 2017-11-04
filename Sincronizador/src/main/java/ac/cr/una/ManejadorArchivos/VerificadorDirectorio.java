
package ac.cr.una.manejadorArchivos;

import ac.cr.una.modelo.Archivo;
import ac.cr.una.modelo.ArchivoControl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    
    public void elimineArhivo(int indice){
        archivos.remove(indice);
    }
    
    public File[] listeArchivosDelDirectorio(String ruta){
        File folder = new File(ruta);
        return folder.listFiles();
    }

    public void ObtenerBackUpArchivoDirectorio(String ruta){
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ruta))) {
            this.archivos = (ArrayList<Archivo>)entrada.readObject();
            entrada.close();
            
        } catch(IOException | ClassNotFoundException e){
            Logger.getLogger(e.getMessage());

        }
    }
    
    public void GuardarBackUpArchivoDirectorio(String ruta, String rutaSincronizacion) throws FileNotFoundException, IOException {
//        almaceneArchivosDelDirectorioEnMemoria(rutaSincronizacion);
        ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ruta));
        salida.writeObject(this.archivos);
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
                eliminados.add(new Archivo(archivosGuardados.get(indice).getFile(), archivosGuardados.get(indice).getMd5()));
                archivos.remove(indice);
                tamano-=1;
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
        int position = 0;
        for (File file : arhivos) {
            if (file.isFile()) {
                String md5Key =  md5.getMD5(file.getAbsolutePath());
                boolean nuevo = esNuevo(file, this.archivos);
                if(nuevo){
                    agregueNuevoArchivo(new Archivo(file, md5Key));
                    control.add(new ArchivoControl(false, true, false, file, md5Key));
                } else{
                    boolean modificado = esModificado(md5Key, file, this.archivos);
                    if(modificado){
                        elimineArhivo(position);
                        agregueNuevoArchivo(new Archivo(file, md5Key));                        
                    }
                    control.add(new ArchivoControl(false, nuevo, modificado, file, md5Key));                    
                }               
            }
            position++;
        }
        
        ArrayList<Archivo> eliminados = obtengaArhivosEliminados(arhivos, this.archivos);
        for(int contador = 0; contador < eliminados.size(); contador++){
            control.add(new ArchivoControl(true, false, false, eliminados.get(contador).getFile(), eliminados.get(contador).getMd5()));
        }
        
        return control;
    }
    
    public ArrayList<ArchivoControl> compareArhivosDelServidor(ArrayList<ArchivoControl> archivosCliente) throws IOException{
        ArrayList<ArchivoControl> archivosServerParaCliente = new ArrayList<>();
        String rutaSincronizacion =  "C://SistemasDistribuidosServer";//archivosCliente.get(0).getFile().getParent();
        File[] arhivosServer = listeArchivosDelDirectorio(rutaSincronizacion);
        for (File file : arhivosServer) {
            boolean encontrado = false;
            for (ArchivoControl archivoCliente : archivosCliente) {
                
                if(archivoCliente.isNuevo()){                    
                    file.createNewFile();
                }
                
                if(archivoCliente.getFile().getName().equals(file.getName())){
<<<<<<< HEAD
                    /*if(!(md5.getMD5(file.getAbsolutePath()).equals(archivoCliente.getMd5()))){
                        
                    }*/
=======
                    //Si es modificado
                    if(!(md5.getMD5(file.getAbsolutePath()).equals(archivoCliente.getMd5()))){
                        if(file.lastModified() > archivoCliente.getFile().lastModified()){
                            file.delete();
                            archivoCliente.getFile().createNewFile();
                        }else{
                            archivosServerParaCliente.add(new ArchivoControl(false, false, true, file, md5.getMD5(file.getAbsolutePath())));
                        }
                    }
                    //Si es eliminado
                    if(archivoCliente.isElimando()){
                        file.delete();
                    }
>>>>>>> origin/master
                    
                    encontrado = true;
                }
            }
            if(!encontrado){
                archivosServerParaCliente.add(new ArchivoControl(false, true, false, file, md5.getMD5(file.getAbsolutePath())));
            }
        }
        
        return archivosServerParaCliente;
    }
    
}
