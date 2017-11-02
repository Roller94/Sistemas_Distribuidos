/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.sincronizador;

import ac.cr.una.controller.ClienteController;
import java.io.IOException;

/**
 *
 * @author ASUS
 */
public class Sincronizador {
    
    public static void main(String args[]) throws IOException, InterruptedException{
        ClienteController clienteController = new ClienteController();
        String backupArchivos = "C:\\BackupArchivos\\archivoGuardado";
        String rutaSincronizacion = "C:\\SistemasDistribuidos";
        String ip = "192.168.0.54";
        clienteController.VerifiqueActualizacionesDelDirectorio(backupArchivos, rutaSincronizacion,ip);
        
    }
}
