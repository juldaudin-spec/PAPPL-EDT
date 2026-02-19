/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import tp.projetpappl.items.Salle;

/**
 *
 * @author nathan
 */
public interface SalleRepositoryCustom {
    Salle getByNumeroSalle(String acronyme);
    Salle create(String numeroSalle, int capacites,String typologie);
    Salle update(String numeroSalle, int capacite, String typologie);
    void remove(String numeroSalle);
}
