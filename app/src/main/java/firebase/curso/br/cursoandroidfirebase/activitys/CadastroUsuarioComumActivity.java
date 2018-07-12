package firebase.curso.br.cursoandroidfirebase.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
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

import org.w3c.dom.Text;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Usuario;
import firebase.curso.br.cursoandroidfirebase.dao.ConfiguracaoFirebase;
import firebase.curso.br.cursoandroidfirebase.helper.Preferencias;

public class CadastroUsuarioComumActivity extends AppCompatActivity {

    private BootstrapEditText email;
    private BootstrapEditText senha1;
    private BootstrapEditText senha2;
    private BootstrapEditText nome;
    private BootstrapEditText cpf;
    private BootstrapEditText rua;
    private BootstrapEditText numero;
    private RadioButton rbFeminino;
    private RadioButton rbMasculino;
    private BootstrapButton btnCadastrar;
    private BootstrapButton btnCancelar;
    private FirebaseAuth autenticacao;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Usuario usuario;
    private TextView txtAbreCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario_comum);
        txtAbreCadastro = (TextView) findViewById(R.id.txtAbreCadastro);
        email = (BootstrapEditText) findViewById(R.id.edtCadEmail);
        senha1 = (BootstrapEditText) findViewById(R.id.edtCadSenha1);
        senha2 = (BootstrapEditText) findViewById(R.id.edtCadSenha2);
        nome = (BootstrapEditText) findViewById(R.id.edtCadNome);
        rua = (BootstrapEditText) findViewById(R.id.edtCadRua);
        numero = (BootstrapEditText) findViewById(R.id.edtCadNumero);
        cpf = (BootstrapEditText) findViewById(R.id.edtCadCpf);
        btnCadastrar = (BootstrapButton) findViewById(R.id.btnCadastrar);
        btnCancelar = (BootstrapButton) findViewById(R.id.btnCancelar);
        rbFeminino = (RadioButton) findViewById(R.id.rbFeminino);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculino);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(senha1.getText().toString().equals(senha2.getText().toString())){
                    usuario = new Usuario();
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha1.getText().toString());
                    usuario.setNome(nome.getText().toString());
                    usuario.setRua(rua.getText().toString());
                    usuario.setNumero(numero.getText().toString());
                    usuario.setTipoUsuario("Comum");

                    if(rbFeminino.isChecked()){
                        usuario.setSexo("Feminino");
                    }
                    if(rbMasculino.isChecked()){
                        usuario.setSexo("Masculino");
                    }

                    cadastrarUsuario();
                }else{
                    Toast.makeText(CadastroUsuarioComumActivity.this, "Senha devem ser iguais", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener
                (this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            insereUsuario(usuario);


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
                            Toast.makeText(CadastroUsuarioComumActivity.this, "Erro! " + erroExcecao, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean insereUsuario(Usuario usuario){
        try {
            databaseReference = ConfiguracaoFirebase.getFaribase().child("usuarios");
            //pega o id
            String key = databaseReference.push().getKey();
            usuario.setKeyUsuario(key);
            databaseReference.child(key).setValue(usuario);
            Toast.makeText(CadastroUsuarioComumActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
            abrirLoginUsuario();
            return true;
        }catch (Exception e){
            Toast.makeText(CadastroUsuarioComumActivity.this, "Erro ao gravar Usuario", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
    private void abrirLoginUsuario(){
       Intent intent = new Intent(this, MainActivity.class);
       startActivity(intent);
       finish();
    }
}
