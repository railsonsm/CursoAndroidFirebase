package firebase.curso.br.cursoandroidfirebase.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Cardapio;
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;
import firebase.curso.br.cursoandroidfirebase.dao.ConfiguracaoFirebase;
import firebase.curso.br.cursoandroidfirebase.helper.Preferencias;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private BootstrapEditText edtEmail;
    private BootstrapEditText edtSenha;
    private BootstrapButton btnLogin;
    private Usuario usuario;
    private TextView txtAbreCadastro;
    private TextView txtRecuperarSenha;
    private DatabaseReference databaseReference;
    private android.support.v7.app.AlertDialog aleta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissao();
        txtAbreCadastro = (TextView) findViewById(R.id.txtAbreCadastro);
        txtRecuperarSenha = (TextView) findViewById(R.id.txtRecuperarSenha);
        edtEmail = (BootstrapEditText) findViewById(R.id.editEmail);
        edtSenha = (BootstrapEditText) findViewById(R.id.editSenha);
        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        autenticacao = FirebaseAuth.getInstance();

        final EditText editTextEmail = new EditText(MainActivity.this);
        editTextEmail.setHint("exemplo@exemplo.com");

        if(usuarioLogado()){
            String email = autenticacao.getCurrentUser().getEmail().toLowerCase();
            abrirTelaPrincipal(email);
            finish();
           // startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
        }else{
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")){
                        usuario = new Usuario();

                        usuario.setEmail(edtEmail.getText().toString());
                        usuario.setSenha(edtSenha.getText().toString());

                        validarLogin();

                    }else {
                        Toast.makeText(MainActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        txtAbreCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CadastroUsuarioComumActivity.class));
                finish();
            }
        });

        txtRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "oi", Toast.LENGTH_SHORT).show();
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Recuperar senha");
            builder.setMessage("Informe o seu email");
            builder.setView(editTextEmail);
            if(!editTextEmail.getText().equals("")){
                builder.setPositiveButton("Recuperar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        autenticacao = FirebaseAuth.getInstance();
                        String emailRecuperar = editTextEmail.getText().toString();
                        autenticacao.sendPasswordResetEmail(emailRecuperar).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Em instante voce receberar um email", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(MainActivity.this, "Falha ao enviar email", Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                                aleta = builder.create();
                                aleta.show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
            }else{
                Toast.makeText(MainActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            abrirTelaPrincipal(usuario.getEmail().toString());
                            Preferencias preferencias = new Preferencias(MainActivity.this);
                            preferencias.salvarUsuarioPreferencias(usuario.getEmail(), usuario.getSenha());
                            Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Usuário e/ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void abrirTelaPrincipal(String emailUsuario){
        String email = autenticacao.getCurrentUser().getEmail().toLowerCase();
        databaseReference.child("usuarios").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                  String tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();
                  if(tipoUsuarioEmail.equals("Administrador")){
                        startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
                        finish();
                  }else if(tipoUsuarioEmail.equals("Atendente")){
                      startActivity(new Intent(MainActivity.this, PrincipalActivityAtendente.class));
                      finish();
                  }else if(tipoUsuarioEmail.equals("Comum")){
                      startActivity(new Intent(MainActivity.this, PrincipalActivityComum.class));
                      finish();
                  }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            return true;
        }
        return false;
    }

    public void permissao(){
        int PERMISSAO_ALL = 1;
        String [] PERMISSAO = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, PERMISSAO,PERMISSAO_ALL);
    }
}
