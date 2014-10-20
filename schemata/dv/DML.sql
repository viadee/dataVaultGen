INSERT INTO HUB_KTO (KTO_NUMBER, KTO_LOAD_DATE, KTO_REC_SOURCE) 
select kontonummer, '2014-07-02','A' from target.DIM_Konto group by kontonummer;

INSERT INTO HUB_PRT (PRT_NUMBER,PRT_LOAD_DATE,PRT_REC_SOURCE)
select ehenummer,'2014-07-02','A' from target.DIM_Kunde where KUNDENTYP = 'E' group by ehenummer;

INSERT INTO HUB_UMS_ART (UMS_ART_NUMBER, UMS_LOAD_DATE, UMS_REC_SOURCE)
select umsatzart, '2014-07-02','A'  from target.DIM_Umsatzart group by umsatzart;

INSERT INTO HUB_GEO (GEO_BEZIRKNUMBER, GEO_LOAD_DATE, GEO_REC_SOURCE)
select Bezirksnummer,'2014-07-02','A' from source.GEO group by Bezirksnummer;

INSERT INTO HUB_KND (KND_NUMBER,KND_LOAD_DATE,KND_REC_SOURCE)
select kundennummer, '2014-07-02','A' from target.DIM_Kunde where KUNDENTYP = 'N' group by kundennummer;

-- INSERT INTO HUB_UMS (UMS_NUMBER, UMS_LOAD_DATE, UMS_REC_SOURCE)
-- select concat_ws('_',b.kontonummer, c.datum) as UMS_NUMBER, timestamp('2014-07-03'), 'A' from target.FACT_Umsatz a join target.DIM_Konto b on a.kontoId = b.kontoId join target.DIM_Buchungstag c on a.datumId = c.datumId;

INSERT INTO TA_LINK_UMS (HUB_KTO_SQN, HUB_UMS_ART_SQN, LOAD_DATE, REC_SOURCE)
select e.SQN as HUB_KTO_SQN, f.SQN as HUB_UMS_ART_SQN, d.datum, 'A'
from target.FACT_Umsatz a 
join target.DIM_Konto b on a.kontoId = b.kontoId
join target.DIM_Umsatzart c on a.umsatzartId = c.umsatzartId
join target.DIM_Buchungstag d on a.datumId = d.datumId
join HUB_KTO e on e.KTO_NUMBER = b.kontonummer
join HUB_UMS_ART f on f.UMS_ART_NUMBER = c.umsatzart;



-- Populate HAL_LINK_GEO
INSERT INTO HAL_LINK_GEO(HUB_GEO_SQN, HUB_GEO_PARENT_SQN, LOAD_DATE, REC_SOURCE)
select y.SQN as HUB_GEO_SQN, x.SQN as HUB_GEO_PARENT_SQN, '2014-07-03', 'A' from 
(select a.Bezirksnummer as PAR_bezirksnummer, b.Bezirksnummer as desc_bezirksnummer from source.GEO a join source.GEO b on a.Bezirksnummer = b.Region where b.Gebiet is null) z 
	join HUB_GEO x on x.GEO_BEZIRKNUMBER = z.PAR_bezirksnummer 
	join HUB_GEO y on y.GEO_BEZIRKNUMBER = z.desc_bezirksnummer;
INSERT INTO HAL_LINK_GEO(HUB_GEO_SQN, HUB_GEO_PARENT_SQN, LOAD_DATE, REC_SOURCE)
select y.SQN as HUB_GEO_SQN, x.SQN as HUB_GEO_PARENT_SQN, '2014-07-03', 'A' from 
(select b.GEBIET PAR_GEBIETNR, b.Bezirksnummer as desc_bezirksnummer from source.GEO a join source.GEO b on a.Bezirksnummer = b.Region where b.Gebiet is not null) z 
	join HUB_GEO x on x.GEO_BEZIRKNUMBER = z.PAR_GEBIETNR 
	join HUB_GEO y on y.GEO_BEZIRKNUMBER = z.desc_bezirksnummer;
-- select y.SQN as HUB_GEO_SQN, x.SQN as HUB_GEO_PARENT_SQN, '2014-07-03', 'A' from 
-- (select a.Bezirksnummer as PAR_bezirksnummer, b.Bezirksnummer as desc_bezirksnummer from source.GEO a join source.GEO b on a.Bezirksnummer = b.Region where b.Gebiet is not null) z 
-- join HUB_GEO x on x.GEO_BEZIRKNUMBER = z.PAR_bezirksnummer 
-- join HUB_GEO y on y.GEO_BEZIRKNUMBER = z.desc_bezirksnummer;


-- Populate SAT_KTO
insert into SAT_KTO (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, TARIF, SPARZIEL, LEBEND)
select b.SQN, a.validFrom, a.validTo, 'A', a.tarif, a.sparziel, (case 
	when(a.schliessungsdatum is null) then 1
	else 0	end) as LEBEND
from target.DIM_Konto a join HUB_KTO b on a.kontonummer = b.KTO_NUMBER;

-- Populate SAT_BESTAND_KTO
insert into SAT_KTO_BESTAND (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, F_KONTOSTAND, F_BEWERTUNGSZAHL)
select c.SQN, d.datum, '2099-01-01' as enddate, 'A' as source, a.kontostand, a.bewertungszahl
from target.FACT_Kontosaldo a join target.DIM_Konto b on a.kontoId = b.kontoId join HUB_KTO c on b.kontonummer = c.KTO_NUMBER join target.DIM_Buchungstag d on a.datumId = d.datumId;

-- Populate SAT_UMS_ART
insert into SAT_UMS_ART (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, NAME, GRUPPENNAME)
select a.SQN, b.validFrom, (case when(b.validTo='2099-01-01') then null else b.validTo end) as validTo, 'A', b.name, b.gruppenname
from HUB_UMS_ART a
join target.DIM_Umsatzart b on a.UMS_ART_NUMBER = b.umsatzart;

-- Populate SAT_KND
INSERT INTO SAT_KND (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, FAMILIENSTAND, GESCHLECHT, NACHNAME, VORNAME, BERUF, GEBURTSDATUM, VERSTORBEN, SPARKASSE, WERBUNGSVERBOT)
select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end) as validTo, 'A', a.familienstand, a.geschlecht, a.name, a.vorname, a.beruf, a.geburtsdatum, a.verstorben, a.sparkasse, a.werbungsverbot from target.DIM_Kunde a join HUB_KND b on a.kundennummer = b.KND_NUMBER;

-- Populate SAT_KND_ADDRESS
INSERT INTO SAT_KND_ADDRESS (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, STRASSE, HAUSNUMMER, POSTLEITZAHL, LAND)
select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end) as validTo, 'A', a.strasse, a.plz, a.ort, a.land from target.DIM_Kunde a join HUB_KND b on a.kundennummer = b.KND_NUMBER;

-- Populate SAT_PRT
select * from target.DIM_Kunde;
select b.SQN, a.validFrom, (case when(a.validTo='2099-01-01') then null else a.validTo end) as validTo, 'A' from target.DIM_Kunde a join HUB_PRT b on a.ehenummer = b.PRT_NUMBER;

-- Populate SAT_GEO
INSERT INTO SAT_GEO (SQN, LOAD_DATE, LOAD_END_DATE, REC_SOURCE, BEZ_GEB_REGION, SPARKASSE)
select b.SQN, '2014-07-04', null, 'A', a.NameBezirkGebietRegion, a.sparkasse from source.GEO a join HUB_GEO b on a.Bezirksnummer = b.GEO_BEZIRKNUMBER;
