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
import entidades.Persona;
import entidades.Rol;
import entidades.DetalleAsignacionCuota;
import java.util.ArrayList;
import java.util.List;
import entidades.ControlPago;
import entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author Administrador
 */
public class DaoUsuario implements Serializable {

    public DaoUsuario(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (usuario.getDetalleAsignacionCuotaList() == null) {
            usuario.setDetalleAsignacionCuotaList(new ArrayList<DetalleAsignacionCuota>());
        }
        if (usuario.getControlPagoList() == null) {
            usuario.setControlPagoList(new ArrayList<ControlPago>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Persona personaId = usuario.getPersonaId();
            if (personaId != null) {
                personaId = em.getReference(personaId.getClass(), personaId.getPersonaId());
                usuario.setPersonaId(personaId);
            }
            Rol rolId = usuario.getRolId();
            if (rolId != null) {
                rolId = em.getReference(rolId.getClass(), rolId.getRolId());
                usuario.setRolId(rolId);
            }
            List<DetalleAsignacionCuota> attachedDetalleAsignacionCuotaList = new ArrayList<DetalleAsignacionCuota>();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach : usuario.getDetalleAsignacionCuotaList()) {
                detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach = em.getReference(detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach.getClass(), detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach.getDetalleAsginacionCuotaId());
                attachedDetalleAsignacionCuotaList.add(detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach);
            }
            usuario.setDetalleAsignacionCuotaList(attachedDetalleAsignacionCuotaList);
            List<ControlPago> attachedControlPagoList = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListControlPagoToAttach : usuario.getControlPagoList()) {
                controlPagoListControlPagoToAttach = em.getReference(controlPagoListControlPagoToAttach.getClass(), controlPagoListControlPagoToAttach.getControlPagoId());
                attachedControlPagoList.add(controlPagoListControlPagoToAttach);
            }
            usuario.setControlPagoList(attachedControlPagoList);
            em.persist(usuario);
            if (personaId != null) {
                personaId.getUsuarioList().add(usuario);
                personaId = em.merge(personaId);
            }
            if (rolId != null) {
                rolId.getUsuarioList().add(usuario);
                rolId = em.merge(rolId);
            }
            for (DetalleAsignacionCuota detalleAsignacionCuotaListDetalleAsignacionCuota : usuario.getDetalleAsignacionCuotaList()) {
                Usuario oldUsuarioIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota = detalleAsignacionCuotaListDetalleAsignacionCuota.getUsuarioId();
                detalleAsignacionCuotaListDetalleAsignacionCuota.setUsuarioId(usuario);
                detalleAsignacionCuotaListDetalleAsignacionCuota = em.merge(detalleAsignacionCuotaListDetalleAsignacionCuota);
                if (oldUsuarioIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota != null) {
                    oldUsuarioIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuotaListDetalleAsignacionCuota);
                    oldUsuarioIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota = em.merge(oldUsuarioIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota);
                }
            }
            for (ControlPago controlPagoListControlPago : usuario.getControlPagoList()) {
                Usuario oldControlPagoUsuarioIdOfControlPagoListControlPago = controlPagoListControlPago.getControlPagoUsuarioId();
                controlPagoListControlPago.setControlPagoUsuarioId(usuario);
                controlPagoListControlPago = em.merge(controlPagoListControlPago);
                if (oldControlPagoUsuarioIdOfControlPagoListControlPago != null) {
                    oldControlPagoUsuarioIdOfControlPagoListControlPago.getControlPagoList().remove(controlPagoListControlPago);
                    oldControlPagoUsuarioIdOfControlPagoListControlPago = em.merge(oldControlPagoUsuarioIdOfControlPagoListControlPago);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUsuario(usuario.getUsuarioId()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUsuarioId());
            Persona personaIdOld = persistentUsuario.getPersonaId();
            Persona personaIdNew = usuario.getPersonaId();
            Rol rolIdOld = persistentUsuario.getRolId();
            Rol rolIdNew = usuario.getRolId();
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListOld = persistentUsuario.getDetalleAsignacionCuotaList();
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListNew = usuario.getDetalleAsignacionCuotaList();
            List<ControlPago> controlPagoListOld = persistentUsuario.getControlPagoList();
            List<ControlPago> controlPagoListNew = usuario.getControlPagoList();
            List<String> illegalOrphanMessages = null;
            for (DetalleAsignacionCuota detalleAsignacionCuotaListOldDetalleAsignacionCuota : detalleAsignacionCuotaListOld) {
                if (!detalleAsignacionCuotaListNew.contains(detalleAsignacionCuotaListOldDetalleAsignacionCuota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleAsignacionCuota " + detalleAsignacionCuotaListOldDetalleAsignacionCuota + " since its usuarioId field is not nullable.");
                }
            }
            for (ControlPago controlPagoListOldControlPago : controlPagoListOld) {
                if (!controlPagoListNew.contains(controlPagoListOldControlPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ControlPago " + controlPagoListOldControlPago + " since its controlPagoUsuarioId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (personaIdNew != null) {
                personaIdNew = em.getReference(personaIdNew.getClass(), personaIdNew.getPersonaId());
                usuario.setPersonaId(personaIdNew);
            }
            if (rolIdNew != null) {
                rolIdNew = em.getReference(rolIdNew.getClass(), rolIdNew.getRolId());
                usuario.setRolId(rolIdNew);
            }
            List<DetalleAsignacionCuota> attachedDetalleAsignacionCuotaListNew = new ArrayList<DetalleAsignacionCuota>();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach : detalleAsignacionCuotaListNew) {
                detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach = em.getReference(detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach.getClass(), detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach.getDetalleAsginacionCuotaId());
                attachedDetalleAsignacionCuotaListNew.add(detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach);
            }
            detalleAsignacionCuotaListNew = attachedDetalleAsignacionCuotaListNew;
            usuario.setDetalleAsignacionCuotaList(detalleAsignacionCuotaListNew);
            List<ControlPago> attachedControlPagoListNew = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListNewControlPagoToAttach : controlPagoListNew) {
                controlPagoListNewControlPagoToAttach = em.getReference(controlPagoListNewControlPagoToAttach.getClass(), controlPagoListNewControlPagoToAttach.getControlPagoId());
                attachedControlPagoListNew.add(controlPagoListNewControlPagoToAttach);
            }
            controlPagoListNew = attachedControlPagoListNew;
            usuario.setControlPagoList(controlPagoListNew);
            usuario = em.merge(usuario);
            if (personaIdOld != null && !personaIdOld.equals(personaIdNew)) {
                personaIdOld.getUsuarioList().remove(usuario);
                personaIdOld = em.merge(personaIdOld);
            }
            if (personaIdNew != null && !personaIdNew.equals(personaIdOld)) {
                personaIdNew.getUsuarioList().add(usuario);
                personaIdNew = em.merge(personaIdNew);
            }
            if (rolIdOld != null && !rolIdOld.equals(rolIdNew)) {
                rolIdOld.getUsuarioList().remove(usuario);
                rolIdOld = em.merge(rolIdOld);
            }
            if (rolIdNew != null && !rolIdNew.equals(rolIdOld)) {
                rolIdNew.getUsuarioList().add(usuario);
                rolIdNew = em.merge(rolIdNew);
            }
            for (DetalleAsignacionCuota detalleAsignacionCuotaListNewDetalleAsignacionCuota : detalleAsignacionCuotaListNew) {
                if (!detalleAsignacionCuotaListOld.contains(detalleAsignacionCuotaListNewDetalleAsignacionCuota)) {
                    Usuario oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota = detalleAsignacionCuotaListNewDetalleAsignacionCuota.getUsuarioId();
                    detalleAsignacionCuotaListNewDetalleAsignacionCuota.setUsuarioId(usuario);
                    detalleAsignacionCuotaListNewDetalleAsignacionCuota = em.merge(detalleAsignacionCuotaListNewDetalleAsignacionCuota);
                    if (oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota != null && !oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota.equals(usuario)) {
                        oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuotaListNewDetalleAsignacionCuota);
                        oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota = em.merge(oldUsuarioIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota);
                    }
                }
            }
            for (ControlPago controlPagoListNewControlPago : controlPagoListNew) {
                if (!controlPagoListOld.contains(controlPagoListNewControlPago)) {
                    Usuario oldControlPagoUsuarioIdOfControlPagoListNewControlPago = controlPagoListNewControlPago.getControlPagoUsuarioId();
                    controlPagoListNewControlPago.setControlPagoUsuarioId(usuario);
                    controlPagoListNewControlPago = em.merge(controlPagoListNewControlPago);
                    if (oldControlPagoUsuarioIdOfControlPagoListNewControlPago != null && !oldControlPagoUsuarioIdOfControlPagoListNewControlPago.equals(usuario)) {
                        oldControlPagoUsuarioIdOfControlPagoListNewControlPago.getControlPagoList().remove(controlPagoListNewControlPago);
                        oldControlPagoUsuarioIdOfControlPagoListNewControlPago = em.merge(oldControlPagoUsuarioIdOfControlPagoListNewControlPago);
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
                Integer id = usuario.getUsuarioId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUsuarioId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListOrphanCheck = usuario.getDetalleAsignacionCuotaList();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListOrphanCheckDetalleAsignacionCuota : detalleAsignacionCuotaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the DetalleAsignacionCuota " + detalleAsignacionCuotaListOrphanCheckDetalleAsignacionCuota + " in its detalleAsignacionCuotaList field has a non-nullable usuarioId field.");
            }
            List<ControlPago> controlPagoListOrphanCheck = usuario.getControlPagoList();
            for (ControlPago controlPagoListOrphanCheckControlPago : controlPagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the ControlPago " + controlPagoListOrphanCheckControlPago + " in its controlPagoList field has a non-nullable controlPagoUsuarioId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Persona personaId = usuario.getPersonaId();
            if (personaId != null) {
                personaId.getUsuarioList().remove(usuario);
                personaId = em.merge(personaId);
            }
            Rol rolId = usuario.getRolId();
            if (rolId != null) {
                rolId.getUsuarioList().remove(usuario);
                rolId = em.merge(rolId);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
