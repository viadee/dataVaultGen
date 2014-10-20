INSERT INTO HUB_KTO (KTO_NUMBER, KTO_LOAD_DATE, KTO_REC_SOURCE) 
select kontonummer, '2014-07-02','A' from target.DIM_Konto group by kontonummer;

INSERT INTO HUB_KND (KND_NUMBER,KND_LOAD_DATE,KND_REC_SOURCE)
select kundennummer, '2014-07-02','A' from target.DIM_Kunde where KUNDENTYP = 'N' group by kundennummer;

INSERT INTO HUB_UMS_ART (UMS_ART_NUMBER, UMS_LOAD_DATE, UMS_REC_SOURCE)
select umsatzart, '2014-07-02','A'  from target.DIM_Umsatzart group by umsatzart;

-- Populate LINK_KTO_KND
insert into LINK_KTO_KND (HUB_KTO_SQN, HUB_KND_SQN, LOAD_DATE, REC_SOURCE)
select d.SQN, c.SQN, a.validFrom, 'A' from target.DIM_Konto a join target.DIM_Kunde b on a.kundennummer = b.kundennummer join HUB_KND c on a.kundennummer = c.KND_NUMBER join HUB_KTO d on a.kontonummer = d.KTO_NUMBER
where b.kundentyp='N';


INSERT INTO TA_LINK_UMS (HUB_KTO_SQN, HUB_UMS_ART_SQN, LOAD_DATE, REC_SOURCE)
select e.SQN as HUB_KTO_SQN, f.SQN as HUB_UMS_ART_SQN, d.datum, 'A'
from target.FACT_Umsatz a 
join target.DIM_Konto b on a.kontoId = b.kontoId
join target.DIM_Umsatzart c on a.umsatzartId = c.umsatzartId
join target.DIM_Buchungstag d on a.datumId = d.datumId
join HUB_KTO e on e.KTO_NUMBER = b.kontonummer
join HUB_UMS_ART f on f.UMS_ART_NUMBER = c.umsatzart;

-- Populate SAT_KTO
insert into SAT_KTO (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, TARIF, SPARZIEL, LEBEND)
select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end), 'A', a.tarif, a.sparziel, (case 
	when(a.schliessungsdatum is null) then 1
	else 0	end) as LEBEND
from target.DIM_Konto a join HUB_KTO b on a.kontonummer = b.KTO_NUMBER;

-- Populate SAT_BESTAND_KTO
insert into SAT_KTO_BESTAND (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, F_KONTOSTAND, F_BEWERTUNGSZAHL)
select c.SQN, d.datum, null as enddate, 'A' as source, a.kontostand, a.bewertungszahl
from target.FACT_Kontosaldo a join target.DIM_Konto b on a.kontoId = b.kontoId join HUB_KTO c on b.kontonummer = c.KTO_NUMBER join target.DIM_Buchungstag d on a.datumId = d.datumId;

-- Populate SAT_KND
INSERT INTO SAT_KND (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, FAMILIENSTAND, GESCHLECHT, NACHNAME, VORNAME, BERUF, GEBURTSDATUM, VERSTORBEN, SPARKASSE, WERBUNGSVERBOT)
select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end) as validTo, 'A', a.familienstand, a.geschlecht, a.name, a.vorname, a.beruf, a.geburtsdatum, a.verstorben, a.sparkasse, a.werbungsverbot from target.DIM_Kunde a join HUB_KND b on a.kundennummer = b.KND_NUMBER;

-- Populate SAT_KND_ADDRESS
INSERT INTO SAT_KND_ADDRESS (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, STRASSE, POSTLEITZAHL, ORT, LAND, HAUSNUMMER)
select b.SQN, addtime(a.validFrom,'4 0:0:0.000000'), (case when(a.validTo='2099-01-01') then null else addtime(a.validTo,'4 0:0:0.000000') end) as validTo, 'A', a.strasse, a.plz, a.ort, a.land, a.hausnummer from target.DIM_Kunde a join HUB_KND b on a.kundennummer = b.KND_NUMBER;
-- select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end) as validTo, 'A', a.strasse, a.plz, a.ort, a.land, a.hausnummer from target.DIM_Kunde a join HUB_KND b on a.kundennummer = b.KND_NUMBER;

-- Populate SAT_UMS
insert into SAT_UMS (SQN, LOAD_DATE, REC_SOURCE,UMSATZBETRAG,STORNO) 
select g.SQN, d.datum, 'A', a.umsatzBetrag, '0'
from target.FACT_Umsatz a 
join target.DIM_Konto b on a.kontoId = b.kontoId
join target.DIM_Umsatzart c on a.umsatzartId = c.umsatzartId
join target.DIM_Buchungstag d on a.datumId = d.datumId
join HUB_KTO e on e.KTO_NUMBER = b.kontonummer
join HUB_UMS_ART f on f.UMS_ART_NUMBER = c.umsatzart
join TA_LINK_UMS g on g.LOAD_DATE = d.datum and g.HUB_KTO_SQN = e.SQN and g.HUB_UMS_ART_SQN =f.SQN;

-- Populate SAT_UMS_ART
insert into SAT_UMS_ART (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, NAME, GRUPPENNAME)
select a.SQN, b.validFrom, (case when(b.validTo='2099-01-01') then null else b.validTo end) as validTo, 'A', b.name, b.gruppenname
from HUB_UMS_ART a
join target.DIM_Umsatzart b on a.UMS_ART_NUMBER = b.umsatzart;

INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, LOAD_END_DATE, KND_LOAD_DATE) VALUES ('19', '1970-01-01 00:00:00', '1970-01-04 00:00:00', '1970-01-01 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, LOAD_END_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('19', '1970-01-05 00:00:00', '2014-06-11 00:00:00', '1970-01-01 00:00:00', '1970-01-05 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, LOAD_END_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('19', '2014-06-12 00:00:00', '2014-06-15 00:00:00', '2014-06-12 00:00:00	', '1970-01-05 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('19', '2014-06-16 00:00:00', '2014-06-12 00:00:00	', '2014-06-16 00:00:00	');

INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, LOAD_END_DATE, KND_LOAD_DATE) VALUES ('20', '1970-01-01 00:00:00', '1970-01-04 00:00:00', '1970-01-01 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('20', '1970-01-05 00:00:00', '1970-01-01 00:00:00', '1970-01-05 00:00:00');

INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, KND_LOAD_DATE, LOAD_END_DATE) VALUES ('21', '1970-01-01 00:00:00', '1970-01-01 00:00:00', '1970-01-04 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('21', '1970-01-05 00:00:00', '1970-01-01 00:00:00', '1970-01-05 00:00:00');

INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, LOAD_END_DATE, KND_LOAD_DATE) VALUES ('22', '1970-01-01 00:00:00', '1970-01-04 00:00:00', '1970-01-01 00:00:00');
INSERT INTO PIT_HUB_KND (SQN, LOAD_DATE, KND_LOAD_DATE, KND_ADDRESS_LOAD_DATE) VALUES ('22', '1970-01-05 00:00:00', '1970-01-01 00:00:00', '1970-01-05 00:00:00');
