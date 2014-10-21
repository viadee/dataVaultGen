<h1>dataVaultGen</h1>
============

<h2>Management Summary</h2>

Data Vault wird als eine sehr agile Modellierungstechnik für Data Warehouses beworben. Jedoch ist die Struktur eines Data Vault für Abfragen durch Endanwender oder Reporting-Systeme nicht ausgelegt. Daher wird wie in Abbildung 1 dargestellt, ein Data Vault in der Regel durch eine herkömmliche Data-Mart-Ebene ergänzt. 

Die Bachelorarbeit von Christian Frieler beschäftigt sich mit der Agilität eines Data Vault-Modells. Bei der Untersuchung wurde die Prämisse definiert, dass das aufbauende Data Warehouse automatisiert und durch generische Prozesse virtualisiert werden kann. Diese Prämisse wurde untersucht, indem das notwendige ETL durch eine prototypische Java-Anwendung realisiert wurde. Die Ergebnisse dieser ergänzenden Untersuchung sind in diesem Dokument dargestellt.

Dan Linstedt; Super Charge your Data Warehouse: Invaluable Data Modeling Rules to Implement Your Data Vault; Amazon Distribution, 2011.

Hans Hultgren; Modeling the Agile Data Warehouse with Data Vault; New Hamilton, 2012.

<h2>Grundlagen</h2>
Auf ein ausführliches Grundlagenkapitel wird an dieser Stelle verzichtet. Diese Inhalte können in der genannten Bachelorarbeit sowie in der erwähnten Literatur nachgelesen werden. Dabei sind insbesondere die wesentlichen Struktur-Elemente eines Data Vault-Modells interessant (Hubs, Links und Satelliten).
Ausgangssituation
Für den generischen ETL-Prozess wird exemplarisch ein einfaches Modell verwendet. Inhaltlich geht es um das Privatkundengeschäft einer deutschen Bank und umfasst die Kernkomponenten Konto, Kunde und Umsatzart, dargestellt anhand der entsprechenden Hubs. Als Messdaten sind Umsätze anhand des transaktionalen Links TA_LINK_UMS vorhanden. Außerdem umfasst ein Satellit des HUB_KTO die Messfelder F_Kontostand und F_Bewertungszahl. Hubs und Links werden anhand der Satelliten beschrieben.

Aus diesem Data Vault-Modell wird anhand des ETL-Programms eine Data Mart-Ebene generiert. Dabei werden beispielsweise aus den Hubs mindestens 4 Dimension-Views abgeleitet. Es stehen jedoch noch weitere Datenbank-Schemata für das Testen der Anwendung zur Verfügung.

<h2>Datenbank-Schemata</h2>

Ein Skript für das Anlegen und Befüllen eines leeren DataVault-Schemas (MySQL-Datenbank) ist im Ordner "Schema" abgelegt. Das Data Vault-Schema enthalt transaktionale Links, Fakteneinträge, eine PIT-Tabelle und einen hierarchischen Link (geografische Einheiten).

<h2>Funktionsweise</h2>
Das entwickelte Java-Programm führt im Wesentlichen drei Schritte aus. Zunächst wird das gegebene Data Vault-Schema ausgelesen und auf seine Richtigkeit hin überprüft. Diese Funktion ist mit dem Service SchemaExtractor umgesetzt. Es folgt die Ableitung der Dimensionen und Fakten. Funktionen für diesen Vorgang werden von den Services DimensionBuilder und FactBuilder bereitgestellt. In dem Anhang Data Mart-Ebene ist beispielhaft das Ergebnis einer solchen Transformation abgebildet.

Neben der Kernfunktionalität werden mit dieser Anwendung außerdem weitere Services umgesetzt, mit denen das Ergebnis des ETL-Prozesses beeinflusst werden kann. Entsprechende Einstellungen werden über Programm Parameter vorgenommen. Es folgen Beschreibungen anhand des in Abbildung 2 dargestellten Data Vault-Modells.

Mit dem Programm-Parameter dwh.modus ist steuerbar, ob beispielsweise für den HUB_KND eine Dimension pro Satellit oder eine Dimension pro Hub erzeugt werden soll. Der Parameter dwh.enhance ermöglicht die Vergabe von eindeutigen technischen Schlüsseln. Zur Verdeutlichung des Nutzens dieser Schlüssel-Funktion folgt ein Beispiel anhand der Dimensions-View zum HUB_KTO.

