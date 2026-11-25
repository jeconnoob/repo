package mx.edu.uth.ws;

import mx.edu.uth.CoordenadaResponse;
import mx.edu.uth.dto.AccesoResponse;
import mx.edu.uth.dto.DetalleTrayectoResponse;
import mx.edu.uth.dto.EliminaTrayectoResponse;
import mx.edu.uth.dto.TrayectoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebApiService {

    @GET("login.php")
    Call<AccesoResponse> validaAcceso(
            @Query("user") String username,
            @Query("pass") String password
    );

    @GET("guardar_coordenadas.php")
    Call<CoordenadaResponse> guardarCoordenada(
            @Query("usuario") String usuario,
            @Query("trayecto") String trayecto,
            @Query("latitud") double latitud,
            @Query("longitud") double longitud
    );

    @GET("listar_trayectos.php")
    Call<TrayectoResponse> getTrayectos(
            @Query("usuario") String usuario
    );

    @GET("obtener_coordenadas.php")
    Call<DetalleTrayectoResponse> getCoordenadas(
            @Query("usuario") String usuario,
            @Query("trayecto") String trayecto
    );

    @GET("eliminar_trayecto.php")
    Call<EliminaTrayectoResponse> eliminarTrayecto(
            @Query("usuario") String usuario,
            @Query("trayecto") String trayecto
    );
}
