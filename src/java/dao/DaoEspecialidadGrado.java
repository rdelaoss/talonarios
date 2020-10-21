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
import entidades.ConfiguracionGrado;
import entidades.EspecialidadGrado;
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
public class DaoEspecialidadGrado implements Serializable {

    public DaoEspecialidadGrado(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EspecialidadGrado especialidadGrado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (especialidadGrado.getConfiguracionGradoList() == null) {
            especialidadGrado.setConfiguracionGradoList(new ArrayList<ConfiguracionGrado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ConfiguracionGrado> attachedConfiguracionGradoList = new ArrayList<ConfiguracionGrado>();
            for (ConfiguracionGrado configuracionGradoListConfiguracionGradoToAttach : especialidadGrado.getConfiguracionGradoList()) {
                configuracionGradoListConfiguracionGradoToAttach = em.getReference(configuracionGradoListConfiguracionGradoToAttach.getClass(), configuracionGradoListConfiguracionGradoToAttach.getConfiguracionGradoId());
                attachedConfiguracionGradoList.add(configuracionGradoListConfiguracionGradoToAttach);
            }
            especialidadGrado.setConfiguracionGradoList(attachedConfiguracionGradoList);
            em.persist(especialidadGrado);
            for (ConfiguracionGrado configuracionGradoListConfiguracionGrado : especialidadGrado.getConfiguracionGradoList()) {
                EspecialidadGrado oldEspecialidadGradoIdOfConfiguracionGradoListConfiguracionGrado = configuracionGradoListConfiguracionGrado.getEspecialidadGradoId();
                configuracionGradoListConfiguracionGrado.setEspecialidadGradoId(especialidadGrado);
                configuracionGradoListConfiguracionGrado = em.merge(configuracionGradoListConfiguracionGrado);
                if (oldEspecialidadGradoIdOfConfiguracionGradoListConfiguracionGrado != null) {
                    oldEspecialidadGradoIdOfConfiguracionGradoListConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListConfiguracionGrado);
                    oldEspecialidadGradoIdOfConfiguracionGradoListConfiguracionGrado = em.merge(oldEspecialidadGradoIdOfConfiguracionGradoListConfiguracionGrado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEspecialidadGrado(especialidadGrado.getEspecialidadGradoId()) != null) {
                throw new PreexistingEntityException("EspecialidadGrado " + especialidadGrado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EspecialidadGrado especialidadGrado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EspecialidadGrado persistentEspecialidadGrado = em.find(EspecialidadGrado.class, especialidadGrado.getEspecialidadGradoId());
            List<ConfiguracionGrado> configuracionGradoListOld = persistentEspecialidadGrado.getConfiguracionGradoList();
            List<ConfiguracionGrado> configuracionGradoListNew = especialidadGrado.getConfiguracionGradoList();
            List<String> illegalOrphanMessages = null;
            for (ConfiguracionGrado configuracionGradoListOldConfiguracionGrado : configuracionGradoListOld) {
                if (!configuracionGradoListNew.contains(configuracionGradoListOldConfiguracionGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConfiguracionGrado " + configuracionGradoListOldConfiguracionGrado + " since its especialidadGradoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ConfiguracionGrado> attachedConfiguracionGradoListNew = new ArrayList<ConfiguracionGrado>();
            for (ConfiguracionGrado configuracionGradoListNewConfiguracionGradoToAttach : configuracionGradoListNew) {
                configuracionGradoListNewConfiguracionGradoToAttach = em.getReference(configuracionGradoListNewConfiguracionGradoToAttach.getClass(), configuracionGradoListNewConfiguracionGradoToAttach.getConfiguracionGradoId());
                attachedConfiguracionGradoListNew.add(configuracionGradoListNewConfiguracionGradoToAttach);
            }
            configuracionGradoListNew = attachedConfiguracionGradoListNew;
            especialidadGrado.setConfiguracionGradoList(configuracionGradoListNew);
            especialidadGrado = em.merge(especialidadGrado);
            for (ConfiguracionGrado configuracionGradoListNewConfiguracionGrado : configuracionGradoListNew) {
                if (!configuracionGradoListOld.contains(configuracionGradoListNewConfiguracionGrado)) {
                    EspecialidadGrado oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado = configuracionGradoListNewConfiguracionGrado.getEspecialidadGradoId();
                    configuracionGradoListNewConfiguracionGrado.setEspecialidadGradoId(especialidadGrado);
                    configuracionGradoListNewConfiguracionGrado = em.merge(configuracionGradoListNewConfiguracionGrado);
                    if (oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado != null && !oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado.equals(especialidadGrado)) {
                        oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListNewConfiguracionGrado);
                        oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado = em.merge(oldEspecialidadGradoIdOfConfiguracionGradoListNewConfiguracionGrado);
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
                Integer id = especialidadGrado.getEspecialidadGradoId();
                if (findEspecialidadGrado(id) == null) {
                    throw new NonexistentEntityException("The especialidadGrado with id " + id + " no longer exists.");
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
            EspecialidadGrado especialidadGrado;
            try {
                especialidadGrado = em.getReference(EspecialidadGrado.class, id);
                especialidadGrado.getEspecialidadGradoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The especialidadGrado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConfiguracionGrado> configuracionGradoListOrphanCheck = especialidadGrado.getConfiguracionGradoList();
            for (ConfiguracionGrado configuracionGradoListOrphanCheckConfiguracionGrado : configuracionGradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EspecialidadGrado (" + especialidadGrado + ") cannot be destroyed since the ConfiguracionGrado " + configuracionGradoListOrphanCheckConfiguracionGrado + " in its configuracionGradoList field has a non-nullable especialidadGradoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(especialidadGrado);
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

    public List<EspecialidadGrado> findEspecialidadGradoEntities() {
        return findEspecialidadGradoEntities(true, -1, -1);
    }

    public List<EspecialidadGrado> findEspecialidadGradoEntities(int maxResults, int firstResult) {
        return findEspecialidadGradoEntities(false, maxResults, firstResult);
    }

    private List<EspecialidadGrado> findEspecialidadGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EspecialidadGrado.class));
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

    public EspecialidadGrado findEspecialidadGrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EspecialidadGrado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspecialidadGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EspecialidadGrado> rt = cq.from(EspecialidadGrado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
