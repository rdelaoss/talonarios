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
@Table(name = "Genero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Genero.findAll", query = "SELECT g FROM Genero g")
    , @NamedQuery(name = "Genero.findByGeneroId", query = "SELECT g FROM Genero g WHERE g.generoId = :generoId")
    , @NamedQuery(name = "Genero.findByGeneroNombre", query = "SELECT g FROM Genero g WHERE g.generoNombre = :generoNombre")
    , @NamedQuery(name = "Genero.findByGeneroActivo", query = "SELECT g FROM Genero g WHERE g.generoActivo = :generoActivo")
    , @NamedQuery(name = "Genero.findByGeneroFechaModificacion", query = "SELECT g FROM Genero g WHERE g.generoFechaModificacion = :generoFechaModificacion")})
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "GeneroId")
    private Integer generoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "GeneroNombre")
    private String generoNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GeneroActivo")
    private boolean generoActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "GeneroFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date generoFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "generoId")
    private List<Persona> personaList;

    public Genero() {
    }

    public Genero(Integer generoId) {
        this.generoId = generoId;
    }

    public Genero(Integer generoId, String generoNombre, boolean generoActivo, Date generoFechaModificacion) {
        this.generoId = generoId;
        this.generoNombre = generoNombre;
        this.generoActivo = generoActivo;
        this.generoFechaModificacion = generoFechaModificacion;
    }

    public Integer getGeneroId() {
        return generoId;
    }

    public void setGeneroId(Integer generoId) {
        this.generoId = generoId;
    }

    public String getGeneroNombre() {
        return generoNombre;
    }

    public void setGeneroNombre(String generoNombre) {
        this.generoNombre = generoNombre;
    }

    public boolean getGeneroActivo() {
        return generoActivo;
    }

    public void setGeneroActivo(boolean generoActivo) {
        this.generoActivo = generoActivo;
    }

    public Date getGeneroFechaModificacion() {
        return generoFechaModificacion;
    }

    public void setGeneroFechaModificacion(Date generoFechaModificacion) {
        this.generoFechaModificacion = generoFechaModificacion;
    }

    @XmlTransient
    public List<Persona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<Persona> personaList) {
        this.personaList = personaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (generoId != null ? generoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genero)) {
            return false;
        }
        Genero other = (Genero) object;
        if ((this.generoId == null && other.generoId != null) || (this.generoId != null && !this.generoId.equals(other.generoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Genero[ generoId=" + generoId + " ]";
    }
    
}
