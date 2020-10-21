/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "Matricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m")
    , @NamedQuery(name = "Matricula.findByMatriculaId", query = "SELECT m FROM Matricula m WHERE m.matriculaId = :matriculaId")
    , @NamedQuery(name = "Matricula.findByMatriculaTalonario", query = "SELECT m FROM Matricula m WHERE m.matriculaTalonario = :matriculaTalonario")
    , @NamedQuery(name = "Matricula.findByMatriculaActivo", query = "SELECT m FROM Matricula m WHERE m.matriculaActivo = :matriculaActivo")
    , @NamedQuery(name = "Matricula.findByMatriculaFechaModificacion", query = "SELECT m FROM Matricula m WHERE m.matriculaFechaModificacion = :matriculaFechaModificacion")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "MatriculaId")
    private Integer matriculaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MatriculaTalonario")
    private int matriculaTalonario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MatriculaActivo")
    private boolean matriculaActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MatriculaFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date matriculaFechaModificacion;
    @JoinColumn(name = "ConfiguracionGradoId", referencedColumnName = "ConfiguracionGradoId")
    @ManyToOne(optional = false)
    private ConfiguracionGrado configuracionGradoId;
    @JoinColumn(name = "EstudianteId", referencedColumnName = "EstudianteId")
    @ManyToOne(optional = false)
    private Estudiante estudianteId;
    @JoinColumn(name = "TipoBeca", referencedColumnName = "TipoBecaId")
    @ManyToOne(optional = false)
    private TipoBeca tipoBeca;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matriculaId")
    private List<ControlPago> controlPagoList;

    public Matricula() {
    }

    public Matricula(Integer matriculaId) {
        this.matriculaId = matriculaId;
    }

    public Matricula(Integer matriculaId, int matriculaTalonario, boolean matriculaActivo, Date matriculaFechaModificacion) {
        this.matriculaId = matriculaId;
        this.matriculaTalonario = matriculaTalonario;
        this.matriculaActivo = matriculaActivo;
        this.matriculaFechaModificacion = matriculaFechaModificacion;
    }

    public Integer getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Integer matriculaId) {
        this.matriculaId = matriculaId;
    }

    public int getMatriculaTalonario() {
        return matriculaTalonario;
    }

    public void setMatriculaTalonario(int matriculaTalonario) {
        this.matriculaTalonario = matriculaTalonario;
    }

    public boolean getMatriculaActivo() {
        return matriculaActivo;
    }

    public void setMatriculaActivo(boolean matriculaActivo) {
        this.matriculaActivo = matriculaActivo;
    }

    public Date getMatriculaFechaModificacion() {
        return matriculaFechaModificacion;
    }

    public void setMatriculaFechaModificacion(Date matriculaFechaModificacion) {
        this.matriculaFechaModificacion = matriculaFechaModificacion;
    }

    public ConfiguracionGrado getConfiguracionGradoId() {
        return configuracionGradoId;
    }

    public void setConfiguracionGradoId(ConfiguracionGrado configuracionGradoId) {
        this.configuracionGradoId = configuracionGradoId;
    }

    public Estudiante getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Estudiante estudianteId) {
        this.estudianteId = estudianteId;
    }

    public TipoBeca getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TipoBeca tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    @XmlTransient
    public List<ControlPago> getControlPagoList() {
        return controlPagoList;
    }

    public void setControlPagoList(List<ControlPago> controlPagoList) {
        this.controlPagoList = controlPagoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaId != null ? matriculaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.matriculaId == null && other.matriculaId != null) || (this.matriculaId != null && !this.matriculaId.equals(other.matriculaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Matricula[ matriculaId=" + matriculaId + " ]";
    }
    
}
