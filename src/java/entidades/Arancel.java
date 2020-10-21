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
@Table(name = "Arancel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arancel.findAll", query = "SELECT a FROM Arancel a")
    , @NamedQuery(name = "Arancel.findByArancelId", query = "SELECT a FROM Arancel a WHERE a.arancelId = :arancelId")
    , @NamedQuery(name = "Arancel.findByCod", query = "SELECT a FROM Arancel a WHERE a.cod = :cod")
    , @NamedQuery(name = "Arancel.findByArancelNombre", query = "SELECT a FROM Arancel a WHERE a.arancelNombre = :arancelNombre")
    , @NamedQuery(name = "Arancel.findByArancelAlias", query = "SELECT a FROM Arancel a WHERE a.arancelAlias = :arancelAlias")
    , @NamedQuery(name = "Arancel.findByArancelMostrarInforme", query = "SELECT a FROM Arancel a WHERE a.arancelMostrarInforme = :arancelMostrarInforme")
    , @NamedQuery(name = "Arancel.findByArancelActivo", query = "SELECT a FROM Arancel a WHERE a.arancelActivo = :arancelActivo")
    , @NamedQuery(name = "Arancel.findByArancelFechaModificacion", query = "SELECT a FROM Arancel a WHERE a.arancelFechaModificacion = :arancelFechaModificacion")})
public class Arancel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ArancelId")
    private Integer arancelId;
    @Size(max = 50)
    @Column(name = "Cod")
    private String cod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ArancelNombre")
    private String arancelNombre;
    @Size(max = 50)
    @Column(name = "ArancelAlias")
    private String arancelAlias;
    @Column(name = "ArancelMostrarInforme")
    private Boolean arancelMostrarInforme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ArancelActivo")
    private boolean arancelActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ArancelFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arancelFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arancelId")
    private List<ControlPago> controlPagoList;

    public Arancel() {
    }

    public Arancel(Integer arancelId) {
        this.arancelId = arancelId;
    }

    public Arancel(Integer arancelId, String arancelNombre, boolean arancelActivo, Date arancelFechaModificacion) {
        this.arancelId = arancelId;
        this.arancelNombre = arancelNombre;
        this.arancelActivo = arancelActivo;
        this.arancelFechaModificacion = arancelFechaModificacion;
    }

    public Integer getArancelId() {
        return arancelId;
    }

    public void setArancelId(Integer arancelId) {
        this.arancelId = arancelId;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getArancelNombre() {
        return arancelNombre;
    }

    public void setArancelNombre(String arancelNombre) {
        this.arancelNombre = arancelNombre;
    }

    public String getArancelAlias() {
        return arancelAlias;
    }

    public void setArancelAlias(String arancelAlias) {
        this.arancelAlias = arancelAlias;
    }

    public Boolean getArancelMostrarInforme() {
        return arancelMostrarInforme;
    }

    public void setArancelMostrarInforme(Boolean arancelMostrarInforme) {
        this.arancelMostrarInforme = arancelMostrarInforme;
    }

    public boolean getArancelActivo() {
        return arancelActivo;
    }

    public void setArancelActivo(boolean arancelActivo) {
        this.arancelActivo = arancelActivo;
    }

    public Date getArancelFechaModificacion() {
        return arancelFechaModificacion;
    }

    public void setArancelFechaModificacion(Date arancelFechaModificacion) {
        this.arancelFechaModificacion = arancelFechaModificacion;
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
        hash += (arancelId != null ? arancelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arancel)) {
            return false;
        }
        Arancel other = (Arancel) object;
        if ((this.arancelId == null && other.arancelId != null) || (this.arancelId != null && !this.arancelId.equals(other.arancelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Arancel[ arancelId=" + arancelId + " ]";
    }
    
}