```
DIM_KTO
SQN	KTO_NUMBER	VALID_FROM	VALID_TO	IS_VALID	…
17	500001	2014-01-03	2014-01-15 	0	…
17	500001	2014-01-16 	2014-05-06 	0	…
17	500001	2014-05-07 		1	…
18	500002	2014-01-03 	2014-03-26 	0	…
18	500002	2014-03-26 		1	…
19	500003	2012-07-28 		1	…
20	500004	2009-03-04 		1	…
```

In diesem View-Auszug wird ersichtlich, dass die SQNs aus den Satelliten nur in Kombination mit dem Feld VALID_FROM eindeutig sind. Abfragen auf solchen Dimensions-Views müssten daher immer eine Einschränkung bezüglich der Gültigkeit enthalten. Im Gegensatz dazu führt die Vergabe von eindeutigen Schlüsseln zu direkteren Abfragen. Bei der MySQL-Implementierung wird diese ID wird aus dem Tupel SQN und VALID_FROM anhand einer Hash-Funktion generiert. 

```
DIM_KTO_ENH
ID	SQN	KTO_NUMBER	VALID_FROM	IS_VALID	…
0b0a68d487edea814c…	17	500001	2014-01-03 	0	…
eb470014e5975af2da…	17	500001	2014-01-16 	0	…
ddbd14fceb653744bb…	17	500001	2014-05-07 	1	…
17ce2e587b44681f15…	18	500002	2014-01-03	0	…
7e3efb1a076060dc38…	18	500002	2014-03-26 	1	…
07eab21e9192a34131…	19	500003	2012-07-28 	1	…
6b44763ea549c171a6…	20	500004	2009-03-04 	1	…
```

Mit anderen DBMS sind andere Ansätze eventuell leistungsstärker (z.B. die Vergabe von Auto-increment-Schlüsseln bei Materialisierten Views). Da die genutzte MySQL-Version keine Materialisierung ermöglicht, können Views anhand des Parameters dwh.persist zumindest persistiert werden. So müssen die Hash-Werte nicht bei jeder Abfrage neu ermittelt werden.

