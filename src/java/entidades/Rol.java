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
@Table(name = "Rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r")
    , @NamedQuery(name = "Rol.findByRolId", query = "SELECT r FROM Rol r WHERE r.rolId = :rolId")
    , @NamedQuery(name = "Rol.findByRolNombre", query = "SELECT r FROM Rol r WHERE r.rolNombre = :rolNombre")
    , @NamedQuery(name = "Rol.findByRolActivo", query = "SELECT r FROM Rol r WHERE r.rolActivo = :rolActivo")
    , @NamedQuery(name = "Rol.findByRolFechaModificacion", query = "SELECT r FROM Rol r WHERE r.rolFechaModificacion = :rolFechaModificacion")})
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RolId")
    private Integer rolId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "RolNombre")
    private String rolNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RolActivo")
    private boolean rolActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RolFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rolFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rolId")
    private List<Usuario> usuarioList;

    public Rol() {
    }

    public Rol(Integer rolId) {
        this.rolId = rolId;
    }

    public Rol(Integer rolId, String rolNombre, boolean rolActivo, Date rolFechaModificacion) {
        this.rolId = rolId;
        this.rolNombre = rolNombre;
        this.rolActivo = rolActivo;
        this.rolFechaModificacion = rolFechaModificacion;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public boolean getRolActivo() {
        return rolActivo;
    }

    public void setRolActivo(boolean rolActivo) {
        this.rolActivo = rolActivo;
    }

    public Date getRolFechaModificacion() {
        return rolFechaModificacion;
    }

    public void setRolFechaModificacion(Date rolFechaModificacion) {
        this.rolFechaModificacion = rolFechaModificacion;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolId != null ? rolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolId == null && other.rolId != null) || (this.rolId != null && !this.rolId.equals(other.rolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Rol[ rolId=" + rolId + " ]";
    }
    
}
