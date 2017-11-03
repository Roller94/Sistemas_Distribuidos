package ac.cr.una.controller;

import ac.cr.una.manejadorArchivos.VerificadorDirectorio;
import ac.cr.una.modelo.ArchivoControl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import org.zeromq.ZMQ;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.zeromq.ZMsg;

public class ServerController {
    
    private VerificadorDirectorio verifique;
    
    public ServerController(){
        verifique = new VerificadorDirectorio();
    }

    public void run() throws Exception {
        ZMQ.Context context = ZMQ.context(1);
        
        String ruta = "D:\\SistemasDistribuidos";

        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:8889");

        while (!Thread.currentThread().isInterrupted()) {
            
            /*ZMsg inMsg = ZMsg.recvMsg(responder);
            String contentType = inMsg.pop().toString();
            String fileName = inMsg.pop().toString();
            String fileDelete = inMsg.pop().toString();
            byte[] fileData = inMsg.pop().getData();
            
            if(fileDelete.equals("SI")){
                File f = new File("D:\\SistemasDistribuidos\\" + fileName);
                if(f.exists() && !f.isDirectory()) {  
                    f.delete();
                    System.out.println("Archivo " + fileName + " elimiando!");
                }
            } else {
                Files.write(Paths.get("D:\\SistemasDistribuidos\\" + fileName), fileData);
                System.out.println("Archivo " + fileName + " creado/actualizado!");
            }*/
            
            byte[] reply = responder.recv(0);
            String listFilesRecv = new String(reply);
            System.out.println(listFilesRecv);
            
            if (reply.length > 2) {
                ArrayList<ArchivoControl> archivosCliente = new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoControl>>() {}.getType());
                ArrayList<ArchivoControl> archivosServidor = verifique.compareArhivosDelServidor(archivosCliente);
                String listFiles = new Gson().toJson(archivosServidor);
                responder.send(listFiles);
                // Comparar los archivos del cliente con los del server
                System.out.println("Trae archivos!");
            } else {
                ArrayList<ArchivoControl> archivos = verifique.obtengaCambiosDelDirectorio(ruta);
                
                if(archivos.size() > 0){
                    // Si hay archivos
                    String listFiles = new Gson().toJson(archivos);
                    responder.send(listFiles);
                } else {
                    responder.send("No hay archivos!");
                }
            }      
        }
        responder.close();
        context.term();
    }
}