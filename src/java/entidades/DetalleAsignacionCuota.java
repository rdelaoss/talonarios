/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "DetalleAsignacionCuota")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleAsignacionCuota.findAll", query = "SELECT d FROM DetalleAsignacionCuota d")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsginacionCuotaId", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsginacionCuotaId = :detalleAsginacionCuotaId")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsignacionCuotaAnterior", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsignacionCuotaAnterior = :detalleAsignacionCuotaAnterior")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsignacionCuotaNueva", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsignacionCuotaNueva = :detalleAsignacionCuotaNueva")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsignacionCuotaObservacion", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsignacionCuotaObservacion = :detalleAsignacionCuotaObservacion")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsignacionCuotaActivo", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsignacionCuotaActivo = :detalleAsignacionCuotaActivo")
    , @NamedQuery(name = "DetalleAsignacionCuota.findByDetalleAsignacionCuotaFecha", query = "SELECT d FROM DetalleAsignacionCuota d WHERE d.detalleAsignacionCuotaFecha = :detalleAsignacionCuotaFecha")})
public class DetalleAsignacionCuota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleAsginacionCuotaId")
    private Integer detalleAsginacionCuotaId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleAsignacionCuotaAnterior")
    private BigDecimal detalleAsignacionCuotaAnterior;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleAsignacionCuotaNueva")
    private BigDecimal detalleAsignacionCuotaNueva;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "DetalleAsignacionCuotaObservacion")
    private String detalleAsignacionCuotaObservacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleAsignacionCuotaActivo")
    private boolean detalleAsignacionCuotaActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DetalleAsignacionCuotaFecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date detalleAsignacionCuotaFecha;
    @JoinColumn(name = "ControlPagoId", referencedColumnName = "ControlPagoId")
    @ManyToOne(optional = false)
    private ControlPago controlPagoId;
    @JoinColumn(name = "UsuarioId", referencedColumnName = "UsuarioId")
    @ManyToOne(optional = false)
    private Usuario usuarioId;

    public DetalleAsignacionCuota() {
    }

    public DetalleAsignacionCuota(Integer detalleAsginacionCuotaId) {
        this.detalleAsginacionCuotaId = detalleAsginacionCuotaId;
    }

    public DetalleAsignacionCuota(Integer detalleAsginacionCuotaId, BigDecimal detalleAsignacionCuotaAnterior, BigDecimal detalleAsignacionCuotaNueva, String detalleAsignacionCuotaObservacion, boolean detalleAsignacionCuotaActivo, Date detalleAsignacionCuotaFecha) {
        this.detalleAsginacionCuotaId = detalleAsginacionCuotaId;
        this.detalleAsignacionCuotaAnterior = detalleAsignacionCuotaAnterior;
        this.detalleAsignacionCuotaNueva = detalleAsignacionCuotaNueva;
        this.detalleAsignacionCuotaObservacion = detalleAsignacionCuotaObservacion;
        this.detalleAsignacionCuotaActivo = detalleAsignacionCuotaActivo;
        this.detalleAsignacionCuotaFecha = detalleAsignacionCuotaFecha;
    }

    public Integer getDetalleAsginacionCuotaId() {
        return detalleAsginacionCuotaId;
    }

    public void setDetalleAsginacionCuotaId(Integer detalleAsginacionCuotaId) {
        this.detalleAsginacionCuotaId = detalleAsginacionCuotaId;
    }

    public BigDecimal getDetalleAsignacionCuotaAnterior() {
        return detalleAsignacionCuotaAnterior;
    }

    public void setDetalleAsignacionCuotaAnterior(BigDecimal detalleAsignacionCuotaAnterior) {
        this.detalleAsignacionCuotaAnterior = detalleAsignacionCuotaAnterior;
    }

    public BigDecimal getDetalleAsignacionCuotaNueva() {
        return detalleAsignacionCuotaNueva;
    }

    public void setDetalleAsignacionCuotaNueva(BigDecimal detalleAsignacionCuotaNueva) {
        this.detalleAsignacionCuotaNueva = detalleAsignacionCuotaNueva;
    }

    public String getDetalleAsignacionCuotaObservacion() {
        return detalleAsignacionCuotaObservacion;
    }

    public void setDetalleAsignacionCuotaObservacion(String detalleAsignacionCuotaObservacion) {
        this.detalleAsignacionCuotaObservacion = detalleAsignacionCuotaObservacion;
    }

    public boolean getDetalleAsignacionCuotaActivo() {
        return detalleAsignacionCuotaActivo;
    }

    public void setDetalleAsignacionCuotaActivo(boolean detalleAsignacionCuotaActivo) {
        this.detalleAsignacionCuotaActivo = detalleAsignacionCuotaActivo;
    }

    public Date getDetalleAsignacionCuotaFecha() {
        return detalleAsignacionCuotaFecha;
    }

    public void setDetalleAsignacionCuotaFecha(Date detalleAsignacionCuotaFecha) {
        this.detalleAsignacionCuotaFecha = detalleAsignacionCuotaFecha;
    }

    public ControlPago getControlPagoId() {
        return controlPagoId;
    }

    public void setControlPagoId(ControlPago controlPagoId) {
        this.controlPagoId = controlPagoId;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detalleAsginacionCuotaId != null ? detalleAsginacionCuotaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleAsignacionCuota)) {
            return false;
        }
        DetalleAsignacionCuota other = (DetalleAsignacionCuota) object;
        if ((this.detalleAsginacionCuotaId == null && other.detalleAsginacionCuotaId != null) || (this.detalleAsginacionCuotaId != null && !this.detalleAsginacionCuotaId.equals(other.detalleAsginacionCuotaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.DetalleAsignacionCuota[ detalleAsginacionCuotaId=" + detalleAsginacionCuotaId + " ]";
    }
    
}
