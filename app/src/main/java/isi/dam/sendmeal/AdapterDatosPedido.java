package isi.dam.sendmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class AdapterDatosPedido extends RecyclerView.Adapter<AdapterDatosPedido.PedidoViewHolder> {

    ArrayList<String> listaNombres, listaPrecios, listaCantidades;
    ArrayList<ImageButton> borrar;

    public AdapterDatosPedido(ArrayList<String> listaCantidades, ArrayList<String> listaNombres, ArrayList<String> listaPrecios) {
        this.listaCantidades = listaCantidades;
        this.listaNombres = listaNombres;
        this.listaPrecios = listaPrecios;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido, null, false);
        /*borrar = new ArrayList<ImageButton>();
        for(int i=0; i < listaNombres.size(); i++) {
            ImageButton borrar_aux = (ImageButton) view.findViewById(R.id.buttonBorrar);
            final int finalI = i;
            borrar_aux.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String plato_a_eliminar = listaNombres.get(finalI);
                    for(int j = 0; j<AdapterDatosRecycler.listaPlatosPedidos.size(); j++) {
                        if(AdapterDatosRecycler.listaPlatosPedidos.get(j).getTitulo().equals(plato_a_eliminar)) {
                            AdapterDatosRecycler.listaPlatosPedidos.removeAll(Collections.singleton(AdapterDatosRecycler.listaPlatosPedidos.get(j)));
                        }
                    }
                }
            });
            borrar.add(borrar_aux);
        }*/

        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        holder.asignarDatos(listaCantidades.get(position), listaNombres.get(position), listaPrecios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView plato, precio, cantidad;
        RelativeLayout layoutBorrar;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);

            cantidad = (TextView) itemView.findViewById(R.id.cantidad);
            plato = (TextView) itemView.findViewById(R.id.nombre_pedido);
            precio = (TextView) itemView.findViewById(R.id.precio_pedido);
            layoutBorrar = (RelativeLayout) itemView.findViewById(R.id.layoutABorrar);

        }

        public void asignarDatos(String cantidades, String platos, String precios) {
            cantidad.setText(cantidades);
            plato.setText(platos);
            precio.setText(precios);
        }

    }

    public void removeItem(int position){
        listaNombres.remove(position);
        listaCantidades.remove(position);
        listaPrecios.remove(position);
        notifyItemRemoved(position);

        for(int i=0; i < listaNombres.size(); i++) {
            String plato_a_eliminar = listaNombres.get(i);
            for(int j = 0; j<AdapterDatosRecycler.listaPlatosPedidos.size(); j++) {
                   if(AdapterDatosRecycler.listaPlatosPedidos.get(j).getTitulo().equals(plato_a_eliminar)) {
                       AdapterDatosRecycler.listaPlatosPedidos.removeAll(Collections.singleton(AdapterDatosRecycler.listaPlatosPedidos.get(j)));
                   }
            }
        }
    }


}