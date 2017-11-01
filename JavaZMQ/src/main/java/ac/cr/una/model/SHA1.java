/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.model;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 *
 * @author jecas
 */
public class SHA1 {

    public SHA1() {
    }
    
    public String getSHA1(String rutaArchivo){
      String SHA = "";
      try{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA"); // Inicializa SHA-1
      
         try{
            InputStream archivo = new FileInputStream( rutaArchivo );
            byte[] buffer = new byte[1];
            int fin_archivo = -1;
            int caracter;
       
            caracter = archivo.read(buffer);
       
            while( caracter != fin_archivo ) {
               messageDigest.update(buffer);
               caracter = archivo.read(buffer);
            }   
       
            archivo.close();
            byte[] resumen = messageDigest.digest(); // Genera el resumen SHA-1
         
            for (int i = 0; i < resumen.length; i++)
            {
               SHA += Integer.toHexString((resumen[i] >> 4) & 0xf);
               SHA += Integer.toHexString(resumen[i] & 0xf);
            }
            //System.out.println("Resumen SHA-1: " + SHA);
         }
         //lectura de los datos del fichero
         catch(java.io.FileNotFoundException fnfe) {}
         catch(java.io.IOException ioe) {}
      
      }   
      //declarar funciones resumen
      catch(java.security.NoSuchAlgorithmException nsae) {}
      
      return SHA;
   }
}
