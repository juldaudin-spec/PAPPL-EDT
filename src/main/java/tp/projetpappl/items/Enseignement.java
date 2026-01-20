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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "enseignement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enseignement.findAll", query = "SELECT e FROM Enseignement e"),
    @NamedQuery(name = "Enseignement.findByAcronyme", query = "SELECT e FROM Enseignement e WHERE e.acronyme = :acronyme"),
    @NamedQuery(name = "Enseignement.findByNomEnseignement", query = "SELECT e FROM Enseignement e WHERE e.nomEnseignement = :nomEnseignement"),
    @NamedQuery(name = "Enseignement.findByFiliere", query = "SELECT e FROM Enseignement e WHERE e.filiere = :filiere")})
public class Enseignement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "acronyme")
    private String acronyme;
    @Size(max = 128)
    @Column(name = "nom_enseignement")
    private String nomEnseignement;
    @Size(max = 64)
    @Column(name = "filiere")
    private String filiere;
    @JoinTable(name = "enseigne", joinColumns = {
        @JoinColumn(name = "acronyme", referencedColumnName = "acronyme")}, inverseJoinColumns = {
        @JoinColumn(name = "initiales", referencedColumnName = "initiales")})
    @ManyToMany
    private List<Enseignant> enseignantList;
    @JoinColumn(name = "responsable", referencedColumnName = "initiales")
    @ManyToOne(optional = false)
    private Enseignant responsable;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acronyme")
    private List<Contient> contientList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "acronyme")
    private List<Seance> seanceList;

    public Enseignement() {
    }

    public Enseignement(String acronyme) {
        this.acronyme = acronyme;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    public String getNomEnseignement() {
        return nomEnseignement;
    }

    public void setNomEnseignement(String nomEnseignement) {
        this.nomEnseignement = nomEnseignement;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    @XmlTransient
    public List<Enseignant> getEnseignantList() {
        return enseignantList;
    }

    public void setEnseignantList(List<Enseignant> enseignantList) {
        this.enseignantList = enseignantList;
    }

    public Enseignant getResponsable() {
        return responsable;
    }

    public void setResponsable(Enseignant responsable) {
        this.responsable = responsable;
    }

    @XmlTransient
    public List<Contient> getContientList() {
        return contientList;
    }

    public void setContientList(List<Contient> contientList) {
        this.contientList = contientList;
    }

    @XmlTransient
    public List<Seance> getSeanceList() {
        return seanceList;
    }

    public void setSeanceList(List<Seance> seanceList) {
        this.seanceList = seanceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acronyme != null ? acronyme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Enseignement)) {
            return false;
        }
        Enseignement other = (Enseignement) object;
        if ((this.acronyme == null && other.acronyme != null) || (this.acronyme != null && !this.acronyme.equals(other.acronyme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Enseignement[ acronyme=" + acronyme + " ]";
    }
    public int compareTo(Object object) {
        if (object == null) {
            return 1;
        } else if (!(object instanceof Enseignement)) {
            return 1;
        }
        Enseignement itemId = (Enseignement) object;
        if (this.getNomEnseignement().toUpperCase().equals(itemId.getNomEnseignement().toUpperCase())){
            return this.getAcronyme().toUpperCase().compareTo(itemId.getAcronyme().toUpperCase());
        } else{
            return this.getNomEnseignement().toUpperCase().compareTo(itemId.getNomEnseignement().toUpperCase());
        }
    }
    public static Comparator<Enseignement> getComparator() {
        return new Comparator<Enseignement>() {
            @Override
            public int compare(Enseignement object1, Enseignement object2){
                if (object1 == null){
                    return -1;
                } else{
                    return object1.compareTo(object2);
                }
            }
        };
    }
}
