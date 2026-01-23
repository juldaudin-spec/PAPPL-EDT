/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.Connection;
import tp.projetpappl.repositories.GroupeRepository;

/**
 * Ceci est le controlleur pour tout ce qui concerne l'affichage de l'emplois du
 * temps (EDT)
 *
 * @author nathan
 */
@Controller
public class AffichageController {

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private AuthHelper authHelper;

    /**
     * permet d'ouvrir la page d'affichage des EDT
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "affichageEDT.do", method = RequestMethod.POST)
    public ModelAndView handleIndexGet(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned = new ModelAndView("affichageEDT");

        if (request != null) {
            String[] idStr = request.getParameterValues("idGroupe");

            List<Groupe> groupes = groupeRepository.findAll();

            Optional<Groupe> result;
            Groupe groupe;
            List<String> listeGroupe = null;
            List<Seance> myList;
            List<List<Seance>> myList2 = null;

            if (idStr != null) {
                listeGroupe = new ArrayList<>(idStr.length);
                myList2 = new ArrayList<>(idStr.length);

                for (String elem : idStr) {
                    result = groupeRepository.findById(elem);
                    if ((result.isPresent())) {
                        groupe = result.get();
                        myList = groupe.getSeanceList();
                        Collections.sort(myList, Seance.getComparator());
                        myList2.add(myList);
                        listeGroupe.add(groupe.getNomGroupe());
                    }
                }
            }

            List<Date> listHDebut = listHDebut(myList2);
            List<LocalDate> listDate = getTheDates(listHDebut);
            List<List<List<Seance>>> listSeance = groupByDates( listDate,myList2);

            returned.addObject("groupes", groupes);
            returned.addObject("HDebut", listDate);
            returned.addObject("listeSeance", listSeance);
            returned.addObject("listeGroupe", listeGroupe);
        }
        returned.addObject("user", user);
        return returned;
    }

    /**
     * Permet de récupérer la liste des heures où les cours commencent
     *
     * @param myList2
     * @return
     */
    public List<Date> listHDebut(List<List<Seance>> myList2) {
        List<Date> listHDebut = null;
        if (myList2 != null) {
            listHDebut = new ArrayList<>();
            for (List<Seance> seanceGroupe : myList2) {
                for (Seance seance : seanceGroupe) {
                    if (!listHDebut.contains(seance.getHDebut())) {
                        listHDebut.add(seance.getHDebut());
                    }
                }
            }
            listHDebut.sort(Comparator.naturalOrder());
        }
        return listHDebut;
    }

    /**
     * permet de transformer la liste qui affiche les cours à l'horizontal vers
     * une liste qui permet d'afficher les cours à la verticale
     *
     * @param myList2
     * @param listHDebut
     * @return
     */
    public List<List<Seance>> transform(List<List<Seance>> myList2, List<Date> listHDebut) {
        List<List<Seance>> listeSeance = null;
        List<Seance> ligne;
        if (myList2 != null && listHDebut != null) {
            listeSeance = new LinkedList<>();
            for (Date horaire : listHDebut) {
                ligne = new ArrayList<>(myList2.size());
                for (List<Seance> seanceGroupe : myList2) {
                    addSeanceLigne(seanceGroupe, ligne, horaire);
                }
                listeSeance.add(ligne);
            }
        }
        return listeSeance;
    }

    /**
     * fonction qui permet d'ajouter une séance à la ligne si l'horaire de la
     * ligne correspond à celle de la séance et que tout va bien
     *
     * @param seanceGroupe
     * @param ligne
     * @param horaire
     */
    public void addSeanceLigne(List<Seance> seanceGroupe, List<Seance> ligne, Date horaire) {
        if (seanceGroupe != null && ligne != null) {
            if ((!seanceGroupe.isEmpty()) && horaire != null) {
                if (horaire.compareTo(seanceGroupe.get(0).getHDebut()) == 0) {
                    ligne.add(seanceGroupe.remove(0));
                } else {
                    ligne.add(null);
                }
            } else {
                ligne.add(null);
            }

        }
    }

    public List<LocalDate> getTheDates(List<Date> listHDebut) {
        List<LocalDate> listOfDay =null;
        if (listHDebut != null) {
            listHDebut.sort(Comparator.naturalOrder());
            Date date;
            LocalDate localDate;
            listOfDay = new ArrayList<>();
            for (int i = 0; i < listHDebut.size(); i++) {
                date = listHDebut.get(i);
                localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (!listOfDay.contains(localDate)){
                    listOfDay.add(localDate);
                }
            }
        }
        return listOfDay;
    }

    public boolean comparerDate(LocalDate date1, LocalDate date2) {
        return ((date1.getYear() == date2.getYear()) && (date1.getMonthValue() == date2.getMonthValue()) && (date1.getDayOfMonth() == date2.getDayOfMonth()));
    }

    public List<List<List<Seance>>> groupByDates(List<LocalDate> listDates, List<List<Seance>> myList2) {
        List<List<List<Seance>>> seanceByDay=null;
        if (listDates != null && myList2 != null) {
            seanceByDay = new ArrayList<>(listDates.size());//par jour et par groupe
            List<List<Seance>> seanceOfDay;
            List<Seance> seanceOfGroupe;
            listDates.sort(Comparator.naturalOrder());
            for (LocalDate date : listDates) {
                seanceOfDay = new ArrayList<>(myList2.size());
                for (List<Seance> seanceGroupe : myList2) {
                    seanceOfGroupe = new ArrayList<>();
                    Collections.sort(seanceGroupe, Seance.getComparator());
                    while (!seanceGroupe.isEmpty()&&comparerDate(date, seanceGroupe.get(0).getHDebut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())) {
                        seanceOfGroupe.add(seanceGroupe.remove(0));
                    }
                    seanceOfDay.add(seanceOfGroupe);
                }
                seanceByDay.add(seanceOfDay);
            }
        }
        return seanceByDay;
    }
}