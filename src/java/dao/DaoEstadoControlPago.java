/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ControlPago;
import entidades.EstadoControlPago;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrador
 */
public class DaoEstadoControlPago implements Serializable {

    public DaoEstadoControlPago(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EstadoControlPago estadoControlPago) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (estadoControlPago.getControlPagoList() == null) {
            estadoControlPago.setControlPagoList(new ArrayList<ControlPago>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ControlPago> attachedControlPagoList = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListControlPagoToAttach : estadoControlPago.getControlPagoList()) {
                controlPagoListControlPagoToAttach = em.getReference(controlPagoListControlPagoToAttach.getClass(), controlPagoListControlPagoToAttach.getControlPagoId());
                attachedControlPagoList.add(controlPagoListControlPagoToAttach);
            }
            estadoControlPago.setControlPagoList(attachedControlPagoList);
            em.persist(estadoControlPago);
            for (ControlPago controlPagoListControlPago : estadoControlPago.getControlPagoList()) {
                EstadoControlPago oldEstadoControlPagoIdOfControlPagoListControlPago = controlPagoListControlPago.getEstadoControlPagoId();
                controlPagoListControlPago.setEstadoControlPagoId(estadoControlPago);
                controlPagoListControlPago = em.merge(controlPagoListControlPago);
                if (oldEstadoControlPagoIdOfControlPagoListControlPago != null) {
                    oldEstadoControlPagoIdOfControlPagoListControlPago.getControlPagoList().remove(controlPagoListControlPago);
                    oldEstadoControlPagoIdOfControlPagoListControlPago = em.merge(oldEstadoControlPagoIdOfControlPagoListControlPago);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstadoControlPago(estadoControlPago.getEstadoControlPagoId()) != null) {
                throw new PreexistingEntityException("EstadoControlPago " + estadoControlPago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EstadoControlPago estadoControlPago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadoControlPago persistentEstadoControlPago = em.find(EstadoControlPago.class, estadoControlPago.getEstadoControlPagoId());
            List<ControlPago> controlPagoListOld = persistentEstadoControlPago.getControlPagoList();
            List<ControlPago> controlPagoListNew = estadoControlPago.getControlPagoList();
            List<String> illegalOrphanMessages = null;
            for (ControlPago controlPagoListOldControlPago : controlPagoListOld) {
                if (!controlPagoListNew.contains(controlPagoListOldControlPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ControlPago " + controlPagoListOldControlPago + " since its estadoControlPagoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ControlPago> attachedControlPagoListNew = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListNewControlPagoToAttach : controlPagoListNew) {
                controlPagoListNewControlPagoToAttach = em.getReference(controlPagoListNewControlPagoToAttach.getClass(), controlPagoListNewControlPagoToAttach.getControlPagoId());
                attachedControlPagoListNew.add(controlPagoListNewControlPagoToAttach);
            }
            controlPagoListNew = attachedControlPagoListNew;
            estadoControlPago.setControlPagoList(controlPagoListNew);
            estadoControlPago = em.merge(estadoControlPago);
            for (ControlPago controlPagoListNewControlPago : controlPagoListNew) {
                if (!controlPagoListOld.contains(controlPagoListNewControlPago)) {
                    EstadoControlPago oldEstadoControlPagoIdOfControlPagoListNewControlPago = controlPagoListNewControlPago.getEstadoControlPagoId();
                    controlPagoListNewControlPago.setEstadoControlPagoId(estadoControlPago);
                    controlPagoListNewControlPago = em.merge(controlPagoListNewControlPago);
                    if (oldEstadoControlPagoIdOfControlPagoListNewControlPago != null && !oldEstadoControlPagoIdOfControlPagoListNewControlPago.equals(estadoControlPago)) {
                        oldEstadoControlPagoIdOfControlPagoListNewControlPago.getControlPagoList().remove(controlPagoListNewControlPago);
                        oldEstadoControlPagoIdOfControlPagoListNewControlPago = em.merge(oldEstadoControlPagoIdOfControlPagoListNewControlPago);
                    }
                }
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
                Integer id = estadoControlPago.getEstadoControlPagoId();
                if (findEstadoControlPago(id) == null) {
                    throw new NonexistentEntityException("The estadoControlPago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EstadoControlPago estadoControlPago;
            try {
                estadoControlPago = em.getReference(EstadoControlPago.class, id);
                estadoControlPago.getEstadoControlPagoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estadoControlPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ControlPago> controlPagoListOrphanCheck = estadoControlPago.getControlPagoList();
            for (ControlPago controlPagoListOrphanCheckControlPago : controlPagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EstadoControlPago (" + estadoControlPago + ") cannot be destroyed since the ControlPago " + controlPagoListOrphanCheckControlPago + " in its controlPagoList field has a non-nullable estadoControlPagoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estadoControlPago);
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

    public List<EstadoControlPago> findEstadoControlPagoEntities() {
        return findEstadoControlPagoEntities(true, -1, -1);
    }

    public List<EstadoControlPago> findEstadoControlPagoEntities(int maxResults, int firstResult) {
        return findEstadoControlPagoEntities(false, maxResults, firstResult);
    }

    private List<EstadoControlPago> findEstadoControlPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstadoControlPago.class));
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

    public EstadoControlPago findEstadoControlPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EstadoControlPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadoControlPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstadoControlPago> rt = cq.from(EstadoControlPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
