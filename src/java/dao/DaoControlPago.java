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
import entidades.Arancel;
import entidades.ControlPago;
import entidades.EstadoControlPago;
import entidades.Matricula;
import entidades.Usuario;
import entidades.DetalleAsignacionCuota;
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
public class DaoControlPago implements Serializable {

    public DaoControlPago(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ControlPago controlPago) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (controlPago.getDetalleAsignacionCuotaList() == null) {
            controlPago.setDetalleAsignacionCuotaList(new ArrayList<DetalleAsignacionCuota>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Arancel arancelId = controlPago.getArancelId();
            if (arancelId != null) {
                arancelId = em.getReference(arancelId.getClass(), arancelId.getArancelId());
                controlPago.setArancelId(arancelId);
            }
            EstadoControlPago estadoControlPagoId = controlPago.getEstadoControlPagoId();
            if (estadoControlPagoId != null) {
                estadoControlPagoId = em.getReference(estadoControlPagoId.getClass(), estadoControlPagoId.getEstadoControlPagoId());
                controlPago.setEstadoControlPagoId(estadoControlPagoId);
            }
            Matricula matriculaId = controlPago.getMatriculaId();
            if (matriculaId != null) {
                matriculaId = em.getReference(matriculaId.getClass(), matriculaId.getMatriculaId());
                controlPago.setMatriculaId(matriculaId);
            }
            Usuario controlPagoUsuarioId = controlPago.getControlPagoUsuarioId();
            if (controlPagoUsuarioId != null) {
                controlPagoUsuarioId = em.getReference(controlPagoUsuarioId.getClass(), controlPagoUsuarioId.getUsuarioId());
                controlPago.setControlPagoUsuarioId(controlPagoUsuarioId);
            }
            List<DetalleAsignacionCuota> attachedDetalleAsignacionCuotaList = new ArrayList<DetalleAsignacionCuota>();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach : controlPago.getDetalleAsignacionCuotaList()) {
                detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach = em.getReference(detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach.getClass(), detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach.getDetalleAsginacionCuotaId());
                attachedDetalleAsignacionCuotaList.add(detalleAsignacionCuotaListDetalleAsignacionCuotaToAttach);
            }
            controlPago.setDetalleAsignacionCuotaList(attachedDetalleAsignacionCuotaList);
            em.persist(controlPago);
            if (arancelId != null) {
                arancelId.getControlPagoList().add(controlPago);
                arancelId = em.merge(arancelId);
            }
            if (estadoControlPagoId != null) {
                estadoControlPagoId.getControlPagoList().add(controlPago);
                estadoControlPagoId = em.merge(estadoControlPagoId);
            }
            if (matriculaId != null) {
                matriculaId.getControlPagoList().add(controlPago);
                matriculaId = em.merge(matriculaId);
            }
            if (controlPagoUsuarioId != null) {
                controlPagoUsuarioId.getControlPagoList().add(controlPago);
                controlPagoUsuarioId = em.merge(controlPagoUsuarioId);
            }
            for (DetalleAsignacionCuota detalleAsignacionCuotaListDetalleAsignacionCuota : controlPago.getDetalleAsignacionCuotaList()) {
                ControlPago oldControlPagoIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota = detalleAsignacionCuotaListDetalleAsignacionCuota.getControlPagoId();
                detalleAsignacionCuotaListDetalleAsignacionCuota.setControlPagoId(controlPago);
                detalleAsignacionCuotaListDetalleAsignacionCuota = em.merge(detalleAsignacionCuotaListDetalleAsignacionCuota);
                if (oldControlPagoIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota != null) {
                    oldControlPagoIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuotaListDetalleAsignacionCuota);
                    oldControlPagoIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota = em.merge(oldControlPagoIdOfDetalleAsignacionCuotaListDetalleAsignacionCuota);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findControlPago(controlPago.getControlPagoId()) != null) {
                throw new PreexistingEntityException("ControlPago " + controlPago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ControlPago controlPago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ControlPago persistentControlPago = em.find(ControlPago.class, controlPago.getControlPagoId());
            Arancel arancelIdOld = persistentControlPago.getArancelId();
            Arancel arancelIdNew = controlPago.getArancelId();
            EstadoControlPago estadoControlPagoIdOld = persistentControlPago.getEstadoControlPagoId();
            EstadoControlPago estadoControlPagoIdNew = controlPago.getEstadoControlPagoId();
            Matricula matriculaIdOld = persistentControlPago.getMatriculaId();
            Matricula matriculaIdNew = controlPago.getMatriculaId();
            Usuario controlPagoUsuarioIdOld = persistentControlPago.getControlPagoUsuarioId();
            Usuario controlPagoUsuarioIdNew = controlPago.getControlPagoUsuarioId();
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListOld = persistentControlPago.getDetalleAsignacionCuotaList();
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListNew = controlPago.getDetalleAsignacionCuotaList();
            List<String> illegalOrphanMessages = null;
            for (DetalleAsignacionCuota detalleAsignacionCuotaListOldDetalleAsignacionCuota : detalleAsignacionCuotaListOld) {
                if (!detalleAsignacionCuotaListNew.contains(detalleAsignacionCuotaListOldDetalleAsignacionCuota)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DetalleAsignacionCuota " + detalleAsignacionCuotaListOldDetalleAsignacionCuota + " since its controlPagoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (arancelIdNew != null) {
                arancelIdNew = em.getReference(arancelIdNew.getClass(), arancelIdNew.getArancelId());
                controlPago.setArancelId(arancelIdNew);
            }
            if (estadoControlPagoIdNew != null) {
                estadoControlPagoIdNew = em.getReference(estadoControlPagoIdNew.getClass(), estadoControlPagoIdNew.getEstadoControlPagoId());
                controlPago.setEstadoControlPagoId(estadoControlPagoIdNew);
            }
            if (matriculaIdNew != null) {
                matriculaIdNew = em.getReference(matriculaIdNew.getClass(), matriculaIdNew.getMatriculaId());
                controlPago.setMatriculaId(matriculaIdNew);
            }
            if (controlPagoUsuarioIdNew != null) {
                controlPagoUsuarioIdNew = em.getReference(controlPagoUsuarioIdNew.getClass(), controlPagoUsuarioIdNew.getUsuarioId());
                controlPago.setControlPagoUsuarioId(controlPagoUsuarioIdNew);
            }
            List<DetalleAsignacionCuota> attachedDetalleAsignacionCuotaListNew = new ArrayList<DetalleAsignacionCuota>();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach : detalleAsignacionCuotaListNew) {
                detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach = em.getReference(detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach.getClass(), detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach.getDetalleAsginacionCuotaId());
                attachedDetalleAsignacionCuotaListNew.add(detalleAsignacionCuotaListNewDetalleAsignacionCuotaToAttach);
            }
            detalleAsignacionCuotaListNew = attachedDetalleAsignacionCuotaListNew;
            controlPago.setDetalleAsignacionCuotaList(detalleAsignacionCuotaListNew);
            controlPago = em.merge(controlPago);
            if (arancelIdOld != null && !arancelIdOld.equals(arancelIdNew)) {
                arancelIdOld.getControlPagoList().remove(controlPago);
                arancelIdOld = em.merge(arancelIdOld);
            }
            if (arancelIdNew != null && !arancelIdNew.equals(arancelIdOld)) {
                arancelIdNew.getControlPagoList().add(controlPago);
                arancelIdNew = em.merge(arancelIdNew);
            }
            if (estadoControlPagoIdOld != null && !estadoControlPagoIdOld.equals(estadoControlPagoIdNew)) {
                estadoControlPagoIdOld.getControlPagoList().remove(controlPago);
                estadoControlPagoIdOld = em.merge(estadoControlPagoIdOld);
            }
            if (estadoControlPagoIdNew != null && !estadoControlPagoIdNew.equals(estadoControlPagoIdOld)) {
                estadoControlPagoIdNew.getControlPagoList().add(controlPago);
                estadoControlPagoIdNew = em.merge(estadoControlPagoIdNew);
            }
            if (matriculaIdOld != null && !matriculaIdOld.equals(matriculaIdNew)) {
                matriculaIdOld.getControlPagoList().remove(controlPago);
                matriculaIdOld = em.merge(matriculaIdOld);
            }
            if (matriculaIdNew != null && !matriculaIdNew.equals(matriculaIdOld)) {
                matriculaIdNew.getControlPagoList().add(controlPago);
                matriculaIdNew = em.merge(matriculaIdNew);
            }
            if (controlPagoUsuarioIdOld != null && !controlPagoUsuarioIdOld.equals(controlPagoUsuarioIdNew)) {
                controlPagoUsuarioIdOld.getControlPagoList().remove(controlPago);
                controlPagoUsuarioIdOld = em.merge(controlPagoUsuarioIdOld);
            }
            if (controlPagoUsuarioIdNew != null && !controlPagoUsuarioIdNew.equals(controlPagoUsuarioIdOld)) {
                controlPagoUsuarioIdNew.getControlPagoList().add(controlPago);
                controlPagoUsuarioIdNew = em.merge(controlPagoUsuarioIdNew);
            }
            for (DetalleAsignacionCuota detalleAsignacionCuotaListNewDetalleAsignacionCuota : detalleAsignacionCuotaListNew) {
                if (!detalleAsignacionCuotaListOld.contains(detalleAsignacionCuotaListNewDetalleAsignacionCuota)) {
                    ControlPago oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota = detalleAsignacionCuotaListNewDetalleAsignacionCuota.getControlPagoId();
                    detalleAsignacionCuotaListNewDetalleAsignacionCuota.setControlPagoId(controlPago);
                    detalleAsignacionCuotaListNewDetalleAsignacionCuota = em.merge(detalleAsignacionCuotaListNewDetalleAsignacionCuota);
                    if (oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota != null && !oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota.equals(controlPago)) {
                        oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota.getDetalleAsignacionCuotaList().remove(detalleAsignacionCuotaListNewDetalleAsignacionCuota);
                        oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota = em.merge(oldControlPagoIdOfDetalleAsignacionCuotaListNewDetalleAsignacionCuota);
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
                Integer id = controlPago.getControlPagoId();
                if (findControlPago(id) == null) {
                    throw new NonexistentEntityException("The controlPago with id " + id + " no longer exists.");
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
            ControlPago controlPago;
            try {
                controlPago = em.getReference(ControlPago.class, id);
                controlPago.getControlPagoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The controlPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DetalleAsignacionCuota> detalleAsignacionCuotaListOrphanCheck = controlPago.getDetalleAsignacionCuotaList();
            for (DetalleAsignacionCuota detalleAsignacionCuotaListOrphanCheckDetalleAsignacionCuota : detalleAsignacionCuotaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ControlPago (" + controlPago + ") cannot be destroyed since the DetalleAsignacionCuota " + detalleAsignacionCuotaListOrphanCheckDetalleAsignacionCuota + " in its detalleAsignacionCuotaList field has a non-nullable controlPagoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Arancel arancelId = controlPago.getArancelId();
            if (arancelId != null) {
                arancelId.getControlPagoList().remove(controlPago);
                arancelId = em.merge(arancelId);
            }
            EstadoControlPago estadoControlPagoId = controlPago.getEstadoControlPagoId();
            if (estadoControlPagoId != null) {
                estadoControlPagoId.getControlPagoList().remove(controlPago);
                estadoControlPagoId = em.merge(estadoControlPagoId);
            }
            Matricula matriculaId = controlPago.getMatriculaId();
            if (matriculaId != null) {
                matriculaId.getControlPagoList().remove(controlPago);
                matriculaId = em.merge(matriculaId);
            }
            Usuario controlPagoUsuarioId = controlPago.getControlPagoUsuarioId();
            if (controlPagoUsuarioId != null) {
                controlPagoUsuarioId.getControlPagoList().remove(controlPago);
                controlPagoUsuarioId = em.merge(controlPagoUsuarioId);
            }
            em.remove(controlPago);
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

    public List<ControlPago> findControlPagoEntities() {
        return findControlPagoEntities(true, -1, -1);
    }

    public List<ControlPago> findControlPagoEntities(int maxResults, int firstResult) {
        return findControlPagoEntities(false, maxResults, firstResult);
    }

    private List<ControlPago> findControlPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ControlPago.class));
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

    public ControlPago findControlPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ControlPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getControlPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ControlPago> rt = cq.from(ControlPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
