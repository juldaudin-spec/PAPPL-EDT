/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;
/**
 *
 * @author nathan
 */
@Repository
public class TypeLeconRepositoryCustomImpl implements TypeLeconRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<String> findAllTypeLecon(){
        TypedQuery<String> query = entityManager.createQuery("SELECT intitule FROM Type_Lecon", String.class);
        return query.getResultList();
    }
    
    @Override
    public List<String> findTypeLeconByEnseignementByGroupe(String acronyme, String nomGroupe){
        String requete = "SELECT intitule FROM Contient JOIN Etudie ON Contient.contient_id=Etudie.contient_id WHERE acronyme= :acronyme AND nom_groupe= :nomGroupe";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        query.setParameter("acronyme", acronyme);
        query.setParameter("nomGrouoe", nomGroupe);
        return query.getResultList();
    }
    
    @Override
    public TypeLecon getByIntitule(String intitule){
        try {
            return entityManager
                    .createNamedQuery("TypeLecon.findByIntitule", TypeLecon.class)
                    .setParameter("intitule", intitule)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
}
