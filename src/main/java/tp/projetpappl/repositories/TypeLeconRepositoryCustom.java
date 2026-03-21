/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
import tp.projetpappl.items.TypeLecon;
/**
 *
 * @author nathan
 */
public interface TypeLeconRepositoryCustom {
    List<String> findAllTypeLecon();
    List<String> findTypeLeconByEnseignementByGroupe(String acronyme, String nomGroupe);
    TypeLecon getByIntitule(String intitule);
}
