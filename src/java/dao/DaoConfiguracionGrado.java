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
import entidades.AnioLectivo;
import entidades.ConfiguracionGrado;
import entidades.EspecialidadGrado;
import entidades.Grado;
import entidades.Matricula;
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
public class DaoConfiguracionGrado implements Serializable {

    public DaoConfiguracionGrado(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConfiguracionGrado configuracionGrado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (configuracionGrado.getMatriculaList() == null) {
            configuracionGrado.setMatriculaList(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            AnioLectivo anioLectivoId = configuracionGrado.getAnioLectivoId();
            if (anioLectivoId != null) {
                anioLectivoId = em.getReference(anioLectivoId.getClass(), anioLectivoId.getAnioLectivoId());
                configuracionGrado.setAnioLectivoId(anioLectivoId);
            }
            EspecialidadGrado especialidadGradoId = configuracionGrado.getEspecialidadGradoId();
            if (especialidadGradoId != null) {
                especialidadGradoId = em.getReference(especialidadGradoId.getClass(), especialidadGradoId.getEspecialidadGradoId());
                configuracionGrado.setEspecialidadGradoId(especialidadGradoId);
            }
            Grado gradoId = configuracionGrado.getGradoId();
            if (gradoId != null) {
                gradoId = em.getReference(gradoId.getClass(), gradoId.getGradoId());
                configuracionGrado.setGradoId(gradoId);
            }
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : configuracionGrado.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            configuracionGrado.setMatriculaList(attachedMatriculaList);
            em.persist(configuracionGrado);
            if (anioLectivoId != null) {
                anioLectivoId.getConfiguracionGradoList().add(configuracionGrado);
                anioLectivoId = em.merge(anioLectivoId);
            }
            if (especialidadGradoId != null) {
                especialidadGradoId.getConfiguracionGradoList().add(configuracionGrado);
                especialidadGradoId = em.merge(especialidadGradoId);
            }
            if (gradoId != null) {
                gradoId.getConfiguracionGradoList().add(configuracionGrado);
                gradoId = em.merge(gradoId);
            }
            for (Matricula matriculaListMatricula : configuracionGrado.getMatriculaList()) {
                ConfiguracionGrado oldConfiguracionGradoIdOfMatriculaListMatricula = matriculaListMatricula.getConfiguracionGradoId();
                matriculaListMatricula.setConfiguracionGradoId(configuracionGrado);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldConfiguracionGradoIdOfMatriculaListMatricula != null) {
                    oldConfiguracionGradoIdOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldConfiguracionGradoIdOfMatriculaListMatricula = em.merge(oldConfiguracionGradoIdOfMatriculaListMatricula);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findConfiguracionGrado(configuracionGrado.getConfiguracionGradoId()) != null) {
                throw new PreexistingEntityException("ConfiguracionGrado " + configuracionGrado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConfiguracionGrado configuracionGrado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConfiguracionGrado persistentConfiguracionGrado = em.find(ConfiguracionGrado.class, configuracionGrado.getConfiguracionGradoId());
            AnioLectivo anioLectivoIdOld = persistentConfiguracionGrado.getAnioLectivoId();
            AnioLectivo anioLectivoIdNew = configuracionGrado.getAnioLectivoId();
            EspecialidadGrado especialidadGradoIdOld = persistentConfiguracionGrado.getEspecialidadGradoId();
            EspecialidadGrado especialidadGradoIdNew = configuracionGrado.getEspecialidadGradoId();
            Grado gradoIdOld = persistentConfiguracionGrado.getGradoId();
            Grado gradoIdNew = configuracionGrado.getGradoId();
            List<Matricula> matriculaListOld = persistentConfiguracionGrado.getMatriculaList();
            List<Matricula> matriculaListNew = configuracionGrado.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its configuracionGradoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (anioLectivoIdNew != null) {
                anioLectivoIdNew = em.getReference(anioLectivoIdNew.getClass(), anioLectivoIdNew.getAnioLectivoId());
                configuracionGrado.setAnioLectivoId(anioLectivoIdNew);
            }
            if (especialidadGradoIdNew != null) {
                especialidadGradoIdNew = em.getReference(especialidadGradoIdNew.getClass(), especialidadGradoIdNew.getEspecialidadGradoId());
                configuracionGrado.setEspecialidadGradoId(especialidadGradoIdNew);
            }
            if (gradoIdNew != null) {
                gradoIdNew = em.getReference(gradoIdNew.getClass(), gradoIdNew.getGradoId());
                configuracionGrado.setGradoId(gradoIdNew);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            configuracionGrado.setMatriculaList(matriculaListNew);
            configuracionGrado = em.merge(configuracionGrado);
            if (anioLectivoIdOld != null && !anioLectivoIdOld.equals(anioLectivoIdNew)) {
                anioLectivoIdOld.getConfiguracionGradoList().remove(configuracionGrado);
                anioLectivoIdOld = em.merge(anioLectivoIdOld);
            }
            if (anioLectivoIdNew != null && !anioLectivoIdNew.equals(anioLectivoIdOld)) {
                anioLectivoIdNew.getConfiguracionGradoList().add(configuracionGrado);
                anioLectivoIdNew = em.merge(anioLectivoIdNew);
            }
            if (especialidadGradoIdOld != null && !especialidadGradoIdOld.equals(especialidadGradoIdNew)) {
                especialidadGradoIdOld.getConfiguracionGradoList().remove(configuracionGrado);
                especialidadGradoIdOld = em.merge(especialidadGradoIdOld);
            }
            if (especialidadGradoIdNew != null && !especialidadGradoIdNew.equals(especialidadGradoIdOld)) {
                especialidadGradoIdNew.getConfiguracionGradoList().add(configuracionGrado);
                especialidadGradoIdNew = em.merge(especialidadGradoIdNew);
            }
            if (gradoIdOld != null && !gradoIdOld.equals(gradoIdNew)) {
                gradoIdOld.getConfiguracionGradoList().remove(configuracionGrado);
                gradoIdOld = em.merge(gradoIdOld);
            }
            if (gradoIdNew != null && !gradoIdNew.equals(gradoIdOld)) {
                gradoIdNew.getConfiguracionGradoList().add(configuracionGrado);
                gradoIdNew = em.merge(gradoIdNew);
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    ConfiguracionGrado oldConfiguracionGradoIdOfMatriculaListNewMatricula = matriculaListNewMatricula.getConfiguracionGradoId();
                    matriculaListNewMatricula.setConfiguracionGradoId(configuracionGrado);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldConfiguracionGradoIdOfMatriculaListNewMatricula != null && !oldConfiguracionGradoIdOfMatriculaListNewMatricula.equals(configuracionGrado)) {
                        oldConfiguracionGradoIdOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldConfiguracionGradoIdOfMatriculaListNewMatricula = em.merge(oldConfiguracionGradoIdOfMatriculaListNewMatricula);
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
                Integer id = configuracionGrado.getConfiguracionGradoId();
                if (findConfiguracionGrado(id) == null) {
                    throw new NonexistentEntityException("The configuracionGrado with id " + id + " no longer exists.");
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
            ConfiguracionGrado configuracionGrado;
            try {
                configuracionGrado = em.getReference(ConfiguracionGrado.class, id);
                configuracionGrado.getConfiguracionGradoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configuracionGrado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matricula> matriculaListOrphanCheck = configuracionGrado.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ConfiguracionGrado (" + configuracionGrado + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable configuracionGradoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AnioLectivo anioLectivoId = configuracionGrado.getAnioLectivoId();
            if (anioLectivoId != null) {
                anioLectivoId.getConfiguracionGradoList().remove(configuracionGrado);
                anioLectivoId = em.merge(anioLectivoId);
            }
            EspecialidadGrado especialidadGradoId = configuracionGrado.getEspecialidadGradoId();
            if (especialidadGradoId != null) {
                especialidadGradoId.getConfiguracionGradoList().remove(configuracionGrado);
                especialidadGradoId = em.merge(especialidadGradoId);
            }
            Grado gradoId = configuracionGrado.getGradoId();
            if (gradoId != null) {
                gradoId.getConfiguracionGradoList().remove(configuracionGrado);
                gradoId = em.merge(gradoId);
            }
            em.remove(configuracionGrado);
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

    public List<ConfiguracionGrado> findConfiguracionGradoEntities() {
        return findConfiguracionGradoEntities(true, -1, -1);
    }

    public List<ConfiguracionGrado> findConfiguracionGradoEntities(int maxResults, int firstResult) {
        return findConfiguracionGradoEntities(false, maxResults, firstResult);
    }

    private List<ConfiguracionGrado> findConfiguracionGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConfiguracionGrado.class));
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

    public ConfiguracionGrado findConfiguracionGrado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConfiguracionGrado.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfiguracionGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConfiguracionGrado> rt = cq.from(ConfiguracionGrado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
