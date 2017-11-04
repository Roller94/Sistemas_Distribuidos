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
                
                verifique.compareArhivosDelCliente(archivosServer);                
            } else {
                // Traigame todo del server
                requester.send("");
                
                byte[] reply = requester.recv(0);
                String listFilesRecv = new String(reply);
                System.out.println(listFilesRecv);
                ArrayList<ArchivoControl> archivosServer = new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoControl>>() {}.getType());
                        
                verifique.compareArhivosDelCliente(archivosServer);          
            }
            
            Thread.sleep(15000);
            verifique.GuardarBackUpArchivoDirectorio(backupArchivos, rutaSincronizacion);
        }
        
        requester.close();
        context.term();
    }
    
}