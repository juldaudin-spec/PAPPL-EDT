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
    List<Contient> findContientByIntituleByEnseignementByGroupe(TypeLecon intitule,Enseignement acronyme,Groupe nomGroupe);
    List<TypeLecon> findIntituleByEnseignementByGroupe(Enseignement acronyme, Groupe nomGroupe);
    List<Enseignement> findEnseignementByGroupe(Groupe nomGroupe);
    List<Groupe> findGroupeByEnseignement(Enseignement acronyme);    
}
