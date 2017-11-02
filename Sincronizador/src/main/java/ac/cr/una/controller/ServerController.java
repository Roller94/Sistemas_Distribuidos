package ac.cr.una.controller;

import ac.cr.una.manejadorArchivos.VerificadorDirectorio;
import ac.cr.una.modelo.ArchivoControl;
import java.io.File;
import org.zeromq.ZMQ;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.zeromq.ZMsg;

public class ServerController {

    public static void main(String[] args) throws Exception {
        ZMQ.Context context = ZMQ.context(1);
        
        String ruta = "D:\\SistemasDistribuidos";

        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:8889");

        while (!Thread.currentThread().isInterrupted()) {
            
            ZMsg inMsg = ZMsg.recvMsg(responder);
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
            }
            
            responder.send("Recibido!");            
        }
        responder.close();
        context.term();
    }
}