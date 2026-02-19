/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;

/**
 *
 * @author nathan
 */
public interface EnseignementRepositoryCustom {
    List<String> findAllAcronyme();
    List<String> findAcronymeByEnseignant(String initiales);
    List<String> findAcronymeByGroupe(String initiales);
    Enseignement getByAcronyme(String acronyme);
    Enseignement update(String acronyme, String nom, String filiere, Enseignant responsable);
    void remove(String acronyme);
    Enseignement create(String acronyme, String nom, String filiere, Enseignant responsable);
}
