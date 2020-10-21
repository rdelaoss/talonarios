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
@Table(name = "ConfiguracionGrado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionGrado.findAll", query = "SELECT c FROM ConfiguracionGrado c")
    , @NamedQuery(name = "ConfiguracionGrado.findByConfiguracionGradoId", query = "SELECT c FROM ConfiguracionGrado c WHERE c.configuracionGradoId = :configuracionGradoId")
    , @NamedQuery(name = "ConfiguracionGrado.findByConfiguracionGradoSeccion", query = "SELECT c FROM ConfiguracionGrado c WHERE c.configuracionGradoSeccion = :configuracionGradoSeccion")
    , @NamedQuery(name = "ConfiguracionGrado.findByConfiguracionGradoActivo", query = "SELECT c FROM ConfiguracionGrado c WHERE c.configuracionGradoActivo = :configuracionGradoActivo")
    , @NamedQuery(name = "ConfiguracionGrado.findByConfiguracionGradoFechaModificacion", query = "SELECT c FROM ConfiguracionGrado c WHERE c.configuracionGradoFechaModificacion = :configuracionGradoFechaModificacion")})
public class ConfiguracionGrado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ConfiguracionGradoId")
    private Integer configuracionGradoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ConfiguracionGradoSeccion")
    private String configuracionGradoSeccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ConfiguracionGradoActivo")
    private boolean configuracionGradoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ConfiguracionGradoFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date configuracionGradoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "configuracionGradoId")
    private List<Matricula> matriculaList;
    @JoinColumn(name = "AnioLectivoId", referencedColumnName = "AnioLectivoId")
    @ManyToOne(optional = false)
    private AnioLectivo anioLectivoId;
    @JoinColumn(name = "EspecialidadGradoId", referencedColumnName = "EspecialidadGradoId")
    @ManyToOne(optional = false)
    private EspecialidadGrado especialidadGradoId;
    @JoinColumn(name = "GradoId", referencedColumnName = "GradoId")
    @ManyToOne(optional = false)
    private Grado gradoId;

    public ConfiguracionGrado() {
    }

    public ConfiguracionGrado(Integer configuracionGradoId) {
        this.configuracionGradoId = configuracionGradoId;
    }

    public ConfiguracionGrado(Integer configuracionGradoId, String configuracionGradoSeccion, boolean configuracionGradoActivo, Date configuracionGradoFechaModificacion) {
        this.configuracionGradoId = configuracionGradoId;
        this.configuracionGradoSeccion = configuracionGradoSeccion;
        this.configuracionGradoActivo = configuracionGradoActivo;
        this.configuracionGradoFechaModificacion = configuracionGradoFechaModificacion;
    }

    public Integer getConfiguracionGradoId() {
        return configuracionGradoId;
    }

    public void setConfiguracionGradoId(Integer configuracionGradoId) {
        this.configuracionGradoId = configuracionGradoId;
    }

    public String getConfiguracionGradoSeccion() {
        return configuracionGradoSeccion;
    }

    public void setConfiguracionGradoSeccion(String configuracionGradoSeccion) {
        this.configuracionGradoSeccion = configuracionGradoSeccion;
    }

    public boolean getConfiguracionGradoActivo() {
        return configuracionGradoActivo;
    }

    public void setConfiguracionGradoActivo(boolean configuracionGradoActivo) {
        this.configuracionGradoActivo = configuracionGradoActivo;
    }

    public Date getConfiguracionGradoFechaModificacion() {
        return configuracionGradoFechaModificacion;
    }

    public void setConfiguracionGradoFechaModificacion(Date configuracionGradoFechaModificacion) {
        this.configuracionGradoFechaModificacion = configuracionGradoFechaModificacion;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    public AnioLectivo getAnioLectivoId() {
        return anioLectivoId;
    }

    public void setAnioLectivoId(AnioLectivo anioLectivoId) {
        this.anioLectivoId = anioLectivoId;
    }

    public EspecialidadGrado getEspecialidadGradoId() {
        return especialidadGradoId;
    }

    public void setEspecialidadGradoId(EspecialidadGrado especialidadGradoId) {
        this.especialidadGradoId = especialidadGradoId;
    }

    public Grado getGradoId() {
        return gradoId;
    }

    public void setGradoId(Grado gradoId) {
        this.gradoId = gradoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configuracionGradoId != null ? configuracionGradoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConfiguracionGrado)) {
            return false;
        }
        ConfiguracionGrado other = (ConfiguracionGrado) object;
        if ((this.configuracionGradoId == null && other.configuracionGradoId != null) || (this.configuracionGradoId != null && !this.configuracionGradoId.equals(other.configuracionGradoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ConfiguracionGrado[ configuracionGradoId=" + configuracionGradoId + " ]";
    }
    
}
