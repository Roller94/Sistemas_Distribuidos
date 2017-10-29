package ac.cr.una.sincronizador;

import ac.cr.una.ManejadorArchivos.ManejadorArchivo;
import java.io.IOException;
import org.zeromq.ZMQ;
import java.nio.file.Path;
import java.nio.file.Paths;

public class hwclient {

    public static void main(String[] args) throws IOException {
        ZMQ.Context context = ZMQ.context(1); 
                Path dir = Paths.get("C:/SistemasDistribuidos");
        new ManejadorArchivo(dir).processEvents();

        //  Socket to talk to server
        System.out.println("Connecting to hello world server…");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");

        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            String request = "Hello";
            System.out.println("Sending Hello " + requestNbr);
            requester.send(request.getBytes(), 0);

            byte[] reply = requester.recv(0);
            System.out.println("Received " + new String(reply) + " " + requestNbr);
        }
        requester.close();
        context.term();
    }
}