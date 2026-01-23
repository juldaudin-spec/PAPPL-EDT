/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 *
 * @author kwyhr
 */
@Entity
@Table(name = "connection")
@NamedQueries({
    @NamedQuery(name = "Connection.findAll", query = "SELECT c FROM Connection c"),
    @NamedQuery(name = "Connection.findByConnectionCode", query = "SELECT c FROM Connection c WHERE c.connectionCode = :connectionCode"),
    @NamedQuery(name = "Connection.findByConnectionLogin", query = "SELECT c FROM Connection c WHERE c.connectionLogin = :connectionLogin")})
public class Connection implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "connection_code")
    private String connectionCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "connection_login")
    private String connectionLogin;

    public Connection() {
    }

    public Connection(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public Connection(String connectionCode, String connectionLogin) {
        this.connectionCode = connectionCode;
        this.connectionLogin = connectionLogin;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionLogin() {
        return connectionLogin;
    }

    public void setConnectionLogin(String connectionLogin) {
        this.connectionLogin = connectionLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (connectionCode != null ? connectionCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Connection)) {
            return false;
        }
        Connection other = (Connection) object;
        if ((this.connectionCode == null && other.connectionCode != null) || (this.connectionCode != null && !this.connectionCode.equals(other.connectionCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tp.projetpappl.items.Connection[ connectionCode=" + connectionCode + " ]";
    }
    
}
