package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Pedido implements Parcelable {

    private ArrayList<Plato> platos;
    private String email;
    private String direccion;
    private Boolean paraEnviar;

    public Pedido() {

    }

    public ArrayList<Plato> getPlatos() {
        return platos;
    }

    public void setPlatos(ArrayList<Plato> platos) {
        this.platos = platos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getParaEnviar() {
        return paraEnviar;
    }

    public void setParaEnviar(Boolean paraEnviar) {
        this.paraEnviar = paraEnviar;
    }

    protected Pedido(Parcel in) {
        if (in.readByte() == 0x01) {
            platos = new ArrayList<Plato>();
            in.readList(platos, Plato.class.getClassLoader());
        } else {
            platos = null;
        }
        email = in.readString();
        direccion = in.readString();
        byte paraEnviarVal = in.readByte();
        paraEnviar = paraEnviarVal == 0x02 ? null : paraEnviarVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (platos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(platos);
        }
        dest.writeString(email);
        dest.writeString(direccion);
        if (paraEnviar == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (paraEnviar ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Pedido> CREATOR = new Parcelable.Creator<Pedido>() {
        @Override
        public Pedido createFromParcel(Parcel in) {
            return new Pedido(in);
        }

        @Override
        public Pedido[] newArray(int size) {
            return new Pedido[size];
        }
    };
}
