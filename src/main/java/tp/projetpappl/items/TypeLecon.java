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
@Table(name = "type_lecon")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeLecon.findAll", query = "SELECT t FROM TypeLecon t"),
    @NamedQuery(name = "TypeLecon.findByIntitule", query = "SELECT t FROM TypeLecon t WHERE t.intitule = :intitule"),
    @NamedQuery(name = "TypeLecon.findByNbEnseignant", query = "SELECT t FROM TypeLecon t WHERE t.nbEnseignant = :nbEnseignant")})
public class TypeLecon implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "nb_enseignant")
    private Integer nbEnseignant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeLecon")
    private List<Contient> contientList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "intitule")
    private List<Seance> seanceList;

    public TypeLecon() {
    }

    public TypeLecon(String intitule) {
        this.intitule = intitule;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public Integer getNbEnseignant() {
        return nbEnseignant;
    }

    public void setNbEnseignant(Integer nbEnseignant) {
        this.nbEnseignant = nbEnseignant;
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
        hash += (intitule != null ? intitule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeLecon)) {
            return false;
        }
        TypeLecon other = (TypeLecon) object;
        if ((this.intitule == null && other.intitule != null) || (this.intitule != null && !this.intitule.equals(other.intitule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.TypeLecon[ intitule=" + intitule + " ]";
    }
    
}
