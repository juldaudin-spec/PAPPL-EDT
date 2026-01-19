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
    private EnseignantRepository enseignantRepository;
    
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
    public Enseignant getByInitiales(String initiales){
        return entityManager.createNamedQuery("Enseignant.findByInitiales", Enseignant.class).setParameter("initiales", initiales).getSingleResult();
    }
    @Override
    public Enseignant update(String initiales, String prenom, String nom){
        Enseignant enseignantInitiales = null;
        if(initiales != null){
            // Ensure validity from database
            enseignantInitiales = getByInitiales(initiales);
            initiales = enseignantInitiales.getInitiales();
        }
        if ((initiales != null)
                && (prenom != null) && (! prenom.isEmpty())
                && (nom != null) && (! nom.isEmpty())){
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
    @Override
    public void remove(String initiales){
        if (initiales !=null){
            //Ensure validity from database
            Enseignant enseignant = getByInitiales(initiales);
            initiales = enseignant.getInitiales();
        }
        if (initiales != null){
            enseignantRepository.delete(getByInitiales(initiales));
        }
    }
    @Override
    public Enseignant create(String initiales, String prenom, String nom){
        if ((nom != null) && (! prenom.isEmpty())
                && (nom != null) && (! nom.isEmpty())){
            Enseignant item = new Enseignant();
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
    
    
}
