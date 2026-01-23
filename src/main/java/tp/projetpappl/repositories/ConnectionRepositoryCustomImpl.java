/* -----------------------------------------
 * Projet Kepler
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
package tp.projetpappl.repositories;

import tp.projetpappl.items.*;
import java.util.Date;
import java.util.Optional;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kwyhr
 */
@Repository
public class ConnectionRepositoryCustomImpl implements ConnectionRepositoryCustom {

    @Autowired
    @Lazy
    private ConnectionRepository repository;

    /**
     * Create pseudo-random code
     *
     * @param login
     * @param now
     * @return
     */
    private static String createCode(Date now) {
        StringBuilder newCode = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        for (int i = 0; i < 5; i++) {
            char c = (char) ('A' + (int) (Math.random() * 26));
            newCode.append(c);
        }
        newCode.append(sdf.format(now));
        for (int i = 0; i < 3; i++) {
            char c = (char) ('A' + (int) (Math.random() * 26));
            newCode.append(c);
        }
        return newCode.toString();
    }

    @Override
    public Connection create(String login) {
        Connection item = null;

        System.out.println("=== ConnectionRepository.create() ===");
        System.out.println("Login parameter: [" + login + "]");

        if ((login != null) && (!login.isEmpty())) {
            try {
                Calendar aCalendar = Calendar.getInstance();
                Date now = aCalendar.getTime();
                String connexionCode = createCode(now);

                System.out.println("Generated connection code: " + connexionCode);

                item = new Connection();
                item.setConnectionCode(connexionCode);
                item.setConnectionLogin(login);

                System.out.println("About to save: code=" + item.getConnectionCode() + ", login=" + item.getConnectionLogin());

                repository.saveAndFlush(item);

                System.out.println("SUCCESS: Saved to database!");

            } catch (Exception e) {
                System.out.println("ERROR in ConnectionRepository.create():");
                System.out.println("Message: " + e.getMessage());
                e.printStackTrace();
                item = null;
            }
        } else {
            System.out.println("ERROR: Login is null or empty!");
        }

        System.out.println("=== End ConnectionRepository.create() ===");
        return item;
    }

    @Override
    public void remove(Connection item) {
        if (item != null) {
            item = repository.getByConnectionCode(item.getConnectionCode());
            if (item != null) {
                repository.delete(item);
                repository.flush();
            }
        }
    }

    @Override
    public Connection getByConnectionCode(String connectionCode) {
        if ((connectionCode != null) && (!connectionCode.isEmpty())) {
            Optional<Connection> result = repository.findById(connectionCode);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }
}