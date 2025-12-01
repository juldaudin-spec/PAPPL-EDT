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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "seance", catalog = "dbpappl", schema = "PAPPL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seance.findAll", query = "SELECT s FROM Seance s"),
    @NamedQuery(name = "Seance.findByHDebut", query = "SELECT s FROM Seance s WHERE s.hDebut = :hDebut"),
    @NamedQuery(name = "Seance.findByDuree", query = "SELECT s FROM Seance s WHERE s.duree = :duree"),
    @NamedQuery(name = "Seance.findByIdSeance", query = "SELECT s FROM Seance s WHERE s.idSeance = :idSeance")})
public class Seance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "h_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hDebut;
    @Column(name = "duree")
    private Integer duree;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_seance")
    private Integer idSeance;
    @JoinTable(name = "participe", joinColumns = {
        @JoinColumn(name = "id_seance", referencedColumnName = "id_seance")}, inverseJoinColumns = {
        @JoinColumn(name = "nom_groupe", referencedColumnName = "nom_groupe")})
    @ManyToMany
    private List<Groupe> groupeList;
    @JoinTable(name = "donne_cours", joinColumns = {
        @JoinColumn(name = "id_seance", referencedColumnName = "id_seance")}, inverseJoinColumns = {
        @JoinColumn(name = "initiales", referencedColumnName = "initiales")})
    @ManyToMany
    private List<Enseignant> enseignantList;
    @JoinTable(name = "a_lieu", joinColumns = {
        @JoinColumn(name = "id_seance", referencedColumnName = "id_seance")}, inverseJoinColumns = {
        @JoinColumn(name = "numero_salle", referencedColumnName = "numero_salle")})
    @ManyToMany
    private List<Salle> salleList;
    @JoinColumn(name = "acronyme", referencedColumnName = "acronyme")
    @ManyToOne(optional = false)
    private Enseignement acronyme;
    @JoinColumn(name = "intitule", referencedColumnName = "intitule")
    @ManyToOne(optional = false)
    private TypeLecon intitule;

    public Seance() {
    }

    public Seance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    public Date getHDebut() {
        return hDebut;
    }

    public void setHDebut(Date hDebut) {
        this.hDebut = hDebut;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Integer getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    @XmlTransient
    public List<Groupe> getGroupeList() {
        return groupeList;
    }

    public void setGroupeList(List<Groupe> groupeList) {
        this.groupeList = groupeList;
    }

    @XmlTransient
    public List<Enseignant> getEnseignantList() {
        return enseignantList;
    }

    public void setEnseignantList(List<Enseignant> enseignantList) {
        this.enseignantList = enseignantList;
    }

    @XmlTransient
    public List<Salle> getSalleList() {
        return salleList;
    }

    public void setSalleList(List<Salle> salleList) {
        this.salleList = salleList;
    }

    public Enseignement getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(Enseignement acronyme) {
        this.acronyme = acronyme;
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
        hash += (idSeance != null ? idSeance.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seance)) {
            return false;
        }
        Seance other = (Seance) object;
        if ((this.idSeance == null && other.idSeance != null) || (this.idSeance != null && !this.idSeance.equals(other.idSeance))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Seance[ idSeance=" + idSeance + " ]";
    }
    
}
