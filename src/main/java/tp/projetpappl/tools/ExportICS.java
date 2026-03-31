/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.tools;

import java.io.File;
import java.util.List;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;
import java.util.Calendar;
import java.util.Date;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Salle;

/**
 *
 * @author julda
 */
public class ExportICS {

    private static String getStringFromDateICS(Date date) {
        Integer year = date.getYear() + 1900;
        Integer month = date.getMonth() + 1;
        Integer day = date.getDate();
        Integer hours = date.getHours();
        Integer minutes = date.getMinutes();
        Integer seconds = date.getSeconds();
        String monthstr = month.toString();
        if (month < 10) {
            monthstr = "0" + month.toString();
        }
        String daystr = day.toString();
        if (day < 10) {
            daystr = "0" + day.toString();
        }
        String hoursstr = hours.toString();
        if (hours < 10) {
            hoursstr = "0" + hours.toString();
        }
        String minutesstr = minutes.toString();
        if (minutes < 10) {
            minutesstr = "0" + minutes.toString();
        }
        String secondsstr = seconds.toString();
        if (seconds < 10) {
            secondsstr = "0" + seconds.toString();
        }
        String returned = year.toString() + monthstr + daystr + "T" + hoursstr + minutesstr + secondsstr;

        return returned;

    }
    
    //Create the event at the ICS format (like it is when we export from Onboard) 
    private static String createEvent(Seance seance) {
        Date debut = seance.getHDebut(); // Date actuelle
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(debut);
        calendar.add(Calendar.MINUTE, seance.getDuree()); // Ajoute 10 minutes
        Date fin = calendar.getTime();
        String debutstr = getStringFromDateICS(debut);
        String finstr = getStringFromDateICS(fin);
        String returned = "BEGIN:VEVENT\n"
                + "DTSTART;TZID=Europe/Paris:" + debutstr + "\n"
                + "DTEND;TZID=Europe/Paris:" + finstr + "\n"
                + "SUMMARY:" + seance.getIntitule().getIntitule() + "_" + seance.getAcronyme().getAcronyme() + "\n"
                + "DESCRIPTION;ENCODING=QUOTED-PRINTABLE:- Infos : =0A- Groupe(s) :";
        for (Groupe groupe : seance.getGroupeList()) {
            returned = returned + ", " + groupe.getNomGroupe();
        }
        returned = returned + "=0A - Intervenant(s) :";
        for (Enseignant enseignant : seance.getEnseignantList()) {
            returned = returned + " " + enseignant.getNomEnseignant();
        }
        returned = returned + "=0A- Zoom : =0A\n"
                + "LOCATION:";
        int i = 0;
        for (Salle salle : seance.getSalleList()) {
            if (i > 0) {
                returned = returned + " / ";
            }
            returned = returned + salle.getNumeroSalle();
        }
        returned = returned + "\nEND:VEVENT\n";
        return returned;
    }

    /**
     *
     * @param groupe groupe we want a EDT from
     * @return EDT at the ICS format with all seances from a groupe (like it is when we export an ICS file from Onboard)
     */
    public static String createCalendarGroupe(Groupe groupe) {
        List<Seance> listSeance = groupe.getSeanceList();
        String returned = "BEGIN:VCALENDAR\n"
                + "METHOD:PUBLISH\n"
                + "VERSION:2.0\n"
                + "X-WR-CALNAME:" + groupe.getNomGroupe() + "\n"
                + "PRODID:-//Apple Inc.//iCal 4.0.4//EN\n"
                + "X-APPLE-CALENDAR-COLOR:#E51717\n"
                + "X-WR-TIMEZONE:Europe/Paris\n"
                + "CALSCALE:GREGORIAN\n";
        for (Seance seance : listSeance) {
            returned = returned + createEvent(seance);
        }
        returned = returned + "END:VCALENDAR";
        return returned;
    }
}
