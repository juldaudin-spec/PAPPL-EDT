/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autre;

/**
 *
 * @author nathan
 */
public class Creation_BDD {

    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    /**
     *
     * @author nathan
     */
    public class Creatable {

        String request;

        public Creatable() {
            request = "CREATE TABLE enseigne("
                    + "initiales CHARACTER VARYING(4) NOT NULL REFERENCES enseignant"
                    + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement"
                    + "PRIMARY KEY(initiales,acronyme)"
                    + ");"
                    + "CREATE TABLE enseignant("
                    + "initiales CHARACTER VARYING(5) NOT NULL PRIMARY KEY"
                    + "nom_enseignant CHARACTER VARYING(64) NOT NULL"
                    + "prenom CHARACTER VARYING(64)"
                    + ");"
                    + "CREATE TABLE enseignement("
                    + "acronyme CHARACTER VARYING(10) NOT NULL PRIMARY KEY"
                    + "nom_enseignement CHARACTER VARYING(128)"
                    + "filiere CHARACTER VARYING(64)"
                    + "responsable CHARACTER VARYING(4) NOT NULL REFERENCES enseignant"
                    + ");"
                    + "CREATE TABLE type_lecon("
                    + "intitule CHARACTER VARYING(8) NOT NUL PRIMARY KEY"
                    + "nb_enseignant INTEGER"
                    + ");"
                    + "CREATE TABLE contient("
                    + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement"
                    + "intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon"
                    + "nb_heure INTEGER"
                    + ");"
                    + "CREATE TABLE groupe("
                    + "nb_eleve INTEGER"
                    + "nom_groupe CHARACTER VARYING(128) NOT NULL PRIMARY KEY"
                    + ");"
                    + "CREATE TABLE etudie("
                    + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement"
                    + "nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe"
                    + "PRIMARY KEY(acronyme,nom_groupe)"
                    + ");"
                    + "CREATE TABLE salle("
                    + "numero_salle INTEGER NOT NULL PRIMARY KEY"
                    + "capacite INTEGER;"
                    + "prise BOOLEAN DEFAULT false"
                    + ");"
                    + "CREATE TABLE seance("
                    + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement"
                    + "intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon"
                    + "h_debut TIMESTAMP"
                    + "duree INTEGER"
                    + "date DATE"
                    + "repetabilite INTEGER DEFAULT 1"
                    + "id_seance SERIAL INTEGER NOT NULL PRIMARY KEY"
                    + ");"
                    + "CREATE TABLE a_lieu("
                    + "id_seance INTEGER NOT NULL REFERENCES seance"
                    + "numero_salle INTEGER REFERENCES salle"
                    + "PRIMARY KEY(id_seance, numero_salle)"
                    + ");"
                    + "CREATE TABLE donne_cours("
                    + "initiales CHARACTER VARYING(4) NOT NULL REFERENCES enseignant"
                    + "id_seance INTEGER NOT NULL REFERENCES seance"
                    + "PRIMARY KEY(initiales,id_seance)"
                    + ");"
                    + "CREATE TABLE participe("
                    + "id_seance INTEGER NOT NULL REFERENCES seance"
                    + "nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe"
                    + ");";
        }
    }
