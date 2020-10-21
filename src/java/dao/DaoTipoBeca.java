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
import entidades.Matricula;
import entidades.TipoBeca;
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
public class DaoTipoBeca implements Serializable {

    public DaoTipoBeca(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoBeca tipoBeca) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (tipoBeca.getMatriculaList() == null) {
            tipoBeca.setMatriculaList(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : tipoBeca.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            tipoBeca.setMatriculaList(attachedMatriculaList);
            em.persist(tipoBeca);
            for (Matricula matriculaListMatricula : tipoBeca.getMatriculaList()) {
                TipoBeca oldTipoBecaOfMatriculaListMatricula = matriculaListMatricula.getTipoBeca();
                matriculaListMatricula.setTipoBeca(tipoBeca);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldTipoBecaOfMatriculaListMatricula != null) {
                    oldTipoBecaOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldTipoBecaOfMatriculaListMatricula = em.merge(oldTipoBecaOfMatriculaListMatricula);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoBeca(tipoBeca.getTipoBecaId()) != null) {
                throw new PreexistingEntityException("TipoBeca " + tipoBeca + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoBeca tipoBeca) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoBeca persistentTipoBeca = em.find(TipoBeca.class, tipoBeca.getTipoBecaId());
            List<Matricula> matriculaListOld = persistentTipoBeca.getMatriculaList();
            List<Matricula> matriculaListNew = tipoBeca.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its tipoBeca field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            tipoBeca.setMatriculaList(matriculaListNew);
            tipoBeca = em.merge(tipoBeca);
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    TipoBeca oldTipoBecaOfMatriculaListNewMatricula = matriculaListNewMatricula.getTipoBeca();
                    matriculaListNewMatricula.setTipoBeca(tipoBeca);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldTipoBecaOfMatriculaListNewMatricula != null && !oldTipoBecaOfMatriculaListNewMatricula.equals(tipoBeca)) {
                        oldTipoBecaOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldTipoBecaOfMatriculaListNewMatricula = em.merge(oldTipoBecaOfMatriculaListNewMatricula);
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
                Integer id = tipoBeca.getTipoBecaId();
                if (findTipoBeca(id) == null) {
                    throw new NonexistentEntityException("The tipoBeca with id " + id + " no longer exists.");
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
            TipoBeca tipoBeca;
            try {
                tipoBeca = em.getReference(TipoBeca.class, id);
                tipoBeca.getTipoBecaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoBeca with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matricula> matriculaListOrphanCheck = tipoBeca.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoBeca (" + tipoBeca + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable tipoBeca field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoBeca);
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

    public List<TipoBeca> findTipoBecaEntities() {
        return findTipoBecaEntities(true, -1, -1);
    }

    public List<TipoBeca> findTipoBecaEntities(int maxResults, int firstResult) {
        return findTipoBecaEntities(false, maxResults, firstResult);
    }

    private List<TipoBeca> findTipoBecaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoBeca.class));
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

    public TipoBeca findTipoBeca(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoBeca.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoBecaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoBeca> rt = cq.from(TipoBeca.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
