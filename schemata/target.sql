drop schema target;
create schema if not exists target;
use target;
CREATE TABLE `DIM_Bezirk` (
  `bezirkId` int(11) NOT NULL DEFAULT '0',
  `bezirksnummer` int(11) NOT NULL,
  `validFrom` date DEFAULT NULL,
  `validTo` date DEFAULT NULL,
  `isValid` tinyint(1) DEFAULT NULL,
  `insertDate` date DEFAULT NULL,
  `bezirk` varchar(45) DEFAULT NULL,
  `gebiet` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `sparkasse` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bezirkId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `DIM_Buchungstag` (
  `datumId` int(11) NOT NULL AUTO_INCREMENT,
  `datum` date NOT NULL,
  `gueltigVon` date DEFAULT NULL,
  `gueltigBis` date DEFAULT NULL,
  `tag` tinyint(4) DEFAULT NULL,
  `monat` tinyint(4) DEFAULT NULL,
  `jahr` int(11) DEFAULT NULL,
  `nameTag` varchar(45) DEFAULT NULL,
  `nameMonat` varchar(45) DEFAULT NULL,
  `monatsultimo` tinyint(1) DEFAULT NULL,
  `datumLesbar` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`datumId`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;

CREATE TABLE `DIM_Konto` (
  `kontoId` int(11) NOT NULL AUTO_INCREMENT,
  `kontonummer` int(11) NOT NULL,
  `kundennummer` int(11) NOT NULL,
  `validFrom` date DEFAULT NULL,
  `validTo` date DEFAULT NULL,
  `isValid` tinyint(1) DEFAULT NULL,
  `insertDate` date DEFAULT NULL,
  `tarif` int(11) DEFAULT NULL,
  `sparziel` decimal(10,2) DEFAULT NULL,
  `beginndatum` date DEFAULT NULL,
  `einloesungsdatum` date DEFAULT NULL,
  `erhoehungsdatum` date DEFAULT NULL,
  `schliessungsdatum` date DEFAULT NULL,
  PRIMARY KEY (`kontoId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

CREATE TABLE `DIM_Kunde` (
  `kundeId` int(11) NOT NULL AUTO_INCREMENT,
  `kundennummer` int(11) NOT NULL,
  `validFrom` date DEFAULT NULL,
  `validTo` date DEFAULT NULL,
  `isValid` tinyint(1) DEFAULT NULL,
  `insertDate` date DEFAULT NULL,
  `kundentyp` char(1) DEFAULT NULL,
  `ehenummer` varchar(45) DEFAULT NULL,
  `geschlecht` tinyint(4) DEFAULT NULL,
  `familienstand` tinyint(4) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `vorname` varchar(50) DEFAULT NULL,
  `beruf` tinyint(4) DEFAULT NULL,
  `geburtsdatum` date DEFAULT NULL,
  `verstorben` tinyint(1) DEFAULT NULL,
  `strasse` varchar(255) DEFAULT NULL,
  `hausnummer` varchar(255) DEFAULT NULL,
  `plz` varchar(45) DEFAULT NULL,
  `ort` varchar(45) DEFAULT NULL,
  `land` varchar(45) DEFAULT NULL,
  `sparkasse` varchar(45) DEFAULT NULL,
  `werbungsverbot` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`kundeId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

CREATE TABLE `DIM_Umsatzart` (
  `umsatzartId` int(11) NOT NULL AUTO_INCREMENT,
  `umsatzart` int(11) NOT NULL,
  `validFrom` date DEFAULT NULL,
  `validTo` date DEFAULT NULL,
  `isValid` tinyint(1) DEFAULT NULL,
  `insertDate` date DEFAULT NULL,
  `storno` tinyint(1) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `gruppenname` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`umsatzartId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

CREATE TABLE `FACT_Betreuungsbezirk` (
  `DIM_Bezirke_bezirkId` int(11) NOT NULL,
  `DIM_Kunde_kundeId` int(11) NOT NULL,
  `DIM_Buchungstag_datumId` int(11) NOT NULL,
  PRIMARY KEY (`DIM_Kunde_kundeId`,`DIM_Bezirke_bezirkId`,`DIM_Buchungstag_datumId`),
  KEY `fk_FAKT_Betreuungsbezirke_DIM_Bezirke_idx` (`DIM_Bezirke_bezirkId`),
  KEY `fk_FAKT_Betreuungsbezirke_DIM_Kunde1_idx` (`DIM_Kunde_kundeId`),
  KEY `fk_FAKT_Betreuungsbezirk_DIM_Buchungstag1_idx` (`DIM_Buchungstag_datumId`),
  CONSTRAINT `fk_FAKT_Betreuungsbezirke_DIM_Bezirke` FOREIGN KEY (`DIM_Bezirke_bezirkId`) REFERENCES `DIM_Bezirk` (`bezirkId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FAKT_Betreuungsbezirke_DIM_Kunde1` FOREIGN KEY (`DIM_Kunde_kundeId`) REFERENCES `DIM_Kunde` (`kundeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FAKT_Betreuungsbezirk_DIM_Buchungstag1` FOREIGN KEY (`DIM_Buchungstag_datumId`) REFERENCES `DIM_Buchungstag` (`datumId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `FACT_Kontosaldo` (
  `kontoId` int(11) NOT NULL,
  `kundeId` int(11) NOT NULL,
  `datumId` int(11) NOT NULL,
  `kontostand` decimal(10,2) DEFAULT NULL,
  `bewertungszahl` decimal(3,2) DEFAULT NULL,
  PRIMARY KEY (`kontoId`,`kundeId`,`datumId`),
  KEY `fk_FACT_Kontosaldo_DIM_Konto1_idx` (`kontoId`),
  KEY `fk_FACT_Kontosaldo_DIM_Buchungstage1_idx` (`datumId`),
  KEY `fk_FACT_Kontosaldo_DIM_Kunde1_idx` (`kundeId`),
  CONSTRAINT `fk_FACT_Kontosaldo_DIM_Buchungstage1` FOREIGN KEY (`datumId`) REFERENCES `DIM_Buchungstag` (`datumId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FACT_Kontosaldo_DIM_Konto1` FOREIGN KEY (`kontoId`) REFERENCES `DIM_Konto` (`kontoId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FACT_Kontosaldo_DIM_Kunde1` FOREIGN KEY (`kundeId`) REFERENCES `DIM_Kunde` (`kundeId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `FACT_Umsatz` (
  `datumId` int(11) NOT NULL,
  `kontoId` int(11) NOT NULL,
  `kundeId` int(11) NOT NULL,
  `umsatzartId` int(11) NOT NULL,
  `umsatzBetrag` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`datumId`,`kontoId`,`kundeId`,`umsatzartId`),
  KEY `fk_FACT_Umsatz_DIM_Konto1_idx` (`kontoId`),
  KEY `fk_FACT_Umsatz_DIM_Buchungstage1_idx` (`datumId`),
  KEY `fk_FACT_Umsatz_DIM_Umsatzarten1_idx` (`umsatzartId`),
  KEY `fk_FACT_Umsatz_DIM_Kunde1_idx` (`kundeId`),
  CONSTRAINT `fk_FACT_Umsatz_DIM_Buchungstage1` FOREIGN KEY (`datumId`) REFERENCES `DIM_Buchungstag` (`datumId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FACT_Umsatz_DIM_Konto1` FOREIGN KEY (`kontoId`) REFERENCES `DIM_Konto` (`kontoId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FACT_Umsatz_DIM_Kunde1` FOREIGN KEY (`kundeId`) REFERENCES `DIM_Kunde` (`kundeId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_FACT_Umsatz_DIM_Umsatzarten1` FOREIGN KEY (`umsatzartId`) REFERENCES `DIM_Umsatzart` (`umsatzartId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO DIM_Bezirk (bezirkId, bezirksnummer, validFrom, validTo, isValid, insertDate, bezirk, gebiet, region, sparkasse)
VALUES (1,100011,'2014-01-31','2014-04-07',0,'2014-03-20','Innenstadt','Muenster','NRW',40050150);
INSERT INTO DIM_Bezirk (bezirkId, bezirksnummer, validFrom, validTo, isValid, insertDate, bezirk, gebiet, region, sparkasse)
VALUES (2,100021,'2014-01-31','2099-01-01',1,'2014-03-20','Zentrum Nord','Muenster','NRW',40154530);
INSERT INTO DIM_Bezirk (bezirkId, bezirksnummer, validFrom, validTo, isValid, insertDate, bezirk, gebiet, region, sparkasse)
VALUES (3,100022,'2014-01-31','2099-01-01',1,'2014-04-08','Gievenbeck','Muenster','NRW',40154531);
INSERT INTO DIM_Bezirk (bezirkId, bezirksnummer, validFrom, validTo, isValid, insertDate, bezirk, gebiet, region, sparkasse)
VALUES (4,100011,'2014-04-08','2099-01-01',1,'2014-04-08','Innenstadt','Muenster','NRW',40050155);


insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
1, '2014-02-24', '2014-02-24', '2014-02-24', 24, 2, 2014, 'Montag', 'Februar', 0, '24. Februar 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
2, '2014-02-25', '2014-02-25', '2014-02-25', 25, 2, 2014, 'Dienstag', 'Februar', 0, '25. Februar 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
3, '2014-02-26', '2014-02-26', '2014-02-26', 26, 2, 2014, 'Mittwoch', 'Februar', 0, '26. Februar 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
4, '2014-02-27', '2014-02-27', '2014-02-27', 27, 2, 2014, 'Donnerstag', 'Februar', 0, '27. Februar 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
5, '2014-02-28', '2014-02-28', '2014-03-02', 28, 2, 2014, 'Freitag', 'Februar', 1, '28. Februar 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
6, '2014-03-03', '2014-03-03', '2014-03-03', 3, 3, 2014, 'Montag', 'Maerz', 0, '03. Maerz 2014');
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
7, '2014-03-04', '2014-03-04', '2014-03-04', 4, 3, 2014, 'Dienstag', 'Maerz', 0, '04. Maerz 2014');				
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
8, '2014-03-05', '2014-03-05', '2014-03-05', 5, 3, 2014, 'Mittwoch', 'Maerz', 0, '05. Maerz 2014');					
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
9, '2014-03-06', '2014-03-06', '2014-03-06', 6, 3, 2014, 'Donnerstag', 'Maerz', 0, '06. Maerz 2014');						
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
10, '2014-03-07', '2014-03-07', '2014-03-09', 7, 3, 2014, 'Freitag', 'Maerz', 0, '07. Maerz 2014');					
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
11, '2014-03-10', '2014-03-10', '2014-03-10', 10, 3, 2014, 'Montag', 'Maerz', 0, '10. Maerz 2014');					
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
12, '2014-04-30', '2014-04-30', '2014-04-30', 30, 4, 2014, 'Mittwoch', 'April', 1, '30. April 2014');						
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
13, '2014-05-31', '2014-05-30', '2014-06-01', 31, 5, 2014, 'Samstag', 'Mai', 1, '31. Mai 2014');				
insert into DIM_Buchungstag (datumId, datum,gueltigVon,gueltigBis,tag,monat,jahr,nameTag,nameMonat,monatsultimo,datumLesbar) values(
14, '2014-06-30', '2014-06-30', '2014-06-30', 30, 6, 2014, 'Montag', 'Juni', 1, '30. Juni 2014');	


INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
1, 500001, 11111, '2014-01-03', '2014-01-15', 0, '2014-01-04', 113, 1000.00, '2014-01-03', null, null, null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
2, 500001, 11111, '2014-01-16', '2014-05-06', 0, '2014-01-17', 113, 5000.00, '2014-01-03', null, '2014-01-15', null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
3, 500002, 100001, '2014-01-03', '2014-03-26', 0, '2014-03-03', 157, 750000.00, '2014-03-03', null, null, null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
4, 500002, 100001, '2014-03-26', '2099-01-01', 1, '2014-03-26', 157, 850000.00, '2014-03-26', null, '2014-03-26', null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
5, 500003, 100004, '2012-07-28', '2099-01-01', 1, '2014-03-26', 119, 4500.00, '2014-03-26', null, null, null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
6, 500004, 11112, '2009-03-04', '2099-01-01', 1, '2014-03-26', 187, 11111.11, '2014-03-26', null, null, null);
INSERT INTO DIM_Konto (kontoId,kontonummer,kundennummer,validFrom,validTo,isValid,insertDate,tarif,sparziel,beginndatum,einloesungsdatum,erhoehungsdatum,schliessungsdatum) VALUES(
7, 500001, 11111, '2014-05-07', '2099-01-01', 1, '2014-05-06', 113, 5000.00, '2014-01-03', null, null, '2014-05-06');


INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
1, 100001, '1970-01-01', '2014-06-11', 0, '2013-12-25', 'N', 11111, 1, 2, 'Mueller', 'Heiner', 1, '1962-03-24', 0, 'Muellerstraße', 10, 48149, 'Muenster', 'DEU', 40050150, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
2, 100003, '1970-01-01', '2099-01-01', 1, '2013-12-25', 'N', 11112, 1, 2, 'Becker', 'Heinz', 2, '1972-04-03', 0, 'Beckerstraße', 30, 48147, 'Muenster', 'DEU', 40154530, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
3, 100002, '1970-01-01', '2099-01-01', 1, '2013-12-25', 'N', 11111, 2, 2, 'Mueller', 'Gertrud', 1, '1963-09-22', 0, 'Muellerstraße', 10, 48149, 'Muenster', 'DEU', 40050150, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
4, 11111, '1970-01-01', '2099-01-01', 1, '2013-12-25', 'E', 11111, null , null, 'Mueller', 'Heiner', 0, '1962-03-24', 0, 'Muellerstraße', 10, 48149, 'Muenster', 'DEU', 40050150, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
5, 100004, '1970-01-01', '2099-01-01', 1, '2013-12-25', 'N', 11112, 2, 2, 'Becker', 'Laura', 3, '1970-07-12', 0, 'Meierstraße', '30a', 48149, 'Muenster', 'DEU', 40050150, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
6, 11112, '1970-01-01', '2099-01-01', 1, '2013-12-25', 'E', 11112, null, null, 'Becker', 'Laura', 3, '1970-07-12', 0, 'Meierstraße', '30a', 48149, 'Muenster', 'DEU', 40050150, 0);
INSERT INTO DIM_Kunde (kundeId,kundennummer,validFrom,validTo,isValid,insertDate,kundentyp,ehenummer,geschlecht,familienstand,name,vorname,beruf,geburtsdatum,verstorben,strasse,hausnummer,plz,ort,land,sparkasse,werbungsverbot) values (
7, 100001, '2014-06-12', '2099-01-01', 1, '2013-12-25', 'N', 11111, 1, 2, 'Mueller', 'Heiner', 2, '1962-03-24', 0, 'Muellerstraße', 10, 48149, 'Muenster', 'DEU', 40050150, 0);


insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
1, 2, '2010-01-01', '2099-01-01', 1, '2013-12-25', 0, 'Einzahlung per Lastschrift', 'Einzahlung');
insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
2, 3, '2011-01-01', '2099-01-01', 1, '2013-12-25', 0, 'Auszahlung bar', 'Auszahlung');
insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
3, 1, '2010-01-01', '2099-01-01', 1, '2013-12-25', 0, 'Einzahlung bar', 'Einzahlung');
insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
4, 4, '2010-01-01', '2099-01-01', 1, '2013-12-25', 0, 'Kirchensteuer', 'Steuern');
insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
5, 5, '2010-01-01', '2099-01-01', 1, '2013-12-25', 0, 'Guthabenzinsen', 'Zinsen');
insert into DIM_Umsatzart (umsatzartId, umsatzart, validFrom, validTo, isValid, insertDate, storno, name, gruppenname) values (
6, 6, '2014-03-27', '2099-01-01', 1, '2014-03-27', 0, 'Abbuchung per Lastschrift', 'Auszahlung');


insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
1, 1, 1);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
1, 2, 2);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
1, 5, 2);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
2, 2, 3);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
3, 3, 3);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
3, 6, 2);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
4, 1, 12);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
4, 2, 12);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values 
(4, 4, 2);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values (
4, 5, 12);
insert into FACT_Betreuungsbezirk (DIM_Bezirke_bezirkId, DIM_Kunde_kundeId, DIM_Buchungstag_datumId) values 
(4, 7, 14);


insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
2, 4, 5,-445.00,2.17);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
4, 1, 10,-79.83,8.79);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
4, 7, 14, 17000.00, 3.90);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
5, 5, 5, 250.00, 9.99);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
5, 5, 14, 4000.00, 9.99);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
6, 6, 5, -67.79, 9.99);
insert into FACT_Kontosaldo (kontoId, kundeId, datumId, kontostand, bewertungszahl) values (
6, 6, 10, 19000.00, 9.99);


INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
3, 5, 5, 3, 250.00);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
4, 4, 1, 1, 700.00);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
4, 4, 1, 2, -50.00);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
5, 6, 6, 2, -67.79);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
6, 2, 4, 2, -445.00);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
7, 2, 4, 3, 24.00);
INSERT INTO FACT_Umsatz (datumId,kontoId,kundeId,umsatzartId,umsatzBetrag) VALUES (
10, 2, 4, 1, 179.00);
