package modelo;

public class Cuenta {
    private String formNo;
    private String tipoCuenta;
    private String numeroTarjeta;
    private String pin;
    private String servicios;
    private double saldo;
    
    public Cuenta() {
    }
    
    public Cuenta(String numeroTarjeta, String tipoCuenta) {
        this.numeroTarjeta = numeroTarjeta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = 0.0;
    }

    // Getters y Setters
    public String getFormNo() {
        return formNo;
    }

    public void setFormNo(String formNo) {
        this.formNo = formNo;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getServicios() {
        return servicios;
    }

    public void setServicios(String servicios) {
        this.servicios = servicios;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroTarjeta='" + numeroTarjeta + '\'' +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldo=" + saldo +
                '}';
    }
}