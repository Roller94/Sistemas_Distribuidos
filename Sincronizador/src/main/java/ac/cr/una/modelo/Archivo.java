/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.modelo;

import java.io.File;

public class Archivo {
    private File file;
    private String md5;

    public Archivo() {
    }

    public Archivo(File file, String md5) {
        this.file = file;
        this.md5 = md5;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
             
}
