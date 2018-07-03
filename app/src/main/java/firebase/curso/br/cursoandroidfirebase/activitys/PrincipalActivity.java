package firebase.curso.br.cursoandroidfirebase.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private DatabaseReference databaseReference;
    private TextView tipoUsuario;
    private Usuario usuario;
    private String tipoUsuarioEmail;
    private String nomeUsuarioEmail;
    private Menu menu1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        autenticacao = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        this.menu1 = menu;

        tipoUsuario = (TextView) findViewById(R.id.txtTipoUsuario);
        String email = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
        }

        databaseReference.child("usuarios").orderByChild("email").equalTo(email.toString())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postStapshots: dataSnapshot.getChildren()){
                            tipoUsuarioEmail = postStapshots.child("tipoUsuario").getValue().toString();
                            nomeUsuarioEmail = postStapshots.child("nome").getValue().toString();
                            tipoUsuario.setText(tipoUsuarioEmail.toString()+ " " + nomeUsuarioEmail);
                            menu1.clear();
                            if(tipoUsuarioEmail.equals("Administrador")){
                                getMenuInflater().inflate(R.menu.menu_admin, menu1);
                            }else if(tipoUsuarioEmail.equals("Atendente")){
                                getMenuInflater().inflate(R.menu.menu_atend, menu1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id == R.id.action_add_usuario){
           startActivity(new Intent(PrincipalActivity.this, CadastroUsuarioActivity.class));
        }else if(id == R.id.action_sair_admin || id == R.id.action_sair_atend){
            deslogarUsuario();
        }else if(id == R.id.action_cad_foto_perfil_atend){
            uploadFotoPerfil();
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
}

