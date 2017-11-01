/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.model;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author jecas
 */
public class ArchivoTemporal implements Serializable{
    private File file;
    private String nameFile;
    private boolean creado;
    private boolean modificado;
    private boolean eliminado;
    private String sha1;
    private boolean reviso;

    public ArchivoTemporal() {
    }

    public ArchivoTemporal(File file, String sha1) {
        this.file = file;
        this.sha1 = sha1;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public boolean isCreado() {
        return creado;
    }

    public void setCreado(boolean creado) {
        this.creado = creado;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public boolean isReviso() {
        return reviso;
    }

    public void setReviso(boolean reviso) {
        this.reviso = reviso;
    }
}
