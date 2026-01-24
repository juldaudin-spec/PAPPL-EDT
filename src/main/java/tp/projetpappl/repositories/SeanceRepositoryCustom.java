/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.Date;
import java.util.List;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;

/**
 *
 * @author nathan
 */
public interface SeanceRepositoryCustom {
    Seance create(Enseignement enseignement, List<Enseignant> Enseignants, TypeLecon typeLecon, List<Groupe> groupe, List<Salle> Salles, Date hDebut, int duree);
    Seance update(int IdSeance, Enseignement enseignement, List<Enseignant> Enseignants, TypeLecon typeLecon, List<Groupe> groupe, List<Salle> Salles, Date hDebut, int duree);
}
