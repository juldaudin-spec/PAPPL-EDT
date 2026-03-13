/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package tp.projetpappl.repositories;

import java.math.BigInteger;
import tp.projetpappl.items.Contient;

/**
 *
 * @author nathan
 */
public interface ContientRepositoryCustom {
    public Contient create(String acronyme, String intitule, BigInteger heures, String salle);
    
}
