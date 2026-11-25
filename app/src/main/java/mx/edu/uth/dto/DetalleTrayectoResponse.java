package mx.edu.uth.dto;

import java.util.List;

public class DetalleTrayectoResponse {
    private boolean encontrado;
    private String mensaje;
    private List<Coordenada> coordenadas;


    public boolean isEncontrado() {
        return encontrado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public List<Coordenada> getCoordenadas() {
        return coordenadas;
    }
}
