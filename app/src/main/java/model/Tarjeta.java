package model;

import java.util.Calendar;
import java.util.Date;

public class Tarjeta {
    private String numero;
    private String ccv;
    private Calendar vencimiento;
    private boolean esCredito;

    public Tarjeta(String numero, String ccv, Calendar vencimiento, boolean esCredito) {
        this.numero = numero;
        this.ccv = ccv;
        this.vencimiento = vencimiento;
        this.esCredito = esCredito;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public Calendar getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Calendar vencimiento) {
        this.vencimiento = vencimiento;
    }

    public boolean isEsCredito() {
        return esCredito;
    }

    public void setEsCredito(boolean esCredito) {
        this.esCredito = esCredito;
    }
}
