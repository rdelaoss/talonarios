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
import entidades.Arancel;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ControlPago;
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
public class DaoArancel implements Serializable {

    public DaoArancel(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Arancel arancel) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (arancel.getControlPagoList() == null) {
            arancel.setControlPagoList(new ArrayList<ControlPago>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ControlPago> attachedControlPagoList = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListControlPagoToAttach : arancel.getControlPagoList()) {
                controlPagoListControlPagoToAttach = em.getReference(controlPagoListControlPagoToAttach.getClass(), controlPagoListControlPagoToAttach.getControlPagoId());
                attachedControlPagoList.add(controlPagoListControlPagoToAttach);
            }
            arancel.setControlPagoList(attachedControlPagoList);
            em.persist(arancel);
            for (ControlPago controlPagoListControlPago : arancel.getControlPagoList()) {
                Arancel oldArancelIdOfControlPagoListControlPago = controlPagoListControlPago.getArancelId();
                controlPagoListControlPago.setArancelId(arancel);
                controlPagoListControlPago = em.merge(controlPagoListControlPago);
                if (oldArancelIdOfControlPagoListControlPago != null) {
                    oldArancelIdOfControlPagoListControlPago.getControlPagoList().remove(controlPagoListControlPago);
                    oldArancelIdOfControlPagoListControlPago = em.merge(oldArancelIdOfControlPagoListControlPago);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findArancel(arancel.getArancelId()) != null) {
                throw new PreexistingEntityException("Arancel " + arancel + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Arancel arancel) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Arancel persistentArancel = em.find(Arancel.class, arancel.getArancelId());
            List<ControlPago> controlPagoListOld = persistentArancel.getControlPagoList();
            List<ControlPago> controlPagoListNew = arancel.getControlPagoList();
            List<String> illegalOrphanMessages = null;
            for (ControlPago controlPagoListOldControlPago : controlPagoListOld) {
                if (!controlPagoListNew.contains(controlPagoListOldControlPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ControlPago " + controlPagoListOldControlPago + " since its arancelId field is not nullable.");
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
            arancel.setControlPagoList(controlPagoListNew);
            arancel = em.merge(arancel);
            for (ControlPago controlPagoListNewControlPago : controlPagoListNew) {
                if (!controlPagoListOld.contains(controlPagoListNewControlPago)) {
                    Arancel oldArancelIdOfControlPagoListNewControlPago = controlPagoListNewControlPago.getArancelId();
                    controlPagoListNewControlPago.setArancelId(arancel);
                    controlPagoListNewControlPago = em.merge(controlPagoListNewControlPago);
                    if (oldArancelIdOfControlPagoListNewControlPago != null && !oldArancelIdOfControlPagoListNewControlPago.equals(arancel)) {
                        oldArancelIdOfControlPagoListNewControlPago.getControlPagoList().remove(controlPagoListNewControlPago);
                        oldArancelIdOfControlPagoListNewControlPago = em.merge(oldArancelIdOfControlPagoListNewControlPago);
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
                Integer id = arancel.getArancelId();
                if (findArancel(id) == null) {
                    throw new NonexistentEntityException("The arancel with id " + id + " no longer exists.");
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
            Arancel arancel;
            try {
                arancel = em.getReference(Arancel.class, id);
                arancel.getArancelId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The arancel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ControlPago> controlPagoListOrphanCheck = arancel.getControlPagoList();
            for (ControlPago controlPagoListOrphanCheckControlPago : controlPagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Arancel (" + arancel + ") cannot be destroyed since the ControlPago " + controlPagoListOrphanCheckControlPago + " in its controlPagoList field has a non-nullable arancelId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(arancel);
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

    public List<Arancel> findArancelEntities() {
        return findArancelEntities(true, -1, -1);
    }

    public List<Arancel> findArancelEntities(int maxResults, int firstResult) {
        return findArancelEntities(false, maxResults, firstResult);
    }

    private List<Arancel> findArancelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Arancel.class));
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

    public Arancel findArancel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Arancel.class, id);
        } finally {
            em.close();
        }
    }

    public int getArancelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Arancel> rt = cq.from(Arancel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
