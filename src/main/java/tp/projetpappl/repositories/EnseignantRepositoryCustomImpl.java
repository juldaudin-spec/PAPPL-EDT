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
import tp.projetpappl.items.Enseignant;


/**
 *
 * @author nathan
 */
@Repository
public class EnseignantRepositoryCustomImpl implements EnseignantRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public List<String> findAllInitaleEnseignant() {
        String requete = "SELECT initiales FROM Enseignant";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }

    @Override
    public List<String> findinitialeEnseignantParEnseignement(String acronyme) {
        String requete = "SELECT initiales FROM Enseigne WHERE acronyme="+acronyme;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
    public Enseignant findByInitiales(String initiales){
        return entityManager.createNamedQuery("Enseignant.findByInitiales", Enseignant.class).setParameter("initiales", initiales).getSingleResult();
    }
    
    
}
