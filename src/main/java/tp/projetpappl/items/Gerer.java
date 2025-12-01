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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author nathan
 */
@Entity
@Table(name = "gerer", catalog = "dbpappl", schema = "PAPPL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gerer.findAll", query = "SELECT g FROM Gerer g"),
    @NamedQuery(name = "Gerer.findByGererId", query = "SELECT g FROM Gerer g WHERE g.gererId = :gererId"),
    @NamedQuery(name = "Gerer.findByInitiales", query = "SELECT g FROM Gerer g WHERE g.initiales = :initiales"),
    @NamedQuery(name = "Gerer.findByAcronyme", query = "SELECT g FROM Gerer g WHERE g.acronyme = :acronyme")})
public class Gerer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gerer_id")
    private Integer gererId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "initiales")
    private String initiales;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "acronyme")
    private String acronyme;

    public Gerer() {
    }

    public Gerer(Integer gererId) {
        this.gererId = gererId;
    }

    public Gerer(Integer gererId, String initiales, String acronyme) {
        this.gererId = gererId;
        this.initiales = initiales;
        this.acronyme = acronyme;
    }

    public Integer getGererId() {
        return gererId;
    }

    public void setGererId(Integer gererId) {
        this.gererId = gererId;
    }

    public String getInitiales() {
        return initiales;
    }

    public void setInitiales(String initiales) {
        this.initiales = initiales;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gererId != null ? gererId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gerer)) {
            return false;
        }
        Gerer other = (Gerer) object;
        if ((this.gererId == null && other.gererId != null) || (this.gererId != null && !this.gererId.equals(other.gererId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Gerer[ gererId=" + gererId + " ]";
    }
    
}
