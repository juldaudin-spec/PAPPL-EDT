/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;
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
    public List<String> findTypeLeconParEnseignementParGroupe(String acronyme, String nomGroupe){
        String requete = "SELECT intitule FROM Contient JOIN Etudie ON Contient.contient_id=Etudie.contient_id WHERE acronyme="+acronyme+"AND nom_groupe="+nomGroupe;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
}
