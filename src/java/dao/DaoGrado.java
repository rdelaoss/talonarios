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
import entidades.NivelEstudio;
import entidades.ConfiguracionGrado;
import entidades.Grado;
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
public class DaoGrado implements Serializable {

    public DaoGrado(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (grado.getConfiguracionGradoList() == null) {
            grado.setConfiguracionGradoList(new ArrayList<ConfiguracionGrado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NivelEstudio nivelEstudioId = grado.getNivelEstudioId();
            if (nivelEstudioId != null) {
                nivelEstudioId = em.getReference(nivelEstudioId.getClass(), nivelEstudioId.getNivelEstudioId());
                grado.setNivelEstudioId(nivelEstudioId);
            }
            List<ConfiguracionGrado> attachedConfiguracionGradoList = new ArrayList<ConfiguracionGrado>();
            for (ConfiguracionGrado configuracionGradoListConfiguracionGradoToAttach : grado.getConfiguracionGradoList()) {
                configuracionGradoListConfiguracionGradoToAttach = em.getReference(configuracionGradoListConfiguracionGradoToAttach.getClass(), configuracionGradoListConfiguracionGradoToAttach.getConfiguracionGradoId());
                attachedConfiguracionGradoList.add(configuracionGradoListConfiguracionGradoToAttach);
            }
            grado.setConfiguracionGradoList(attachedConfiguracionGradoList);
            em.persist(grado);
            if (nivelEstudioId != null) {
                nivelEstudioId.getGradoList().add(grado);
                nivelEstudioId = em.merge(nivelEstudioId);
            }
            for (ConfiguracionGrado configuracionGradoListConfiguracionGrado : grado.getConfiguracionGradoList()) {
                Grado oldGradoIdOfConfiguracionGradoListConfiguracionGrado = configuracionGradoListConfiguracionGrado.getGradoId();
                configuracionGradoListConfiguracionGrado.setGradoId(grado);
                configuracionGradoListConfiguracionGrado = em.merge(configuracionGradoListConfiguracionGrado);
                if (oldGradoIdOfConfiguracionGradoListConfiguracionGrado != null) {
                    oldGradoIdOfConfiguracionGradoListConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListConfiguracionGrado);
                    oldGradoIdOfConfiguracionGradoListConfiguracionGrado = em.merge(oldGradoIdOfConfiguracionGradoListConfiguracionGrado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findGrado(grado.getGradoId()) != null) {
                throw new PreexistingEntityException("Grado " + grado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grado persistentGrado = em.find(Grado.class, grado.getGradoId());
            NivelEstudio nivelEstudioIdOld = persistentGrado.getNivelEstudioId();
            NivelEstudio nivelEstudioIdNew = grado.getNivelEstudioId();
            List<ConfiguracionGrado> configuracionGradoListOld = persistentGrado.getConfiguracionGradoList();
            List<ConfiguracionGrado> configuracionGradoListNew = grado.getConfiguracionGradoList();
            List<String> illegalOrphanMessages = null;
            for (ConfiguracionGrado configuracionGradoListOldConfiguracionGrado : configuracionGradoListOld) {
                if (!configuracionGradoListNew.contains(configuracionGradoListOldConfiguracionGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConfiguracionGrado " + configuracionGradoListOldConfiguracionGrado + " since its gradoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nivelEstudioIdNew != null) {
                nivelEstudioIdNew = em.getReference(nivelEstudioIdNew.getClass(), nivelEstudioIdNew.getNivelEstudioId());
                grado.setNivelEstudioId(nivelEstudioIdNew);
            }
            List<ConfiguracionGrado> attachedConfiguracionGradoListNew = new ArrayList<ConfiguracionGrado>();
            for (ConfiguracionGrado configuracionGradoListNewConfiguracionGradoToAttach : configuracionGradoListNew) {
                configuracionGradoListNewConfiguracionGradoToAttach = em.getReference(configuracionGradoListNewConfiguracionGradoToAttach.getClass(), configuracionGradoListNewConfiguracionGradoToAttach.getConfiguracionGradoId());
                attachedConfiguracionGradoListNew.add(configuracionGradoListNewConfiguracionGradoToAttach);
            }
            configuracionGradoListNew = attachedConfiguracionGradoListNew;
            grado.setConfiguracionGradoList(configuracionGradoListNew);
            grado = em.merge(grado);
            if (nivelEstudioIdOld != null && !nivelEstudioIdOld.equals(nivelEstudioIdNew)) {
                nivelEstudioIdOld.getGradoList().remove(grado);
                nivelEstudioIdOld = em.merge(nivelEstudioIdOld);
            }
            if (nivelEstudioIdNew != null && !nivelEstudioIdNew.equals(nivelEstudioIdOld)) {
                nivelEstudioIdNew.getGradoList().add(grado);
                nivelEstudioIdNew = em.merge(nivelEstudioIdNew);
            }
            for (ConfiguracionGrado configuracionGradoListNewConfiguracionGrado : configuracionGradoListNew) {
                if (!configuracionGradoListOld.contains(configuracionGradoListNewConfiguracionGrado)) {
                    Grado oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado = configuracionGradoListNewConfiguracionGrado.getGradoId();
                    configuracionGradoListNewConfiguracionGrado.setGradoId(grado);
                    configuracionGradoListNewConfiguracionGrado = em.merge(configuracionGradoListNewConfiguracionGrado);
                    if (oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado != null && !oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado.equals(grado)) {
                        oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListNewConfiguracionGrado);
                        oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado = em.merge(oldGradoIdOfConfiguracionGradoListNewConfiguracionGrado);
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
                Integer id = grado.getGradoId();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
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
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getGradoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConfiguracionGrado> configuracionGradoListOrphanCheck = grado.getConfiguracionGradoList();
            for (ConfiguracionGrado configuracionGradoListOrphanCheckConfiguracionGrado : configuracionGradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grado (" + grado + ") cannot be destroyed since the ConfiguracionGrado " + configuracionGradoListOrphanCheckConfiguracionGrado + " in its configuracionGradoList field has a non-nullable gradoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            NivelEstudio nivelEstudioId = grado.getNivelEstudioId();
            if (nivelEstudioId != null) {
                nivelEstudioId.getGradoList().remove(grado);
                nivelEstudioId = em.merge(nivelEstudioId);
            }
            em.remove(grado);
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

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
