
package ac.cr.una.modelo;

import java.io.File;
import java.io.Serializable;

public class ArchivoControl extends Archivo implements Serializable {

    private boolean elimando;
    private boolean nuevo;
    private boolean modificado;

    public ArchivoControl() {
    }

    public ArchivoControl(boolean elimando, boolean nuevo, boolean modificado) {
        this.elimando = elimando;
        this.nuevo = nuevo;
        this.modificado = modificado;
    }

    public ArchivoControl(boolean elimando, boolean nuevo, boolean modificado, File file, String md5) {
        super(file, md5);
        this.elimando = elimando;
        this.nuevo = nuevo;
        this.modificado = modificado;
    }

    public boolean isElimando() {
        return elimando;
    }

    public void setElimando(boolean elimando) {
        this.elimando = elimando;
    }

    public boolean isNuevo() {
        return nuevo;
    }

    public void setNuevo(boolean nuevo) {
        this.nuevo = nuevo;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

}
