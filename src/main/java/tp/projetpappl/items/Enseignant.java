/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.items;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "enseignant")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enseignant.findAll", query = "SELECT e FROM Enseignant e"),
    @NamedQuery(name = "Enseignant.findByInitiales", query = "SELECT e FROM Enseignant e WHERE e.initiales = :initiales"),
    @NamedQuery(name = "Enseignant.findByNomEnseignant", query = "SELECT e FROM Enseignant e WHERE e.nomEnseignant = :nomEnseignant"),
    @NamedQuery(name = "Enseignant.findByPrenom", query = "SELECT e FROM Enseignant e WHERE e.prenom = :prenom"),
    @NamedQuery(name = "Enseignant.findByLogin", query = "SELECT e FROM Enseignant e WHERE e.login = :login"),
    @NamedQuery(name = "Enseignant.findByMdp", query = "SELECT e FROM Enseignant e WHERE e.mdp = :mdp")})
public class Enseignant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "initiales")
    private String initiales;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "nom_enseignant")
    private String nomEnseignant;
    @Size(max = 64)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 64)
    @Column(name = "login")
    private String login;
    @Size(max = 64)
    @Column(name = "mdp")
    private String mdp;
    @ManyToMany(mappedBy = "enseignantList")
    private List<Seance> seanceList;
    @ManyToMany(mappedBy = "enseignantList")
    private List<Enseignement> enseignementList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "responsable")
    private List<Enseignement> enseignementList1;

    public Enseignant() {
    }

    public Enseignant(String initiales) {
        this.initiales = initiales;
    }

    public Enseignant(String initiales, String nomEnseignant) {
        this.initiales = initiales;
        this.nomEnseignant = nomEnseignant;
    }

    public String getInitiales() {
        return initiales;
    }

    public void setInitiales(String initiales) {
        this.initiales = initiales;
    }

    public String getNomEnseignant() {
        return nomEnseignant;
    }

    public void setNomEnseignant(String nomEnseignant) {
        this.nomEnseignant = nomEnseignant;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    @XmlTransient
    public List<Seance> getSeanceList() {
        return seanceList;
    }

    public void setSeanceList(List<Seance> seanceList) {
        this.seanceList = seanceList;
    }

    @XmlTransient
    public List<Enseignement> getEnseignementList() {
        return enseignementList;
    }

    public void setEnseignementList(List<Enseignement> enseignementList) {
        this.enseignementList = enseignementList;
    }

    @XmlTransient
    public List<Enseignement> getEnseignementList1() {
        return enseignementList1;
    }

    public void setEnseignementList1(List<Enseignement> enseignementList1) {
        this.enseignementList1 = enseignementList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (initiales != null ? initiales.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enseignant)) {
            return false;
        }
        Enseignant other = (Enseignant) object;
        if ((this.initiales == null && other.initiales != null) || (this.initiales != null && !this.initiales.equals(other.initiales))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Enseignant[ initiales=" + initiales + " ]";
    }
    
}
