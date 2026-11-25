package mx.edu.uth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mx.edu.uth.adapters.TrayectoAdapter;
import mx.edu.uth.dto.Trayecto;
import mx.edu.uth.dto.TrayectoResponse;
import mx.edu.uth.ws.WebApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaTrayectosActivity extends AppCompatActivity {
    private RecyclerView rvTrayectos;
    private ProgressBar progress;
    private TextView tvMensaje;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_trayectos);

        rvTrayectos = findViewById(R.id.rvTrayectos);
        progress = findViewById(R.id.progress);
        tvMensaje = findViewById(R.id.tvMensaje);

        rvTrayectos.setLayoutManager(new LinearLayoutManager(this));
        rvTrayectos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        usuario = getIntent().getStringExtra("usuario");
        if (usuario == null) usuario = "invitado";
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
        }
        if (tvMensaje != null) {
            tvMensaje.setVisibility(View.GONE);
        }
        cargarTrayectos();
    }

    private void cargarTrayectos() {
        WebApiAdapter.getApiService()
                .getTrayectos(usuario)
                .enqueue(new Callback<TrayectoResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TrayectoResponse> call,
                                           @NonNull Response<TrayectoResponse> response) {
                        progress.setVisibility(View.GONE);
                        TrayectoResponse body = response.body();

                        if (response.isSuccessful() && body != null && body.isEncontrado()) {
                            rvTrayectos.setAdapter(new TrayectoAdapter(body.getTrayectos(),
                                    trayecto -> abrirTrayecto(trayecto)));

                        } else {
                            mostrarMensaje("Aún no tienes trayectos registrados");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TrayectoResponse> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                        mostrarMensaje("Sin conexión con el servidor");
                    }
                });
    }

    private void mostrarMensaje(String texto) {
        tvMensaje.setText(texto);
        tvMensaje.setVisibility(View.VISIBLE);
    }
// lleva los datos el mapa trayecto activity
    private void abrirTrayecto(Trayecto trayecto) {
        Intent intent = new Intent(this, MapaTrayectoActivity.class);
        intent.putExtra("usuario", usuario);                   // Envía el usuario actual
        intent.putExtra("trayecto", trayecto.getNombre());     // Envía el nombre del trayecto elegido
        startActivity(intent);
        Toast.makeText(this, "Elegiste: " + trayecto.getNombre(), Toast.LENGTH_SHORT).show();
    }
}
