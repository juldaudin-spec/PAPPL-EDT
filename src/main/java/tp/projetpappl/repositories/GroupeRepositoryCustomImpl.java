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
public class GroupeRepositoryCustomImpl implements GroupeRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
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
}
