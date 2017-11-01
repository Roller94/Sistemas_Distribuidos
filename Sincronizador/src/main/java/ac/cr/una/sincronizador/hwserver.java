package ac.cr.una.sincronizador;

import org.zeromq.ZMQ;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.zeromq.ZMsg;

public class hwserver {

    public static void main(String[] args) throws Exception {
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (!Thread.currentThread().isInterrupted()) {
            
            ZMsg inMsg = ZMsg.recvMsg(responder);
            String contentType = inMsg.pop().toString();
            String fileName = inMsg.pop().toString();
            byte[] fileData = inMsg.pop().getData();

            Files.write(Paths.get("F:\\SistemasDistribuidos\\"+fileName),fileData);
            
            
            
//            // Wait for next request from the client
//            byte[] request = responder.recv(0);
//            System.out.println("Received Hello");
//
//            // Do some 'work'
//            Thread.sleep(1000);
////            File file = new File("C:\\SistemasDistribuidos");
//            byte[] array = Files.readAllBytes(new File("C:\\SistemasDistribuidos\\a.txt").toPath());
//
//            // Send reply back to client
////            String reply = "World";
            responder.send("ok");
            
        }
        responder.close();
        context.term();
    }
}