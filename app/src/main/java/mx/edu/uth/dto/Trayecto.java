package mx.edu.uth.dto;
import java.util.Date;

public class Trayecto {
    private int id;
    private boolean activo;
    private String usuario;
    private String nombre;
    private String fecha_creacion;
    private int puntos;

    public int getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public int getPuntos() {
        return puntos;
    }
    public boolean isActivo() {
        return activo;
    }

}
