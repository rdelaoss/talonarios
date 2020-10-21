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
@Table(name = "EstadoControlPago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoControlPago.findAll", query = "SELECT e FROM EstadoControlPago e")
    , @NamedQuery(name = "EstadoControlPago.findByEstadoControlPagoId", query = "SELECT e FROM EstadoControlPago e WHERE e.estadoControlPagoId = :estadoControlPagoId")
    , @NamedQuery(name = "EstadoControlPago.findByEstadoControlPagoNombre", query = "SELECT e FROM EstadoControlPago e WHERE e.estadoControlPagoNombre = :estadoControlPagoNombre")
    , @NamedQuery(name = "EstadoControlPago.findByEstadoControlPagoAlias", query = "SELECT e FROM EstadoControlPago e WHERE e.estadoControlPagoAlias = :estadoControlPagoAlias")
    , @NamedQuery(name = "EstadoControlPago.findByEstadoControlPagoFechaModificacion", query = "SELECT e FROM EstadoControlPago e WHERE e.estadoControlPagoFechaModificacion = :estadoControlPagoFechaModificacion")
    , @NamedQuery(name = "EstadoControlPago.findByEstadoControlPagoActivo", query = "SELECT e FROM EstadoControlPago e WHERE e.estadoControlPagoActivo = :estadoControlPagoActivo")})
public class EstadoControlPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstadoControlPagoId")
    private Integer estadoControlPagoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EstadoControlPagoNombre")
    private String estadoControlPagoNombre;
    @Size(max = 50)
    @Column(name = "EstadoControlPagoAlias")
    private String estadoControlPagoAlias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstadoControlPagoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date estadoControlPagoFechaModificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstadoControlPagoActivo")
    private boolean estadoControlPagoActivo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoControlPagoId")
    private List<ControlPago> controlPagoList;

    public EstadoControlPago() {
    }

    public EstadoControlPago(Integer estadoControlPagoId) {
        this.estadoControlPagoId = estadoControlPagoId;
    }

    public EstadoControlPago(Integer estadoControlPagoId, String estadoControlPagoNombre, Date estadoControlPagoFechaModificacion, boolean estadoControlPagoActivo) {
        this.estadoControlPagoId = estadoControlPagoId;
        this.estadoControlPagoNombre = estadoControlPagoNombre;
        this.estadoControlPagoFechaModificacion = estadoControlPagoFechaModificacion;
        this.estadoControlPagoActivo = estadoControlPagoActivo;
    }

    public Integer getEstadoControlPagoId() {
        return estadoControlPagoId;
    }

    public void setEstadoControlPagoId(Integer estadoControlPagoId) {
        this.estadoControlPagoId = estadoControlPagoId;
    }

    public String getEstadoControlPagoNombre() {
        return estadoControlPagoNombre;
    }

    public void setEstadoControlPagoNombre(String estadoControlPagoNombre) {
        this.estadoControlPagoNombre = estadoControlPagoNombre;
    }

    public String getEstadoControlPagoAlias() {
        return estadoControlPagoAlias;
    }

    public void setEstadoControlPagoAlias(String estadoControlPagoAlias) {
        this.estadoControlPagoAlias = estadoControlPagoAlias;
    }

    public Date getEstadoControlPagoFechaModificacion() {
        return estadoControlPagoFechaModificacion;
    }

    public void setEstadoControlPagoFechaModificacion(Date estadoControlPagoFechaModificacion) {
        this.estadoControlPagoFechaModificacion = estadoControlPagoFechaModificacion;
    }

    public boolean getEstadoControlPagoActivo() {
        return estadoControlPagoActivo;
    }

    public void setEstadoControlPagoActivo(boolean estadoControlPagoActivo) {
        this.estadoControlPagoActivo = estadoControlPagoActivo;
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
        hash += (estadoControlPagoId != null ? estadoControlPagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoControlPago)) {
            return false;
        }
        EstadoControlPago other = (EstadoControlPago) object;
        if ((this.estadoControlPagoId == null && other.estadoControlPagoId != null) || (this.estadoControlPagoId != null && !this.estadoControlPagoId.equals(other.estadoControlPagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.EstadoControlPago[ estadoControlPagoId=" + estadoControlPagoId + " ]";
    }
    
}
