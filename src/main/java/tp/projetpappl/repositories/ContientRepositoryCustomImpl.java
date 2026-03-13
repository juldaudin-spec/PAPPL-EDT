/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import java.math.BigInteger;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.TypeLecon;
/**
 *
 * @author nathan
 */
@Repository
public class ContientRepositoryCustomImpl implements ContientRepositoryCustom {
    @Autowired
    @Lazy
    private EnseignementRepository enseignementRepository;
    
    @Autowired
    @Lazy
    private ContientRepository contientRepository;
    @Autowired
    @Lazy
    private TypeLeconRepository typeLeconRepository;
    @Autowired
    @Lazy
    private SalleRepository salleRepository;
    public Contient create(String acronyme, String intitule, BigInteger minutes, String salle){
        TypeLecon typeLecon = null;
        Enseignement enseignement = null;
        if (acronyme != null) {
            enseignement = enseignementRepository.getByAcronyme(acronyme);
        }
        if (intitule != null) {
            typeLecon = typeLeconRepository.getByIntitule(intitule);
        }

        // Build new seance
        if ((enseignement != null) && (typeLecon != null) && (minutes != null)) {
            Contient item = new Contient();
            item.setAcronyme(enseignement);
            item.setIntitule(typeLecon);
            item.setVolumetrie(minutes);
            item.setSallePreconisee(salleRepository.getByNumeroSalle(salle));
            contientRepository.saveAndFlush(item);

            Optional<Contient> result = contientRepository.findById(item.getContientId());
            if (result.isPresent()) {
                item = result.get();

                // Set reverse fields
                enseignement.getContientList().add(item);
                enseignementRepository.saveAndFlush(enseignement);
                
                typeLecon.getContientList().add(item);
                typeLeconRepository.saveAndFlush(typeLecon);
                // return item
                return item;
            }
        }
        return null;
    }
    
    }
