/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.sincronizador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Contiene los metodos necesarios para sincronizar archivos, hacer 
 * comparaciones y mostrar informacion
 * 
 * @author Aguirre Alvarez J Giovanni
 * 
 */
public class Sincronizador 
{
	
	/**
	 * Compara los archivos que hay en ambos directorios, si hay algun
	 * archivo o directorio que aparesca en el folder de origen, pero no
	 * aparesca en el folder destino, entonces lo copiara al directorio destino
	 * 
	 * Tambien analiza todos los subdirectorios que existan en los directorios
	 * 
	 * NOTA: Este metodo no actualiza archivos que ya hayan sido copiados y
	 * posteriormente modificados, es decir, solo sincroniza archivos nuevos
	 * pero no archivos modificados
	 * 
	 * @param rutaOrigen Representa la ruta hasta el folder origen para sincronizar
	 * @param rutaDestino Representa la ruta hasta el folder destino para sincronizar
	 */
	public void sincronizar(String rutaOrigen, String rutaDestino)
	{
		File dirOrigen  = new File(rutaOrigen);
		File dirDestino = new File(rutaDestino);
		
		if(validaDir(dirOrigen) && validaDir(dirDestino))
		{
			File archivosOrigen[]  = dirOrigen.listFiles();
			File archivosDestino[] = dirDestino.listFiles();
			
			// Archivos que deben copiarse al directorio destino
			ArrayList<File> porCopiar = comparaListas(archivosOrigen, archivosDestino);
			
			// Copiar todos los archivos que se deben copiar
			for(File file : porCopiar)
			{
				if(file.isDirectory())
				{
					//copiarDirectorio(file, dirDestino.getAbsolutePath() + "/");
				}
				else
				{
					copiarArchivo(file, dirDestino.getAbsolutePath() + "/");
				}
			}
		}
	}
	
	/**
	 * Devuelve un ArrayList con los archivos que estan en el origen, pero
	 * que no estan en el destino.
	 * 
	 * En caso de que haya dos subdirectorios iguales tanto en origen como
	 * en destino, el programa sincronizara primero estos y luego continuara
	 * con la sincronizacion de los directorios padre
	 * 
	 * @param origen Arreglo con los archivos del directorio origen
	 * @param destino Arreglo con los archivos del directorio destino
	 * @return Los archivos que estan en origen y no en destino
	 */
	private ArrayList<File> comparaListas(File origen[], File destino[])
	{
		ArrayList<File> porCopiar = new ArrayList<File>();
		
		for(File archOrigen : origen)
		{
			// Indica si archOrigen existe en archivosDestino
			boolean existe = false;
			for(File archDestino : destino)
			{
				if(archOrigen.getName().compareTo(archDestino.getName()) == 0) 
				{
					// Si ambos son directorios, entonces los sincronizamos
					if(archOrigen.isDirectory() && archDestino.isDirectory())
					{
						sincronizar(archOrigen.getAbsolutePath(), archDestino.getAbsolutePath());
					}
					existe = true;
					break;
				}
			}
			if(!existe) {
				porCopiar.add(archOrigen);
			}
		}
		return porCopiar;
	}
	
	/**
	 * Copia un archivo hacia un directorio destino
	 * 
	 * NOTA: Para que el archivo se copie en la direccion adecuada
	 * el String dirDestino debera venir con un "/" al final
	 * 
	 * @param archivo El archivo que se escribira en el destino
	 * @param dirDestino La ruta hacia el directorio donde se escribira el archivo
	 *        Notese que debera tener un "/" al final para que se copie el 
	 *        archivo en la ruta apropiada
	 */
	private void copiarArchivo(File archivo, String dirDestino)
	{
		String nombre = archivo.getName();
		File archDestino = new File(dirDestino + nombre);
		
		try 
		{
			archDestino.createNewFile();
			
			InputStream in = new FileInputStream(archivo);
			OutputStream out = new FileOutputStream(archDestino);
			
			byte[] buffer = new byte[1024];
			int len;
			while( (len=in.read(buffer)) > 0 )
			{
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
			System.out.println("Se copio: ");
			System.out.println(archDestino.getAbsolutePath());
		} 
		catch(Exception err)
		{
			
		}
	}
	
	/**
	 * Copia un directorio (Con sus subdirectorios e hijos de manera recursiva)
	 * hacia un directorio destino.
	 * 
	 * NOTA: Para que el directorio se copie en la direccion adecuada
	 * el String dirDestino debera venir con un "/" al final
	 * 
	 * @param directorio El directorio que se escribira en el destino
	 * @param dirDestino La ruta hacia el directorio donde se escribira el directorio
	 *        Notese que debera tener un "/" al final para que se copie el 
	 *        directorio en la ruta apropiada
	 */
	private void copiarDirectorio(File directorio, String dirDestino)
	{
		String dirNombre = directorio.getName();
		File archDestino = new File(dirDestino + dirNombre);
		
		archDestino.mkdir();
		File [] porCopiar = directorio.listFiles();
		
		for(File file : porCopiar)
		{
			if(file.isDirectory())
			{
				copiarDirectorio(file, archDestino.getAbsolutePath() + "/");
			}
			else
			{
				copiarArchivo(file, archDestino.getAbsolutePath() + "/");
			}
		}
	}
	
	/**
	 * Valida si un archivo es un directorio o no
	 * 
	 * @param dir El archivo que se validara
	 * @return "true" en caso de que sea un directorio valido, en caso contrario
	 *         muestra un mensaje al usuario y retorna "false"
	 */
	public boolean validaDir(File dir)
	{
		if(dir.isDirectory())
		{
			return true;
		} 
		else
		{
			JOptionPane.showMessageDialog(null, "Ingrese un directorio valido");
			return false;
		}
	}

}