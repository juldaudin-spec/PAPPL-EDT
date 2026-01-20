/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
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
     *
     * @return
     */
    @Override
    public List<String> findAllNomGroupe(){
        TypedQuery<String> query = entityManager.createQuery("SELECT nom_groupe FROM Groupe", String.class);
        return query.getResultList();
    }
    
    @Override
    public List<String> findGroupeParEnseignement(String acronyme){
        String requete = "SELECT nom_groupe FROM Etudie JOIN Contient ON Contient.contient_id=Etudie.contient_id WHERE acronyme="+acronyme;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
    @Override
    public Groupe getByNomGroupe(String nom){
        return entityManager.createNamedQuery("Groupe.findByNomGroupe", Groupe.class).setParameter("nomGroupe", nom).getSingleResult();
    }
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
}
