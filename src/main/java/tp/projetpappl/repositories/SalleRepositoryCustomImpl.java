/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Salle;
/**
 *
 * @author nathan
 */
@Repository
public class SalleRepositoryCustomImpl implements SalleRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    private SalleRepository salleRepository;
    
    @Override
    public Salle getByNumeroSalle(String numeroSalle) {
        try {
            return entityManager
                    .createNamedQuery("Salle.findByNumeroSalle", Salle.class)
                    .setParameter("numeroSalle", numeroSalle)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    @Override
    public Salle update(String numeroSalle, int capacite, String typologie){
        Salle salle = null;
        if(numeroSalle != null){
            // Ensure validity from database
            salle = getByNumeroSalle(numeroSalle);
            numeroSalle = salle.getNumeroSalle();
        }
        if ((numeroSalle != null && (!numeroSalle.isEmpty()))){
            // Update data
            salle = getByNumeroSalle(numeroSalle);
            salle.setCapacite(capacite);
            salle.setTypologie(typologie);
            // Save to database
            salleRepository.saveAndFlush(salle);
            //Ensure we have the last version
            salle = getByNumeroSalle(salle.getNumeroSalle());
        }
        return salle;
    }
    @Override
    public void remove(String numeroSalle){
        if (numeroSalle !=null){
            //Ensure validity from database
            Salle salle = getByNumeroSalle(numeroSalle);
            numeroSalle = salle.getNumeroSalle();
        }
        if (numeroSalle != null){
            salleRepository.delete(getByNumeroSalle(numeroSalle));
        }
    }
    @Override
    public Salle create(String numeroSalle, int capacites,String typologie){
        if (numeroSalle != null && (!numeroSalle.isEmpty())){
            Salle item = new Salle(numeroSalle);
            // Update data
            item.setCapacite(capacites);
            item.setTypologie(typologie);
            // Save to database
            salleRepository.saveAndFlush(item);
            //Ensure we have the last version
            return getByNumeroSalle(numeroSalle);
        }
        return null;
    }
}
