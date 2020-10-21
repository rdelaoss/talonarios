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
import entidades.Estudiante;
import entidades.TipoBeca;
import entidades.ControlPago;
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
public class DaoMatricula implements Serializable {

    public DaoMatricula(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("TalonariosPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matricula matricula) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (matricula.getControlPagoList() == null) {
            matricula.setControlPagoList(new ArrayList<ControlPago>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ConfiguracionGrado configuracionGradoId = matricula.getConfiguracionGradoId();
            if (configuracionGradoId != null) {
                configuracionGradoId = em.getReference(configuracionGradoId.getClass(), configuracionGradoId.getConfiguracionGradoId());
                matricula.setConfiguracionGradoId(configuracionGradoId);
            }
            Estudiante estudianteId = matricula.getEstudianteId();
            if (estudianteId != null) {
                estudianteId = em.getReference(estudianteId.getClass(), estudianteId.getEstudianteId());
                matricula.setEstudianteId(estudianteId);
            }
            TipoBeca tipoBeca = matricula.getTipoBeca();
            if (tipoBeca != null) {
                tipoBeca = em.getReference(tipoBeca.getClass(), tipoBeca.getTipoBecaId());
                matricula.setTipoBeca(tipoBeca);
            }
            List<ControlPago> attachedControlPagoList = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListControlPagoToAttach : matricula.getControlPagoList()) {
                controlPagoListControlPagoToAttach = em.getReference(controlPagoListControlPagoToAttach.getClass(), controlPagoListControlPagoToAttach.getControlPagoId());
                attachedControlPagoList.add(controlPagoListControlPagoToAttach);
            }
            matricula.setControlPagoList(attachedControlPagoList);
            em.persist(matricula);
            if (configuracionGradoId != null) {
                configuracionGradoId.getMatriculaList().add(matricula);
                configuracionGradoId = em.merge(configuracionGradoId);
            }
            if (estudianteId != null) {
                estudianteId.getMatriculaList().add(matricula);
                estudianteId = em.merge(estudianteId);
            }
            if (tipoBeca != null) {
                tipoBeca.getMatriculaList().add(matricula);
                tipoBeca = em.merge(tipoBeca);
            }
            for (ControlPago controlPagoListControlPago : matricula.getControlPagoList()) {
                Matricula oldMatriculaIdOfControlPagoListControlPago = controlPagoListControlPago.getMatriculaId();
                controlPagoListControlPago.setMatriculaId(matricula);
                controlPagoListControlPago = em.merge(controlPagoListControlPago);
                if (oldMatriculaIdOfControlPagoListControlPago != null) {
                    oldMatriculaIdOfControlPagoListControlPago.getControlPagoList().remove(controlPagoListControlPago);
                    oldMatriculaIdOfControlPagoListControlPago = em.merge(oldMatriculaIdOfControlPagoListControlPago);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMatricula(matricula.getMatriculaId()) != null) {
                throw new PreexistingEntityException("Matricula " + matricula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matricula matricula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getMatriculaId());
            ConfiguracionGrado configuracionGradoIdOld = persistentMatricula.getConfiguracionGradoId();
            ConfiguracionGrado configuracionGradoIdNew = matricula.getConfiguracionGradoId();
            Estudiante estudianteIdOld = persistentMatricula.getEstudianteId();
            Estudiante estudianteIdNew = matricula.getEstudianteId();
            TipoBeca tipoBecaOld = persistentMatricula.getTipoBeca();
            TipoBeca tipoBecaNew = matricula.getTipoBeca();
            List<ControlPago> controlPagoListOld = persistentMatricula.getControlPagoList();
            List<ControlPago> controlPagoListNew = matricula.getControlPagoList();
            List<String> illegalOrphanMessages = null;
            for (ControlPago controlPagoListOldControlPago : controlPagoListOld) {
                if (!controlPagoListNew.contains(controlPagoListOldControlPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ControlPago " + controlPagoListOldControlPago + " since its matriculaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (configuracionGradoIdNew != null) {
                configuracionGradoIdNew = em.getReference(configuracionGradoIdNew.getClass(), configuracionGradoIdNew.getConfiguracionGradoId());
                matricula.setConfiguracionGradoId(configuracionGradoIdNew);
            }
            if (estudianteIdNew != null) {
                estudianteIdNew = em.getReference(estudianteIdNew.getClass(), estudianteIdNew.getEstudianteId());
                matricula.setEstudianteId(estudianteIdNew);
            }
            if (tipoBecaNew != null) {
                tipoBecaNew = em.getReference(tipoBecaNew.getClass(), tipoBecaNew.getTipoBecaId());
                matricula.setTipoBeca(tipoBecaNew);
            }
            List<ControlPago> attachedControlPagoListNew = new ArrayList<ControlPago>();
            for (ControlPago controlPagoListNewControlPagoToAttach : controlPagoListNew) {
                controlPagoListNewControlPagoToAttach = em.getReference(controlPagoListNewControlPagoToAttach.getClass(), controlPagoListNewControlPagoToAttach.getControlPagoId());
                attachedControlPagoListNew.add(controlPagoListNewControlPagoToAttach);
            }
            controlPagoListNew = attachedControlPagoListNew;
            matricula.setControlPagoList(controlPagoListNew);
            matricula = em.merge(matricula);
            if (configuracionGradoIdOld != null && !configuracionGradoIdOld.equals(configuracionGradoIdNew)) {
                configuracionGradoIdOld.getMatriculaList().remove(matricula);
                configuracionGradoIdOld = em.merge(configuracionGradoIdOld);
            }
            if (configuracionGradoIdNew != null && !configuracionGradoIdNew.equals(configuracionGradoIdOld)) {
                configuracionGradoIdNew.getMatriculaList().add(matricula);
                configuracionGradoIdNew = em.merge(configuracionGradoIdNew);
            }
            if (estudianteIdOld != null && !estudianteIdOld.equals(estudianteIdNew)) {
                estudianteIdOld.getMatriculaList().remove(matricula);
                estudianteIdOld = em.merge(estudianteIdOld);
            }
            if (estudianteIdNew != null && !estudianteIdNew.equals(estudianteIdOld)) {
                estudianteIdNew.getMatriculaList().add(matricula);
                estudianteIdNew = em.merge(estudianteIdNew);
            }
            if (tipoBecaOld != null && !tipoBecaOld.equals(tipoBecaNew)) {
                tipoBecaOld.getMatriculaList().remove(matricula);
                tipoBecaOld = em.merge(tipoBecaOld);
            }
            if (tipoBecaNew != null && !tipoBecaNew.equals(tipoBecaOld)) {
                tipoBecaNew.getMatriculaList().add(matricula);
                tipoBecaNew = em.merge(tipoBecaNew);
            }
            for (ControlPago controlPagoListNewControlPago : controlPagoListNew) {
                if (!controlPagoListOld.contains(controlPagoListNewControlPago)) {
                    Matricula oldMatriculaIdOfControlPagoListNewControlPago = controlPagoListNewControlPago.getMatriculaId();
                    controlPagoListNewControlPago.setMatriculaId(matricula);
                    controlPagoListNewControlPago = em.merge(controlPagoListNewControlPago);
                    if (oldMatriculaIdOfControlPagoListNewControlPago != null && !oldMatriculaIdOfControlPagoListNewControlPago.equals(matricula)) {
                        oldMatriculaIdOfControlPagoListNewControlPago.getControlPagoList().remove(controlPagoListNewControlPago);
                        oldMatriculaIdOfControlPagoListNewControlPago = em.merge(oldMatriculaIdOfControlPagoListNewControlPago);
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
                Integer id = matricula.getMatriculaId();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
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
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getMatriculaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ControlPago> controlPagoListOrphanCheck = matricula.getControlPagoList();
            for (ControlPago controlPagoListOrphanCheckControlPago : controlPagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matricula (" + matricula + ") cannot be destroyed since the ControlPago " + controlPagoListOrphanCheckControlPago + " in its controlPagoList field has a non-nullable matriculaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ConfiguracionGrado configuracionGradoId = matricula.getConfiguracionGradoId();
            if (configuracionGradoId != null) {
                configuracionGradoId.getMatriculaList().remove(matricula);
                configuracionGradoId = em.merge(configuracionGradoId);
            }
            Estudiante estudianteId = matricula.getEstudianteId();
            if (estudianteId != null) {
                estudianteId.getMatriculaList().remove(matricula);
                estudianteId = em.merge(estudianteId);
            }
            TipoBeca tipoBeca = matricula.getTipoBeca();
            if (tipoBeca != null) {
                tipoBeca.getMatriculaList().remove(matricula);
                tipoBeca = em.merge(tipoBeca);
            }
            em.remove(matricula);
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

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
