/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.control;

import ac.cr.una.model.ArchivoTemporal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import org.zeromq.ZMQ;

/**
 *
 * @author jecas
 */
public class ControlServer {
    private String ruta;
    private ZMQ.Context context;
    private ZMQ.Socket socket;
    private ControlArchivos control;

    public ControlServer() {
    }

    public ControlServer(String ruta) {
        this.ruta = ruta;
        this.control = new ControlArchivos();
    }
    
    public void runServer() throws IOException{
        System.out.println("Esperando clientes..");
        this.control.setDirectorioMemoria(ruta);
        if(this.control.loadFiles())
            this.control.revisarCambiosDirectorio();
        else
            this.control.primeraVez();
        
        System.out.println("Conexión establecida..");
        byte[] reply = this.socket.recv(0);
        //this.socket.send("Conexión establecida..");
        
        String listFilesRecv = new String(reply);
        if (reply.length > 0) {
            System.out.println("Lista Recibida..");
            this.control.setListCliente(new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoTemporal>>() {}.getType()));
            System.out.println("Sincronizando Cambios..");
            this.control.revisarCambiosClienteServer();
            this.control.revisarCambiosServerCliente();
            System.out.println("Cambios Sincronizados..");
            String listFiles = new Gson().toJson(this.control.getListCliente());
            System.out.println("Enviando la siguiente lista: " + listFiles);

            this.socket.send(listFiles);
            System.out.println("Lista Enviada..");
            this.control.revisarCambiosADirectorio(this.ruta);
            }
        else{
            String listFiles = new Gson().toJson(this.control.getListArchivoTemporal());
            System.out.println("Enviando la siguiente lista: " + listFiles);

            this.socket.send(listFiles);
            System.out.println("Lista Enviada..");
        }
        
        this.control.saveFiles();
        
    }
    
    public void connect(){
        context = ZMQ.context(1);
        // Socket to talk to server
        System.out.println("Server iniciado");
        socket = context.socket(ZMQ.REP);
        socket.bind("tcp://*:8889");
    }
    
    public void close(){
        System.out.println("Cerrando conexión..");
        socket.close();
        context.term();
    }

    // <editor-fold defaultstate="collapsed" desc="Set's & Get's">
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public ZMQ.Context getContext() {
        return context;
    }

    public void setContext(ZMQ.Context context) {
        this.context = context;
    }

    public ZMQ.Socket getSocket() {
        return socket;
    }

    public void setSocket(ZMQ.Socket socket) {
        this.socket = socket;
    }

    public ControlArchivos getControl() {
        return control;
    }

    public void setControl(ControlArchivos control) {
        this.control = control;
    }
    //</editor-fold>
}
