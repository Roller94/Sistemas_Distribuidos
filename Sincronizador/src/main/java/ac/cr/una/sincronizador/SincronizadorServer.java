/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.sincronizador;

import ac.cr.una.controller.ServerController;

public class SincronizadorServer {
    
    public static void main(String args[]) throws Exception{
        ServerController sincronizadorServer = new ServerController();
        sincronizadorServer.run();        
    }

}
