
package mx.edu.uth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.ExerciseRoute;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Locale;

import mx.edu.uth.dto.AccesoResponse;
import mx.edu.uth.ws.WebApiAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrayectoActivity extends AppCompatActivity {
    private static final long INTERVALO_MS = 10000;

    private EditText etTrayecto;
    private Button btnCaptura;


    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private  boolean capturando = false;
    private  int puntosEnviados = 0;

    private TextView tvUltimaCoordenada;
    private TextView tvEstado;


    private FusedLocationProviderClient clienteUbicacion;

    private String usuario;
    private String trayecto;
    //private static final int CODIGO_PERMISO_GPS = 1001;
    private final ActivityResultLauncher<String[]> permisosLauncher =
            registerForActivityResult(new ActivityResultContracts.
                    RequestMultiplePermissions(),
                    resultado ->{
                Boolean finaccess = resultado.get(Manifest.permission.ACCESS_FINE_LOCATION);
                if(Boolean.TRUE.equals(finaccess)){
                    iniciarCaptura();

                }else{
                    Toast.makeText(this,
                            "Se necesita el permiso de ubicación",
                            Toast.LENGTH_LONG).show();
                }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trayecto);

        etTrayecto = findViewById(R.id.etTrayecto);
        btnCaptura = findViewById(R.id.btnCaptura);
        tvEstado = findViewById(R.id.tvEstado);
        tvUltimaCoordenada = findViewById(R.id.tvUltimaCoordenada);

        usuario = getIntent().getStringExtra("usuario");
        if(usuario==null){
            usuario="Invitado";
        }

        clienteUbicacion =
                LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,
                INTERVALO_MS).setMinUpdateIntervalMillis(INTERVALO_MS/2).build();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location ubicacion = locationResult.getLastLocation();
                if (ubicacion != null) {
                    enviarCoordenada(
                            ubicacion.getLatitude(),
                            ubicacion.getLongitude());
                }
            }
        };
        btnCaptura.setOnClickListener(v -> {
            if(capturando){
                detenerCaptura();
            }else{
                validarYComenzar();
            }
        });
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(capturando){
                    Toast.makeText(TrayectoActivity.this,
                            "Deten la captura antes de salir",Toast.LENGTH_SHORT).show();
                }else{
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

    }
    private void validarYComenzar(){
        trayecto=etTrayecto.getText().toString().trim();
        if(trayecto.isEmpty()){
            etTrayecto.setError("Escribe el nombre del trayecto");
            return;
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED){
            iniciarCaptura();
        }else{
            permisosLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }
    }

    @SuppressLint("MissingPermission")
    private void iniciarCaptura(){
        clienteUbicacion.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        capturando = true;
        puntosEnviados = 0;
        etTrayecto.setEnabled(false);
        btnCaptura.setText("Detener Captura");
        tvEstado.setText("Capturando.. esperando la primera coordenada");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void detenerCaptura(){
        clienteUbicacion.removeLocationUpdates(locationCallback);
        capturando=false;
        etTrayecto.setEnabled(true);
        btnCaptura.setText("Iniciar Captura");
        tvEstado.setText("Captura detenida. Puntos enviados: "+ puntosEnviados);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void enviarCoordenada(double latitud, double longitud) {
        tvUltimaCoordenada.setText(String.format(Locale.US, "Última: %.7f, %.7f",
                latitud, longitud));

        WebApiAdapter.getApiService()
                .guardarCoordenada(usuario, trayecto, latitud, longitud)
                .enqueue(new Callback<CoordenadaResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CoordenadaResponse> call,
                                           @NonNull Response<CoordenadaResponse> response) {
                        CoordenadaResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.isGuardado()) {
                            puntosEnviados++;
                            tvEstado.setText("Capturando... puntos enviados: " + puntosEnviados);
                        } else {
                            tvEstado.setText("El servidor respondió con error (" + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CoordenadaResponse> call, @NonNull Throwable t) {
                        tvEstado.setText("Sin conexión, se reintentará con la siguiente coordenada");
                    }

                });
    }

    protected void OnDestroy(){
        super.onDestroy();
        if(capturando){
            clienteUbicacion.removeLocationUpdates(locationCallback);
        }
    }
}

