
-- Mitarbeiter
insert into Mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
values ('MAHA', 'Mayer Hans', 'ANGESTELLTER', 2400);

insert into Mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
values ('WABE', 'Wallner Benedikt', 'CHEF', 4000);

insert into Mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
values ('WILU', 'Wisberger Lukas', 'LEHRLING', 1100);

insert into Mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
values ('KAFL', 'Kainrath Florian', 'MEISTER', 3999);

insert into Mitarbeiter (namenskuerzel, name, rolle, monatsgehalt)
values ('HATH', 'Hablecker Thomas', 'ANGESTELLTER', 2700);

-- 'Eiche', 'Fichte', 'Kirsche', 'Tanne', 'Birke', 'Zirbe', 'Buche'
insert into Produkttypen(produkttyp_id, holzart, produktart)
values (1, 'FICHTE', 'Nachttisch');

insert into Produkttypen(produkttyp_id, holzart, produktart)
values (2, 'EICHE', 'Esstisch');

insert into Produkttypen(produkttyp_id, holzart, produktart)
values (3, 'KIRSCHE', 'Sessel');

insert into Produkttypen(produkttyp_id, holzart, produktart)
values (4, 'ZIRBE', 'Bett');

insert into Produkttypen(produkttyp_id, holzart, produktart)
values (5, 'BUCHE', 'Kleiderschrank');


-- Kunden
insert into Kunden (kunden_id, email, name)
values (1, 'eine@kunden.mail', 'Meier Johann');

insert into Kunden (kunden_id, email, name)
values (2, 'manfred@k.com', 'Koller Manfred');

insert into Kunden (kunden_id, email, name)
values (3, 'christoph@schreiber.htl', 'Riesenhuber Gerd');

insert into Kunden (kunden_id, email, name)
values (4, 'nikolaus@gmail.com', 'Zachhalmel Nikolaus');

insert into Kunden (kunden_id, email, name)
values (5, 'goetz@gmail.com', 'Götz Jacob');

-- Bewertung
insert into Bewertungen (bewertungsnummer, titel, bewertungs_text, sterne, kunde)
values (1, 'Tolles Unternehmen', 'Würde sofort wieder dort kaufen!', 5, 1);

insert into Bewertungen (bewertungsnummer, titel, bewertungs_text, sterne, kunde)
values (2, 'Nie wieder! :(', 'Bitte nicht dort kaufen', 0, 1);

insert into Bewertungen (bewertungsnummer, titel, bewertungs_text, sterne, kunde)
values (3, 'Nicht sehr toll :(', 'Schlechte Beratung', 0, 4);

insert into Bewertungen (bewertungsnummer, titel, bewertungs_text, sterne, kunde)
values (4, 'Möbel sind beschädigt', 'Einige Möbel waren leider beschädigt, sonst toller Service ;)', 3, 2);

insert into Bewertungen (bewertungsnummer, titel, bewertungs_text, sterne, kunde)
values (5, 'Besser gehts nicht!!!', 'Kaufen, Kaufen, Kaufen!!!', 5, 5);

-- Bestellung
insert into Bestellungen (bestellnummer, bestelldatum, kunde, mitarbeiter)
values (1, '2020-09-09', 1, 'HATH');

insert into Bestellungen (bestellnummer, bestelldatum, kunde, mitarbeiter)
values (2, '2021-10-19', 4, 'MAHA');

insert into Bestellungen (bestellnummer, bestelldatum, kunde, mitarbeiter)
values (3, '2021-10-21', 2, 'WILU');

insert into Bestellungen (bestellnummer, bestelldatum, kunde, mitarbeiter)
values (4, '2021-11-03', 3, 'WABE');

insert into Bestellungen (bestellnummer, bestelldatum, kunde, mitarbeiter)
values (5, '2020-11-10', 5, 'KAFL');

-- Bestellungsinhalt
insert into BestellungsInhalt (bestellnummer, produkttyp, amount)
values (1, 1, 3);

insert into BestellungsInhalt (bestellnummer,produkttyp, amount)
values (1, 4, 1);

insert into BestellungsInhalt (bestellnummer, produkttyp, amount)
values (2, 3, 5);

insert into BestellungsInhalt (bestellnummer,produkttyp, amount)
values (3, 1, 3);

insert into BestellungsInhalt (bestellnummer, produkttyp, amount)
values (1, 2, 3);

insert into BestellungsInhalt (bestellnummer, produkttyp, amount)
values (2, 1, 3);