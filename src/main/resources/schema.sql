
drop table if exists BestellungsInhalt;
drop table if exists Bestellungen;
drop table if exists Bewertungen;
drop table if exists Kunden;
drop table if exists Produkttypen;
drop table if exists Mitarbeiter;

create table Mitarbeiter(
                            namenskuerzel char(4) NOT NULL PRIMARY KEY,
                            name varchar(255) NOT NULL,
                            rolle varchar(32) NOT NULL CONSTRAINT cn_rolle CHECK (rolle in ('MEISTER', 'CHEF', 'ANGESTELLTER', 'LEHRLING')),
                            monatsgehalt numeric(7,2) NOT NULL CONSTRAINT positives_gehalt CHECK (monatsgehalt > 0)
);

create table Produkttypen(
                             produkttyp_ID int NOT NULL PRIMARY KEY,
                             produktart varchar(255) NOT NULL,
                             holzart varchar(255) NOT NULL CONSTRAINT correct_holzart CHECK (holzart in ('EICHE', 'FICHTE', 'KIRSCHE', 'TANNE', 'BIRKE', 'ZIRBE', 'BUCHE'))
);

create table Kunden(
                       kunden_id int NOT NULL PRIMARY KEY,
                       email varchar(255) NOT NULL,
                       name varchar(255) NOT NULL,
                       CONSTRAINT valid_email CHECK (email LIKE '_%@_%._%')
);


create table Bewertungen (
                             bewertungsnummer int NOT NULL PRIMARY KEY,
                             titel varchar(255) NOT NULL,
                             bewertungs_text text,
                             sterne int NOT NULL CONSTRAINT correctamount_sterne check(sterne >= 0 AND sterne <=5),
                             kunde int NOT NULL,
                             CONSTRAINT FK_bewertung_kunde FOREIGN KEY (kunde) REFERENCES Kunden(kunden_id) ON DELETE Cascade
);


create table Bestellungen (
                              bestellnummer int NOT NULL PRIMARY KEY,
                              bestelldatum date NOT NULL,
                              kunde int,
                              CONSTRAINT FK_Bestellung_kunde FOREIGN KEY(kunde) REFERENCES Kunden(kunden_id) ON DELETE SET NULL,
                              mitarbeiter char(4),
                              CONSTRAINT FK_Bestellung_mitarbeiter FOREIGN KEY(mitarbeiter) REFERENCES Mitarbeiter(namenskuerzel) ON DELETE SET NULL
);

create table BestellungsInhalt (
                                   bestellnummer int NOT NULL,
                                   produkttyp int,
                                   amount int CONSTRAINT correct_amount CHECK(amount > 0),
                                   CONSTRAINT PK_BestellungsInhalt PRIMARY KEY(bestellnummer, produkttyp),
                                   CONSTRAINT FK_BestellungsInahlt_produkt FOREIGN KEY(produkttyp) REFERENCES Produkttypen(produkttyp_ID),
                                   CONSTRAINT FK_BestellungsInahlt_bestellung FOREIGN KEY(bestellnummer) REFERENCES Bestellungen(bestellnummer)
);