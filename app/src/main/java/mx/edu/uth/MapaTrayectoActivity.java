package mx.edu.uth;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import mx.edu.uth.dto.Coordenada;
import mx.edu.uth.dto.DetalleTrayectoResponse;
import mx.edu.uth.dto.EliminaTrayectoResponse;
import mx.edu.uth.ws.WebApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaTrayectoActivity extends AppCompatActivity {
    private MapView mapa;
    private TextView tvResumen;
    private Button btnElimina;
    private String usuario;
    private String trayecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        Configuration.getInstance().setOsmdroidBasePath(getCacheDir());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mapa_trayecto);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuario = getIntent().getStringExtra("usuario");
        trayecto = getIntent().getStringExtra("trayecto");

        if (usuario == null) usuario = "invitado";
        if (trayecto == null) trayecto = "Sin nombre";

        TextView tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText(trayecto);
        tvResumen = findViewById(R.id.tvResumen);
        btnElimina = findViewById(R.id.btnElimina);

        btnElimina.setOnClickListener(v -> eliminarTrayecto());

        mapa = findViewById(R.id.mapa);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.setMultiTouchControls(true);
        mapa.getController().setZoom(15.0);

        cargarCoordenadas();
    }

    public void cargarCoordenadas() {
        WebApiAdapter.getApiService().getCoordenadas(usuario, trayecto)
                .enqueue(new Callback<DetalleTrayectoResponse>() {
                    @Override
                    public void onResponse(Call<DetalleTrayectoResponse> call,
                                           Response<DetalleTrayectoResponse> response) {
                        DetalleTrayectoResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.isEncontrado()) {
                            tvResumen.setText("Coordenadas cargadas: " + body.getCoordenadas().size());
                            dibujarTrayecto(body.getCoordenadas());
                        } else {
                            tvResumen.setText("No se encontraron coordenadas para este trayecto.");
                        }
                    }

                    @Override
                    public void onFailure(Call<DetalleTrayectoResponse> call, Throwable t) {
                        tvResumen.setText("Error de red: " + t.getMessage());
                    }
                });
    }

    private void eliminarTrayecto() {
        WebApiAdapter.getApiService().eliminarTrayecto(usuario, trayecto)
                .enqueue(new Callback<EliminaTrayectoResponse>() {
                    @Override
                    public void onResponse(Call<EliminaTrayectoResponse> call,
                                           Response<EliminaTrayectoResponse> response) {
                        EliminaTrayectoResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.isEliminado()) {
                            Toast.makeText(MapaTrayectoActivity.this, "Trayecto eliminado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (response.isSuccessful() && body != null) {
                            Toast.makeText(MapaTrayectoActivity.this, body.getMensaje(), Toast.LENGTH_SHORT).show();
                        } else if (response.errorBody() != null) {
                            try {
                                com.google.gson.Gson gson = new com.google.gson.Gson();
                                EliminaTrayectoResponse errBody = gson.fromJson(response.errorBody().string(), EliminaTrayectoResponse.class);
                                String msg = (errBody != null && errBody.getMensaje() != null) ? errBody.getMensaje() : "El trayecto no existe";
                                Toast.makeText(MapaTrayectoActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(MapaTrayectoActivity.this, "El trayecto no existe", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MapaTrayectoActivity.this, "El trayecto no existe", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EliminaTrayectoResponse> call, Throwable t) {
                        Toast.makeText(MapaTrayectoActivity.this, "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dibujarTrayecto(List<Coordenada> coordenadas) {
        if (coordenadas == null || coordenadas.isEmpty()) {
            return;
        }
        List<GeoPoint> puntos = new ArrayList<>();

        for (Coordenada c : coordenadas) {
            puntos.add(new GeoPoint(c.getLatitud(), c.getLongitud()));
        }
        if (puntos.isEmpty()) {
            return;
        }

        Polyline caminito = new Polyline(mapa);
        caminito.setPoints(puntos);
        caminito.getOutlinePaint().setColor(Color.parseColor("#FF00FF"));
        caminito.getOutlinePaint().setStrokeWidth(10f);
        mapa.getOverlays().add(caminito);

        GeoPoint inicio = puntos.get(0);
        Marker marcadorInicio = new Marker(mapa);
        marcadorInicio.setPosition(inicio);
        marcadorInicio.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marcadorInicio.setTitle("Inicio");
        marcadorInicio.setSnippet("Fecha: " + coordenadas.get(0).getFecha_registro());
        mapa.getOverlays().add(marcadorInicio);

        GeoPoint fin = puntos.get(puntos.size() - 1);
        Marker marcadorFin = new Marker(mapa);
        marcadorFin.setPosition(fin);
        marcadorFin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marcadorFin.setTitle("Fin");
        marcadorFin.setSnippet("Fecha: " + coordenadas.get(coordenadas.size() - 1).getFecha_registro());
        mapa.getOverlays().add(marcadorFin);

        mapa.getController().setCenter(inicio);
        // Ajustar el zoom automáticamente para que se vea todo el trayecto completo
        BoundingBox boundingBox = BoundingBox.fromGeoPoints(puntos);
        mapa.zoomToBoundingBox(boundingBox, true, 100); // 100 es el margen (padding) en píxeles

        mapa.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapa != null) {
            mapa.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapa != null) {
            mapa.onPause();
        }
    }
}
