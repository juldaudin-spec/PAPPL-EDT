/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julda
 */
public class Tools {

    public static int getIntFromString(String value) {
        int intValue = -1;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            Logger.getLogger(GroupeController.class.getName()).log(Level.WARNING, null, ex);
        }
        return intValue;
    }

    public static Date getDateFromString(String aDate, String format) {
        Date returnedValue = null;
        try {
            //try to convert
            SimpleDateFormat aFormater = new SimpleDateFormat(format);
            returnedValue = aFormater.parse(aDate);
        } catch (ParseException ex) {
        }

        if (returnedValue != null) {
            Calendar aCalendar = Calendar.getInstance();
            aCalendar.setTime(returnedValue);
        }
        return returnedValue;
    }

    private static Object transform(String value, Class type) {//retourne la valeur dans le type demandé : valable seulement pour les int, String et Date sous la forme "yyyy-MM-dd'T'HH:mm"
        Object voila = null;
        if (type == int.class) {
            try {
                voila = getIntFromString(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (type == Date.class) {
                try {
                    voila = getDateFromString(value, "yyyy-MM-dd'T'HH:mm");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                voila = value;
            }
        }

        return voila;
    }

    public static ArrayList<ArrayList<Object>> haveValues(
            String file, List<Class> rowFormat) throws IOException {

        ArrayList<ArrayList<Object>> finalList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // skip header
            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                ArrayList<Object> values = new ArrayList<>();

                for (int i = 0; i < Math.min(row.length, rowFormat.size()); i++) {
                    values.add(transform(row[i], rowFormat.get(i)));
                }
                finalList.add(values);
            }
        }

        return finalList;
    }

    private static HashMap<String, String> buildIndexedMap(HttpServletRequest request, String value) {
        // Build param map
        Map<String, String[]> tempOrig = request.getParameterMap();
        if (tempOrig == null) {
            return null;
        }

        HashMap<String, String> temp = new HashMap<String, String>();
        for (String s : tempOrig.keySet()) {
            String[] valueP = tempOrig.get(s);
            if (valueP.length == 1) {
                temp.put(s, valueP[0]);
            }
        }

        return temp;
    }

    public static HashMap<String, String> getArrayFromRequest(HttpServletRequest request, String value) {
        HashMap<String, String> returnedValue = new HashMap<String, String>();
        String searchFor = value + "[";
        int len = searchFor.length();

        HashMap<String, String> temp = buildIndexedMap(request, value);

        // Retrieve array
        for (String s : temp.keySet()) {
            if (s.startsWith(searchFor)) {
                int pos = s.indexOf("]", len);
                String index = s.substring(len, pos).trim();
                if (index.isEmpty()) {
                    index = "" + returnedValue.keySet().size();
                }
                returnedValue.put(index, temp.get(s));
            }
        }

        return returnedValue;
    }

}
