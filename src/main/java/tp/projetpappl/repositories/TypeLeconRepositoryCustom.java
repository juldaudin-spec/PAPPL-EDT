/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
/**
 *
 * @author nathan
 */
public interface TypeLeconRepositoryCustom {
    List<String> findAllTypeLecon();
    List<String> findTypeLeconParEnseignementParGroupe(String acronyme, String nomGroupe);
}
