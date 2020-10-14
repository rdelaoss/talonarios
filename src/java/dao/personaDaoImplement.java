package dao;

import java.util.List;
import model.Persona;
import persistencia.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
// -----------------
import org.hibernate.Query;

public class personaDaoImplement implements personaDao {

    @Override
    public List<Persona> listarPersona() {
        List<Persona> lista = null;
        Session sesion = HibernateUtil.getSessionFactory().openSession();
        Transaction t = sesion.beginTransaction();
        String hql = "FROM Persona";
        try {
            lista = sesion.createQuery(hql).list();
            t.commit();
            sesion.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista;
    }

//    @Override
//    public void agregar(Persona persona) {
//        Session sesion = null;
//        try {
//            sesion = HibernateUtil.getSessionFactory().openSession();
//            sesion.beginTransaction();
//            sesion.save(persona);
//            sesion.getTransaction().commit();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            sesion.getTransaction().rollback();
//        } finally {
//            if (sesion != null) {
//                sesion.close();
//            }
//        }
//    }

////    @Override
////    public void modificar(Persona persona) {
////        Session sesion = null;
////        try {
////            sesion = HibernateUtil.getSessionFactory().openSession();
////            sesion.beginTransaction();
////            sesion.update(persona);
////            sesion.getTransaction().commit();
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////            sesion.getTransaction().rollback();
////        } finally {
////            if (sesion != null) {
////                sesion.close();
////            }
////        }
////    }

//    @Override
//    public void eliminar(Persona persona) {
//        Session sesion = null;
//        try {
//            sesion = HibernateUtil.getSessionFactory().openSession();
//            sesion.beginTransaction();
//            sesion.delete(persona);
//            sesion.getTransaction().commit();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            sesion.getTransaction().rollback();
//        } finally {
//            if (sesion != null) {
//                sesion.close();
//            }
//        }
//    }


}
