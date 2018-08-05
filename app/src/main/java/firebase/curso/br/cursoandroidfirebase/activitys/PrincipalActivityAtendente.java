package firebase.curso.br.cursoandroidfirebase.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import firebase.curso.br.cursoandroidfirebase.R;

public class PrincipalActivityAtendente extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_atendente);
        autenticacao = FirebaseAuth.getInstance();

        getWindow().setBackgroundDrawable(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_atend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.action_cad_foto_perfil_atend){
            startActivity(new Intent(this, UploadFotoActivity.class));
        }else if(id == R.id.action_sair_atend){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
