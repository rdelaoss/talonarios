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
@Table(name = "EspecialidadGrado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspecialidadGrado.findAll", query = "SELECT e FROM EspecialidadGrado e")
    , @NamedQuery(name = "EspecialidadGrado.findByEspecialidadGradoId", query = "SELECT e FROM EspecialidadGrado e WHERE e.especialidadGradoId = :especialidadGradoId")
    , @NamedQuery(name = "EspecialidadGrado.findByEspecialidadGradoNombre", query = "SELECT e FROM EspecialidadGrado e WHERE e.especialidadGradoNombre = :especialidadGradoNombre")
    , @NamedQuery(name = "EspecialidadGrado.findByEspecialidadGradoActivo", query = "SELECT e FROM EspecialidadGrado e WHERE e.especialidadGradoActivo = :especialidadGradoActivo")
    , @NamedQuery(name = "EspecialidadGrado.findByEspecialidadGradoFechaModificacion", query = "SELECT e FROM EspecialidadGrado e WHERE e.especialidadGradoFechaModificacion = :especialidadGradoFechaModificacion")})
public class EspecialidadGrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EspecialidadGradoId")
    private Integer especialidadGradoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EspecialidadGradoNombre")
    private String especialidadGradoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EspecialidadGradoActivo")
    private boolean especialidadGradoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EspecialidadGradoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date especialidadGradoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especialidadGradoId")
    private List<ConfiguracionGrado> configuracionGradoList;

    public EspecialidadGrado() {
    }

    public EspecialidadGrado(Integer especialidadGradoId) {
        this.especialidadGradoId = especialidadGradoId;
    }

    public EspecialidadGrado(Integer especialidadGradoId, String especialidadGradoNombre, boolean especialidadGradoActivo, Date especialidadGradoFechaModificacion) {
        this.especialidadGradoId = especialidadGradoId;
        this.especialidadGradoNombre = especialidadGradoNombre;
        this.especialidadGradoActivo = especialidadGradoActivo;
        this.especialidadGradoFechaModificacion = especialidadGradoFechaModificacion;
    }

    public Integer getEspecialidadGradoId() {
        return especialidadGradoId;
    }

    public void setEspecialidadGradoId(Integer especialidadGradoId) {
        this.especialidadGradoId = especialidadGradoId;
    }

    public String getEspecialidadGradoNombre() {
        return especialidadGradoNombre;
    }

    public void setEspecialidadGradoNombre(String especialidadGradoNombre) {
        this.especialidadGradoNombre = especialidadGradoNombre;
    }

    public boolean getEspecialidadGradoActivo() {
        return especialidadGradoActivo;
    }

    public void setEspecialidadGradoActivo(boolean especialidadGradoActivo) {
        this.especialidadGradoActivo = especialidadGradoActivo;
    }

    public Date getEspecialidadGradoFechaModificacion() {
        return especialidadGradoFechaModificacion;
    }

    public void setEspecialidadGradoFechaModificacion(Date especialidadGradoFechaModificacion) {
        this.especialidadGradoFechaModificacion = especialidadGradoFechaModificacion;
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
        hash += (especialidadGradoId != null ? especialidadGradoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EspecialidadGrado)) {
            return false;
        }
        EspecialidadGrado other = (EspecialidadGrado) object;
        if ((this.especialidadGradoId == null && other.especialidadGradoId != null) || (this.especialidadGradoId != null && !this.especialidadGradoId.equals(other.especialidadGradoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.EspecialidadGrado[ especialidadGradoId=" + especialidadGradoId + " ]";
    }
    
}
