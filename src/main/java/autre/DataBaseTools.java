/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package autre;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nathan
 */
public class DataBaseTools {

    private String login;
    private String password;
    private String url;
    private Connection connection;

    /**
     * Load infos
     */
    public DataBaseTools() {
        try {
            // Get Properties file
            ResourceBundle properties = ResourceBundle.getBundle(DataBaseTools.class.getPackage().getName() + ".database");

            // USE config parameters
            login = properties.getString("postgres");
            password = properties.getString("postgres");
            String server = properties.getString("localhost");
            String database = properties.getString("dbpappl");
            url = "jdbc:postgresql://" + server + "/" + database;

            // Mount driver
            Driver driver = DriverManager.getDriver(url);
            if (driver == null) {
                Class.forName("org.postgresql.Driver");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DataBaseTools.class.getName()).log(Level.SEVERE, null, ex);
            // If driver is not found, cancel url
            url = null;
        }
        this.connection = null;
    }

    /**
     * Get connection to the database
     */
    public void connect() {
        if ((this.connection == null) && (url != null) && (!url.isEmpty())) {
            try {
                this.connection = DriverManager.getConnection(url, login, password);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Disconnect from database
     */
    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void creationTable() throws SQLException {
        this.connect();
        String query = "CREATE TABLE enseignant("
                + "initiales CHARACTER VARYING(5) NOT NULL PRIMARY KEY,"
                + "nom_enseignant CHARACTER VARYING(64) NOT NULL,"
                + "prenom CHARACTER VARYING(64),"
                + "login CHARACTER VARYING(64),"
                + "mdp CHARACTER VARYING(64)"
                + ");"
                + "CREATE TABLE enseignement("
                + "acronyme CHARACTER VARYING(10) NOT NULL PRIMARY KEY,"
                + "nom_enseignement CHARACTER VARYING(128),"
                + "filiere CHARACTER VARYING(64),"
                + "responsable CHARACTER VARYING(4) NOT NULL REFERENCES enseignant"
                + ");"
                +"CREATE TABLE gerer("
                +"gerer_id SERIAL NOT NULL PRIMARY KEY,"
                + "initiales CHARACTER VARYING(5) NOT NULL,"
                + "acronyme CHARACTER VARYING(10) NOT NULL,"
                + "UNIQUE(initiales,acronyme)"
                + ");"
                + "CREATE TABLE enseigne("
                + "initiales CHARACTER VARYING(4) NOT NULL REFERENCES enseignant,"
                + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,"
                + "PRIMARY KEY(initiales,acronyme)"
                + ");"
                + "CREATE TABLE type_lecon("
                + "intitule CHARACTER VARYING(8) NOT NULL PRIMARY KEY,"
                + "nb_enseignant INTEGER"
                + ");"
                + "CREATE TABLE salle("
                + "numero_salle CHARACTER VARYING(4) NOT NULL PRIMARY KEY,"
                + "capacite INTEGER,"
                + "typologie CHARACTER VARYING(20)"
                + ");"
                + "CREATE TABLE contient("
                +"contient_id SERIAL NOT NULL PRIMARY KEY,"
                + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,"
                + "intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon,"
                + "salle_preconisee CHARACTER VARYING(4) REFERENCES salle,"
                + "volumetrie NUMERIC,"
                + "UNIQUE(intitule,acronyme)"
                + ");"
                + "CREATE TABLE groupe("
                + "nb_eleve INTEGER,"
                + "nom_groupe CHARACTER VARYING(128) NOT NULL PRIMARY KEY"
                + ");"
                +"CREATE TABLE depend("
                + "pere CHARACTER VARYING(128) NOT NULL REFERENCES groupe,"
                + "fils CHARACTER VARYING(128) NOT NULL REFERENCES groupe,"
                + "PRIMARY KEY(pere,fils)"
                + ");"
                + "CREATE TABLE etudie("
                + "contient_id INTEGER NOT NULL,"
                + "nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe,"
                + "PRIMARY KEY(contient_id,nom_groupe),"
                + "FOREIGN KEY (contient_id) REFERENCES contient"
                + ");"
                + "CREATE TABLE seance("
                + "acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,"
                + "intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon,"
                + "h_debut TIMESTAMP,"
                + "duree INTEGER,"
                + "id_seance SERIAL NOT NULL PRIMARY KEY"
                + ");"
                + "CREATE TABLE a_lieu("
                + "id_seance INTEGER NOT NULL REFERENCES seance,"
                + "numero_salle CHARACTER VARYING(4) REFERENCES salle,"
                + "PRIMARY KEY(id_seance, numero_salle)"
                + ");"
                + "CREATE TABLE donne_cours("
                + "initiales CHARACTER VARYING(4) NOT NULL REFERENCES enseignant,"
                + "id_seance INTEGER NOT NULL REFERENCES seance,"
                + "PRIMARY KEY(initiales,id_seance)"
                + ");"
                + "CREATE TABLE participe("
                + "id_seance INTEGER NOT NULL REFERENCES seance,"
                + "nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe"
                + ");";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
        this.disconnect();
    }

}
