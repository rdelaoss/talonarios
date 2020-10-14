package model;
// Generated 10-10-2020 10:11:52 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Matricula generated by hbm2java
 */
public class Matricula  implements java.io.Serializable {


     private int matriculaId;
     private ConfiguracionGrado configuracionGrado;
     private Estudiante estudiante;
     private TipoBeca tipoBeca;
     private int matriculaTalonario;
     private boolean matriculaActivo;
     private Date matriculaFechaModificacion;
     private Set<ControlPago> controlPagos = new HashSet<ControlPago>(0);
     private Set<ArancelMatricula> arancelMatriculas = new HashSet<ArancelMatricula>(0);

    public Matricula() {
    }

	
    public Matricula(int matriculaId, ConfiguracionGrado configuracionGrado, Estudiante estudiante, TipoBeca tipoBeca, int matriculaTalonario, boolean matriculaActivo, Date matriculaFechaModificacion) {
        this.matriculaId = matriculaId;
        this.configuracionGrado = configuracionGrado;
        this.estudiante = estudiante;
        this.tipoBeca = tipoBeca;
        this.matriculaTalonario = matriculaTalonario;
        this.matriculaActivo = matriculaActivo;
        this.matriculaFechaModificacion = matriculaFechaModificacion;
    }
    public Matricula(int matriculaId, ConfiguracionGrado configuracionGrado, Estudiante estudiante, TipoBeca tipoBeca, int matriculaTalonario, boolean matriculaActivo, Date matriculaFechaModificacion, Set<ControlPago> controlPagos, Set<ArancelMatricula> arancelMatriculas) {
       this.matriculaId = matriculaId;
       this.configuracionGrado = configuracionGrado;
       this.estudiante = estudiante;
       this.tipoBeca = tipoBeca;
       this.matriculaTalonario = matriculaTalonario;
       this.matriculaActivo = matriculaActivo;
       this.matriculaFechaModificacion = matriculaFechaModificacion;
       this.controlPagos = controlPagos;
       this.arancelMatriculas = arancelMatriculas;
    }
   
    public int getMatriculaId() {
        return this.matriculaId;
    }
    
    public void setMatriculaId(int matriculaId) {
        this.matriculaId = matriculaId;
    }
    public ConfiguracionGrado getConfiguracionGrado() {
        return this.configuracionGrado;
    }
    
    public void setConfiguracionGrado(ConfiguracionGrado configuracionGrado) {
        this.configuracionGrado = configuracionGrado;
    }
    public Estudiante getEstudiante() {
        return this.estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    public TipoBeca getTipoBeca() {
        return this.tipoBeca;
    }
    
    public void setTipoBeca(TipoBeca tipoBeca) {
        this.tipoBeca = tipoBeca;
    }
    public int getMatriculaTalonario() {
        return this.matriculaTalonario;
    }
    
    public void setMatriculaTalonario(int matriculaTalonario) {
        this.matriculaTalonario = matriculaTalonario;
    }
    public boolean isMatriculaActivo() {
        return this.matriculaActivo;
    }
    
    public void setMatriculaActivo(boolean matriculaActivo) {
        this.matriculaActivo = matriculaActivo;
    }
    public Date getMatriculaFechaModificacion() {
        return this.matriculaFechaModificacion;
    }
    
    public void setMatriculaFechaModificacion(Date matriculaFechaModificacion) {
        this.matriculaFechaModificacion = matriculaFechaModificacion;
    }
    public Set<ControlPago> getControlPagos() {
        return this.controlPagos;
    }
    
    public void setControlPagos(Set<ControlPago> controlPagos) {
        this.controlPagos = controlPagos;
    }
    public Set<ArancelMatricula> getArancelMatriculas() {
        return this.arancelMatriculas;
    }
    
    public void setArancelMatriculas(Set<ArancelMatricula> arancelMatriculas) {
        this.arancelMatriculas = arancelMatriculas;
    }




}