Diese und weitere Parameter sind mit ihren möglichen Werten im Kapitel Programm-Parameter genauer beschrieben.
Administrative Aspekte
Im Folgenden sind notwendige Schritte beschrieben, um die Anwendung auszuführen.
Voraussetzungen
Für die Ausführung des Programms werden benötigt:
-	MySQL-Datenbank (Version 5.5.35)
-	MySQL-Workbench für das Ausführen der Skripte
-	JDK 1.7.0 (Java 7)
-	Maven (http://maven.apache.org/download.cgi - Source zip)
-	Eclipse Java EE IDE (Luna 4.4.0) mit 
o	M2E Connector
o	Spring Tool Suite
o	Subversive Team Provider (http://www.eclipse.org/subversive/)
o	Polarion SVN Connector (siehe Anhang Polarion-Pakete)
(http://community.polarion.com/projects/subversive/download/eclipse/4.0/luna-site/)
-	Anpassung der Umgebungsvariablen (siehe Anhang Umgebungsvariablen)

Das zugrundeliegende Data Vault muss den nachfolgend beschriebenen Prämissen genügen (z.B. der Namenskonvention). Erfüllt es diese Prämissen nicht, werden bei Ausführung des Programms geeignete Fehlermeldungen ausgegeben und der ETL-Prozess wird abgebrochen.
Außerdem gilt es zu beachten, dass bisher nur MySQL-Datenbanken unterstützt werden. Die verwendete Architektur erlaubt aber eine Koppelung mit weiteren Datenbanken. Dazu müssen die DDL-Compositor-Interfaces neu implementiert werden (im Projekt-Paket dv.sql). 
Installation
Nachdem das Projekt aus dem Repository importiert wurde, müssen mit Maven die Bibliotheken geladen werden. Achtung: Maven umgeht in der Standard-Konfiguration die Proxy-Einstellungen des Systems.
Start der Anwendung
Vor dem ersten Start der Anwendung müssen zwingend die Parameter in der Datei src/main/resources/database.properties angepasst werden. Beispielhafte Einstellungen sind in folgender Tabelle abgebildet.
 

```
Parameter	            Beispiel
jdbc.driverClassName=	com.mysql.jdbc.Driver
jdbc.url=	            jdbc:mysql://192.168.56.101:3306/
jdbc.schema=	        [muss leer sein]
jdbc.targetschema=  	[muss leer sein]
jdbc.username=	      lenaDB
jdbc.password=	      viadee
```

Das Quell- und das Zielschema müssen beim Start der Anwendung an die Main-Methode übergeben werden. Die bei dem Parameter jdbc.url angegebene Datenbank muss beide Schemata enthalten. Das Quell- und Zielschema können identisch sein. Ein beispielhafter Aufruf ist im Kapitel Beispielhafter Programmaufruf dargestellt.
Programm-Parameter
Das Programm bietet mehrere Parameter, mit denen das Verhalten der Anwendung steuerbar ist. Diese sind in der Datei src/main/resources/application.properties anpassbar.

Parameter	Werte	Beschreibung
dwh.modus	0 / 1	Bei 1 wird für alle Satelliten eines Hubs eine gemeinsame Dimensions-View erstellt. Bei 0 erhält jeder Satellit eine einzelne Dimension.
dwh.history	true/false	Bei true werden alle Historieneinträge in die Views übernommen. Bei false entfällt die Historie samt der zugehörigen Historisierungsfelder (nach Kimball valid_from, valid_to und is_valid).
dwh.enhance	true/false	Bei true werden die Views um eindeutige technische Schlüssel ergänzt. Die technischen Schlüssel erleichtern spätere Abfragen. 
dwh.persist	true/false	Bei true werden die Views persistiert. Aktuell erfolgt dies nur auf Basis einer MySQL-Datenbank über das Erstellen einer Tabelle durch CREATE TABLE [NAME] AS SELECT. Demnach wird keine referentielle Integrität gewährleistet.
dwh.
flathierarchies	true/false	Hierarchien in Dimensionen werden aufgelöst, indem die Parent-Einträge denormalisiert neben das Child geschrieben werden. Prämisse: dwh.history = false.
Aufbau der Anwendung
Die Architektur ist geteilt in drei Schichten. Der Zugriff erfolgt über die Konsole. Die Haupt-Services sind der SchemaExtractor, der das gegebene Data Vault-Schema ausliest. Der DimensionBuilder setzt aus den Inhalten des Schemas Dimensionen zusammen. Entsprechend funktioniert der FactBuilder. Für den Datenzugriff werden DAOs verwendet. Utility Services (Materializer, ViewEnhancer…) stellen weitere Funktionen für das Zusammenstellen des Zielschemas bereit. Einfache Entitäten (POJOs) erleichtern die Handhabe mit verschiedenen Informationen.

Die Klassen sind auf die folgenden Pakete aufgeteilt.
Paket	Beschreibung
dv.application	Enthält die Main-Methode sowie den nach Aufbau des Application Context aufzurufenden Executor auf. Letzterer führt anhand des SchemaExtractors, DimensionBuilders und FactBuilders den Prozess aus.
dv.model	Enthält POJOs für die Abbildung von Objekten und Objektrelationen.
dv.repository	Enthält alle DAOs (Hub, Link, Satellit, Dimension und Fact).
dv.service	Enthält die drei Services SchemaExtractor, DimensionBuilder und FactBuilder.
dv.service.
supplement	Enthält Services für die Schema-Validierung, die Materialisierung, das Abflachen von Hierarchien und die Erweiterung von Views.
dv.sql	Enthält Services für das Zusammensetzen von DDL und DML-Befehlen. 
dv.sql.mysql	Enthält Implementierung der DDL- und DML-Services für MySQL-Datenbanken.

Ein Klassendiagramm ist in dem Eclipse-Projekt enthalten. Für die Verwendung ist das Eclipse-Plugin ObjectAid zu installieren (http://www.objectaid.com/download).

<h2>Prämissen-Katalog</h2>
<pre>
Die in diesem Katalog aufgelisteten Prämissen sind für die Ausführung des ETL-Programms einzuhalten. Verstößt ein gegebenes Data Vault gegen diese Prämissen, wird der ETL-Prozess vor der Generierung der Data Mart-Ebene  unterbrochen.
Prämissen bezüglich der Systemannahmen
1.	Das Ergebnis der Anwendung (Dimensions- und Faktentabellen) ist interpretierbar, wenn auch nicht interpretiert. Das bedeutet, dass die fachliche Logik nicht durch die Automatisierung umgesetzt wird.
2.	Das Quell-Schema (Data Vault) und das Zielschema müssen vor der Ausführung des Programms existieren und auf demselben Server liegen.
Prämissen bezüglich der Faktentabellen
1.	Kennzahlen können in Satelliten eingebettet sein. In diesem Fall werden die entsprechenden Felder mit dem Präfix F_ markiert. Alle Fakteneinträge eines Satelliten werden unter Einbezug des nächstgelegenen Hubs zu einer Faktentabelle vereint.
2.	Kennzahlen können anhand von transaktionalen Links abgebildet werden. Bei diesem Fall wird der Link mit seinem Satelliten zu einer Faktentabelle vereint. Der transaktionale Link muss einen beschreibenden Satelliten haben.
Prämissen bezüglich der Historie
Parameter: dwh.history = true
1.	Wenn ein Hub mehrere Satelliten hat, können diese Satelliten zu einer Dimension vereint werden. Voraussetzung dafür ist, dass die Gültigkeitsfelder der verschiedenen Satelliten vereint werden. Nach Linstedt (S. 97 ff.) und Hultgren (S. 385 ff.) geschieht das mit der Verwendung einer PIT-Tabelle .
2.	Ist eine PIT-Tabelle vorhanden, können mit dem Parameter dwh.modus = 1 mehrere Satelliten zu einer Dimension vereint werden.
3.	Ist keine PIT-Tabelle vorhanden, wird für den betroffenen Hub mit dem Modus dwh.modus = 0 gearbeitet.
Prämissen bezüglich der eindeutigen Schlüssel (Hashes)
Parameter: dwh.enhance = true
Voraussetzungen für die Vergaben von Hash-Keys bei Dimensionen:
1.	Die Historie muss eingeschaltet sein
2.	Weitere Parameter habe keinen Einfluss
Voraussetzungen für die Vergabe von Hash-Keys bei Fakten:
1.	Die Historie muss eingeschaltet sein
2.	Der Parameter dwh.modus muss auf 1 stehen, sodass mehrere Satelliten eines Hubs zu einer Dimension vereint werden (siehe Backlog-Eintrag 11). 
Prämissen bezüglich Hierarchien
Parameter: dwh.flathierarchies = true
Voraussetzungen:
1.	Alle Einträge einer Hierarchie haben die gleiche Tiefe.
2.	Hierarchien werden nur dann aufgelöst, wenn die Historie deaktiviert ist. Ansonsten müsste das Mapping der Gültigkeiten über alle Hierarchieebenen hinweg erfolgen.
Verhalten:
3.	Bei unabhängigen Dimensionen (modus=0) werden die Hierarchien aller Dimensionen abgeflacht. 
Prämissen bezüglich der Persistierung
Parameter: dwh.persist = true
Voraussetzungen:
1.	Die View des zu persistierenden Fakts/Dimension muss vorhanden sein
Verhalten:
2.	Wenn dwh.enhance auf true gesetzt ist, wird die erweiterte View mit dem Postfix _ENH persistiert. Wenn der Parameter auf false steht, wird die ursprünglich erstellte View ohne Hash-Werte verwendet.
3.	Wenn dwh.history auf false steht und dwh.flathierarchies auf true, werden zusätzlich die Views mit abgeflachten Hierarchien persistiert.
4.	Fakten werden nur mit HashKeys persistiert wenn
-	dwh.history = true
-	dwh.enhance = true
-	dwh.modus = 1
</pre>

Prämissen bezüglich der Struktur und der Namenskonvention
Im Folgenden sind weitere Prämissen in tabellarischer Form dargestellt.

```
Tabellenpräfixe
SAT_	Satelliten
HUB_	Hubs
LINK_	Links (Standard)
HAL_LINK_	Hierarchische Links
TA_LINK_	Transaktionale Links
PIT_HUB_	Point-In-Time-Tabelle eines Hubs
```

```
Feldpräfixe und -bezeichnungen
SQN	Die SQN eines Hubs, Links (inkl. TA_LINK & HAL_LINK) oder Satelliten heißt immer SQN.
LOAD_DATE	Das LOAD_DATE eines Links, oder Satelliten heißt immer LOA_DATE.
LOAD_END_DATE	Das LOAD_END_DATE eines Links, oder Satelliten heißt immer LOAD_END_DATE.
REC_SOURCE	Die Quellenbeschreibungen heißen immer REC_SOURCE.
F_	Fakteneinträge in Satelliten sind mit F_ zu kennzeichnen.
[KRZ]_NUMBER	Der Business Key eines Hubs setzt sich aus einem Teil des Hub-Namens und dem Postfix _NUMBER zusammen. Wenn der Hub als HUB_KND benannt ist, heißt das Feld KND_NUMBER. 
[KRZ]_LOAD_DATE	Das LOAD_DATE eines Hubs ist immer mit einem Kürzel versehen (siehe [KRZ]_NUMBER). 
[KRZ]_REC_SOURCE	Das REC_SOURCE-Feld eines Hubs ist immer mit einem Kürzel versehen (siehe [KRZ]_NUMBER).
```

```
Pflichtfelder
Hubs	SQN, [KRZ]_NUMBER, [KRZ]_LOAD_DATE
Link	SQN, FREMD_SQNs (min. 2), LOAD_DATE
Hierarchischer Link	SQN, FREMD_SQN, FREMD_PARENT_SQN, LOAD_DATE
Transaktionaler Link	SQN, FREMD_SQNs (min. 2), LOAD_DATE
Satelliten eines Hubs	SQN, LOAD_DATE, LOAD_END_DATE
Satelliten eines TA_Links	SQN, LOAD_DATE
```

Referenzen
Link & TA_Link	Ein Link verbindet mehrere Hubs. In einem Link sind daher die SQNs der Hubs abgelegt, wobei der Tabellenname des Hubs als Präfix dient. Am Beispiel des Hubs HUB_KTO wird das verweisende Feld im Link HUB_KTO_SQN genannt.
Link & TA_Link & HAL_Link	Ein Link hat niemals optionale SQN-Felder.
HAL_Link	Ein hierarchischer Link verbindet immer einen Hub-Eintrag mit seinem Parent. Daher hat jeder Link zwei Referenzen: Einen auf den Hubeintrag des Parent und einen auf den Hubeintrag des Childs.


<h2>Besonderheiten</h2>
TA_LINK	Der Satellit eines TA_LINKS hat niemals ein LOAD_END_DATE. Die in dem Satelliten liegenden Informationen stellen ein Ereignis oder Messergebnis (FAKT!) zu einem Zeitpunkt dar, nicht zu einer Zeitdauer.
TA_LINK	Ein TA_LINK hat nur einen Satelliten
LINK	Ein Link hat keine Satelliten (Siehe Backlog-Eintrag 15).
HUB	Ein Hub kann mehrere Satelliten haben.
HUB	Für das Vereinen mehrerer Satelliten eines Hubs muss eine PIT-Tabelle gepflegt sein.
HUB_PIT	Eine PIT-Tabelle umfasst die Gültigkeiten aller Satelliten des Hubs.
F_	F_-Felder eines Satelliten werden zu einem Fakt vereint. Dabei wird die SQN des Satelliten übernommen. Es findet keine direkte Zuordnung zu einer Dimension statt.

<h2>Anhang</h2>
Data Mart-Ebene
Die aus dem Data Vault-Schema dvMedium (siehe Abbildung 2) generierte Data Mart-Ebene könnte wie folgt aussehen (in Abhängigkeit der Einstellungen).

DIM_GEO
Eine einfache Dimensionsview könnte wie folgt aussehen:
```
CREATE VIEW dvTarget.DIM_GEO AS
    select 
        dvMedium.SAT_GEO.SQN AS SQN,
        dvMedium.HUB_GEO.GEO_NUMBER AS GEO_NUMBER,
        dvMedium.SAT_GEO.LOAD_DATE AS VALID_FROM,
        dvMedium.SAT_GEO.LOAD_END_DATE AS VALID_TO,
        (case
            when (dvMedium.SAT_GEO.LOAD_END_DATE is not null) then 0
            else 1
        end) AS IS_VALID,
        dvMedium.SAT_GEO.BEZ_GEB_REGION AS BEZ_GEB_REGION,
        dvMedium.SAT_GEO.BANK AS BANK
    from
        (dvMedium.SAT_GEO
        join dvMedium.HUB_GEO ON ((dvMedium.SAT_GEO.SQN = dvMedium.HUB_GEO.SQN)))

SQN	GEO_NUMBER	VALID_FROM	VALID_TO	IS_VALID	BEZ_GEB_REGION	BANK
14	1001	2014-07-04 		1	Westfalen	
15	1002	2014-07-04 		1	Rheinland	
16	10001	2014-07-04 		1	Münster	
17	10002	2014-07-04 		1	Dortmund	
18	100011	2014-07-04 		1	Innenstadt	40050155
19	100021	2014-07-04 		1	Zentrum Nord	40154530
20	100022	2014-07-04 		1	Gievenbeck	40154530
```

FACT_UMS
Eine einfache Faktenview könnte wie folgt aussehen:
```
CREATE VIEW dvTarget.FACT_UMS AS
    select 
        dvMedium.TA_LINK_UMS.SQN AS SQN,
        dvMedium.TA_LINK_UMS.HUB_KTO_SQN AS HUB_KTO_SQN,
        dvMedium.TA_LINK_UMS.HUB_UMS_ART_SQN AS HUB_UMS_ART_SQN,
        dvMedium.SAT_UMS.LOAD_DATE AS LOAD_DATE,
        dvMedium.SAT_UMS.UMSATZBETRAG AS UMSATZBETRAG,
        dvMedium.SAT_UMS.STORNO AS STORNO
    from
        (dvMedium.TA_LINK_UMS
        join dvMedium.SAT_UMS ON ((dvMedium.TA_LINK_UMS.SQN = dvMedium.SAT_UMS.SQN)))


SQN	HUB_KTO_SQN	HUB_UMS_ART_SQN	LOAD_DATE	UMSATZBETRAG	STORNO
50	19	34	2014-02-26	250.00	0
51	18	35	2014-02-27	700.00	0
52	18	36	2014-02-27	-50.00	0
53	20	36	2014-02-28	-67.79	0
54	17	36	2014-03-03	-445.00	0
55	17	34	2014-03-04	24.00	0
56	17	35	2014-03-07	179.00	0
```

FACT_UMS_ENH
Eine um technische Schlüssel ergänzte Faktenview könnte wie folgt aussehen:

```
CREATE VIEW dvTarget.FACT_UMS_ENH AS
    select 
        FACT_UMS.SQN AS SQN,
        md5(concat_ws('_',
                        DIM_KTO.SQN,
                        DIM_KTO.VALID_FROM)) AS DIM_KTO_ID,
        md5(concat_ws('_',
                        DIM_UMS_ART.SQN,
                        DIM_UMS_ART.VALID_FROM)) AS DIM_UMS_ART_ID,
        FACT_UMS.LOAD_DATE AS LOAD_DATE,
        FACT_UMS.UMSATZBETRAG AS UMSATZBETRAG,
        FACT_UMS.STORNO AS STORNO
    from
        ((dvTarget.FACT_UMS
        join dvTarget.DIM_KTO ON ((FACT_UMS.HUB_KTO_SQN = DIM_KTO.SQN)))
        join dvTarget.DIM_UMS_ART ON ((FACT_UMS.HUB_UMS_ART_SQN = DIM_UMS_ART.SQN)))
    where
        (((FACT_UMS.LOAD_DATE between DIM_KTO.VALID_FROM and DIM_KTO.VALID_TO)
            or ((FACT_UMS.LOAD_DATE >= DIM_KTO.VALID_FROM)
            and isnull(DIM_KTO.VALID_TO)))
            and ((FACT_UMS.LOAD_DATE between DIM_UMS_ART.VALID_FROM and DIM_UMS_ART.VALID_TO)
            or ((FACT_UMS.LOAD_DATE >= DIM_UMS_ART.VALID_FROM)
            and isnull(DIM_UMS_ART.VALID_TO))))
```
