/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.items;

import jakarta.persistence.Basic;
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
@Table(name = "salle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Salle.findAll", query = "SELECT s FROM Salle s"),
    @NamedQuery(name = "Salle.findByNumeroSalle", query = "SELECT s FROM Salle s WHERE s.numeroSalle = :numeroSalle"),
    @NamedQuery(name = "Salle.findByCapacite", query = "SELECT s FROM Salle s WHERE s.capacite = :capacite"),
    @NamedQuery(name = "Salle.findByTypologie", query = "SELECT s FROM Salle s WHERE s.typologie = :typologie")})
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "numero_salle")
    private String numeroSalle;
    @Column(name = "capacite")
    private Integer capacite;
    @Size(max = 20)
    @Column(name = "typologie")
    private String typologie;
    @ManyToMany(mappedBy = "salleList")
    private List<Seance> seanceList;
    @OneToMany(mappedBy = "sallePreconisee")
    private List<Contient> contientList;

    public Salle() {
    }

    public Salle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public String getNumeroSalle() {
        return numeroSalle;
    }

    public void setNumeroSalle(String numeroSalle) {
        this.numeroSalle = numeroSalle;
    }

    public Integer getCapacite() {
        return capacite;
    }

    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }

    public String getTypologie() {
        return typologie;
    }

    public void setTypologie(String typologie) {
        this.typologie = typologie;
    }

    @XmlTransient
    public List<Seance> getSeanceList() {
        return seanceList;
    }

    public void setSeanceList(List<Seance> seanceList) {
        this.seanceList = seanceList;
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
        hash += (numeroSalle != null ? numeroSalle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salle)) {
            return false;
        }
        Salle other = (Salle) object;
        if ((this.numeroSalle == null && other.numeroSalle != null) || (this.numeroSalle != null && !this.numeroSalle.equals(other.numeroSalle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Salle[ numeroSalle=" + numeroSalle + " ]";
    }
    
}
