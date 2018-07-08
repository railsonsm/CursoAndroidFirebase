package firebase.curso.br.cursoandroidfirebase.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;
import firebase.curso.br.cursoandroidfirebase.dao.ConfiguracaoFirebase;
import firebase.curso.br.cursoandroidfirebase.helper.Preferencias;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private BootstrapEditText email;
    private BootstrapEditText senha1;
    private BootstrapEditText senha2;
    private BootstrapEditText nome;
    private RadioButton rbAdmin;
    private RadioButton rbAtendente;
    private BootstrapButton btnCadastrar;
    private BootstrapButton btnCancelar;
    private FirebaseAuth autenticacao;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        email = (BootstrapEditText) findViewById(R.id.edtCadEmail);
        senha1 = (BootstrapEditText) findViewById(R.id.edtCadSenha1);
        senha2 = (BootstrapEditText) findViewById(R.id.edtCadSenha2);
        nome = (BootstrapEditText) findViewById(R.id.edtCadNome);
        btnCadastrar = (BootstrapButton) findViewById(R.id.btnCadastrar);
        btnCancelar = (BootstrapButton) findViewById(R.id.btnCancelar);
        rbAdmin = (RadioButton) findViewById(R.id.rbAdmin);
        rbAtendente = (RadioButton) findViewById(R.id.rbAtend);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(senha1.getText().toString().equals(senha2.getText().toString())){
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha1.getText().toString());
                    usuario.setNome(nome.getText().toString());

                    if(rbAdmin.isChecked()){
                        usuario.setTipoUsuario("Administrador");
                    }
                    if(rbAtendente.isChecked()){
                        usuario.setTipoUsuario("Atendente");
                    }

                    cadastrarUsuario();
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this, "Senha devem ser iguais", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            insereUsuario(usuario);
                            finish();
                            //deslogar ao adicionar usuario
                            autenticacao.signOut();
                            //abre tela depois de reautenticar
                            abreTelaPrincipal();

                        }else{
                            String erroExcecao = "";
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                erroExcecao = "Digite uma senha mais forte, contendo no minimo 8 caracteres e que" +
                                        " contenham letras e numeros";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcecao = "Email digitado é invalido, digite novamente";
                            }catch (FirebaseAuthUserCollisionException e){
                                erroExcecao = "Esse email ja etá cadastro";
                            }catch (Exception e){
                                erroExcecao = "Erro ao executar o cadastro";
                            }
                            Toast.makeText(CadastroUsuarioActivity.this, "Erro! " + erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean insereUsuario(Usuario usuario){
        try {
            databaseReference = ConfiguracaoFirebase.getFaribase().child("usuarios");
            databaseReference.push().setValue(usuario);
            Toast.makeText(CadastroUsuarioActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroUsuarioActivity.this, "Erro ao gravar Usuario", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
    private void abreTelaPrincipal(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        Preferencias preferencias= new Preferencias(CadastroUsuarioActivity.this);
        autenticacao.signInWithEmailAndPassword(preferencias.getEmailUsuarioLogado(), preferencias.getSenhaUsuarioLogado())
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this, "logado", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CadastroUsuarioActivity.this, PrincipalActivity.class));
                    finish();
                }else{
                    Toast.makeText(CadastroUsuarioActivity.this, "Falha", Toast.LENGTH_LONG).show();
                    autenticacao.signOut();
                    startActivity(new Intent(CadastroUsuarioActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

}
