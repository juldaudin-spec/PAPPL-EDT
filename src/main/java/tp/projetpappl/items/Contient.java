/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.items;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "contient", catalog = "dbpappl", schema = "PAPPL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contient.findAll", query = "SELECT c FROM Contient c"),
    @NamedQuery(name = "Contient.findByContientId", query = "SELECT c FROM Contient c WHERE c.contientId = :contientId"),
    @NamedQuery(name = "Contient.findByVolumetrie", query = "SELECT c FROM Contient c WHERE c.volumetrie = :volumetrie")})
public class Contient implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "contient_id")
    private Integer contientId;
    @Column(name = "volumetrie")
    private BigInteger volumetrie;
    @JoinTable(name = "etudie", joinColumns = {
        @JoinColumn(name = "contient_id", referencedColumnName = "contient_id")}, inverseJoinColumns = {
        @JoinColumn(name = "nom_groupe", referencedColumnName = "nom_groupe")})
    @ManyToMany
    private List<Groupe> groupeList;
    @JoinColumn(name = "acronyme", referencedColumnName = "acronyme")
    @ManyToOne(optional = false)
    private Enseignement acronyme;
    @JoinColumn(name = "salle_preconisee", referencedColumnName = "numero_salle")
    @ManyToOne
    private Salle sallePreconisee;
    @JoinColumn(name = "intitule", referencedColumnName = "intitule")
    @ManyToOne(optional = false)
    private TypeLecon intitule;

    public Contient() {
    }

    public Contient(Integer contientId) {
        this.contientId = contientId;
    }

    public Integer getContientId() {
        return contientId;
    }

    public void setContientId(Integer contientId) {
        this.contientId = contientId;
    }

    public BigInteger getVolumetrie() {
        return volumetrie;
    }

    public void setVolumetrie(BigInteger volumetrie) {
        this.volumetrie = volumetrie;
    }

    @XmlTransient
    public List<Groupe> getGroupeList() {
        return groupeList;
    }

    public void setGroupeList(List<Groupe> groupeList) {
        this.groupeList = groupeList;
    }

    public Enseignement getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(Enseignement acronyme) {
        this.acronyme = acronyme;
    }

    public Salle getSallePreconisee() {
        return sallePreconisee;
    }

    public void setSallePreconisee(Salle sallePreconisee) {
        this.sallePreconisee = sallePreconisee;
    }

    public TypeLecon getIntitule() {
        return intitule;
    }

    public void setIntitule(TypeLecon intitule) {
        this.intitule = intitule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contientId != null ? contientId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contient)) {
            return false;
        }
        Contient other = (Contient) object;
        if ((this.contientId == null && other.contientId != null) || (this.contientId != null && !this.contientId.equals(other.contientId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Contient[ contientId=" + contientId + " ]";
    }
    
}
