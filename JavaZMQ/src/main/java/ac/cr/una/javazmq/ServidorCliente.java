/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.javazmq;

import ac.cr.una.control.ControlClient;
import ac.cr.una.control.ControlServer;
import java.io.IOException;
/**
 *
 * @author jecas
 */
public class ServidorCliente {
    
    private static ControlClient client;
    private static ControlServer server;
     /**
     * @param args
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        //if(args.length > 0){
            //if(args.length == 1){
//                server = new ControlServer("F:/SistemasDistribuidos");
//                server.connect();
//                server.runServer();
//                server.close();
            //}
            //else{
                client = new ControlClient("C:/SistemasDistribuidos", "192.168.1.4");
                client.connect();
                client.runClient();
                client.close();
            //}
//        } 
    }
}
