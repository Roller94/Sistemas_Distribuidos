/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ac.cr.una.model;

import java.io.Serializable;

/**
 *
 * @author jecas
 */
public class ArchivoGuardado implements Serializable{
    private String name;
    private long size;
    private String date;
    private String sha1;

    public ArchivoGuardado() {
    }

    public ArchivoGuardado(String name, long size, String date, String sha1) {
        this.name = name;
        this.size = size;
        this.date = date;
        this.sha1 = sha1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}
