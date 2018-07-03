package firebase.curso.br.cursoandroidfirebase.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;
import firebase.curso.br.cursoandroidfirebase.dao.ConfiguracaoFirebase;
import firebase.curso.br.cursoandroidfirebase.helper.Preferencias;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnLogin;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissao();

        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtSenha = (EditText) findViewById(R.id.editSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        if(usuarioLogado()){
            startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
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
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            abrirTelaPrincipal();
                            Preferencias preferencias = new Preferencias(MainActivity.this);
                            preferencias.salvarUsuarioPreferencias(usuario.getEmail(), usuario.getSenha());
                            Toast.makeText(MainActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Usuário e/ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void abrirTelaPrincipal(){
        finish();
        startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
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
