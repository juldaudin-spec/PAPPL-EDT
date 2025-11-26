/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.tp_programation_web.repositories;

import tp.tp_programation_web.items.Person;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author nathan
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, PersonRepositoryCustom{
    
}
