/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
}
