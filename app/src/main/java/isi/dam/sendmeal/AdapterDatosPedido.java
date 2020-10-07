package isi.dam.sendmeal;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;

public class AdapterDatosPedido extends RecyclerView.Adapter<AdapterDatosPedido.PedidoViewHolder> {

    ArrayList<String> listaNombres, listaPrecios;

    public AdapterDatosPedido(ArrayList<String> listaNombres, ArrayList<String> listaPrecios) {
        this.listaNombres = listaNombres;
        this.listaPrecios = listaPrecios;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido, null, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        holder.asignarDatos(listaNombres.get(position), listaPrecios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView plato, precio;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            plato = (TextView) itemView.findViewById(R.id.nombre_pedido);
            precio = (TextView) itemView.findViewById(R.id.precio_pedido);

        }

        public void asignarDatos(String platos, String precios) {
            plato.setText(platos);
            precio.setText(precios);
        }
    }
}