package isi.dam.sendmeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDatosRecycler extends RecyclerView.Adapter<AdapterDatosRecycler.PlatoViewHolder> {

    ArrayList<String> listaNombres, listaPrecios;

    public AdapterDatosRecycler(ArrayList<String> listaNombres, ArrayList<String> listaPrecios) {
        this.listaNombres = listaNombres;
        this.listaPrecios = listaPrecios;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_plato, null, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        holder.asignarDatos(listaNombres.get(position), listaPrecios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public class PlatoViewHolder extends RecyclerView.ViewHolder {

        TextView plato, precio;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);

            plato = (TextView) itemView.findViewById(R.id.plato);
            precio = (TextView) itemView.findViewById(R.id.precio);

        }

        public void asignarDatos(String platos, String precios) {
            plato.setText(platos);
            precio.setText(precios);
        }
    }
}
