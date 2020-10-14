package model;
// Generated 10-10-2020 10:11:52 AM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Estudiante generated by hbm2java
 */
public class Estudiante  implements java.io.Serializable {


     private int estudianteId;
     private Persona persona;
     private boolean estudianteActivo;
     private Date estudianteFechaModificacion;
     private Set<Matricula> matriculas = new HashSet<Matricula>(0);

    public Estudiante() {
    }

	
    public Estudiante(int estudianteId, Persona persona, boolean estudianteActivo, Date estudianteFechaModificacion) {
        this.estudianteId = estudianteId;
        this.persona = persona;
        this.estudianteActivo = estudianteActivo;
        this.estudianteFechaModificacion = estudianteFechaModificacion;
    }
    public Estudiante(int estudianteId, Persona persona, boolean estudianteActivo, Date estudianteFechaModificacion, Set<Matricula> matriculas) {
       this.estudianteId = estudianteId;
       this.persona = persona;
       this.estudianteActivo = estudianteActivo;
       this.estudianteFechaModificacion = estudianteFechaModificacion;
       this.matriculas = matriculas;
    }
   
    public int getEstudianteId() {
        return this.estudianteId;
    }
    
    public void setEstudianteId(int estudianteId) {
        this.estudianteId = estudianteId;
    }
    public Persona getPersona() {
        return this.persona;
    }
    
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    public boolean isEstudianteActivo() {
        return this.estudianteActivo;
    }
    
    public void setEstudianteActivo(boolean estudianteActivo) {
        this.estudianteActivo = estudianteActivo;
    }
    public Date getEstudianteFechaModificacion() {
        return this.estudianteFechaModificacion;
    }
    
    public void setEstudianteFechaModificacion(Date estudianteFechaModificacion) {
        this.estudianteFechaModificacion = estudianteFechaModificacion;
    }
    public Set<Matricula> getMatriculas() {
        return this.matriculas;
    }
    
    public void setMatriculas(Set<Matricula> matriculas) {
        this.matriculas = matriculas;
    }




}


