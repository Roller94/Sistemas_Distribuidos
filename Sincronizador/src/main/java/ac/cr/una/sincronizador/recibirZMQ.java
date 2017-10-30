
package ac.cr.una.sincronizador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

public class recibirZMQ {
    public static void main(String[] args) throws Exception {
        try {
            ZMQ.Context context = ZMQ.context(1);
            //  Socket to talk to clients
            ZMQ.Socket responder = context.socket(ZMQ.REP);
            responder.bind("tcp://*:5555");

            ZMsg inMsg = ZMsg.recvMsg(responder);
            String contentType = inMsg.pop().toString();
            String fileName = inMsg.pop().toString();
            byte[] fileData = inMsg.pop().getData();

            Files.write(Paths.get("D:\\SistemasDistribuidos\\"+fileName),fileData);

            responder.close();
            context.term();
        } catch (IOException ex) {
            Logger.getLogger(recibirZMQ.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
