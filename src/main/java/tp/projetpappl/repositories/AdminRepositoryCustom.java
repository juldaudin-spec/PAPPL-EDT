package tp.projetpappl.repositories;

import tp.projetpappl.items.Admin;

public interface AdminRepositoryCustom {
    public Admin getByLogin(String login);
    public Admin create(String login, String nom, String prenom);
    public void remove(String login);
}