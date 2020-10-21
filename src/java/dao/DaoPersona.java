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
import entidades.Genero;
import entidades.Estudiante;
import entidades.Persona;
import entidades.Usuario;
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
public class DaoPersona implements Serializable {

    public DaoPersona(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void agregar(Persona persona) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (persona.getUsuarioList() == null) {
            persona.setUsuarioList(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Genero generoId = persona.getGeneroId();
            if (generoId != null) {
                generoId = em.getReference(generoId.getClass(), generoId.getGeneroId());
                persona.setGeneroId(generoId);
            }
            Estudiante estudiante = persona.getEstudiante();
            if (estudiante != null) {
                estudiante = em.getReference(estudiante.getClass(), estudiante.getEstudianteId());
                persona.setEstudiante(estudiante);
            }
            List<Usuario> attachedUsuarioList = new ArrayList<Usuario>();
            for (Usuario usuarioListUsuarioToAttach : persona.getUsuarioList()) {
                usuarioListUsuarioToAttach = em.getReference(usuarioListUsuarioToAttach.getClass(), usuarioListUsuarioToAttach.getUsuarioId());
                attachedUsuarioList.add(usuarioListUsuarioToAttach);
            }
            persona.setUsuarioList(attachedUsuarioList);
            em.persist(persona);
            if (generoId != null) {
                generoId.getPersonaList().add(persona);
                generoId = em.merge(generoId);
            }
            if (estudiante != null) {
                Persona oldPersonaIdOfEstudiante = estudiante.getPersonaId();
                if (oldPersonaIdOfEstudiante != null) {
                    oldPersonaIdOfEstudiante.setEstudiante(null);
                    oldPersonaIdOfEstudiante = em.merge(oldPersonaIdOfEstudiante);
                }
                estudiante.setPersonaId(persona);
                estudiante = em.merge(estudiante);
            }
            for (Usuario usuarioListUsuario : persona.getUsuarioList()) {
                Persona oldPersonaIdOfUsuarioListUsuario = usuarioListUsuario.getPersonaId();
                usuarioListUsuario.setPersonaId(persona);
                usuarioListUsuario = em.merge(usuarioListUsuario);
                if (oldPersonaIdOfUsuarioListUsuario != null) {
                    oldPersonaIdOfUsuarioListUsuario.getUsuarioList().remove(usuarioListUsuario);
                    oldPersonaIdOfUsuarioListUsuario = em.merge(oldPersonaIdOfUsuarioListUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPersona(persona.getPersonaId()) != null) {
                throw new PreexistingEntityException("Persona " + persona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Persona persona) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona persistentPersona = em.find(Persona.class, persona.getPersonaId());
            Genero generoIdOld = persistentPersona.getGeneroId();
            Genero generoIdNew = persona.getGeneroId();
            Estudiante estudianteOld = persistentPersona.getEstudiante();
            Estudiante estudianteNew = persona.getEstudiante();
            List<Usuario> usuarioListOld = persistentPersona.getUsuarioList();
            List<Usuario> usuarioListNew = persona.getUsuarioList();
            List<String> illegalOrphanMessages = null;
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Estudiante " + estudianteOld + " since its personaId field is not nullable.");
            }
            for (Usuario usuarioListOldUsuario : usuarioListOld) {
                if (!usuarioListNew.contains(usuarioListOldUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuario " + usuarioListOldUsuario + " since its personaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (generoIdNew != null) {
                generoIdNew = em.getReference(generoIdNew.getClass(), generoIdNew.getGeneroId());
                persona.setGeneroId(generoIdNew);
            }
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getEstudianteId());
                persona.setEstudiante(estudianteNew);
            }
            List<Usuario> attachedUsuarioListNew = new ArrayList<Usuario>();
            for (Usuario usuarioListNewUsuarioToAttach : usuarioListNew) {
                usuarioListNewUsuarioToAttach = em.getReference(usuarioListNewUsuarioToAttach.getClass(), usuarioListNewUsuarioToAttach.getUsuarioId());
                attachedUsuarioListNew.add(usuarioListNewUsuarioToAttach);
            }
            usuarioListNew = attachedUsuarioListNew;
            persona.setUsuarioList(usuarioListNew);
            persona = em.merge(persona);
            if (generoIdOld != null && !generoIdOld.equals(generoIdNew)) {
                generoIdOld.getPersonaList().remove(persona);
                generoIdOld = em.merge(generoIdOld);
            }
            if (generoIdNew != null && !generoIdNew.equals(generoIdOld)) {
                generoIdNew.getPersonaList().add(persona);
                generoIdNew = em.merge(generoIdNew);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                Persona oldPersonaIdOfEstudiante = estudianteNew.getPersonaId();
                if (oldPersonaIdOfEstudiante != null) {
                    oldPersonaIdOfEstudiante.setEstudiante(null);
                    oldPersonaIdOfEstudiante = em.merge(oldPersonaIdOfEstudiante);
                }
                estudianteNew.setPersonaId(persona);
                estudianteNew = em.merge(estudianteNew);
            }
            for (Usuario usuarioListNewUsuario : usuarioListNew) {
                if (!usuarioListOld.contains(usuarioListNewUsuario)) {
                    Persona oldPersonaIdOfUsuarioListNewUsuario = usuarioListNewUsuario.getPersonaId();
                    usuarioListNewUsuario.setPersonaId(persona);
                    usuarioListNewUsuario = em.merge(usuarioListNewUsuario);
                    if (oldPersonaIdOfUsuarioListNewUsuario != null && !oldPersonaIdOfUsuarioListNewUsuario.equals(persona)) {
                        oldPersonaIdOfUsuarioListNewUsuario.getUsuarioList().remove(usuarioListNewUsuario);
                        oldPersonaIdOfUsuarioListNewUsuario = em.merge(oldPersonaIdOfUsuarioListNewUsuario);
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
                Integer id = persona.getPersonaId();
                if (findPersona(id) == null) {
                    throw new NonexistentEntityException("The persona with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void eliminar(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona persona;
            try {
                persona = em.getReference(Persona.class, id);
                persona.getPersonaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The persona with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Estudiante estudianteOrphanCheck = persona.getEstudiante();
            if (estudianteOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Estudiante " + estudianteOrphanCheck + " in its estudiante field has a non-nullable personaId field.");
            }
            List<Usuario> usuarioListOrphanCheck = persona.getUsuarioList();
            for (Usuario usuarioListOrphanCheckUsuario : usuarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Persona (" + persona + ") cannot be destroyed since the Usuario " + usuarioListOrphanCheckUsuario + " in its usuarioList field has a non-nullable personaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Genero generoId = persona.getGeneroId();
            if (generoId != null) {
                generoId.getPersonaList().remove(persona);
                generoId = em.merge(generoId);
            }
            em.remove(persona);
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

    public List<Persona> listarPersonas() {
        return findPersonaEntities(true, -1, -1);
    }

    public List<Persona> findPersonaEntities(int maxResults, int firstResult) {
        return findPersonaEntities(false, maxResults, firstResult);
    }

    private List<Persona> findPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Persona.class));
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

    public Persona findPersona(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Persona.class, id);
        } finally {
            em.close();
        }
    }

    public int getTotalPersonas() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Persona> rt = cq.from(Persona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
