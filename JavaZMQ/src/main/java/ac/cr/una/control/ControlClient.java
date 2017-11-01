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
import org.zeromq.ZMsg;

/**
 *
 * @author jecas
 */
public class ControlClient {
    private ControlArchivos control;
    private String ruta;
    private String ip;
    private ZMQ.Context context;
    private ZMQ.Socket socket;
    private ZMsg outMsg;
    
    public ControlClient() {
    }

    public ControlClient(String ruta, String ip) {
        this.ruta = ruta;
        this.ip = ip;
        this.outMsg = new ZMsg();
        this.control = new ControlArchivos();
    }
    
    public void runClient() throws IOException{
        this.control = new ControlArchivos();
        this.control.setDirectorioMemoria(this.ruta);
        if(this.control.loadFiles())
            this.control.revisarCambiosDirectorio();
        
        //Enviando la lista
        String listFiles = new Gson().toJson(this.control.getListArchivoTemporal());
        System.out.println("Enviando la siguiente lista: " + listFiles);
        
        this.socket.send(listFiles);
        System.out.println("Lista Enviada..");
        
        byte[] reply = this.socket.recv(0);
        String listFilesRecv = new String(reply);
        if (reply.length > 0) {
            System.out.println("Lista Recibida..");
            this.control.setListArchivoTemporal(new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoTemporal>>() {}.getType()));
            System.out.println("Sincronizando Cambios..");
            this.control.revisarCambiosADirectorio(this.ruta);
            System.out.println("Cambios Sincronizados..");
        }
        
        this.control.saveFiles();
    }
    
    public void connect(){
        this.context = ZMQ.context(1);
        // Socket to talk to server
        System.out.println("Creando conexión al server");
        this.socket = context.socket(ZMQ.REQ);
        this.socket.connect ("tcp://" + this.ip + ":8889");
    }
    
    public void close(){
        System.out.println("Cerrando conexión..");
        this.socket.close();
        this.context.term();
    }

    // <editor-fold defaultstate="collapsed" desc="Get's & Set's">
    public ControlArchivos getControl() {
        return control;
    }

    public void setControl(ControlArchivos control) {
        this.control = control;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public ZMsg getOutMsg() {
        return outMsg;
    }

    public void setOutMsg(ZMsg outMsg) {
        this.outMsg = outMsg;
    }
    //</editor-fold>
}
