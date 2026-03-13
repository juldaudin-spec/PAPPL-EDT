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
 * Implementation of custom Connection repository methods
 * 
 * @author Assistant
 */
@Repository
public class ConnectionRepositoryCustomImpl implements ConnectionRepositoryCustom {

    @Autowired
    @Lazy
    private ConnectionRepository repository;

    /**
     * Create pseudo-random code
     * Format: 5 random letters + timestamp + 3 random letters
     * 
     * @param now Current date
     * @return Generated code
     */
    private static String createCode(Date now) {
        StringBuilder newCode = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        // 5 random letters
        for (int i = 0; i < 5; i++) {
            char c = (char) ('A' + (int) (Math.random() * 26));
            newCode.append(c);
        }
        
        // Timestamp
        newCode.append(sdf.format(now));
        
        // 3 random letters
        for (int i = 0; i < 3; i++) {
            char c = (char) ('A' + (int) (Math.random() * 26));
            newCode.append(c);
        }
        
        return newCode.toString();
    }

    @Override
    public Connection create(String login) {
        Connection item = null;

        if ((login != null) && (!login.isEmpty())) {
            try {
                Calendar aCalendar = Calendar.getInstance();
                Date now = aCalendar.getTime();
                String connexionCode = createCode(now);

                item = new Connection();
                item.setConnectionCode(connexionCode);
                item.setConnectionLogin(login);
                repository.saveAndFlush(item);
                
            } catch (Exception e) {
                System.err.println("ERROR in ConnectionRepository.create(): " + e.getMessage());
                e.printStackTrace();
                item = null;
            }
        }
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
