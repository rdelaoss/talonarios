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
import entidades.AnioLectivo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.ConfiguracionGrado;
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
public class DaoAnioLectivo implements Serializable {

    public DaoAnioLectivo(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AnioLectivo anioLectivo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (anioLectivo.getConfiguracionGradoList() == null) {
            anioLectivo.setConfiguracionGradoList(new ArrayList<ConfiguracionGrado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<ConfiguracionGrado> attachedConfiguracionGradoList = new ArrayList<ConfiguracionGrado>();
            for (ConfiguracionGrado configuracionGradoListConfiguracionGradoToAttach : anioLectivo.getConfiguracionGradoList()) {
                configuracionGradoListConfiguracionGradoToAttach = em.getReference(configuracionGradoListConfiguracionGradoToAttach.getClass(), configuracionGradoListConfiguracionGradoToAttach.getConfiguracionGradoId());
                attachedConfiguracionGradoList.add(configuracionGradoListConfiguracionGradoToAttach);
            }
            anioLectivo.setConfiguracionGradoList(attachedConfiguracionGradoList);
            em.persist(anioLectivo);
            for (ConfiguracionGrado configuracionGradoListConfiguracionGrado : anioLectivo.getConfiguracionGradoList()) {
                AnioLectivo oldAnioLectivoIdOfConfiguracionGradoListConfiguracionGrado = configuracionGradoListConfiguracionGrado.getAnioLectivoId();
                configuracionGradoListConfiguracionGrado.setAnioLectivoId(anioLectivo);
                configuracionGradoListConfiguracionGrado = em.merge(configuracionGradoListConfiguracionGrado);
                if (oldAnioLectivoIdOfConfiguracionGradoListConfiguracionGrado != null) {
                    oldAnioLectivoIdOfConfiguracionGradoListConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListConfiguracionGrado);
                    oldAnioLectivoIdOfConfiguracionGradoListConfiguracionGrado = em.merge(oldAnioLectivoIdOfConfiguracionGradoListConfiguracionGrado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAnioLectivo(anioLectivo.getAnioLectivoId()) != null) {
                throw new PreexistingEntityException("AnioLectivo " + anioLectivo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AnioLectivo anioLectivo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AnioLectivo persistentAnioLectivo = em.find(AnioLectivo.class, anioLectivo.getAnioLectivoId());
            List<ConfiguracionGrado> configuracionGradoListOld = persistentAnioLectivo.getConfiguracionGradoList();
            List<ConfiguracionGrado> configuracionGradoListNew = anioLectivo.getConfiguracionGradoList();
            List<String> illegalOrphanMessages = null;
            for (ConfiguracionGrado configuracionGradoListOldConfiguracionGrado : configuracionGradoListOld) {
                if (!configuracionGradoListNew.contains(configuracionGradoListOldConfiguracionGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ConfiguracionGrado " + configuracionGradoListOldConfiguracionGrado + " since its anioLectivoId field is not nullable.");
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
            anioLectivo.setConfiguracionGradoList(configuracionGradoListNew);
            anioLectivo = em.merge(anioLectivo);
            for (ConfiguracionGrado configuracionGradoListNewConfiguracionGrado : configuracionGradoListNew) {
                if (!configuracionGradoListOld.contains(configuracionGradoListNewConfiguracionGrado)) {
                    AnioLectivo oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado = configuracionGradoListNewConfiguracionGrado.getAnioLectivoId();
                    configuracionGradoListNewConfiguracionGrado.setAnioLectivoId(anioLectivo);
                    configuracionGradoListNewConfiguracionGrado = em.merge(configuracionGradoListNewConfiguracionGrado);
                    if (oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado != null && !oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado.equals(anioLectivo)) {
                        oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado.getConfiguracionGradoList().remove(configuracionGradoListNewConfiguracionGrado);
                        oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado = em.merge(oldAnioLectivoIdOfConfiguracionGradoListNewConfiguracionGrado);
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
                Integer id = anioLectivo.getAnioLectivoId();
                if (findAnioLectivo(id) == null) {
                    throw new NonexistentEntityException("The anioLectivo with id " + id + " no longer exists.");
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
            AnioLectivo anioLectivo;
            try {
                anioLectivo = em.getReference(AnioLectivo.class, id);
                anioLectivo.getAnioLectivoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anioLectivo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ConfiguracionGrado> configuracionGradoListOrphanCheck = anioLectivo.getConfiguracionGradoList();
            for (ConfiguracionGrado configuracionGradoListOrphanCheckConfiguracionGrado : configuracionGradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AnioLectivo (" + anioLectivo + ") cannot be destroyed since the ConfiguracionGrado " + configuracionGradoListOrphanCheckConfiguracionGrado + " in its configuracionGradoList field has a non-nullable anioLectivoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(anioLectivo);
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

    public List<AnioLectivo> findAnioLectivoEntities() {
        return findAnioLectivoEntities(true, -1, -1);
    }

    public List<AnioLectivo> findAnioLectivoEntities(int maxResults, int firstResult) {
        return findAnioLectivoEntities(false, maxResults, firstResult);
    }

    private List<AnioLectivo> findAnioLectivoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AnioLectivo.class));
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

    public AnioLectivo findAnioLectivo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AnioLectivo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnioLectivoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AnioLectivo> rt = cq.from(AnioLectivo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
