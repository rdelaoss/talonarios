/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ControlPago;
import entidades.DetalleAsignacionCuota;
import entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrador
 */
public class DaoDetalleAsignacionCuota implements Serializable {

    public DaoDetalleAsignacionCuota(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleAsignacionCuota detalleAsignacionCuota) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ControlPago controlPagoId = detalleAsignacionCuota.getControlPagoId();
            if (controlPagoId != null) {
                controlPagoId = em.getReference(controlPagoId.getClass(), controlPagoId.getControlPagoId());
                detalleAsignacionCuota.setControlPagoId(controlPagoId);
            }
            Usuario usuarioId = detalleAsignacionCuota.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getUsuarioId());
                detalleAsignacionCuota.setUsuarioId(usuarioId);
            }
            em.persist(detalleAsignacionCuota);
            if (controlPagoId != null) {
                controlPagoId.getDetalleAsignacionCuotaList().add(detalleAsignacionCuota);
                controlPagoId = em.merge(controlPagoId);
            }
            if (usuarioId != null) {
                usuarioId.getDetalleAsignacionCuotaList().add(detalleAsignacionCuota);
                usuarioId = em.merge(usuarioId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDetalleAsignacionCuota(detalleAsignacionCuota.getDetalleAsginacionCuotaId()) != null) {
                throw new PreexistingEntityException("DetalleAsignacionCuota " + detalleAsignacionCuota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleAsignacionCuota detalleAsignacionCuota) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleAsignacionCuota persistentDetalleAsignacionCuota = em.find(DetalleAsignacionCuota.class, detalleAsignacionCuota.getDetalleAsginacionCuotaId());
            ControlPago controlPagoIdOld = persistentDetalleAsignacionCuota.getControlPagoId();
            ControlPago controlPagoIdNew = detalleAsignacionCuota.getControlPagoId();
            Usuario usuarioIdOld = persistentDetalleAsignacionCuota.getUsuarioId();
            Usuario usuarioIdNew = detalleAsignacionCuota.getUsuarioId();
            if (controlPagoIdNew != null) {
                controlPagoIdNew = em.getReference(controlPagoIdNew.getClass(), controlPagoIdNew.getControlPagoId());
                detalleAsignacionCuota.setControlPagoId(controlPagoIdNew);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getUsuarioId());
                detalleAsignacionCuota.setUsuarioId(usuarioIdNew);
            }
            detalleAsignacionCuota = em.merge(detalleAsignacionCuota);
            if (controlPagoIdOld != null && !controlPagoIdOld.equals(controlPagoIdNew)) {
                controlPagoIdOld.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuota);
                controlPagoIdOld = em.merge(controlPagoIdOld);
            }
            if (controlPagoIdNew != null && !controlPagoIdNew.equals(controlPagoIdOld)) {
                controlPagoIdNew.getDetalleAsignacionCuotaList().add(detalleAsignacionCuota);
                controlPagoIdNew = em.merge(controlPagoIdNew);
            }
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuota);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getDetalleAsignacionCuotaList().add(detalleAsignacionCuota);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleAsignacionCuota.getDetalleAsginacionCuotaId();
                if (findDetalleAsignacionCuota(id) == null) {
                    throw new NonexistentEntityException("The detalleAsignacionCuota with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DetalleAsignacionCuota detalleAsignacionCuota;
            try {
                detalleAsignacionCuota = em.getReference(DetalleAsignacionCuota.class, id);
                detalleAsignacionCuota.getDetalleAsginacionCuotaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleAsignacionCuota with id " + id + " no longer exists.", enfe);
            }
            ControlPago controlPagoId = detalleAsignacionCuota.getControlPagoId();
            if (controlPagoId != null) {
                controlPagoId.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuota);
                controlPagoId = em.merge(controlPagoId);
            }
            Usuario usuarioId = detalleAsignacionCuota.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuota);
                usuarioId = em.merge(usuarioId);
            }
            em.remove(detalleAsignacionCuota);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleAsignacionCuota> findDetalleAsignacionCuotaEntities() {
        return findDetalleAsignacionCuotaEntities(true, -1, -1);
    }

    public List<DetalleAsignacionCuota> findDetalleAsignacionCuotaEntities(int maxResults, int firstResult) {
        return findDetalleAsignacionCuotaEntities(false, maxResults, firstResult);
    }

    private List<DetalleAsignacionCuota> findDetalleAsignacionCuotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleAsignacionCuota.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DetalleAsignacionCuota findDetalleAsignacionCuota(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleAsignacionCuota.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleAsignacionCuotaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleAsignacionCuota> rt = cq.from(DetalleAsignacionCuota.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
