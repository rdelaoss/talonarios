package model;
// Generated 10-10-2020 10:11:52 AM by Hibernate Tools 4.3.1


import java.io.Serializable;

/**
 * Tmp generated by hbm2java
 */
public class Tmp  implements java.io.Serializable {


     private int id;
     private Serializable valor;

    public Tmp() {
    }

	
    public Tmp(int id) {
        this.id = id;
    }
    public Tmp(int id, Serializable valor) {
       this.id = id;
       this.valor = valor;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    public Serializable getValor() {
        return this.valor;
    }
    
    public void setValor(Serializable valor) {
        this.valor = valor;
    }




}

