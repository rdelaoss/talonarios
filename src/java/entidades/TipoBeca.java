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
@Table(name = "TipoBeca")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoBeca.findAll", query = "SELECT t FROM TipoBeca t")
    , @NamedQuery(name = "TipoBeca.findByTipoBecaId", query = "SELECT t FROM TipoBeca t WHERE t.tipoBecaId = :tipoBecaId")
    , @NamedQuery(name = "TipoBeca.findByTipoBecaNombre", query = "SELECT t FROM TipoBeca t WHERE t.tipoBecaNombre = :tipoBecaNombre")
    , @NamedQuery(name = "TipoBeca.findByTipoBecaActivo", query = "SELECT t FROM TipoBeca t WHERE t.tipoBecaActivo = :tipoBecaActivo")
    , @NamedQuery(name = "TipoBeca.findByMostrarSolvencia", query = "SELECT t FROM TipoBeca t WHERE t.mostrarSolvencia = :mostrarSolvencia")
    , @NamedQuery(name = "TipoBeca.findByTipoBecaFechaModificacion", query = "SELECT t FROM TipoBeca t WHERE t.tipoBecaFechaModificacion = :tipoBecaFechaModificacion")})
public class TipoBeca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoBecaId")
    private Integer tipoBecaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TipoBecaNombre")
    private String tipoBecaNombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoBecaActivo")
    private boolean tipoBecaActivo;
    @Column(name = "MostrarSolvencia")
    private Boolean mostrarSolvencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoBecaFechaModificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tipoBecaFechaModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoBeca")
    private List<Matricula> matriculaList;

    public TipoBeca() {
    }

    public TipoBeca(Integer tipoBecaId) {
        this.tipoBecaId = tipoBecaId;
    }

    public TipoBeca(Integer tipoBecaId, String tipoBecaNombre, boolean tipoBecaActivo, Date tipoBecaFechaModificacion) {
        this.tipoBecaId = tipoBecaId;
        this.tipoBecaNombre = tipoBecaNombre;
        this.tipoBecaActivo = tipoBecaActivo;
        this.tipoBecaFechaModificacion = tipoBecaFechaModificacion;
    }

    public Integer getTipoBecaId() {
        return tipoBecaId;
    }

    public void setTipoBecaId(Integer tipoBecaId) {
        this.tipoBecaId = tipoBecaId;
    }

    public String getTipoBecaNombre() {
        return tipoBecaNombre;
    }

    public void setTipoBecaNombre(String tipoBecaNombre) {
        this.tipoBecaNombre = tipoBecaNombre;
    }

    public boolean getTipoBecaActivo() {
        return tipoBecaActivo;
    }

    public void setTipoBecaActivo(boolean tipoBecaActivo) {
        this.tipoBecaActivo = tipoBecaActivo;
    }

    public Boolean getMostrarSolvencia() {
        return mostrarSolvencia;
    }

    public void setMostrarSolvencia(Boolean mostrarSolvencia) {
        this.mostrarSolvencia = mostrarSolvencia;
    }

    public Date getTipoBecaFechaModificacion() {
        return tipoBecaFechaModificacion;
    }

    public void setTipoBecaFechaModificacion(Date tipoBecaFechaModificacion) {
        this.tipoBecaFechaModificacion = tipoBecaFechaModificacion;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoBecaId != null ? tipoBecaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoBeca)) {
            return false;
        }
        TipoBeca other = (TipoBeca) object;
        if ((this.tipoBecaId == null && other.tipoBecaId != null) || (this.tipoBecaId != null && !this.tipoBecaId.equals(other.tipoBecaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.TipoBeca[ tipoBecaId=" + tipoBecaId + " ]";
    }
    
}
