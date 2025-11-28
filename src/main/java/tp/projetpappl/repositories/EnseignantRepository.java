/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import tp.projetpappl.items.Enseignant;
/**
 *
 * @author nathan
 */
@Repository
public interface EnseignantRepository extends JpaRepository<Enseignant, String>, EnseignantRepositoryCustom {
    
}
