/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.sincronizador;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 *
 * @author jecas
 */
public class MD5 {
    public static void main(String [] args)
   {
   
    //declarar funciones resumen
      try{
      MessageDigest messageDigest = MessageDigest.getInstance("MD5"); // Inicializa MD5
      MessageDigest messageDigest2 = MessageDigest.getInstance("SHA"); // Inicializa SHA-1
      
      //leer fichero byte a byte
      
         try{
            InputStream archivo = new FileInputStream( "F:\\SD\\Tarea 2 y 3 Sistemas Distribuidos.docx" );
            byte[] buffer = new byte[1];
            int fin_archivo = -1;
            int caracter;
       
            caracter = archivo.read(buffer);
       
            while( caracter != fin_archivo ) {
         
               messageDigest.update(buffer); // Pasa texto claro a la función resumen
               messageDigest2.update(buffer);
               caracter = archivo.read(buffer);
            }   
       
            archivo.close();
            byte[] resumen = messageDigest.digest(); // Genera el resumen MD5
            byte[] resumen2 = messageDigest2.digest(); // Genera el resumen SHA-1
       
            //Pasar los resumenes a hexadecimal
       
            String s = "";
            for (int i = 0; i < resumen.length; i++)
            {
               s += Integer.toHexString((resumen[i] >> 4) & 0xf);
               s += Integer.toHexString(resumen[i] & 0xf);
            }
            System.out.println("Resumen MD5: " + s);
       
         
            String m = "";
            for (int i = 0; i < resumen2.length; i++)
            {
               m += Integer.toHexString((resumen2[i] >> 4) & 0xf);
               m += Integer.toHexString(resumen2[i] & 0xf);
            }
            System.out.println("Resumen SHA-1: " + m);
         
         
         }
         //lectura de los datos del fichero
         catch(java.io.FileNotFoundException fnfe) {}
         catch(java.io.IOException ioe) {}
      
      }   
      //declarar funciones resumen
      catch(java.security.NoSuchAlgorithmException nsae) {}
      
   }
}