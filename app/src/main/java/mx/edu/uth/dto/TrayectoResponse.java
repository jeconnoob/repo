package mx.edu.uth.dto;

import java.lang.reflect.Array;
import java.util.List;

public class TrayectoResponse {
    private boolean encontrado;
    private int total;
    List<Trayecto> trayectos;

    public boolean isEncontrado() {
        return encontrado;
    }

    public int getTotal() {
        return total;
    }

    public List<Trayecto> getTrayectos() {
        return trayectos;
    }
}
