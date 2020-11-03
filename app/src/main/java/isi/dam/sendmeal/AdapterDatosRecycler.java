package isi.dam.sendmeal;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Plato;

public class AdapterDatosRecycler extends RecyclerView.Adapter<AdapterDatosRecycler.PlatoViewHolder> {

    ArrayList<String> listaNombres, listaPrecios;
    public static ArrayList<Plato> listaPlatosPedidos = new ArrayList<Plato>();
    String pantallaAnterior;

    public AdapterDatosRecycler(ArrayList<String> listaNombres, ArrayList<String> listaPrecios, String pantallaAnterior) {
        this.listaNombres = listaNombres;
        this.listaPrecios = listaPrecios;
        this.pantallaAnterior = pantallaAnterior;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_plato, null, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        holder.asignarDatos(listaNombres.get(position), listaPrecios.get(position), pantallaAnterior);
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public class PlatoViewHolder extends RecyclerView.ViewHolder {

        TextView plato, precio, cantidad;
        Button pedir, aumentarCantidad, disminuirCantidad;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);

            plato = (TextView) itemView.findViewById(R.id.plato);
            precio = (TextView) itemView.findViewById(R.id.precio_pedido);
            cantidad = (TextView) itemView.findViewById(R.id.cantidadPedido);
            pedir = (Button) itemView.findViewById(R.id.pedir_btn);
            aumentarCantidad = (Button) itemView.findViewById(R.id.buttonAumentar);
            disminuirCantidad = (Button) itemView.findViewById(R.id.buttonDisminuir);

        }

        public void asignarDatos(String platos, String precios, String pantallaAnterior) {
            plato.setText(platos);
            precio.setText(precios);

            if(pantallaAnterior.equals("home")){
                pedir.setVisibility(View.GONE);
                cantidad.setVisibility(View.GONE);
                aumentarCantidad.setVisibility(View.GONE);
                disminuirCantidad.setVisibility(View.GONE);
            }
            else if(pantallaAnterior.equals("pedido")){
                pedir.setVisibility(View.VISIBLE);
            }

            pedir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0; i<Plato.lista_platos.size(); i++){
                        if(Plato.lista_platos.get(i).getTitulo().equals(plato.getText().toString())){
                            for(int j=0; j<Integer.parseInt(cantidad.getText().toString()); j++) {
                                listaPlatosPedidos.add(Plato.lista_platos.get(i));
                            }
                        }
                    }
                }
            });

            aumentarCantidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Integer cant_int = Integer.parseInt(cantidad.getText().toString());
                   cant_int ++;
                   String cant = Integer.toString(cant_int);
                   cantidad.setText(cant);
                }
            });

            disminuirCantidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer cant_int = Integer.parseInt(cantidad.getText().toString());
                    if(cant_int>0) cant_int --;
                    String cant = Integer.toString(cant_int);
                    cantidad.setText(cant);
                }
            });

        }
    }
}
