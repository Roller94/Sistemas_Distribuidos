/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.test;

import ac.cr.una.control.ControlArchivos;
import ac.cr.una.control.ControlClient;
import ac.cr.una.control.ControlServer;
import ac.cr.una.model.ArchivoTemporal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author jecas
 */
public class TestGuardar {
    
    public TestGuardar() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    //@Test
    public void save() throws IOException {
        ControlArchivos c = new ControlArchivos();
        c.setDirectorioMemoria("C:/SistemasDistribuidos");
        c.saveFiles();
    }
    
    //@Test
    public void load() throws IOException, ClassNotFoundException {
        ControlArchivos c = new ControlArchivos();
        c.setDirectorioMemoria("C:/SistemasDistribuidos");
        c.loadFiles();
        c.revisarCambiosDirectorio();
        List<ArchivoTemporal> a = c.getListArchivoTemporal();
    }
    
    //@Test
    public void probarServer() throws IOException{
        ControlServer server;
        server = new ControlServer("C:/SistemasDistribuidos2");
        //server.connect();
        //server.runServer();
        server.getControl().setDirectorioMemoria(server.getRuta());
        if(server.getControl().loadFiles())
            server.getControl().revisarCambiosDirectorio();
        else
            server.getControl().primeraVez();
        //byte[] reply = new byte[];
        
        String listFiles = new Gson().toJson(server.getControl().getListArchivoTemporal());
        System.out.println("Enviando la siguiente lista: " + listFiles);
        
        server.getControl().saveFiles();
        //server.close();
        
    }
    
    //@Test
    public void probarCliente() throws IOException{
        ControlClient cliente = new ControlClient("C:/SistemasDistribuidos", "1111");
        
        cliente.getControl().setDirectorioMemoria(cliente.getRuta());
        if(cliente.getControl().loadFiles())
            cliente.getControl().revisarCambiosDirectorio();
        
        String listFilesRecv = "[{\"file\":{\"path\":\"C:\\\\SistemasDistribuidos\\\\b.txt.txt\"},\"creado\":true,\"modificado\":false,\"eliminado\":false,\"sha1\":\"da39a3ee5e6b4b0d3255bfef95601890afd80709\",\"reviso\":false},{\"file\":{\"path\":\"C:\\\\SistemasDistribuidos\\\\Proyecto2_IIS20178.pdf\"},\"creado\":true,\"modificado\":false,\"eliminado\":false,\"sha1\":\"1ab3554d141ca45d1f528067c5502f38e431cb8d\",\"reviso\":false}]";
        //if (reply.length > 0) {
            System.out.println("Lista Recibida..");
            cliente.getControl().setListArchivoTemporal(new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoTemporal>>() {}.getType()));
            System.out.println("Sincronizando Cambios..");
            cliente.getControl().revisarCambiosADirectorio(cliente.getRuta());
            System.out.println("Cambios Sincronizados..");
        //}
        
        cliente.getControl().saveFiles();
    }
    
    //@Test
    public void probarClienteServer() throws IOException{
        ControlClient cliente = new ControlClient("C:/SistemasDistribuidos", "1111");
        
        cliente.getControl().setDirectorioMemoria(cliente.getRuta());
        if(cliente.getControl().loadFiles())
            cliente.getControl().revisarCambiosDirectorio();
        
        String listFiles = new Gson().toJson(cliente.getControl().getListArchivoTemporal());
        System.out.println("Enviando la siguiente lista: " + listFiles);
        
        
        cliente.getControl().saveFiles();
    }
    
//    @Test
    public void probarServerCliente() throws IOException{
        ControlServer server;
        server = new ControlServer("C:/SistemasDistribuidos2");

        server.getControl().setDirectorioMemoria(server.getRuta());
        if(server.getControl().loadFiles())
            server.getControl().revisarCambiosDirectorio();
        else
            server.getControl().primeraVez();
        
        String listFilesRecv = "[{\"file\":{\"path\":\"C:\\\\SistemasDistribuidos2\\\\a.txt.txt\"},\"creado\":true,\"modificado\":false,\"eliminado\":false,\"sha1\":\"da39a3ee5e6b4b0d3255bfef95601890afd80709\",\"reviso\":false},{\"file\":{\"path\":\"C:\\\\SistemasDistribuidos2\\\\b.txt.txt\"},\"creado\":false,\"modificado\":true,\"eliminado\":false,\"sha1\":\"ebf5477311d55c23600a242ffe9d1c16d8541065\",\"reviso\":false},{\"nameFile\":\"Proyecto2_IIS20178.pdf\",\"creado\":false,\"modificado\":false,\"eliminado\":true,\"sha1\":\"1ab3554d141ca45d1f528067c5502f38e431cb8d\",\"reviso\":false}]";
        
        System.out.println("Lista Recibida..");
        server.getControl().setListCliente(new Gson().fromJson(listFilesRecv, new TypeToken<ArrayList<ArchivoTemporal>>() {}.getType()));
        System.out.println("Sincronizando Cambios..");
        server.getControl().revisarCambiosClienteServer();
        server.getControl().revisarCambiosServerCliente();
        System.out.println("Cambios Sincronizados..");
        String listFiles = new Gson().toJson(server.getControl().getListCliente());
        System.out.println("Enviando la siguiente lista: " + listFiles);
        
        server.getControl().revisarCambiosADirectorio(server.getRuta());
        
        server.getControl().saveFiles();
        //[{"file":{"path":"C:\\SistemasDistribuidos\\a.txt.txt"},"creado":true,"modificado":false,"eliminado":false,"sha1":"da39a3ee5e6b4b0d3255bfef95601890afd80709","reviso":false},{"file":{"path":"C:\\SistemasDistribuidos\\b.txt.txt"},"creado":false,"modificado":true,"eliminado":false,"sha1":"ebf5477311d55c23600a242ffe9d1c16d8541065","reviso":false},{"nameFile":"Proyecto2_IIS20178.pdf","creado":false,"modificado":false,"eliminado":true,"sha1":"1ab3554d141ca45d1f528067c5502f38e431cb8d","reviso":false}]
    }
    
    //[{"file":{"path":"C:\\SistemasDistribuidos2\\a.txt.txt"},"creado":false,"modificado":false,"eliminado":false,"sha1":"da39a3ee5e6b4b0d3255bfef95601890afd80709","reviso":false},{"file":{"path":"C:\\SistemasDistribuidos2\\b.txt.txt"},"creado":false,"modificado":false,"eliminado":false,"sha1":"ebf5477311d55c23600a242ffe9d1c16d8541065","reviso":false}]
}
