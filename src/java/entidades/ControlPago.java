/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "ControlPago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControlPago.findAll", query = "SELECT c FROM ControlPago c")
    , @NamedQuery(name = "ControlPago.findByControlPagoId", query = "SELECT c FROM ControlPago c WHERE c.controlPagoId = :controlPagoId")
    , @NamedQuery(name = "ControlPago.findByControlPagoFechaIngresoPago", query = "SELECT c FROM ControlPago c WHERE c.controlPagoFechaIngresoPago = :controlPagoFechaIngresoPago")
    , @NamedQuery(name = "ControlPago.findByControlPagoArancelAsignado", query = "SELECT c FROM ControlPago c WHERE c.controlPagoArancelAsignado = :controlPagoArancelAsignado")
    , @NamedQuery(name = "ControlPago.findByControlPagoArancelPagado", query = "SELECT c FROM ControlPago c WHERE c.controlPagoArancelPagado = :controlPagoArancelPagado")
    , @NamedQuery(name = "ControlPago.findByControlPagoPagadoEnColegio", query = "SELECT c FROM ControlPago c WHERE c.controlPagoPagadoEnColegio = :controlPagoPagadoEnColegio")
    , @NamedQuery(name = "ControlPago.findByControlPagoMora", query = "SELECT c FROM ControlPago c WHERE c.controlPagoMora = :controlPagoMora")
    , @NamedQuery(name = "ControlPago.findByControlPagoObservacion", query = "SELECT c FROM ControlPago c WHERE c.controlPagoObservacion = :controlPagoObservacion")
    , @NamedQuery(name = "ControlPago.findByControlPagoActivo", query = "SELECT c FROM ControlPago c WHERE c.controlPagoActivo = :controlPagoActivo")
    , @NamedQuery(name = "ControlPago.findByControlPagoFechaModificacion", query = "SELECT c FROM ControlPago c WHERE c.controlPagoFechaModificacion = :controlPagoFechaModificacion")})
