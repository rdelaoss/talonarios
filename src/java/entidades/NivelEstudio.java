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
@Table(name = "NivelEstudio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelEstudio.findAll", query = "SELECT n FROM NivelEstudio n")
    , @NamedQuery(name = "NivelEstudio.findByNivelEstudioId", query = "SELECT n FROM NivelEstudio n WHERE n.nivelEstudioId = :nivelEstudioId")
    , @NamedQuery(name = "NivelEstudio.findByNivelEstudioNombre", query = "SELECT n FROM NivelEstudio n WHERE n.nivelEstudioNombre = :nivelEstudioNombre")
    , @NamedQuery(name = "NivelEstudio.findByNivelEstudioActivo", query = "SELECT n FROM NivelEstudio n WHERE n.nivelEstudioActivo = :nivelEstudioActivo")
    , @NamedQuery(name = "NivelEstudio.findByNivelEstudioFechaModificacion", query = "SELECT n FROM NivelEstudio n WHERE n.nivelEstudioFechaModificacion = :nivelEstudioFechaModificacion")})
public class NivelEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NivelEstudioId")
    private Integer nivelEstudioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NivelEstudioNombre")
    private String nivelEstudioNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NivelEstudioActivo")
    private boolean nivelEstudioActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NivelEstudioFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date nivelEstudioFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivelEstudioId")
    private List<Grado> gradoList;

    public NivelEstudio() {
    }

    public NivelEstudio(Integer nivelEstudioId) {
        this.nivelEstudioId = nivelEstudioId;
    }

    public NivelEstudio(Integer nivelEstudioId, String nivelEstudioNombre, boolean nivelEstudioActivo, Date nivelEstudioFechaModificacion) {
        this.nivelEstudioId = nivelEstudioId;
        this.nivelEstudioNombre = nivelEstudioNombre;
        this.nivelEstudioActivo = nivelEstudioActivo;
        this.nivelEstudioFechaModificacion = nivelEstudioFechaModificacion;
    }

    public Integer getNivelEstudioId() {
        return nivelEstudioId;
    }

    public void setNivelEstudioId(Integer nivelEstudioId) {
        this.nivelEstudioId = nivelEstudioId;
    }

    public String getNivelEstudioNombre() {
        return nivelEstudioNombre;
    }

    public void setNivelEstudioNombre(String nivelEstudioNombre) {
        this.nivelEstudioNombre = nivelEstudioNombre;
    }

    public boolean getNivelEstudioActivo() {
        return nivelEstudioActivo;
    }

    public void setNivelEstudioActivo(boolean nivelEstudioActivo) {
        this.nivelEstudioActivo = nivelEstudioActivo;
    }

    public Date getNivelEstudioFechaModificacion() {
        return nivelEstudioFechaModificacion;
    }

    public void setNivelEstudioFechaModificacion(Date nivelEstudioFechaModificacion) {
        this.nivelEstudioFechaModificacion = nivelEstudioFechaModificacion;
    }

    @XmlTransient
    public List<Grado> getGradoList() {
        return gradoList;
    }

    public void setGradoList(List<Grado> gradoList) {
        this.gradoList = gradoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivelEstudioId != null ? nivelEstudioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelEstudio)) {
            return false;
        }
        NivelEstudio other = (NivelEstudio) object;
        if ((this.nivelEstudioId == null && other.nivelEstudioId != null) || (this.nivelEstudioId != null && !this.nivelEstudioId.equals(other.nivelEstudioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.NivelEstudio[ nivelEstudioId=" + nivelEstudioId + " ]";
    }
    
}
