package firebase.curso.br.cursoandroidfirebase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import firebase.curso.br.cursoandroidfirebase.R;
import firebase.curso.br.cursoandroidfirebase.classes.Cardapio;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioAdapter.ViewHolder> {
    private List<Cardapio> mCardapioList;
    private Context context;
    private DatabaseReference databaseReference;
    private List<Cardapio> cardapios;
    private Cardapio todosProdutos;


    public CardapioAdapter(List<Cardapio> l , Context c){
        context = c;
        mCardapioList = l;
    }

    @NonNull
    @Override
    public CardapioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_todos_produtos, viewGroup, false);
        return new CardapioAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardapioAdapter.ViewHolder holder, int position) {
        final Cardapio item= mCardapioList.get(position);
        cardapios = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("cardapio").orderByChild("keyProduto").equalTo(item.getKeyProduto()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cardapios.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    todosProdutos = postSnapshot.getValue(Cardapio.class);
                    cardapios.add(todosProdutos);
                    //pega tamanho da tela
                    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                    final int heigth = (displayMetrics.heightPixels / 4);
                    final int width = (displayMetrics.widthPixels / 2);

                    Picasso.get().load(todosProdutos.getUrlImagem()).resize(width, heigth).centerCrop().into(holder.fotoProdutoCardaapio);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeProdutoCardapio.setText(item.getNomePrato());
        holder.txtDescricaorodutoCardapio.setText("Ingredientes " + item.getDescricao());
        holder.txtServeQuantidadeCardapio.setText("Serve " + item.getServeQuantidade());
        holder.txtPrecoProdutoCardapio.setText("R$ " + item.getPreco());
        holder.linearLayoutProdutoCardapio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardapioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView txtNomeProdutoCardapio;
        protected TextView txtDescricaorodutoCardapio;
        protected TextView txtPrecoProdutoCardapio;
        protected TextView txtServeQuantidadeCardapio;
        protected ImageView fotoProdutoCardaapio;
        protected LinearLayout linearLayoutProdutoCardapio;

        public ViewHolder (View viewHolder){
            super(viewHolder);
            txtNomeProdutoCardapio = (TextView)itemView.findViewById(R.id.txtProdutoCardapio);
            txtDescricaorodutoCardapio = (TextView)itemView.findViewById(R.id.txtDescricaoProdutoCardapio);
            txtPrecoProdutoCardapio = (TextView)itemView.findViewById(R.id.txtPrecoProdutoCardapio);
            txtServeQuantidadeCardapio = (TextView)itemView.findViewById(R.id.txtServeQuantidadeProdutoCardapio);
            fotoProdutoCardaapio = (ImageView)itemView.findViewById(R.id.fotoProdutoCardapio);
            linearLayoutProdutoCardapio = (LinearLayout)itemView.findViewById(R.id.linerPesquisaCardapio);

        }

    }
}
