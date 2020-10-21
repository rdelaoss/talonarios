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
@Table(name = "AnioLectivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AnioLectivo.findAll", query = "SELECT a FROM AnioLectivo a")
    , @NamedQuery(name = "AnioLectivo.findByAnioLectivoId", query = "SELECT a FROM AnioLectivo a WHERE a.anioLectivoId = :anioLectivoId")
    , @NamedQuery(name = "AnioLectivo.findByAnioLectivoNombre", query = "SELECT a FROM AnioLectivo a WHERE a.anioLectivoNombre = :anioLectivoNombre")
    , @NamedQuery(name = "AnioLectivo.findByAnioLectivoActivo", query = "SELECT a FROM AnioLectivo a WHERE a.anioLectivoActivo = :anioLectivoActivo")
    , @NamedQuery(name = "AnioLectivo.findByAnioLectivoFechaModificacion", query = "SELECT a FROM AnioLectivo a WHERE a.anioLectivoFechaModificacion = :anioLectivoFechaModificacion")})
public class AnioLectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "AnioLectivoId")
    private Integer anioLectivoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "AnioLectivoNombre")
    private String anioLectivoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AnioLectivoActivo")
    private boolean anioLectivoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AnioLectivoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date anioLectivoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "anioLectivoId")
    private List<ConfiguracionGrado> configuracionGradoList;

    public AnioLectivo() {
    }

    public AnioLectivo(Integer anioLectivoId) {
        this.anioLectivoId = anioLectivoId;
    }

    public AnioLectivo(Integer anioLectivoId, String anioLectivoNombre, boolean anioLectivoActivo, Date anioLectivoFechaModificacion) {
        this.anioLectivoId = anioLectivoId;
        this.anioLectivoNombre = anioLectivoNombre;
        this.anioLectivoActivo = anioLectivoActivo;
        this.anioLectivoFechaModificacion = anioLectivoFechaModificacion;
    }

    public Integer getAnioLectivoId() {
        return anioLectivoId;
    }

    public void setAnioLectivoId(Integer anioLectivoId) {
        this.anioLectivoId = anioLectivoId;
    }

    public String getAnioLectivoNombre() {
        return anioLectivoNombre;
    }

    public void setAnioLectivoNombre(String anioLectivoNombre) {
        this.anioLectivoNombre = anioLectivoNombre;
    }

    public boolean getAnioLectivoActivo() {
        return anioLectivoActivo;
    }

    public void setAnioLectivoActivo(boolean anioLectivoActivo) {
        this.anioLectivoActivo = anioLectivoActivo;
    }

    public Date getAnioLectivoFechaModificacion() {
        return anioLectivoFechaModificacion;
    }

    public void setAnioLectivoFechaModificacion(Date anioLectivoFechaModificacion) {
        this.anioLectivoFechaModificacion = anioLectivoFechaModificacion;
    }

    @XmlTransient
    public List<ConfiguracionGrado> getConfiguracionGradoList() {
        return configuracionGradoList;
    }

    public void setConfiguracionGradoList(List<ConfiguracionGrado> configuracionGradoList) {
        this.configuracionGradoList = configuracionGradoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anioLectivoId != null ? anioLectivoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnioLectivo)) {
            return false;
        }
        AnioLectivo other = (AnioLectivo) object;
        if ((this.anioLectivoId == null && other.anioLectivoId != null) || (this.anioLectivoId != null && !this.anioLectivoId.equals(other.anioLectivoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.AnioLectivo[ anioLectivoId=" + anioLectivoId + " ]";
    }
    
}
