/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.modelo;

public class Archivo {
    private String nombre;
    private Long fechaModificacion;
    private Long tamano;
    private String md5;

    public Archivo() {
    }

    public Archivo(String nombre, Long fechaModificacion, Long tamano, String md5) {
        
        this.nombre = nombre;
        this.fechaModificacion = fechaModificacion;
        this.tamano = tamano;
        this.md5 = md5;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Long fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Long getTamano() {
        return tamano;
    }

    public void setTamano(Long tamano) {
        this.tamano = tamano;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    
                        
}
