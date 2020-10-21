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
@Table(name = "Usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByUsuarioId", query = "SELECT u FROM Usuario u WHERE u.usuarioId = :usuarioId")
    , @NamedQuery(name = "Usuario.findByUsuarioNombre", query = "SELECT u FROM Usuario u WHERE u.usuarioNombre = :usuarioNombre")
    , @NamedQuery(name = "Usuario.findByUsuarioPassword", query = "SELECT u FROM Usuario u WHERE u.usuarioPassword = :usuarioPassword")
    , @NamedQuery(name = "Usuario.findByUsuarioActivo", query = "SELECT u FROM Usuario u WHERE u.usuarioActivo = :usuarioActivo")
    , @NamedQuery(name = "Usuario.findByUsuarioFechaModificacion", query = "SELECT u FROM Usuario u WHERE u.usuarioFechaModificacion = :usuarioFechaModificacion")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "UsuarioId")
    private Integer usuarioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UsuarioNombre")
    private String usuarioNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UsuarioPassword")
    private String usuarioPassword;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UsuarioActivo")
    private boolean usuarioActivo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UsuarioFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date usuarioFechaModificacion;
    @JoinColumn(name = "PersonaId", referencedColumnName = "PersonaId")
    @ManyToOne(optional = false)
    private Persona personaId;
    @JoinColumn(name = "RolId", referencedColumnName = "RolId")
    @ManyToOne(optional = false)
    private Rol rolId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<DetalleAsignacionCuota> detalleAsignacionCuotaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "controlPagoUsuarioId")
    private List<ControlPago> controlPagoList;

    public Usuario() {
    }

    public Usuario(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Usuario(Integer usuarioId, String usuarioNombre, String usuarioPassword, boolean usuarioActivo, Date usuarioFechaModificacion) {
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
        this.usuarioPassword = usuarioPassword;
        this.usuarioActivo = usuarioActivo;
        this.usuarioFechaModificacion = usuarioFechaModificacion;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioPassword() {
        return usuarioPassword;
    }

    public void setUsuarioPassword(String usuarioPassword) {
        this.usuarioPassword = usuarioPassword;
    }

    public boolean getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(boolean usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public Date getUsuarioFechaModificacion() {
        return usuarioFechaModificacion;
    }

    public void setUsuarioFechaModificacion(Date usuarioFechaModificacion) {
        this.usuarioFechaModificacion = usuarioFechaModificacion;
    }

    public Persona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(Persona personaId) {
        this.personaId = personaId;
    }

    public Rol getRolId() {
        return rolId;
    }

    public void setRolId(Rol rolId) {
        this.rolId = rolId;
    }

    @XmlTransient
    public List<DetalleAsignacionCuota> getDetalleAsignacionCuotaList() {
        return detalleAsignacionCuotaList;
    }

    public void setDetalleAsignacionCuotaList(List<DetalleAsignacionCuota> detalleAsignacionCuotaList) {
        this.detalleAsignacionCuotaList = detalleAsignacionCuotaList;
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
        hash += (usuarioId != null ? usuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.usuarioId == null && other.usuarioId != null) || (this.usuarioId != null && !this.usuarioId.equals(other.usuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Usuario[ usuarioId=" + usuarioId + " ]";
    }
    
}
