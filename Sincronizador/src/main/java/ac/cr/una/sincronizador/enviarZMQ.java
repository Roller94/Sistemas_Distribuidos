
package ac.cr.una.sincronizador;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class enviarZMQ {
    public static void main(String[] args) {

        try {
            ZMQ.Context context = ZMQ.context(1);
            
            //  Socket to talk to server
            System.out.println("Connecting to hello world server…");
            
            ZMQ.Socket requester = context.socket(ZMQ.REQ);
            requester.connect("tcp://localhost:5555");
            
            String path = "C:\\SistemasDistribuidos\\Pruebas.txt";
            File dirOrigen  = new File(path);
            byte[] array = Files.readAllBytes(dirOrigen.toPath());
            
            ZMsg outMsg = new ZMsg();
            outMsg.add(new ZFrame("application/xml"));
            outMsg.add(new ZFrame(dirOrigen.getName()));
            outMsg.add(new ZFrame(array));
            outMsg.send(requester);
                        
            requester.close();
            context.term();
        } catch (IOException ex) {
            Logger.getLogger(enviarZMQ.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
