/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import java.math.BigInteger;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.controllers.Tools;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import java.math.BigInteger;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.TypeLecon;
/**
 *
 * @author nathan
 */
@Repository
public class ContientRepositoryCustomImpl implements ContientRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    private GroupeRepository groupeRepository;
    @Autowired
    @Lazy
    private EnseignementRepository enseignementRepository;
    
    @Autowired
    @Lazy
    private ContientRepository contientRepository;
    @Autowired
    @Lazy
    private TypeLeconRepository typeLeconRepository;

    @Autowired
    @Lazy
    private SalleRepository salleRepository;
    /**
     * créer un élément de la maquette d'une matière (la quantité d'un type de leçon et les salles espérées)
     * create a piece of syllabus for a course (quantity of a lesson type and the rooms expected)
     * @param acronyme
     * @param intitule
     * @param minutes
     * @param salle
     * @return 
     */
    public Contient create(String acronyme, String intitule, BigInteger minutes, String salle){
        TypeLecon typeLecon = null;
        Enseignement enseignement = null;
        if (acronyme != null) {
            enseignement = enseignementRepository.getByAcronyme(acronyme);
        }
        if (intitule != null) {
            typeLecon = typeLeconRepository.getByIntitule(intitule);
        }

        // Build new seance
        if ((enseignement != null) && (typeLecon != null) && (minutes != null)) {
            Contient item = new Contient();
            item.setAcronyme(enseignement);
            item.setIntitule(typeLecon);
            item.setVolumetrie(minutes);
            item.setSallePreconisee(salleRepository.getByNumeroSalle(salle));
            contientRepository.saveAndFlush(item);

            Optional<Contient> result = contientRepository.findById(item.getContientId());
            if (result.isPresent()) {
                item = result.get();

                // Set reverse fields
                enseignement.getContientList().add(item);
                enseignementRepository.saveAndFlush(enseignement);
                
                typeLecon.getContientList().add(item);
                typeLeconRepository.saveAndFlush(typeLecon);
                // return item
                return item;
            }
        }
        return null;
    }

    
    /**
     * récupère les éléments de la maquette pour un type de leçon d'un enseignement que suit un groupe
     * return the pieces of the syllabus for a lesson type for a course followed by a group
     * @param intitule
     * @param acronyme
     * @param groupe
     * @return 
     */
    @Override
    public List<Contient> findContientByIntituleByEnseignementByGroupe(TypeLecon intitule, Enseignement acronyme, Groupe groupe) {
        List<Contient> listContient =null;
        if (intitule!=null&&acronyme!=null&&groupe!=null){
            String requete = "SELECT c FROM Contient c JOIN c.groupeList g WHERE c.acronyme= :acronyme AND c.intitule= :intitule AND g.nomGroupe= :nomGroupe";
            TypedQuery<Contient> query = entityManager.createQuery(requete, Contient.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("intitule", intitule);
            query.setParameter("nomGroupe", groupe.getNomGroupe());
            listContient = query.getResultList();
        }
        return listContient;
    }
/**
 * récupère les types de leçon que doit suivre le groupe dans la matière 
 * return the lesson type that the groupe must follow in that course
 * @param acronyme
 * @param groupe
 * @return 
 */
    @Override
    public List<TypeLecon> findIntituleByEnseignementByGroupe(Enseignement acronyme, Groupe groupe) {
        List<TypeLecon> listType = null;
        if (acronyme!=null&&groupe!=null){
            String requete = "SELECT c.intitule FROM Contient c JOIN c.groupeList g WHERE c.acronyme= :acronyme AND g.nomGroupe= :nomGroupe";
            TypedQuery<TypeLecon> query = entityManager.createQuery(requete, TypeLecon.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("nomGroupe", groupe.getNomGroupe());
            listType = query.getResultList();
        }
        return listType;
    }
/**
 * récupère les matières que le groupe doit suivre
 * return the courses that a group must follow
 * @param groupe
 * @return 
 */
    @Override
    public List<Enseignement> findEnseignementByGroupe(Groupe groupe) {
        String requete = "SELECT c.acronyme FROM Contient c JOIN c.groupeList g WHERE g.nomGroupe= :nomGroupe";
        TypedQuery<Enseignement> query = entityManager.createQuery(requete, Enseignement.class);
        query.setParameter("nomGroupe", groupe.getNomGroupe());
        return query.getResultList();
    }
/**
 * renvoie les groupes qui doivent suivre la matière
 * return the groups who must follow this course
 * @param acronyme
 * @return 
 */
    @Override
    public List<Groupe> findGroupeByEnseignement(Enseignement acronyme) {
        String requete = "SELECT g FROM Contient c JOIN c.groupeList g WHERE c.acronyme= :acronyme";
        TypedQuery<Groupe> query = entityManager.createQuery(requete, Groupe.class);
        query.setParameter("acronyme", acronyme);
        return query.getResultList();
    }
}
