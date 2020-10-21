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
import entidades.Estudiante;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Persona;
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
public class DaoEstudiante implements Serializable {

    public DaoEstudiante(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiante estudiante) throws IllegalOrphanException, PreexistingEntityException, RollbackFailureException, Exception {
        if (estudiante.getMatriculaList() == null) {
            estudiante.setMatriculaList(new ArrayList<Matricula>());
        }
        List<String> illegalOrphanMessages = null;
        Persona personaIdOrphanCheck = estudiante.getPersonaId();
        if (personaIdOrphanCheck != null) {
            Estudiante oldEstudianteOfPersonaId = personaIdOrphanCheck.getEstudiante();
            if (oldEstudianteOfPersonaId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Persona " + personaIdOrphanCheck + " already has an item of type Estudiante whose personaId column cannot be null. Please make another selection for the personaId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona personaId = estudiante.getPersonaId();
            if (personaId != null) {
                personaId = em.getReference(personaId.getClass(), personaId.getPersonaId());
                estudiante.setPersonaId(personaId);
            }
            List<Matricula> attachedMatriculaList = new ArrayList<Matricula>();
            for (Matricula matriculaListMatriculaToAttach : estudiante.getMatriculaList()) {
                matriculaListMatriculaToAttach = em.getReference(matriculaListMatriculaToAttach.getClass(), matriculaListMatriculaToAttach.getMatriculaId());
                attachedMatriculaList.add(matriculaListMatriculaToAttach);
            }
            estudiante.setMatriculaList(attachedMatriculaList);
            em.persist(estudiante);
            if (personaId != null) {
                personaId.setEstudiante(estudiante);
                personaId = em.merge(personaId);
            }
            for (Matricula matriculaListMatricula : estudiante.getMatriculaList()) {
                Estudiante oldEstudianteIdOfMatriculaListMatricula = matriculaListMatricula.getEstudianteId();
                matriculaListMatricula.setEstudianteId(estudiante);
                matriculaListMatricula = em.merge(matriculaListMatricula);
                if (oldEstudianteIdOfMatriculaListMatricula != null) {
                    oldEstudianteIdOfMatriculaListMatricula.getMatriculaList().remove(matriculaListMatricula);
                    oldEstudianteIdOfMatriculaListMatricula = em.merge(oldEstudianteIdOfMatriculaListMatricula);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstudiante(estudiante.getEstudianteId()) != null) {
                throw new PreexistingEntityException("Estudiante " + estudiante + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudiante estudiante) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estudiante persistentEstudiante = em.find(Estudiante.class, estudiante.getEstudianteId());
            Persona personaIdOld = persistentEstudiante.getPersonaId();
            Persona personaIdNew = estudiante.getPersonaId();
            List<Matricula> matriculaListOld = persistentEstudiante.getMatriculaList();
            List<Matricula> matriculaListNew = estudiante.getMatriculaList();
            List<String> illegalOrphanMessages = null;
            if (personaIdNew != null && !personaIdNew.equals(personaIdOld)) {
                Estudiante oldEstudianteOfPersonaId = personaIdNew.getEstudiante();
                if (oldEstudianteOfPersonaId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Persona " + personaIdNew + " already has an item of type Estudiante whose personaId column cannot be null. Please make another selection for the personaId field.");
                }
            }
            for (Matricula matriculaListOldMatricula : matriculaListOld) {
                if (!matriculaListNew.contains(matriculaListOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaListOldMatricula + " since its estudianteId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personaIdNew != null) {
                personaIdNew = em.getReference(personaIdNew.getClass(), personaIdNew.getPersonaId());
                estudiante.setPersonaId(personaIdNew);
            }
            List<Matricula> attachedMatriculaListNew = new ArrayList<Matricula>();
            for (Matricula matriculaListNewMatriculaToAttach : matriculaListNew) {
                matriculaListNewMatriculaToAttach = em.getReference(matriculaListNewMatriculaToAttach.getClass(), matriculaListNewMatriculaToAttach.getMatriculaId());
                attachedMatriculaListNew.add(matriculaListNewMatriculaToAttach);
            }
            matriculaListNew = attachedMatriculaListNew;
            estudiante.setMatriculaList(matriculaListNew);
            estudiante = em.merge(estudiante);
            if (personaIdOld != null && !personaIdOld.equals(personaIdNew)) {
                personaIdOld.setEstudiante(null);
                personaIdOld = em.merge(personaIdOld);
            }
            if (personaIdNew != null && !personaIdNew.equals(personaIdOld)) {
                personaIdNew.setEstudiante(estudiante);
                personaIdNew = em.merge(personaIdNew);
            }
            for (Matricula matriculaListNewMatricula : matriculaListNew) {
                if (!matriculaListOld.contains(matriculaListNewMatricula)) {
                    Estudiante oldEstudianteIdOfMatriculaListNewMatricula = matriculaListNewMatricula.getEstudianteId();
                    matriculaListNewMatricula.setEstudianteId(estudiante);
                    matriculaListNewMatricula = em.merge(matriculaListNewMatricula);
                    if (oldEstudianteIdOfMatriculaListNewMatricula != null && !oldEstudianteIdOfMatriculaListNewMatricula.equals(estudiante)) {
                        oldEstudianteIdOfMatriculaListNewMatricula.getMatriculaList().remove(matriculaListNewMatricula);
                        oldEstudianteIdOfMatriculaListNewMatricula = em.merge(oldEstudianteIdOfMatriculaListNewMatricula);
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
                Integer id = estudiante.getEstudianteId();
                if (findEstudiante(id) == null) {
                    throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.");
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
            Estudiante estudiante;
            try {
                estudiante = em.getReference(Estudiante.class, id);
                estudiante.getEstudianteId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Matricula> matriculaListOrphanCheck = estudiante.getMatriculaList();
            for (Matricula matriculaListOrphanCheckMatricula : matriculaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estudiante (" + estudiante + ") cannot be destroyed since the Matricula " + matriculaListOrphanCheckMatricula + " in its matriculaList field has a non-nullable estudianteId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona personaId = estudiante.getPersonaId();
            if (personaId != null) {
                personaId.setEstudiante(null);
                personaId = em.merge(personaId);
            }
            em.remove(estudiante);
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

    public List<Estudiante> findEstudianteEntities() {
        return findEstudianteEntities(true, -1, -1);
    }

    public List<Estudiante> findEstudianteEntities(int maxResults, int firstResult) {
        return findEstudianteEntities(false, maxResults, firstResult);
    }

    private List<Estudiante> findEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiante.class));
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

    public Estudiante findEstudiante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiante> rt = cq.from(Estudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
