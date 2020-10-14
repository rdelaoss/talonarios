package model;
// Generated 10-10-2020 10:11:52 AM by Hibernate Tools 4.3.1


import java.io.Serializable;
import java.util.Date;

/**
 * LogError generated by hbm2java
 */
public class LogError  implements java.io.Serializable {


     private int logErrorId;
     private Serializable logErrorOrigen;
     private Serializable logErrorDescripcion;
     private Serializable logErrorSqlgeneradora;
     private Date logErrorFechaHora;

    public LogError() {
    }

    public LogError(int logErrorId, Serializable logErrorOrigen, Serializable logErrorDescripcion, Serializable logErrorSqlgeneradora, Date logErrorFechaHora) {
       this.logErrorId = logErrorId;
       this.logErrorOrigen = logErrorOrigen;
       this.logErrorDescripcion = logErrorDescripcion;
       this.logErrorSqlgeneradora = logErrorSqlgeneradora;
       this.logErrorFechaHora = logErrorFechaHora;
    }
   
    public int getLogErrorId() {
        return this.logErrorId;
    }
    
    public void setLogErrorId(int logErrorId) {
        this.logErrorId = logErrorId;
    }
    public Serializable getLogErrorOrigen() {
        return this.logErrorOrigen;
    }
    
    public void setLogErrorOrigen(Serializable logErrorOrigen) {
        this.logErrorOrigen = logErrorOrigen;
    }
    public Serializable getLogErrorDescripcion() {
        return this.logErrorDescripcion;
    }
    
    public void setLogErrorDescripcion(Serializable logErrorDescripcion) {
        this.logErrorDescripcion = logErrorDescripcion;
    }
    public Serializable getLogErrorSqlgeneradora() {
        return this.logErrorSqlgeneradora;
    }
    
    public void setLogErrorSqlgeneradora(Serializable logErrorSqlgeneradora) {
        this.logErrorSqlgeneradora = logErrorSqlgeneradora;
    }
    public Date getLogErrorFechaHora() {
        return this.logErrorFechaHora;
    }
    
    public void setLogErrorFechaHora(Date logErrorFechaHora) {
        this.logErrorFechaHora = logErrorFechaHora;
    }




}


