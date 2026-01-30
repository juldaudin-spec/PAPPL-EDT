/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.List;
import tp.projetpappl.items.Enseignant;

/**
 *
 * @author nathan
 */
public interface EnseignantRepositoryCustom {
    List<String> findAllInitaleEnseignant();
    List<String> findinitialeEnseignantByEnseignement(String initiale);
    public Enseignant getByInitiales(String initiales);
    public Enseignant update(String initiales, String prenom, String nom);
    public Enseignant create(String initiales, String prenom, String nom);
    public void remove(String initiales);

}
