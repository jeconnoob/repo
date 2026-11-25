package mx.edu.uth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipalActivity extends AppCompatActivity {
    private ListView ListaDeTrayectos;
    private String[] elementos;



    // Recuperar el objeto de acceso que mandó el LoginActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperar el objeto de acceso que mandó el LoginActivity
        mx.edu.uth.dto.AccesoResponse usuarioObj = (mx.edu.uth.dto.AccesoResponse) getIntent().getSerializableExtra("usuario_obj");
        String username = (usuarioObj != null) ? usuarioObj.getUsername() : "invitado";
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ListaDeTrayectos=findViewById(R.id.lista_menu);
        elementos=new String[]{"Capturar trayectos","Lista de trayectos"};
        ArrayAdapter adaptador=new ArrayAdapter(this, android.R.layout.simple_list_item_1,elementos);
        ListaDeTrayectos.setAdapter(adaptador);
        ListaDeTrayectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                Intent ii = null;
                Toast.makeText(MenuPrincipalActivity.this,"indice"+i, Toast.LENGTH_SHORT).show();
                switch (i){
                    case 0:
                        Toast.makeText(MenuPrincipalActivity.this,"",Toast.LENGTH_SHORT).show();
                        ii=new Intent(getApplicationContext(),TrayectoActivity.class);
                        ii.putExtra("usuario", username);
                        break;
                    case 1:
                        Toast.makeText(MenuPrincipalActivity.this,"",Toast.LENGTH_SHORT).show();
                        ii = new Intent(getApplicationContext(),ListaTrayectosActivity.class);
                        ii.putExtra("usuario", username);
                        break;
                }
                startActivity(ii);
            }
        });
    }
}