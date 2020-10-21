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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "Estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e")
    , @NamedQuery(name = "Estudiante.findByEstudianteId", query = "SELECT e FROM Estudiante e WHERE e.estudianteId = :estudianteId")
    , @NamedQuery(name = "Estudiante.findByEstudianteActivo", query = "SELECT e FROM Estudiante e WHERE e.estudianteActivo = :estudianteActivo")
    , @NamedQuery(name = "Estudiante.findByEstudianteFechaModificacion", query = "SELECT e FROM Estudiante e WHERE e.estudianteFechaModificacion = :estudianteFechaModificacion")})
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstudianteId")
    private Integer estudianteId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstudianteActivo")
    private boolean estudianteActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EstudianteFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date estudianteFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudianteId")
    private List<Matricula> matriculaList;
    @JoinColumn(name = "PersonaId", referencedColumnName = "PersonaId")
    @OneToOne(optional = false)
    private Persona personaId;

    public Estudiante() {
    }

    public Estudiante(Integer estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Estudiante(Integer estudianteId, boolean estudianteActivo, Date estudianteFechaModificacion) {
        this.estudianteId = estudianteId;
        this.estudianteActivo = estudianteActivo;
        this.estudianteFechaModificacion = estudianteFechaModificacion;
    }

    public Integer getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Integer estudianteId) {
        this.estudianteId = estudianteId;
    }

    public boolean getEstudianteActivo() {
        return estudianteActivo;
    }

    public void setEstudianteActivo(boolean estudianteActivo) {
        this.estudianteActivo = estudianteActivo;
    }

    public Date getEstudianteFechaModificacion() {
        return estudianteFechaModificacion;
    }

    public void setEstudianteFechaModificacion(Date estudianteFechaModificacion) {
        this.estudianteFechaModificacion = estudianteFechaModificacion;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    public Persona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Persona personaId) {
        this.personaId = personaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudianteId != null ? estudianteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.estudianteId == null && other.estudianteId != null) || (this.estudianteId != null && !this.estudianteId.equals(other.estudianteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Estudiante[ estudianteId=" + estudianteId + " ]";
    }
    
}
