package mx.edu.uth.dto;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AccesoResponse implements Serializable {

    @SerializedName("acceso")
    private boolean acceso;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("username")
    private String username;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellidoPaterno")
    private String apellidoPaterno;

    @SerializedName("apellidoMaterno")
    private String apellidoMaterno;

    @SerializedName("correoElectronico")
    private String correoElectronico;

    @SerializedName("idPerfil")
    private int idPerfil;

    public AccesoResponse() {
    }

    public boolean isAcceso() {
        return acceso;
    }

    public void setAcceso(boolean acceso) {
        this.acceso = acceso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }
}