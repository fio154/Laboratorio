package isi.dam.sendmeal;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    ArrayList<String> listaNombres, listaPrecios, listaDescripciones;
    ArrayList<TextView> listaCantidades;
    public static ArrayList<Plato> listaPlatosPedidos = new ArrayList<Plato>();
    String pantallaAnterior;
    Dialog myDialog;

    public AdapterDatosRecycler(ArrayList<String> listaNombres, ArrayList<String> listaPrecios, ArrayList<String> listaDescripciones, String pantallaAnterior, Dialog myDialog) {
        this.listaNombres = listaNombres;
        this.listaPrecios = listaPrecios;
        this.listaDescripciones = listaDescripciones;
        this.listaCantidades = new ArrayList<TextView>();
        this.pantallaAnterior = pantallaAnterior;
        this.myDialog = myDialog;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_plato, null, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        holder.asignarDatos(listaNombres.get(position), listaPrecios.get(position), listaDescripciones.get(position), pantallaAnterior);
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public class PlatoViewHolder extends RecyclerView.ViewHolder {

        TextView plato, precio, cantidad;
        Button aumentarCantidad, disminuirCantidad, verDescripcion;


        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);

            plato = (TextView) itemView.findViewById(R.id.plato);
            precio = (TextView) itemView.findViewById(R.id.precio_pedido);
            cantidad = (TextView) itemView.findViewById(R.id.cantidadPedido);
            listaCantidades.add(cantidad);
            aumentarCantidad = (Button) itemView.findViewById(R.id.buttonAumentar);
            disminuirCantidad = (Button) itemView.findViewById(R.id.buttonDisminuir);
            verDescripcion = (Button) itemView.findViewById(R.id.ver);


        }

        public void asignarDatos(String platos, String precios, final String descripciones, String pantallaAnterior) {
            plato.setText(platos);
            precio.setText(precios);

            if(pantallaAnterior.equals("home")){
                cantidad.setVisibility(View.GONE);
                aumentarCantidad.setVisibility(View.GONE);
                disminuirCantidad.setVisibility(View.GONE);
            }
            else if(pantallaAnterior.equals("pedido")){
                cantidad.setVisibility(View.VISIBLE);
                aumentarCantidad.setVisibility(View.VISIBLE);
                disminuirCantidad.setVisibility(View.VISIBLE);
            }

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


            verDescripcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowPopup(view, descripciones);
                }
            });

        }
    }

    public void ShowPopup(View v, String descripciones) {
        TextView txt_cerrar, descripcion;
        Button btnFollow;
        myDialog.setContentView(R.layout.pop_up_descripcion);
        txt_cerrar =(TextView) myDialog.findViewById(R.id.txt_cerrar);
        descripcion = (TextView) myDialog.findViewById(R.id.descripcion_pop_up);
        descripcion.setText(descripciones);
        txt_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
