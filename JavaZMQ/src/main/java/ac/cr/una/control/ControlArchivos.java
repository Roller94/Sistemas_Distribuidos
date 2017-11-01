/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.control;

import ac.cr.una.model.ArchivoGuardado;
import ac.cr.una.model.ArchivoTemporal;
import ac.cr.una.model.SHA1;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author jecas
 */
public class ControlArchivos {
    private List<ArchivoTemporal> listArchivoTemporal;
    private List<ArchivoGuardado> listArchivoGuardado;
    private List<ArchivoTemporal> listCliente;
    private File[] fileByte;
    private SHA1 sha1;
    private final SimpleDateFormat format;

    public ControlArchivos() {
        this.format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.listArchivoTemporal = new ArrayList<>();
        this.sha1 = new SHA1();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Otros métodos">
    public void setDirectorioMemoria(String ruta){
        this.fileByte = getArchivosDirectorio(ruta);

        for (File file : this.fileByte) {
            if (file.isFile()) {
                this.listArchivoTemporal.add(new ArchivoTemporal(file, sha1.getSHA1(file.getAbsolutePath())));
            }
        }
    }
        
    public File[] getArchivosDirectorio(String ruta){
        return new File(ruta).listFiles();
    }
    
    public void setListGuardados(){
        this.listArchivoGuardado = new ArrayList<>();
        this.listArchivoTemporal.forEach((temporal) -> {
            if(!temporal.isEliminado())
                this.listArchivoGuardado.add(new ArchivoGuardado(temporal.getFile().getName(), temporal.getFile().length(), this.format.format(temporal.getFile().lastModified()), temporal.getSha1()));
        });
    }
    
    public void saveFiles() throws IOException{
        setListGuardados();
        ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("C:/guardarArchivo/lista.obj"));
        salida.writeObject(this.listArchivoGuardado);
    }
    
