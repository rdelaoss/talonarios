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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "Grado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grado.findAll", query = "SELECT g FROM Grado g")
    , @NamedQuery(name = "Grado.findByGradoId", query = "SELECT g FROM Grado g WHERE g.gradoId = :gradoId")
    , @NamedQuery(name = "Grado.findByGradoNombre", query = "SELECT g FROM Grado g WHERE g.gradoNombre = :gradoNombre")
    , @NamedQuery(name = "Grado.findByGradoActivo", query = "SELECT g FROM Grado g WHERE g.gradoActivo = :gradoActivo")
    , @NamedQuery(name = "Grado.findByGradoFechaModificacion", query = "SELECT g FROM Grado g WHERE g.gradoFechaModificacion = :gradoFechaModificacion")})
public class Grado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GradoId")
    private Integer gradoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "GradoNombre")
    private String gradoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GradoActivo")
    private boolean gradoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GradoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gradoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gradoId")
    private List<ConfiguracionGrado> configuracionGradoList;
    @JoinColumn(name = "NivelEstudioId", referencedColumnName = "NivelEstudioId")
    @ManyToOne(optional = false)
    private NivelEstudio nivelEstudioId;

    public Grado() {
    }

    public Grado(Integer gradoId) {
        this.gradoId = gradoId;
    }

    public Grado(Integer gradoId, String gradoNombre, boolean gradoActivo, Date gradoFechaModificacion) {
        this.gradoId = gradoId;
        this.gradoNombre = gradoNombre;
        this.gradoActivo = gradoActivo;
        this.gradoFechaModificacion = gradoFechaModificacion;
    }

    public Integer getGradoId() {
        return gradoId;
    }

    public void setGradoId(Integer gradoId) {
        this.gradoId = gradoId;
    }

    public String getGradoNombre() {
        return gradoNombre;
    }

    public void setGradoNombre(String gradoNombre) {
        this.gradoNombre = gradoNombre;
    }

    public boolean getGradoActivo() {
        return gradoActivo;
    }

    public void setGradoActivo(boolean gradoActivo) {
        this.gradoActivo = gradoActivo;
    }

    public Date getGradoFechaModificacion() {
        return gradoFechaModificacion;
    }

    public void setGradoFechaModificacion(Date gradoFechaModificacion) {
        this.gradoFechaModificacion = gradoFechaModificacion;
    }

    @XmlTransient
    public List<ConfiguracionGrado> getConfiguracionGradoList() {
        return configuracionGradoList;
    }

    public void setConfiguracionGradoList(List<ConfiguracionGrado> configuracionGradoList) {
        this.configuracionGradoList = configuracionGradoList;
    }

    public NivelEstudio getNivelEstudioId() {
        return nivelEstudioId;
    }

    public void setNivelEstudioId(NivelEstudio nivelEstudioId) {
        this.nivelEstudioId = nivelEstudioId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gradoId != null ? gradoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grado)) {
            return false;
        }
        Grado other = (Grado) object;
        if ((this.gradoId == null && other.gradoId != null) || (this.gradoId != null && !this.gradoId.equals(other.gradoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Grado[ gradoId=" + gradoId + " ]";
    }
    
}
