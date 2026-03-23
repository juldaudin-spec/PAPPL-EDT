# PAPPL-EDT — Guide de démarrage

## Prérequis

- **Java JDK 17+** — [Télécharger](https://www.oracle.com/java/technologies/downloads/)
- **Apache Tomcat 10.1.x** — [Télécharger](https://tomcat.apache.org/download-10.cgi) (choisir la version 10.1, **pas** 11)
- **PostgreSQL 14+** — [Télécharger](https://www.postgresql.org/download/)
- **Maven 3.8+** — [Télécharger](https://maven.apache.org/download.cgi)

---

## Étape 1 — Configurer PostgreSQL

1. Créer une base de données :
```sql
CREATE DATABASE dbpappl;
```

2. Ouvrir le fichier `src/main/resources/META-INF/persistence.xml` et modifier les paramètres selon votre configuration PostgreSQL :

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/dbpappl"/>
<property name="jakarta.persistence.jdbc.user" value="postgres"/>
<property name="jakarta.persistence.jdbc.password" value="VOTRE_MOT_DE_PASSE"/>
```

Remplacez `VOTRE_MOT_DE_PASSE` par le mot de passe de votre utilisateur PostgreSQL.

---

## Étape 2 — Initialiser la base de données

1. Exécuter le script SQL fourni **`SQLscript.sql`** dans votre base `dbpappl` :

```bash
psql -U postgres -d dbpappl -f SQLscript.sql
```

Ou via pgAdmin : ouvrir l'outil Query Tool, charger le fichier `SQLscript.sql` et l'exécuter.

Ce script crée toutes les tables nécessaires et insère des données de base.

---

## Étape 3 — Ajouter votre compte administrateur

Pour accéder aux fonctionnalités d'administration, vous devez vous ajouter en tant qu'admin global. Exécutez ces deux requêtes SQL en remplaçant les valeurs par les vôtres :

```sql
-- Ajouter votre compte enseignant (si pas déjà fait)
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login)
VALUES ('XX', 'VotreNom', 'VotrePrenom', 'votre_uid_cas');

-- Ajouter votre compte admin
INSERT INTO admin (login, nom, prenom)
VALUES ('votre_uid_cas', 'VotreNom', 'VotrePrenom');
```

> **Important :** Le champ `login` doit correspondre exactement à votre **uid CAS** de Centrale Nantes (exemple : `p.nom` ou `prenom.nom2024`). C'est ce que le serveur CAS retourne lors de votre connexion.

---

## Étape 4 — Compiler le projet

Dans le répertoire racine du projet, exécuter :

```bash
mvn clean package
```

Le fichier `pappl.war` sera généré dans le dossier `target/`.

---

## Étape 5 — Déployer sur Tomcat

1. Copier le fichier `target/pappl.war` dans le dossier `webapps/` de votre installation Tomcat :

```bash
cp target/pappl.war /chemin/vers/tomcat/webapps/
```

2. Démarrer Tomcat :

```bash
# Linux/Mac
/chemin/vers/tomcat/bin/startup.sh

# Windows
C:\tomcat\bin\startup.bat
```

3. Accéder à l'application dans votre navigateur :

```
http://localhost:8080/pappl
```

---

## Étape 6 — Connexion

L'application utilise l'authentification **CAS de Centrale Nantes**. Cliquez sur "Se connecter" et entrez vos identifiants ECN.

> Les utilisateurs sans compte dans la table `admin` ou sans enseignement dont ils sont responsables auront un accès en **lecture seule**.

---

## Structure des permissions

| Rôle | Accès |
|------|-------|
| **Visiteur connecté (CAS)** | Consultation de l'emploi du temps uniquement |
| **Responsable de filière** | Modification des enseignements, séances et contenus de sa filière |
| **Admin global** | Accès complet à toutes les fonctionnalités |


