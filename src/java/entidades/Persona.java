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
import javax.persistence.OneToOne;
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
@Table(name = "Persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p")
    , @NamedQuery(name = "Persona.findByPersonaId", query = "SELECT p FROM Persona p WHERE p.personaId = :personaId")
    , @NamedQuery(name = "Persona.findByPersonaNombre", query = "SELECT p FROM Persona p WHERE p.personaNombre = :personaNombre")
    , @NamedQuery(name = "Persona.findByPersonaApellidos", query = "SELECT p FROM Persona p WHERE p.personaApellidos = :personaApellidos")
    , @NamedQuery(name = "Persona.findByPersonaActivo", query = "SELECT p FROM Persona p WHERE p.personaActivo = :personaActivo")
    , @NamedQuery(name = "Persona.findByPersonaFechaModificacion", query = "SELECT p FROM Persona p WHERE p.personaFechaModificacion = :personaFechaModificacion")})
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PersonaId")
    private Integer personaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PersonaNombre")
    private String personaNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PersonaApellidos")
    private String personaApellidos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PersonaActivo")
    private boolean personaActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PersonaFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date personaFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personaId")
    private List<Usuario> usuarioList;
    @JoinColumn(name = "GeneroId", referencedColumnName = "GeneroId")
    @ManyToOne(optional = false)
    private Genero generoId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personaId")
    private Estudiante estudiante;

    public Persona() {
    }

    public Persona(Integer personaId) {
        this.personaId = personaId;
    }

    public Persona(Integer personaId, String personaNombre, String personaApellidos, boolean personaActivo, Date personaFechaModificacion) {
        this.personaId = personaId;
        this.personaNombre = personaNombre;
        this.personaApellidos = personaApellidos;
        this.personaActivo = personaActivo;
        this.personaFechaModificacion = personaFechaModificacion;
    }

    public Integer getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Integer personaId) {
        this.personaId = personaId;
    }

    public String getPersonaNombre() {
        return personaNombre;
    }

    public void setPersonaNombre(String personaNombre) {
        this.personaNombre = personaNombre;
    }

    public String getPersonaApellidos() {
        return personaApellidos;
    }

    public void setPersonaApellidos(String personaApellidos) {
        this.personaApellidos = personaApellidos;
    }

    public boolean getPersonaActivo() {
        return personaActivo;
    }

    public void setPersonaActivo(boolean personaActivo) {
        this.personaActivo = personaActivo;
    }

    public Date getPersonaFechaModificacion() {
        return personaFechaModificacion;
    }

    public void setPersonaFechaModificacion(Date personaFechaModificacion) {
        this.personaFechaModificacion = personaFechaModificacion;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    public Genero getGeneroId() {
        return generoId;
    }

    public void setGeneroId(Genero generoId) {
        this.generoId = generoId;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personaId != null ? personaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.personaId == null && other.personaId != null) || (this.personaId != null && !this.personaId.equals(other.personaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Persona[ personaId=" + personaId + " ]";
    }
    
}
