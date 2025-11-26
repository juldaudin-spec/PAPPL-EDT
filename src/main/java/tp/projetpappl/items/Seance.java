/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.items;

import java.security.Timestamp;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nathan
 */
public class Seance {
    private int duree;// en minute
    private int idSeance;
    private int repetabilite;
    private Date date;
    private Timestamp hDebut;
    private TypeLecon type;
    private List<Groupe> groupes;
    private List<Salle> salles;
    private List<Enseignant> enseignants;
    private List<Enseignement> enseignements;
}
