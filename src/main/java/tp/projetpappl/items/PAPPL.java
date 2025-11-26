/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tp.projetpappl.items;
import autre.DataBaseTools;
import java.sql.SQLException;
/**
 *
 * @author nathan
 */
public class PAPPL {

    public static void main(String[] args) throws SQLException {
        System.out.println("Hello World!");
        DataBaseTools database = new DataBaseTools();
        database.creationTable();
    }
}
