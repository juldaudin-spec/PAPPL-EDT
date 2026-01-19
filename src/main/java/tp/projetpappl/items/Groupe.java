/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.items;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
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
@Table(name = "groupe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Groupe.findAll", query = "SELECT g FROM Groupe g"),
    @NamedQuery(name = "Groupe.findByNbEleve", query = "SELECT g FROM Groupe g WHERE g.nbEleve = :nbEleve"),
    @NamedQuery(name = "Groupe.findByNomGroupe", query = "SELECT g FROM Groupe g WHERE g.nomGroupe = :nomGroupe")})
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "nb_eleve")
    private Integer nbEleve;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "nom_groupe")
    private String nomGroupe;
    @ManyToMany(mappedBy = "groupeList")
    private List<Seance> seanceList;
    @JoinTable(name = "depend", joinColumns = {
        @JoinColumn(name = "fils", referencedColumnName = "nom_groupe")}, inverseJoinColumns = {
        @JoinColumn(name = "pere", referencedColumnName = "nom_groupe")})
    @ManyToMany
    private List<Groupe> groupeList;
    @ManyToMany(mappedBy = "groupeList")
    private List<Groupe> groupeList1;
    @ManyToMany(mappedBy = "groupeList")
    private List<Contient> contientList;

    public Groupe() {
    }

    public Groupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    public Integer getNbEleve() {
        return nbEleve;
    }

    public void setNbEleve(Integer nbEleve) {
        this.nbEleve = nbEleve;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public void setNomGroupe(String nomGroupe) {
        this.nomGroupe = nomGroupe;
    }

    @XmlTransient
    public List<Seance> getSeanceList() {
        return seanceList;
    }

    public void setSeanceList(List<Seance> seanceList) {
        this.seanceList = seanceList;
    }

    @XmlTransient
    public List<Groupe> getGroupeList() {
        return groupeList;
    }

    public void setGroupeList(List<Groupe> groupeList) {
        this.groupeList = groupeList;
    }

    @XmlTransient
    public List<Groupe> getGroupeList1() {
        return groupeList1;
    }

    public void setGroupeList1(List<Groupe> groupeList1) {
        this.groupeList1 = groupeList1;
    }

    @XmlTransient
    public List<Contient> getContientList() {
        return contientList;
    }

    public void setContientList(List<Contient> contientList) {
        this.contientList = contientList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nomGroupe != null ? nomGroupe.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groupe)) {
            return false;
        }
        Groupe other = (Groupe) object;
        if ((this.nomGroupe == null && other.nomGroupe != null) || (this.nomGroupe != null && !this.nomGroupe.equals(other.nomGroupe))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Groupe[ nomGroupe=" + nomGroupe + " ]";
    }
    
}
