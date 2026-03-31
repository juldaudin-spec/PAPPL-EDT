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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Enseignant;

/**
 *
 * @author nathan
 */
@Repository
public class EnseignantRepositoryCustomImpl implements EnseignantRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    EnseignantRepository enseignantRepository;
/**
 * renvoie tous les enseignants
 * return every teachers
 * @return 
 */
    @Override
    public List<String> findAllInitaleEnseignant() {
        String requete = "SELECT initiales FROM Enseignant";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
/**
 * renvoie les enseignants qui enseignent dans une matière donnée
 * return teachers who teach in a given course
 * @param acronyme
 * @return 
 */
    @Override
    public List<String> findinitialeEnseignantByEnseignement(String acronyme) {
        String requete = "SELECT initiales FROM Enseigne WHERE acronyme= :acronyme";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        query.setParameter("acronyme", acronyme);
        return query.getResultList();
    }
/**
 * renvoie un enseignant par son id
 * return a teacher by its id
 * @param initiales
 * @return 
 */
    @Override
    public Enseignant getByInitiales(String initiales) {
        try {
            return entityManager
                    .createNamedQuery("Enseignant.findByInitiales", Enseignant.class)
                    .setParameter("initiales", initiales)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
/**
 * met à jour les infos d'un enseignant
 * update informations of this teacher
 * @param initiales
 * @param prenom
 * @param nom
 * @return 
 */
    @Override
    public Enseignant update(String initiales, String prenom, String nom) {
        Enseignant enseignantInitiales = null;
        if (initiales != null) {
            // Ensure validity from database
            enseignantInitiales = getByInitiales(initiales);
            initiales = enseignantInitiales.getInitiales();
        }
        if ((initiales != null)
                && (prenom != null) && (!prenom.isEmpty())
                && (nom != null) && (!nom.isEmpty())) {
            // Update data
            enseignantInitiales = getByInitiales(initiales);
            enseignantInitiales.setPrenom(prenom);
            enseignantInitiales.setNomEnseignant(nom);
            // Save to database
            enseignantRepository.saveAndFlush(enseignantInitiales);
            //Ensure we have the last version
            enseignantInitiales = getByInitiales(enseignantInitiales.getInitiales());
        }
        return enseignantInitiales;
    }
/**
 * supprime un enseignant
 * delete a teacher
 * @param initiales 
 */
    @Override
    public void remove(String initiales) {
        if (initiales != null) {
            //Ensure validity from database
            Enseignant enseignant = getByInitiales(initiales);
            initiales = enseignant.getInitiales();
        }
        if (initiales != null) {
            enseignantRepository.delete(getByInitiales(initiales));
        }
    }
/**
 * créer un enseignant
 * create a teacher
 * @param initiales
 * @param prenom
 * @param nom
 * @return 
 */
    @Override
    public Enseignant create(String initiales, String prenom, String nom) {
        if ((nom != null) && (!nom.isEmpty())
                && (prenom != null) && (!prenom.isEmpty())
                && (initiales != null && (!initiales.isEmpty()))) {
            Enseignant item = new Enseignant(initiales);
            // Update data
            item.setPrenom(prenom);
            item.setNomEnseignant(nom);
            // Save to database
            enseignantRepository.saveAndFlush(item);
            //Ensure we have the last version
            return getByInitiales(initiales);
        }
        return null;
    }
    /**
     * créer des enseignants à partir d'une liste
     * create teachers from a list
     * @param listEnseignantStr
     * @return 
     */
    public List<Enseignant> createByListStr(List<List<String>> listEnseignantStr) {
        List<Enseignant> listEnseignant = new ArrayList<Enseignant>();
        for(List<String> enseignantStr : listEnseignantStr){
            Enseignant enseignant = enseignantRepository.create(enseignantStr.get(0),enseignantStr.get(1),enseignantStr.get(2));
            if(enseignant != null)
                listEnseignant.add(enseignant);
        }
        return listEnseignant;
    }
/**
 * récupère un enseignant à partir de son login
 * get a teacher from its login
 * @param login
 * @return 
 */
    @Override
    public Enseignant getByLogin(String login) {
        try {
            return entityManager
                    .createNamedQuery("Enseignant.findByLogin", Enseignant.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }



}
