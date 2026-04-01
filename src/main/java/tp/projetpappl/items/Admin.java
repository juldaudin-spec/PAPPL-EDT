package tp.projetpappl.items;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * Entité JPA représentant un administrateur global de l'application.
 *
 * Un administrateur global a accès à toutes les fonctionnalités de
 * modification, sans restriction de filière. Les administrateurs sont
 * enregistrés manuellement en base de données via leur uid CAS.
 *
 * Le champ login doit correspondre exactement à l'uid retourné par le
 * serveur CAS lors de la connexion (ex: "p.nom2024").
 *
 * @author Oussama
 */
@Entity
@Table(name = "admin")
@NamedQueries({
        @NamedQuery(
                name = "Admin.findAll",
                query = "SELECT a FROM Admin a"
        ),
        @NamedQuery(
                name = "Admin.findByLogin",
                query = "SELECT a FROM Admin a WHERE a.login = :login"
        )
})
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant CAS de l'administrateur (uid retourné par le serveur CAS).
     * C'est la clé primaire de l'entité.
     * Ce champ est utilisé par AuthHelper.isAdmin() pour vérifier
     * si un utilisateur connecté est administrateur global.
     */
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "login")
    private String login;

    /**
     * Nom de famille de l'administrateur.
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "nom")
    private String nom;

    /**
     * Prénom de l'administrateur (facultatif).
     */
    @Size(max = 128)
    @Column(name = "prenom")
    private String prenom;

    /**
     * Constructeur par défaut requis par JPA.
     */
    public Admin() {
    }

    /**
     * Constructeur avec le login uniquement.
     *
     * @param login l'uid CAS de l'administrateur
     */
    public Admin(String login) {
        this.login = login;
    }

    /**
     * @return l'uid CAS de l'administrateur
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login l'uid CAS de l'administrateur
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return le nom de famille de l'administrateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom le nom de famille de l'administrateur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return le prénom de l'administrateur
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom le prénom de l'administrateur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Le hash est basé sur le login, qui est la clé primaire de l'entité.
     */
    @Override
    public int hashCode() {
        return (login != null ? login.hashCode() : 0);
    }

    /**
     * Deux administrateurs sont considérés égaux s'ils partagent le même login.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) object;
        if ((this.login == null && other.login != null)
                || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Admin[ login=" + login + " ]";
    }
}