public class ControlPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoId")
    private Integer controlPagoId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoFechaIngresoPago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date controlPagoFechaIngresoPago;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ControlPagoArancelAsignado")
    private BigDecimal controlPagoArancelAsignado;
    @Column(name = "ControlPagoArancelPagado")
    private BigDecimal controlPagoArancelPagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoPagadoEnColegio")
    private boolean controlPagoPagadoEnColegio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoMora")
    private boolean controlPagoMora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 75)
    @Column(name = "ControlPagoObservacion")
    private String controlPagoObservacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoActivo")
    private boolean controlPagoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ControlPagoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date controlPagoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "controlPagoId")
    private List<DetalleAsignacionCuota> detalleAsignacionCuotaList;
    @JoinColumn(name = "ArancelId", referencedColumnName = "ArancelId")
    @ManyToOne(optional = false)
    private Arancel arancelId;
    @JoinColumn(name = "EstadoControlPagoId", referencedColumnName = "EstadoControlPagoId")
    @ManyToOne(optional = false)
    private EstadoControlPago estadoControlPagoId;
    @JoinColumn(name = "MatriculaId", referencedColumnName = "MatriculaId")
    @ManyToOne(optional = false)
    private Matricula matriculaId;
    @JoinColumn(name = "ControlPagoUsuarioId", referencedColumnName = "UsuarioId")
    @ManyToOne(optional = false)
    private Usuario controlPagoUsuarioId;

    public ControlPago() {
    }

    public ControlPago(Integer controlPagoId) {
        this.controlPagoId = controlPagoId;
    }

    public ControlPago(Integer controlPagoId, Date controlPagoFechaIngresoPago, boolean controlPagoPagadoEnColegio, boolean controlPagoMora, String controlPagoObservacion, boolean controlPagoActivo, Date controlPagoFechaModificacion) {
        this.controlPagoId = controlPagoId;
        this.controlPagoFechaIngresoPago = controlPagoFechaIngresoPago;
        this.controlPagoPagadoEnColegio = controlPagoPagadoEnColegio;
        this.controlPagoMora = controlPagoMora;
        this.controlPagoObservacion = controlPagoObservacion;
        this.controlPagoActivo = controlPagoActivo;
        this.controlPagoFechaModificacion = controlPagoFechaModificacion;
    }

    public Integer getControlPagoId() {
        return controlPagoId;
    }

    public void setControlPagoId(Integer controlPagoId) {
        this.controlPagoId = controlPagoId;
    }

    public Date getControlPagoFechaIngresoPago() {
        return controlPagoFechaIngresoPago;
    }

    public void setControlPagoFechaIngresoPago(Date controlPagoFechaIngresoPago) {
        this.controlPagoFechaIngresoPago = controlPagoFechaIngresoPago;
    }

    public BigDecimal getControlPagoArancelAsignado() {
        return controlPagoArancelAsignado;
    }

    public void setControlPagoArancelAsignado(BigDecimal controlPagoArancelAsignado) {
        this.controlPagoArancelAsignado = controlPagoArancelAsignado;
    }

    public BigDecimal getControlPagoArancelPagado() {
        return controlPagoArancelPagado;
    }

    public void setControlPagoArancelPagado(BigDecimal controlPagoArancelPagado) {
        this.controlPagoArancelPagado = controlPagoArancelPagado;
    }

    public boolean getControlPagoPagadoEnColegio() {
        return controlPagoPagadoEnColegio;
    }

    public void setControlPagoPagadoEnColegio(boolean controlPagoPagadoEnColegio) {
        this.controlPagoPagadoEnColegio = controlPagoPagadoEnColegio;
    }

    public boolean getControlPagoMora() {
        return controlPagoMora;
    }

    public void setControlPagoMora(boolean controlPagoMora) {
        this.controlPagoMora = controlPagoMora;
    }

    public String getControlPagoObservacion() {
        return controlPagoObservacion;
    }

    public void setControlPagoObservacion(String controlPagoObservacion) {
        this.controlPagoObservacion = controlPagoObservacion;
    }

    public boolean getControlPagoActivo() {
        return controlPagoActivo;
    }

    public void setControlPagoActivo(boolean controlPagoActivo) {
        this.controlPagoActivo = controlPagoActivo;
    }

    public Date getControlPagoFechaModificacion() {
        return controlPagoFechaModificacion;
    }

    public void setControlPagoFechaModificacion(Date controlPagoFechaModificacion) {
        this.controlPagoFechaModificacion = controlPagoFechaModificacion;
    }

    @XmlTransient
    public List<DetalleAsignacionCuota> getDetalleAsignacionCuotaList() {
        return detalleAsignacionCuotaList;
    }

    public void setDetalleAsignacionCuotaList(List<DetalleAsignacionCuota> detalleAsignacionCuotaList) {
        this.detalleAsignacionCuotaList = detalleAsignacionCuotaList;
    }

    public Arancel getArancelId() {
        return arancelId;
    }

    public void setArancelId(Arancel arancelId) {
        this.arancelId = arancelId;
    }

    public EstadoControlPago getEstadoControlPagoId() {
        return estadoControlPagoId;
    }

    public void setEstadoControlPagoId(EstadoControlPago estadoControlPagoId) {
        this.estadoControlPagoId = estadoControlPagoId;
    }

    public Matricula getMatriculaId() {
        return matriculaId;
    }

    public void setMatriculaId(Matricula matriculaId) {
        this.matriculaId = matriculaId;
    }

    public Usuario getControlPagoUsuarioId() {
        return controlPagoUsuarioId;
    }

    public void setControlPagoUsuarioId(Usuario controlPagoUsuarioId) {
        this.controlPagoUsuarioId = controlPagoUsuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (controlPagoId != null ? controlPagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ControlPago)) {
            return false;
        }
        ControlPago other = (ControlPago) object;
        if ((this.controlPagoId == null && other.controlPagoId != null) || (this.controlPagoId != null && !this.controlPagoId.equals(other.controlPagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ControlPago[ controlPagoId=" + controlPagoId + " ]";
    }
    
}