    public boolean loadFiles() {
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("C:/guardarArchivo/lista.obj"))) {
            this.listArchivoGuardado = (List<ArchivoGuardado>)entrada.readObject();
            entrada.close();
            return this.listArchivoGuardado != null;
        } catch(IOException | ClassNotFoundException e){
            Logger.getLogger(e.getMessage());
            return false;
        }
    }
    
    public void revisarCambiosADirectorio(String ruta) throws IOException{
        List<ArchivoTemporal> eliminados = new ArrayList<>();
        for(ArchivoTemporal tem : this.listArchivoTemporal){
            if(tem.isCreado() && tem.isModificado()){
                String[] nameFile = tem.getFile().getName().split(".");
                File newName = new File(ruta + "/" + nameFile[0] + " - copia." + nameFile[1]);
                if(tem.getFile().renameTo(newName))
                    System.out.println("Creada copia en conflicto..");
            }
            
            if(tem.isCreado() && !tem.isModificado()){
                if (tem.getFile().createNewFile())
                    System.out.println("El fichero " + tem.getFile().getName() + " se ha creado correctamente..");
            }
            
            if(tem.isModificado() && !tem.isCreado()){
                File aCrear = tem.getFile();
                if(tem.getFile().delete()){
                    if(aCrear.createNewFile())
                        System.out.println("El fichero " + aCrear.getName() + " se ha modificado correctamente..");
                }
                    
            }
                
            if(tem.isEliminado()){
                String name = tem.getFile().getName();
                if(tem.getFile().delete()){
                    System.out.println("Archivo " + name + " eliminado");
                    eliminados.add(tem);
                    //this.listArchivoTemporal.remove(tem);
                }
            }
        }
        eliminados.forEach(e -> { this.listArchivoTemporal.remove(e); });
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Control Cliente">
    public boolean nuevoArchivo(File file){
        boolean nuevo = true;
        for(int i = 0; i < this.listArchivoGuardado.size(); i++){
            if(this.listArchivoGuardado.get(i).getName().equals(file.getName())){
                nuevo = false;
                break;
            }
        }
        return nuevo;
    }
    
    public boolean archivoModificado(String sha1File, File file){
        boolean modificado = false;
        for(int i = 0; i < this.listArchivoGuardado.size(); i++){
            if(this.listArchivoGuardado.get(i).getName().equals(file.getName())){
                if(!this.listArchivoGuardado.get(i).getSha1().equals(sha1File)){
                    modificado = true;
                }
            }
        }
        return modificado;
    }
    
    public void agregarEliminados(){
        boolean eliminado = false;
        for(int i = 0; i < this.listArchivoGuardado.size(); i++){
            eliminado = false;
            for (File file : this.fileByte) {
                if(this.listArchivoGuardado.get(i).getName().equals(file.getName())){
                    eliminado = true;
                }
            }
            if(!eliminado){
                ArchivoTemporal a = new ArchivoTemporal();
                a.setNameFile(this.listArchivoGuardado.get(i).getName());
                a.setSha1(this.listArchivoGuardado.get(i).getSha1());
                a.setEliminado(true);
                this.listArchivoTemporal.add(a);
                this.listArchivoGuardado.remove(i);
                i--;
            }
        }
    }
    
    public void revisarCambiosDirectorio(){
        int j = 0;
        for (File file : this.fileByte) {
            if (file.isFile()) {
                String sha1File =  sha1.getSHA1(file.getAbsolutePath());
                if(nuevoArchivo(file)){
                    this.listArchivoGuardado.add(j, new ArchivoGuardado(file.getName(), file.length(), this.format.format(file.lastModified()), sha1File));
                    this.listArchivoTemporal.get(j).setCreado(true);
                } else{
                    if(archivoModificado(sha1File, file)){
                        this.listArchivoGuardado.remove(j);
                        this.listArchivoTemporal.get(j).setModificado(true);
                    }
                }
            }
            j++;
        }
        
        agregarEliminados();
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Control Server">
    public void primeraVez(){
        this.listArchivoTemporal.forEach(e -> {
            e.setCreado(true);
        });
    }
    
    public boolean nuevoArchivoClienteServer(String nameCliente){
        boolean nuevo = true;
        String nameServer;
        for(ArchivoTemporal temporal: this.listArchivoTemporal){
            nameServer = (temporal.getFile() != null) ? temporal.getFile().getName() : temporal.getNameFile();
            if(nameServer.equals(nameCliente)){
                nuevo = false;
                break;
            }
        }
        return nuevo;
    }
    
    public int modificadoArchivoClienteServer(ArchivoTemporal cliente, String nameCliente, List<ArchivoTemporal> guardadosS, List<ArchivoTemporal> guardadosC){
        boolean modificado = false;
        String nameServer;
        int tam = 0;
        for(ArchivoTemporal temporal : this.listArchivoTemporal){
            nameServer = (temporal.getFile() != null) ? temporal.getFile().getName() : temporal.getNameFile();
            if( nameServer.equals(nameCliente) 
                && !temporal.getSha1().equals(cliente.getSha1())){
                    if(cliente.isCreado() && !temporal.isEliminado()){
                        // El nuevo registro va tener creado y modificado en true para saber si se
                        // debe hacer la copia en conflicto
                        cliente.setModificado(true);
                        cliente.setReviso(true);
                        temporal.setReviso(true);
                        guardadosS.add(cliente);
                        //this.listArchivoTemporal.add(cliente);
                        temporal.setCreado(true);
                        temporal.setModificado(false);
                        temporal.setEliminado(false);
                        guardadosC.add(temporal);
                        //this.listCliente.add(temporal);
                        break;
                    }
                    if(cliente.isCreado() && temporal.isEliminado()){
                        modificado = true;
                        ArchivoTemporal newTem = new ArchivoTemporal();
                        newTem.setCreado(true);
                        newTem.setReviso(true);
                        newTem.setFile(cliente.getFile());
                        newTem.setNameFile(cliente.getNameFile());
                        newTem.setSha1(cliente.getSha1());
                        temporal.setReviso(true);
                        guardadosS.add(newTem);
                        //this.listArchivoTemporal.add(newTem);
                        cliente.setCreado(false);
                        cliente.setModificado(false);
                        cliente.setEliminado(false);
                        break;
                    }
                    if(cliente.isModificado()){
                        ArchivoTemporal newTem = new ArchivoTemporal();
                        newTem.setCreado(true);
                        newTem.setReviso(true);
                        newTem.setFile(cliente.getFile());
                        newTem.setNameFile(cliente.getNameFile());
                        newTem.setSha1(cliente.getSha1());
                        temporal.setReviso(true);
                        temporal.setCreado(false);
                        temporal.setModificado(false);
                        temporal.setEliminado(true);
                        guardadosS.add(newTem);
                        //this.listArchivoTemporal.add(newTem);
                        cliente.setCreado(false);
                        cliente.setModificado(false);
                        cliente.setEliminado(false);
                        modificado = true;
                        break;   
                    }
            }
                    
            /* Cambiar nombre
            if(!temporal.getFile().getName().equals(cliente.getFile().getName()) && temporal.getSha1().equals(cliente.getSha1()))
                temporal.getFile().renameTo("");
            */
            tam++;
        }
        if(modificado)
            return tam;
        else
            return -1;
    }
    
    public void eliminarArchivoClienteServer(){
        String nameClient;
        String nameServer;
        List<ArchivoTemporal> eliminadosC = new ArrayList<>();
        List<ArchivoTemporal> eliminadosS = new ArrayList<>();
        for(ArchivoTemporal cliente :  this.listCliente){
            nameClient = (cliente.getFile() !=null) ? cliente.getFile().getName() : cliente.getNameFile();
            for (ArchivoTemporal temporal : this.listArchivoTemporal) {
                nameServer = (temporal.getFile() !=null) ? temporal.getFile().getName() : temporal.getNameFile();
                if(nameClient.equals(nameServer)){
                    if(cliente.isEliminado()){
                        temporal.setCreado(false);
                        temporal.setModificado(false);
                        temporal.setEliminado(true);
                        temporal.setReviso(true);
                        eliminadosC.add(cliente);
                        //this.listCliente.remove(cliente);
                        break;
                    }
                    if(temporal.isEliminado() && !temporal.isReviso()){
                        cliente.setCreado(false);
                        cliente.setModificado(false);
                        cliente.setEliminado(true);
                        eliminadosS.add(temporal);
                        //this.listArchivoTemporal.remove(temporal);
                    }
                }
            }
        }
        
        eliminadosC.forEach(c -> { this.listCliente.remove(c); });
        eliminadosS.forEach(s -> { this.listArchivoTemporal.remove(s); });
    }
    
    public void revisarCambiosClienteServer(){
        int j = 0;
        String nameCliente;
        List<ArchivoTemporal> guardadosS = new ArrayList<>();
        List<ArchivoTemporal> guardadosC = new ArrayList<>();
        int tamTemporal;
        for (ArchivoTemporal archivo : this.listCliente) {
            nameCliente = (archivo.getFile() != null) ? archivo.getFile().getName() : archivo.getNameFile();
                if(nuevoArchivoClienteServer(nameCliente)){
                    ArchivoTemporal newTem = new ArchivoTemporal();
                    newTem.setCreado(true);
                    newTem.setFile(this.listCliente.get(j).getFile());
                    newTem.setNameFile(this.listCliente.get(j).getNameFile());
                    newTem.setSha1(this.listCliente.get(j).getSha1());
                    newTem.setReviso(true);
                    guardadosS.add(newTem);
                    //this.listArchivoTemporal.add(newTem);
                    this.listCliente.get(j).setCreado(false);
                    this.listCliente.get(j).setModificado(false);
                    this.listCliente.get(j).setEliminado(false);
                } else{
                    tamTemporal = modificadoArchivoClienteServer(archivo, nameCliente, guardadosS, guardadosC);
                    //if(tamTemporal >= 0){
                        //this.listArchivoTemporal.get(tamTemporal).setEliminado(true);
                        //this.listArchivoTemporal.remove(tamTemporal);
                    //}
                }
            j++;
        }
        guardadosC.forEach(c -> { this.listCliente.add(c); });
        guardadosS.forEach(s -> { this.listArchivoTemporal.add(s); });
        eliminarArchivoClienteServer();
    }
    
    public void revisarCambiosServerCliente(){
        boolean cambio;
        String nameCliente;
        String nameServer;
        for(ArchivoTemporal tem : this.listArchivoTemporal){
            nameServer = (tem.getFile() != null) ? tem.getFile().getName() : tem.getNameFile();
            if(!tem.isReviso()){
                cambio = false;
                for(ArchivoTemporal cli : this.listCliente){
                    nameCliente = (cli.getFile() != null) ? cli.getFile().getName() : cli.getNameFile();
                    if(nameCliente.equals(nameServer)){
                        cli = tem;
                        cambio = true;
                        tem.setCreado(false);
                        tem.setModificado(false);
                        tem.setEliminado(false);
                        break;
                    }
                }
                if(!cambio)
                    this.listCliente.add(tem);
            }
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Set's & Get's">
    public List<ArchivoTemporal> getListArchivoTemporal() {
        return listArchivoTemporal;
    }

    public void setListArchivoTemporal(List<ArchivoTemporal> listArchivoTemporal) {
        this.listArchivoTemporal = listArchivoTemporal;
    }

    public List<ArchivoGuardado> getListArchivoGuardado() {
        return listArchivoGuardado;
    }

    public void setListArchivoGuardado(List<ArchivoGuardado> listArchivoGuardado) {
        this.listArchivoGuardado = listArchivoGuardado;
    }

    public File[] getFileByte() {
        return fileByte;
    }

    public void setFileByte(File[] fileByte) {
        this.fileByte = fileByte;
    }

    public SHA1 getSha1() {
        return sha1;
    }

    public void setSha1(SHA1 sha1) {
        this.sha1 = sha1;
    }
    
    public List<ArchivoTemporal> getListCliente() {
        return listCliente;
    }

    public void setListCliente(List<ArchivoTemporal> listCliente) {
        this.listCliente = listCliente;
    }
    //</editor-fold>    
}
