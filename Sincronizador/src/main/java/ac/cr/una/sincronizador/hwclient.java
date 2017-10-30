package ac.cr.una.sincronizador;

import ac.cr.una.ManejadorArchivos.ManejadorArchivo;
import ac.cr.una.ManejadorArchivos.VerificadorDirectorio;
import ac.cr.una.modelo.ArchivoControl;
import java.io.IOException;
import org.zeromq.ZMQ;
import java.util.ArrayList;

public class hwclient {

    public static void main(String[] args) throws IOException, InterruptedException {
        ZMQ.Context context = ZMQ.context(1); 
//                Path dir = Paths.get("C:/SistemasDistribuidos");
//        new ManejadorArchivo(dir).processEvents();

        //  Socket to talk to server
        
        String ruta = "C://SistemasDistribuidos";
        String ip = "192.168.1.54";
        
        VerificadorDirectorio verifique = new VerificadorDirectorio();
        verifique.almaceneArchivosDelDirectorioEnMemoria(ruta);
                
        
        
        
        
        System.out.println("Connecting to hello world server…");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");

        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            
            Thread.sleep(10000);            
            ArrayList<ArchivoControl> archivos = verifique.obtengaCambiosDelDirectorio(ruta);            
            
            for(int i = 0; i < archivos.size(); i++){
                System.out.println("Nombre: " + archivos.get(i).getFile().getName()+"  Eliminado: " + archivos.get(i).isElimando()+"  Modificado: " + archivos.get(i).isModificado()+"  Nuevo: " + archivos.get(i).isNuevo());
                System.err.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");                
            }
            archivos.clear();
//            String request = "Hello";
//            System.out.println("Sending Hello " + requestNbr);
//            requester.send(request.getBytes(), 0);
//
//            byte[] reply = requester.recv(0);
//            System.out.println("Received " + new String(reply) + " " + requestNbr);
        }
        requester.close();
        context.term();
    }
}