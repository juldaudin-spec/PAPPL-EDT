/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.controllers.Tools;
import tp.projetpappl.items.Salle;
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
    
    /**
     * récupère une salle par son id
     * get a room by its id
     * @param numeroSalle
     * @return 
     */
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
    /**
     * met les infos d'une salle à jour
     * update room's information
     * @param numeroSalle
     * @param capacite
     * @param typologie
     * @return 
     */
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
    /**
     * suppression d'une salle
     * deletion of a room
     * @param numeroSalle 
     */
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
    /**
     * création d'une salle
     * creation of a room
     * @param numeroSalle
     * @param capacites
     * @param typologie
     * @return 
     */
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
    /**
     * création d'une liste de salle
     * creation of a room list
     * @param listEnseignantStr
     * @return 
     */
    public List<Salle> createByListStr(List<List<String>> listEnseignantStr) {
        List<Salle> listSalle = new ArrayList<Salle>();
        for(List<String> salleStr : listEnseignantStr){
            Salle salle = salleRepository.create(salleStr.get(0),Tools.getIntFromString(salleStr.get(1)),salleStr.get(2));
            if(salle != null)
                listSalle.add(salle);
        }
        return listSalle;
    }
}
