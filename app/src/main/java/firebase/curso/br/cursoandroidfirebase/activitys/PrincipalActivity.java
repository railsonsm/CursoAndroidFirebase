package firebase.curso.br.cursoandroidfirebase.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Cardapio;
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference;
    private TextView tipoUsuario;
    private Usuario usuario;
    private String tipoUsuarioEmail;
    private String nomeUsuarioEmail;
    private Menu menu1;
    private LinearLayout linearLayoutAddProdutos;
    private LinearLayout totalVendido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        autenticacao = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        linearLayoutAddProdutos = (LinearLayout) findViewById(R.id.linearAddProdutos);
        totalVendido = (LinearLayout) findViewById(R.id.totalVendas);

        linearLayoutAddProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastroProduto();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.action_add_usuario){
           startActivity(new Intent(PrincipalActivity.this, CadastroUsuarioActivity.class));
        }else if(id == R.id.action_sair_admin){
            deslogarUsuario();
        }else if(id == R.id.action_cardapio){
            startActivity(new Intent(PrincipalActivity.this, CardapioActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void deslogarUsuario(){
        autenticacao.signOut();
        startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
        finish();
    }

    private void uploadFotoPerfil(){
        startActivity(new Intent(PrincipalActivity.this, UploadFotoActivity.class));
        finish();
    }

    private void abrirTelaCadastroProduto(){

    }
}

