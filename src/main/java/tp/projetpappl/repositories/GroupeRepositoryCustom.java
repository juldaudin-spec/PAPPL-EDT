/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
import tp.projetpappl.items.Groupe;

/**
 *
 * @author nathan
 */
public interface GroupeRepositoryCustom {
    List<String> findAllNomGroupe();
    List<String> findGroupeByEnseignement(String acronyme);
    Groupe getByNomGroupe(String nom);
    Groupe update(String nomGroupe, int nbEleves);
    void remove(String nomGroupe);
    Groupe create(String nomGroupe, int nbEleves);
}
