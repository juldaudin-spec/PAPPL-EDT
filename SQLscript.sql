-- ============================================
-- DROP ALL
-- ============================================
DROP TRIGGER IF EXISTS cleanup_expired_sessions ON connection CASCADE;
DROP FUNCTION IF EXISTS delete_expired_sessions() CASCADE;
DROP TABLE IF EXISTS participe CASCADE;
DROP TABLE IF EXISTS donne_cours CASCADE;
DROP TABLE IF EXISTS a_lieu CASCADE;
DROP TABLE IF EXISTS seance CASCADE;
DROP TABLE IF EXISTS etudie CASCADE;
DROP TABLE IF EXISTS contient CASCADE;
DROP TABLE IF EXISTS depend CASCADE;
DROP TABLE IF EXISTS groupe CASCADE;
DROP TABLE IF EXISTS enseigne CASCADE;
DROP TABLE IF EXISTS gerer CASCADE;
DROP TABLE IF EXISTS enseignement CASCADE;
DROP TABLE IF EXISTS type_lecon CASCADE;
DROP TABLE IF EXISTS salle CASCADE;
DROP TABLE IF EXISTS enseignant CASCADE;
DROP TABLE IF EXISTS connection CASCADE;
DROP TABLE IF EXISTS admin CASCADE;

-- ============================================
-- CREATE TABLES
-- ============================================

CREATE TABLE enseignant (
    initiales CHARACTER VARYING(5) NOT NULL PRIMARY KEY,
    nom_enseignant CHARACTER VARYING(64) NOT NULL,
    prenom CHARACTER VARYING(64),
    login CHARACTER VARYING(64),
    mdp CHARACTER VARYING(64)
);

CREATE TABLE enseignement (
    acronyme CHARACTER VARYING(10) NOT NULL PRIMARY KEY,
    nom_enseignement CHARACTER VARYING(128),
    filiere CHARACTER VARYING(64),
    responsable CHARACTER VARYING(5) NOT NULL REFERENCES enseignant
);

CREATE TABLE gerer (
    gerer_id SERIAL NOT NULL PRIMARY KEY,
    initiales CHARACTER VARYING(5) NOT NULL,
    acronyme CHARACTER VARYING(10) NOT NULL,
    UNIQUE(initiales, acronyme)
);

CREATE TABLE enseigne (
    initiales CHARACTER VARYING(5) NOT NULL REFERENCES enseignant,
    acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,
    PRIMARY KEY(initiales, acronyme)
);

CREATE TABLE type_lecon (
    intitule CHARACTER VARYING(8) NOT NULL PRIMARY KEY,
    nb_enseignant INTEGER
);

CREATE TABLE salle (
    numero_salle CHARACTER VARYING(4) NOT NULL PRIMARY KEY,
    capacite INTEGER,
    typologie CHARACTER VARYING(20)
);

CREATE TABLE contient (
    contient_id SERIAL NOT NULL PRIMARY KEY,
    acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,
    intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon,
    salle_preconisee CHARACTER VARYING(4) REFERENCES salle,
    volumetrie NUMERIC,
    UNIQUE(intitule, acronyme)
);

CREATE TABLE groupe (
    nb_eleve INTEGER,
    nom_groupe CHARACTER VARYING(128) NOT NULL PRIMARY KEY
);

CREATE TABLE depend (
    pere CHARACTER VARYING(128) NOT NULL REFERENCES groupe,
    fils CHARACTER VARYING(128) NOT NULL REFERENCES groupe,
    PRIMARY KEY(pere, fils)
);

CREATE TABLE etudie (
    contient_id INTEGER NOT NULL,
    nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe,
    PRIMARY KEY(contient_id, nom_groupe),
    FOREIGN KEY (contient_id) REFERENCES contient
);

CREATE TABLE seance (
    acronyme CHARACTER VARYING(10) NOT NULL REFERENCES enseignement,
    intitule CHARACTER VARYING(8) NOT NULL REFERENCES type_lecon,
    h_debut TIMESTAMP,
    duree INTEGER,
    id_seance SERIAL NOT NULL PRIMARY KEY
);

CREATE TABLE a_lieu (
    id_seance INTEGER NOT NULL REFERENCES seance,
    numero_salle CHARACTER VARYING(4) REFERENCES salle,
    PRIMARY KEY(id_seance, numero_salle)
);

CREATE TABLE donne_cours (
    initiales CHARACTER VARYING(5) NOT NULL REFERENCES enseignant,
    id_seance INTEGER NOT NULL REFERENCES seance,
    PRIMARY KEY(initiales, id_seance)
);

CREATE TABLE participe (
    id_seance INTEGER NOT NULL REFERENCES seance,
    nom_groupe CHARACTER VARYING(128) NOT NULL REFERENCES groupe
);

