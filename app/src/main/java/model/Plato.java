package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Plato implements Parcelable {
    private String titulo, descripcion;
    private Double precio;
    private Integer calorias;
    private String urlFoto="";
    public static ArrayList<Plato> lista_platos = new ArrayList<Plato>();

    public Plato(Parcel in) {
        titulo = in.readString();
        descripcion = in.readString();
        if (in.readByte() == 0) {
            precio = null;
        } else {
            precio = in.readDouble();
        }
        if (in.readByte() == 0) {
            calorias = null;
        } else {
            calorias = in.readInt();
        }
    }

    public static final Creator<Plato> CREATOR = new Creator<Plato>() {
        @Override
        public Plato createFromParcel(Parcel in) {
            return new Plato(in);
        }

        @Override
        public Plato[] newArray(int size) {
            return new Plato[size];
        }
    };

    public Plato() {

    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

    public String getUrlFoto(){return urlFoto; }

    public void setUrlFoto(String url) { this.urlFoto = url; }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(titulo);
        parcel.writeString(descripcion);
        if (precio == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(precio);
        }
        if (calorias == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(calorias);
        }
    }
}
