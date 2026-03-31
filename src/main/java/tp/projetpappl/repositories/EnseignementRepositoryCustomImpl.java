/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Seance;
/**
 *
 * @author nathan
 */
@Repository
public class EnseignementRepositoryCustomImpl implements EnseignementRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    EnseignementRepository enseignementRepository;
    @Autowired
    @Lazy
    EnseignantRepository enseignantRepository;
    /**
     * récupération de toutes les matières
     * get all courses
     * @return 
     */
    @Override
    public List<String> findAllAcronyme() {
        String requete = "SELECT Acronyme FROM Enseignement";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
/**
 * récupère les matières dans lesquelles cet enseignant enseigne
 * get the courses where this teacher teach
 * @param acronyme
 * @return 
 */
    @Override
    public List<String> findAcronymeByEnseignant(String acronyme) {// Il y a un problème ici entre le Custom et le CustomImpl
        String requete = "SELECT acronyme FROM Enseigne e WHERE e.acronyme= :acronyme";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        query.setParameter("acronyme", acronyme);
        return query.getResultList();
    }
    /**
     * renvoie les matières suivies par ce groupe 
     * get the courses followed by this group
     * @param nomGroupe
     * @return 
     */
    @Override
    public List<String> findAcronymeByGroupe(String nomGroupe) {
        String requete = "SELECT c.acronyme FROM Contient c JOIN c.groupeList g WHERE g.nomGroupe= :nomGroupe";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        query.setParameter("nomGroupe", nomGroupe);
        return query.getResultList();
    }
    /**
     * renvoie la séance par son id
     * get seance by its id
     * @param acronyme
     * @return 
     */
    @Override
    public Enseignement getByAcronyme(String acronyme) {
        try {
            return entityManager
                    .createNamedQuery("Enseignement.findByAcronyme", Enseignement.class)
                    .setParameter("acronyme", acronyme)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    /**
     * met la matière à jour
     * update courses informations
     * @param acronyme
     * @param nom
     * @param filiere
     * @param responsable
     * @param Enseignants
     * @return 
     */
    @Override
    public Enseignement update(String acronyme, String nom, String filiere, Enseignant responsable, ArrayList<Enseignant> Enseignants){
        Enseignement enseignementAcronyme = null;
        
        for (int i = 0; i < Enseignants.size(); i++) {
            Enseignants.set(i, enseignantRepository.getByInitiales(Enseignants.get(i).getInitiales()));
        }
        if(acronyme != null){
            // Ensure validity from database
            enseignementAcronyme = getByAcronyme(acronyme);
            acronyme = enseignementAcronyme.getAcronyme();
        }
        if ((acronyme != null)
                && ((nom != null) && (! nom.isEmpty())
                && (filiere != null) && (! filiere.isEmpty())
                && (responsable != null))){
            // Update data
            enseignementAcronyme = getByAcronyme(acronyme);
            enseignementAcronyme.setFiliere(filiere);
            enseignementAcronyme.setNomEnseignement(nom);
            enseignementAcronyme.setEnseignantList(Enseignants);
            // Save to database
            responsable.getEnseignementList1().add(enseignementAcronyme);
            enseignantRepository.saveAndFlush(responsable);
            enseignementRepository.saveAndFlush(enseignementAcronyme);
            
            Optional<Enseignement> result = enseignementRepository.findById(enseignementAcronyme.getAcronyme());
            if(result.isPresent()){
                for (Enseignant enseignant : Enseignants) {
                    enseignant.getEnseignementList().add(enseignementAcronyme);
                    enseignantRepository.saveAndFlush(enseignant);
                }
            }
            //Ensure we have the last version
            enseignementAcronyme = getByAcronyme(enseignementAcronyme.getAcronyme());
        }
        return enseignementAcronyme;
    }
    /**
     * supprime la matière
     * delete this course
     * @param acronyme 
     */
    @Override
    public void remove(String acronyme){
        if (acronyme !=null){
            //Ensure validity from database
            Enseignement Enseignement = getByAcronyme(acronyme);
            acronyme = Enseignement.getAcronyme();
        }
        if (acronyme != null){
            enseignementRepository.delete(getByAcronyme(acronyme));
        }
    }
    /**
     * créer une matière
     * create a course
     * @param acronyme
     * @param nom
     * @param filiere
     * @param responsable
     * @param Enseignants
     * @return 
     */
    @Override
    public Enseignement create(String acronyme, String nom, String filiere, Enseignant responsable, ArrayList<Enseignant> Enseignants){
        for (int i = 0; i < Enseignants.size(); i++) {
            if (Enseignants.get(i) != null) {
                Enseignants.set(i, enseignantRepository.getByInitiales(Enseignants.get(i).getInitiales()));
            }
        }
        if ((nom != null) && (! nom.isEmpty())
                && (filiere != null) && (! filiere.isEmpty())
                && (responsable != null)
                && (acronyme != null && (!acronyme.isEmpty()))){
            Enseignement item = new Enseignement(acronyme);
            // Update data
            item.setNomEnseignement(nom);
            item.setFiliere(filiere);
            item.setResponsable(responsable);
            item.setEnseignantList(Enseignants);
            // Save to database
            enseignementRepository.saveAndFlush(item);
            responsable.getEnseignementList1().add(item);
            enseignantRepository.saveAndFlush(responsable);
            for (Enseignant enseignant : Enseignants) {
                    enseignant.getEnseignementList().add(item);
                    enseignantRepository.saveAndFlush(enseignant);
                }
            //Ensure we have the last version
            return getByAcronyme(acronyme);
        }
        return null;
    }
    /**
     * renvoie vrai si et seulement si un enseignant est responsable d'une matière pour une filière donnée
     * return true only if a teacher is a course manager in this particular program
     * @param responsable
     * @param filiere
     * @return 
     */
    @Override
    public boolean existsByResponsableAndFiliere(Enseignant responsable, String filiere) {
        try {
            Long count = entityManager.createQuery(
                            "SELECT COUNT(e) FROM Enseignement e WHERE e.responsable = :responsable AND e.filiere = :filiere",
                            Long.class)
                    .setParameter("responsable", responsable)
                    .setParameter("filiere", filiere)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