CREATE TABLE connection (
    connection_code VARCHAR(128) PRIMARY KEY,
    connection_login VARCHAR(256) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
    login VARCHAR(256) PRIMARY KEY,
    nom VARCHAR(128) NOT NULL,
    prenom VARCHAR(128)
);

-- ============================================
-- INDEXES
-- ============================================

CREATE INDEX idx_connection_login ON connection(connection_login);
CREATE INDEX idx_connection_created_at ON connection(created_at);

-- ============================================
-- SESSION EXPIRATION TRIGGER
-- ============================================

CREATE OR REPLACE FUNCTION delete_expired_sessions()
RETURNS trigger AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM connection
    WHERE created_at < NOW() - INTERVAL '2 hours';

    GET DIAGNOSTICS deleted_count = ROW_COUNT;

    IF deleted_count > 0 THEN
        RAISE NOTICE 'Deleted % expired session(s)', deleted_count;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER cleanup_expired_sessions
    AFTER INSERT ON connection
    FOR EACH STATEMENT
    EXECUTE FUNCTION delete_expired_sessions();

-- ============================================
-- TYPE DE LECON
-- ============================================

INSERT INTO type_lecon (intitule, nb_enseignant) VALUES ('CM', 1);
INSERT INTO type_lecon (intitule, nb_enseignant) VALUES ('TD', 1);
INSERT INTO type_lecon (intitule, nb_enseignant) VALUES ('TP', 2);

-- ============================================
-- ENSEIGNANTS
-- ============================================

INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('OK', 'Kazoubi', 'Oussama', 'okazoubi2024');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('AD', 'Dupont', 'Alice', 'adupont');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('BM', 'Martin', 'Bruno', 'bmartin');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('CL', 'Laurent', 'Claire', 'claurent');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('DR', 'Rousseau', 'David', 'drousseau');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('EP', 'Petit', 'Emma', 'epetit');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('ND', 'Degoget', 'Nathan', 'ndegodet2024');
INSERT INTO enseignant (initiales, nom_enseignant, prenom, login) VALUES ('JD', 'Daudin', 'Julien', 'jdaudin2024');

-- ============================================
-- ENSEIGNEMENTS INFOSI
-- ============================================

INSERT INTO enseignement (acronyme, nom_enseignement, filiere, responsable) VALUES ('ALGO', 'Algorithmique', 'INFOSI', 'OK');
INSERT INTO enseignement (acronyme, nom_enseignement, filiere, responsable) VALUES ('BD', 'Bases de données', 'INFOSI', 'AD');
INSERT INTO enseignement (acronyme, nom_enseignement, filiere, responsable) VALUES ('SYS', 'Systèmes', 'INFOSI', 'BM');
INSERT INTO enseignement (acronyme, nom_enseignement, filiere, responsable) VALUES ('WEB', 'Développement Web', 'INFOSI', 'ND');
INSERT INTO enseignement (acronyme, nom_enseignement, filiere, responsable) VALUES ('IA', 'Intelligence Artificielle', 'INFOSI', 'JD');

-- ============================================
-- ADMINS
-- ============================================

INSERT INTO admin (login, nom, prenom) VALUES ('okazoubi2024', 'Kazoubi', 'Oussama');
INSERT INTO admin (login, nom, prenom) VALUES ('adupont', 'Dupont', 'Alice');
INSERT INTO admin (login, nom, prenom) VALUES ('bmartin', 'Martin', 'Bruno');
INSERT INTO admin (login, nom, prenom) VALUES ('claurent', 'Laurent', 'Claire');
INSERT INTO admin (login, nom, prenom) VALUES ('drousseau', 'Rousseau', 'David');
INSERT INTO admin (login, nom, prenom) VALUES ('epetit', 'Petit', 'Emma');
INSERT INTO admin (login, nom, prenom) VALUES ('ndegodet2024', 'Degoget', 'Nathan');;
INSERT INTO admin (login, nom, prenom) VALUES ('jdaudin2024', 'Daudin', 'Julien');

-- ============================================
-- SALLES
-- ============================================

INSERT INTO salle (numero_salle, capacite, typologie) VALUES ('C008', 30, 'TD');
INSERT INTO salle (numero_salle, capacite, typologie) VALUES ('C117', 100, 'CM');
INSERT INTO salle (numero_salle, capacite, typologie) VALUES ('C120', 30, 'TP');
INSERT INTO salle (numero_salle, capacite, typologie) VALUES ('B201', 20, 'TP');

-- ============================================
-- GROUPES
-- ============================================

INSERT INTO groupe (nom_groupe, nb_eleve) VALUES ('INFOSI', 30);
INSERT INTO groupe (nom_groupe, nb_eleve) VALUES ('INFOSI-A', 15);
INSERT INTO groupe (nom_groupe, nb_eleve) VALUES ('INFOSI-B', 15);


