package mx.edu.uth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mx.edu.uth.dto.AccesoResponse;
import mx.edu.uth.ws.WebApiAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Callback<AccesoResponse> {

    public EditText valorUser;
    public EditText valorPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        valorUser = findViewById(R.id.campoUser);
        valorPassword = findViewById(R.id.campoPassword);
    }


    public void validaAcceso(View v) {
        String usuario = valorUser.getText().toString().trim();
        String password = valorPassword.getText().toString().trim();


        if (usuario.isEmpty()) {
            valorUser.setError("El usuario es obligatorio");
            valorUser.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            valorPassword.setError("La contraseña es obligatoria");
            valorPassword.requestFocus();
            return;
        }


        Call<AccesoResponse> call = WebApiAdapter.getApiService().validaAcceso(usuario, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<AccesoResponse> call, Response<AccesoResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            AccesoResponse respuesta = response.body();
            if (respuesta.isAcceso()) {
                Intent intent = new Intent(this, MenuPrincipalActivity.class);


                intent.putExtra("usuario_obj", respuesta);
                startActivity(intent);


                finish();
            } else {
                Toast.makeText(this, "Datos de acceso incorrectos", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Respuesta no válida del servidor", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<AccesoResponse> call, Throwable t) {
        Toast.makeText(this, "Error al intentar consumir el WS: " + t.getMessage(), Toast.LENGTH_LONG).show();
    }
}