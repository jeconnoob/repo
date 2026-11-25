package mx.edu.uth;

import com.google.gson.annotations.SerializedName;

public class CoordenadaResponse {
    private boolean guardado;
    private String mensaje;

    @SerializedName("trayecto_id")
    private String trayecto_id;

    public boolean isGuardado(){
        return guardado;

    }
    public String getMensaje(){
        return mensaje;
    }
    public String getTrayecto_id(){
        return trayecto_id;
    }

}
