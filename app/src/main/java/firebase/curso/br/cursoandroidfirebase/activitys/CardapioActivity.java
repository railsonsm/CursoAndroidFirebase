package firebase.curso.br.cursoandroidfirebase.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.adapter.CardapioAdapter;
import firebase.curso.br.cursoandroidfirebase.classes.Cardapio;

public class CardapioActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CardapioAdapter adapter;
    private List<Cardapio>cardapios;
    private DatabaseReference reference;
    private Cardapio todosCardapios;
    private LinearLayoutManager linearLayoutManagerTodosProdutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleViewTodosProdutos);

        carregarTodosProdutos();


    }

    private void carregarTodosProdutos(){
        linearLayoutManagerTodosProdutos = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManagerTodosProdutos);
        mRecyclerView.setHasFixedSize(true);
        cardapios = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference();


        reference.child("cardapio").orderByChild("nomePrato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todosCardapios = postSnapshot.getValue(Cardapio.class);
                    cardapios.add(todosCardapios);
                  }
                  adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        adapter = new CardapioAdapter(cardapios, this);
        mRecyclerView.setAdapter(adapter);

    }
}
