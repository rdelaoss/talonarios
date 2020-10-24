package model;

public class model_talonario {

    private String carnet;
    private String nombre;
    private String apellido;
    private String mes;
    private String grado;
    private String cuota;
    private String mora;
    private String fdesde;
    private String fhasta;
    private String npe;
    private String codebar;

    public model_talonario() {
    }

    public model_talonario(String carnet, String nombre, String apellido, String mes, String grado, String cuota, String mora, String fdesde, String fhasta, String npe, String codebar) {
        this.carnet = carnet;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mes = mes;
        this.grado = grado;
        this.cuota = cuota;
        this.mora = mora;
        this.fdesde = fdesde;
        this.fhasta = fhasta;
        this.npe = npe;
        this.codebar = codebar;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getMora() {
        return mora;
    }

    public void setMora(String mora) {
        this.mora = mora;
    }

    public String getFdesde() {
        return fdesde;
    }

    public void setFdesde(String fdesde) {
        this.fdesde = fdesde;
    }

    public String getFhasta() {
        return fhasta;
    }

    public void setFhasta(String fhasta) {
        this.fhasta = fhasta;
    }

    public String getNpe() {
        return npe;
    }

    public void setNpe(String npe) {
        this.npe = npe;
    }

    public String getCodebar() {
        return codebar;
    }

    public void setCodebar(String codebar) {
        this.codebar = codebar;
    }

}
