/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.TypeLecon;

/**
 *
 * @author nathan
 */
public interface ContientRepositoryCustom {
    List<Contient> findContientByIntituleByEnseignementByGroupe(String intitule,String acronyme,String nomGroupe);
    List<TypeLecon> findIntituleByEnseignementByGroupe(String acronyme, String nomGroupe);
    List<Enseignement> findEnseignementByGroupe(String nomGroupe);
    List<Groupe> findGroupeByEnseignement(String acronyme);    
}
