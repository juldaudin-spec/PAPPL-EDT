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
import tp.projetpappl.controllers.Tools;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Groupe;
/**
 *
 * @author nathan
 */
@Repository
public class GroupeRepositoryCustomImpl implements GroupeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    private GroupeRepository groupeRepository;
    /**
     * récupère tous les groupes de la base de données
     * get every group from database
     * @return 
     */
    @Override
    public List<String> findAllNomGroupe(){
        TypedQuery<String> query = entityManager.createQuery("SELECT nom_groupe FROM Groupe", String.class);
        return query.getResultList();
    }
    /**
     * récupère tous les groupes qui doivent suivre un enseignement 
     * get every group who must follow a course
     * @param acronyme
     * @return 
     */
    @Override
    public List<String> findGroupeByEnseignement(String acronyme){
        String requete = "SELECT nom_groupe FROM Etudie JOIN Contient ON Contient.contient_id=Etudie.contient_id WHERE acronyme= :acronyme";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        query.setParameter("acronyme", acronyme);
        return query.getResultList();
    }
    /**
     * récupère un groupe par son id
     * get a group from its id
     * @param nom
     * @return 
     */
    @Override
    public Groupe getByNomGroupe(String nom){
        return entityManager.createNamedQuery("Groupe.findByNomGroupe", Groupe.class).setParameter("nomGroupe", nom).getSingleResult();
    }
    /**
     * mise à jour des info d'un groupe
     * update group's information
     * @param nomGroupe
     * @param nbEleve
     * @return 
     */
    @Override
    public Groupe update(String nomGroupe, int nbEleve){
        Groupe groupe = null;
        if(nomGroupe != null){
            // Ensure validity from database
            groupe = getByNomGroupe(nomGroupe);
            nomGroupe = groupe.getNomGroupe();
        }
        if ((nomGroupe != null && (!nomGroupe.isEmpty()))){
            // Update data
            groupe = getByNomGroupe(nomGroupe);
            groupe.setNbEleve(nbEleve);
            // Save to database
            groupeRepository.saveAndFlush(groupe);
            //Ensure we have the last version
            groupe = getByNomGroupe(groupe.getNomGroupe());
        }
        return groupe;
    }
    /**
     * supprime un groupe
     * delete a group
     * @param nomGroupe 
     */
    @Override
    public void remove(String nomGroupe){
        if (nomGroupe !=null){
            //Ensure validity from database
            Groupe groupe = getByNomGroupe(nomGroupe);
            nomGroupe = groupe.getNomGroupe();
        }
        if (nomGroupe != null){
            groupeRepository.delete(getByNomGroupe(nomGroupe));
        }
    }
    /**
     * créer un groupe
     * create a group
     * @param nomGroupe
     * @param nbEleves
     * @return 
     */
    @Override
    public Groupe create(String nomGroupe, int nbEleves){
        if (nomGroupe != null && (!nomGroupe.isEmpty())){
            Groupe item = new Groupe(nomGroupe);
            // Update data
            item.setNbEleve(nbEleves);
            // Save to database
            groupeRepository.saveAndFlush(item);
            //Ensure we have the last version
            return getByNomGroupe(nomGroupe);
        }
        return null;
    }
    public List<Groupe> createByListStr(List<List<String>> listEnseignantStr) {
        List<Groupe> listGroupe = new ArrayList<Groupe>();
        for(List<String> groupeStr : listEnseignantStr){
            Groupe groupe = groupeRepository.create(groupeStr.get(0),Tools.getIntFromString(groupeStr.get(1)));
            if(groupe != null)
                listGroupe.add(groupe);
        }
        return listGroupe;
    }
}
