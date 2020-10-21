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
import entidades.Grado;
import entidades.NivelEstudio;
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
public class DaoNivelEstudio implements Serializable {

    public DaoNivelEstudio(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NivelEstudio nivelEstudio) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (nivelEstudio.getGradoList() == null) {
            nivelEstudio.setGradoList(new ArrayList<Grado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Grado> attachedGradoList = new ArrayList<Grado>();
            for (Grado gradoListGradoToAttach : nivelEstudio.getGradoList()) {
                gradoListGradoToAttach = em.getReference(gradoListGradoToAttach.getClass(), gradoListGradoToAttach.getGradoId());
                attachedGradoList.add(gradoListGradoToAttach);
            }
            nivelEstudio.setGradoList(attachedGradoList);
            em.persist(nivelEstudio);
            for (Grado gradoListGrado : nivelEstudio.getGradoList()) {
                NivelEstudio oldNivelEstudioIdOfGradoListGrado = gradoListGrado.getNivelEstudioId();
                gradoListGrado.setNivelEstudioId(nivelEstudio);
                gradoListGrado = em.merge(gradoListGrado);
                if (oldNivelEstudioIdOfGradoListGrado != null) {
                    oldNivelEstudioIdOfGradoListGrado.getGradoList().remove(gradoListGrado);
                    oldNivelEstudioIdOfGradoListGrado = em.merge(oldNivelEstudioIdOfGradoListGrado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findNivelEstudio(nivelEstudio.getNivelEstudioId()) != null) {
                throw new PreexistingEntityException("NivelEstudio " + nivelEstudio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NivelEstudio nivelEstudio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            NivelEstudio persistentNivelEstudio = em.find(NivelEstudio.class, nivelEstudio.getNivelEstudioId());
            List<Grado> gradoListOld = persistentNivelEstudio.getGradoList();
            List<Grado> gradoListNew = nivelEstudio.getGradoList();
            List<String> illegalOrphanMessages = null;
            for (Grado gradoListOldGrado : gradoListOld) {
                if (!gradoListNew.contains(gradoListOldGrado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Grado " + gradoListOldGrado + " since its nivelEstudioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Grado> attachedGradoListNew = new ArrayList<Grado>();
            for (Grado gradoListNewGradoToAttach : gradoListNew) {
                gradoListNewGradoToAttach = em.getReference(gradoListNewGradoToAttach.getClass(), gradoListNewGradoToAttach.getGradoId());
                attachedGradoListNew.add(gradoListNewGradoToAttach);
            }
            gradoListNew = attachedGradoListNew;
            nivelEstudio.setGradoList(gradoListNew);
            nivelEstudio = em.merge(nivelEstudio);
            for (Grado gradoListNewGrado : gradoListNew) {
                if (!gradoListOld.contains(gradoListNewGrado)) {
                    NivelEstudio oldNivelEstudioIdOfGradoListNewGrado = gradoListNewGrado.getNivelEstudioId();
                    gradoListNewGrado.setNivelEstudioId(nivelEstudio);
                    gradoListNewGrado = em.merge(gradoListNewGrado);
                    if (oldNivelEstudioIdOfGradoListNewGrado != null && !oldNivelEstudioIdOfGradoListNewGrado.equals(nivelEstudio)) {
                        oldNivelEstudioIdOfGradoListNewGrado.getGradoList().remove(gradoListNewGrado);
                        oldNivelEstudioIdOfGradoListNewGrado = em.merge(oldNivelEstudioIdOfGradoListNewGrado);
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
                Integer id = nivelEstudio.getNivelEstudioId();
                if (findNivelEstudio(id) == null) {
                    throw new NonexistentEntityException("The nivelEstudio with id " + id + " no longer exists.");
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
            NivelEstudio nivelEstudio;
            try {
                nivelEstudio = em.getReference(NivelEstudio.class, id);
                nivelEstudio.getNivelEstudioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivelEstudio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Grado> gradoListOrphanCheck = nivelEstudio.getGradoList();
            for (Grado gradoListOrphanCheckGrado : gradoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NivelEstudio (" + nivelEstudio + ") cannot be destroyed since the Grado " + gradoListOrphanCheckGrado + " in its gradoList field has a non-nullable nivelEstudioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(nivelEstudio);
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

    public List<NivelEstudio> findNivelEstudioEntities() {
        return findNivelEstudioEntities(true, -1, -1);
    }

    public List<NivelEstudio> findNivelEstudioEntities(int maxResults, int firstResult) {
        return findNivelEstudioEntities(false, maxResults, firstResult);
    }

    private List<NivelEstudio> findNivelEstudioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NivelEstudio.class));
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

    public NivelEstudio findNivelEstudio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NivelEstudio.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelEstudioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NivelEstudio> rt = cq.from(NivelEstudio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
