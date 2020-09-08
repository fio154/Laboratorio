package model;

public class CuentaBancaria {
    private String cbu;
    private String alias;

    public CuentaBancaria(String cbu, String alias) {
        this.cbu = cbu;
        this.alias = alias;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
