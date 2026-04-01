package tp.projetpappl.items;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entité JPA représentant une session utilisateur active dans l'application.
 *
 * Chaque entrée correspond à un utilisateur authentifié via le CAS.
 * La session est créée à la connexion et supprimée à la déconnexion.
 * Un trigger PostgreSQL supprime automatiquement les sessions expirées
 * après 2 heures d'inactivité.
 *
 * @author Oussama
 */
@Entity
@Table(name = "connection")
@NamedQueries({
        @NamedQuery(
                name = "Connection.findAll",
                query = "SELECT c FROM Connection c"
        ),
        @NamedQuery(
                name = "Connection.findByConnectionCode",
                query = "SELECT c FROM Connection c WHERE c.connectionCode = :connectionCode"
        ),
        @NamedQuery(
                name = "Connection.findByConnectionLogin",
                query = "SELECT c FROM Connection c WHERE c.connectionLogin = :connectionLogin"
        )
})
public class Connection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Code de session unique généré à la connexion.
     * Format : 5 lettres aléatoires + timestamp + 3 lettres aléatoires.
     * Ce code est transmis dans chaque formulaire JSP via un champ caché
     * pour identifier l'utilisateur à chaque requête.
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "connection_code")
    private String connectionCode;

    /**
     * Identifiant CAS de l'utilisateur (uid retourné par le serveur CAS).
     * Exemple : "p.nom2024".
     * Ce champ est utilisé par AuthHelper pour retrouver l'enseignant
     * correspondant et vérifier ses droits d'accès.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "connection_login")
    private String connectionLogin;

    /**
     * Constructeur par défaut requis par JPA.
     */
    public Connection() {
    }

    /**
     * Constructeur avec le code de session uniquement.
     *
     * @param connectionCode le code de session unique
     */
    public Connection(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    /**
     * Constructeur complet.
     *
     * @param connectionCode  le code de session unique
     * @param connectionLogin l'uid CAS de l'utilisateur
     */
    public Connection(String connectionCode, String connectionLogin) {
        this.connectionCode = connectionCode;
        this.connectionLogin = connectionLogin;
    }

    /**
     * @return le code de session unique
     */
    public String getConnectionCode() {
        return connectionCode;
    }

    /**
     * @param connectionCode le code de session unique
     */
    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    /**
     * @return l'uid CAS de l'utilisateur
     */
    public String getConnectionLogin() {
        return connectionLogin;
    }

    /**
     * @param connectionLogin l'uid CAS de l'utilisateur
     */
    public void setConnectionLogin(String connectionLogin) {
        this.connectionLogin = connectionLogin;
    }

    /**
     * Le hash est basé uniquement sur le code de session,
     * qui est la clé primaire de l'entité.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (connectionCode != null ? connectionCode.hashCode() : 0);
        return hash;
    }

    /**
     * Deux sessions sont considérées égales si elles partagent
     * le même code de session.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Connection)) {
            return false;
        }
        Connection other = (Connection) object;
        if ((this.connectionCode == null && other.connectionCode != null)
                || (this.connectionCode != null && !this.connectionCode.equals(other.connectionCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Connection[ connectionCode=" + connectionCode + " ]";
    }
}