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
public class EnseignementRepositoryCustomImpl implements EnseignementRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<String> findAllAcronyme() {
        String requete = "SELECT Acronyme FROM Enseignement";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }

    @Override
    public List<String> findAcronymeParEnseignant(String initiales) {
        String requete = "SELECT acronyme FROM Enseigne WHERE initiales="+initiales;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
    
    @Override
    public List<String> findAcronymeParGroupe(String nomGroupe) {
        String requete = "SELECT acronyme FROM Contient JOIN Etudie ON Contient.contient_id=Etudie.contient_id WHERE nom_groupe="+nomGroupe;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
}
