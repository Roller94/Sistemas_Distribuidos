package ac.cr.una.controller;

import ac.cr.una.manejadorArchivos.VerificadorDirectorio;
import ac.cr.una.modelo.ArchivoControl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import org.zeromq.ZMQ;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.zeromq.ZFrame;
import org.zeromq.ZMsg;

public class ClienteController {
    
    private VerificadorDirectorio verifique;
    
    public ClienteController(){
        verifique = new VerificadorDirectorio();
    }

    public void GuardarBackUpArchivoDirectorio(String ruta, String rutaSincronizacion) throws IOException{
        verifique.GuardarBackUpArchivoDirectorio(ruta, rutaSincronizacion);
    }
    
    public void VerifiqueActualizacionesDelDirectorio(String backupArchivos, String rutaSincronizacion, String ip) throws InterruptedException, IOException{
        
        ZMQ.Context context = ZMQ.context(1);                 
         
        System.out.println("Connecting to server…");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://" + ip + ":8889");       
        
        verifique.ObtenerBackUpArchivoDirectorio(backupArchivos);
        
        while (!Thread.currentThread().isInterrupted()) {
                       
            ArrayList<ArchivoControl> archivos = verifique.obtengaCambiosDelDirectorio(rutaSincronizacion); 
            
            if(archivos.size() > 0){
                // Si hay archivos
                String listFiles = new Gson().toJson(archivos);
                requester.send(listFiles);
                
                byte[] reply = requester.recv(0);
                String listFilesRecv = new String(reply);
                System.out.println(listFilesRecv);
                ArrayList<ArchivoControl> archivosServer = new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoControl>>() {}.getType());
            
                RealizarAjustesDirectorio(archivosServer);                
            } else {
                // Traigame todo del server
                String listFiles = new Gson().toJson(archivos);
                requester.send(listFiles);
                
                byte[] reply = requester.recv(0);
                String listFilesRecv = new String(reply);
                System.out.println(listFilesRecv);
                ArrayList<ArchivoControl> archivosServer = new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoControl>>() {}.getType());
                        
                RealizarAjustesDirectorio(archivosServer);
            }
            
            Thread.sleep(15000);
            verifique.GuardarBackUpArchivoDirectorio(backupArchivos, rutaSincronizacion);
        }
        
        requester.close();
        context.term();
    }
    
    public void RealizarAjustesDirectorio(ArrayList<ArchivoControl> archivosServer) throws IOException {
        
        for (ArchivoControl archivo : archivosServer) {            
            if(archivo.isNuevo() && archivo.isModificado()){
                String[] nameFile = archivo.getFile().getName().split(".");
                File file = new File("C://SistemasDistribuidos" + nameFile[0] + " - copia." + nameFile[1]);
                if(archivo.getFile().renameTo(file)){
                    System.out.println("Copia en conflicto creada correctamente!");
                }
            } 
            
            if(archivo.isNuevo() && !archivo.isModificado()){
                if(archivo.getFile().createNewFile()){
                    System.out.println("El archivo " + archivo.getFile().getName() + " se ha creado correctamente!");
                }
            }
            
            if(archivo.isModificado() && !archivo.isNuevo()){
                File file = archivo.getFile();
                if(archivo.getFile().delete()){
                    if(file.createNewFile()){
                        System.out.println("El archivo " + file.getName() + " se ha modificado correctamente!");
                    }
                }                    
            }
                
            if(archivo.isElimando()){
                String fileName = archivo.getFile().getName();
                if(archivo.getFile().delete()){
                    File file = new File("C:\\SistemasDistribuidos\\" + fileName);
                    if(file.exists() && !file.isDirectory()) {  
                        file.delete();
                        System.out.println("Archivo " + fileName + " elimiando!");
                    }
                }
            }
        }
    }
    
}