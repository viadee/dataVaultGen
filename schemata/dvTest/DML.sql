use dvTest;
INSERT INTO `dvTest`.`HUB_CAT` (`SQN`, `CAT_NUMBER`, `CAT_LOAD_DATE`, `CAT_REC_SOURCE`) VALUES ('24a', '24a', '2014-01-01', 'A');
INSERT INTO `dvTest`.`HUB_CAT` (`CAT_NUMBER`, `CAT_LOAD_DATE`, `CAT_REC_SOURCE`) VALUES ('24', '2013-10-10', 'A');
INSERT INTO `dvTest`.`HUB_CAT` (`CAT_NUMBER`, `CAT_LOAD_DATE`, `CAT_REC_SOURCE`) VALUES ('24b', '2014-01-01', 'A');
INSERT INTO `dvTest`.`HUB_CAT` (`CAT_NUMBER`, `CAT_LOAD_DATE`, `CAT_REC_SOURCE`) VALUES ('27', '2014-01-01', 'A');

INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('2493', '2014-01-30', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('4657', '2014-01-30', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('5476', '2014-01-30', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('4546', '2014-01-30', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('3745', '2014-02-02', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('7987', '2014-02-02', 'A');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('Z137', '2014-02-02', 'B');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('Z124', '2014-02-02', 'B');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('Z572', '2014-02-02', 'C');
INSERT INTO `dvTest`.`HUB_PRODUCT` (`PRODUCT_NUMBER`, `PRODUCT_LOAD_DATE`, `PRODUCT_REC_SOURCE`) VALUES ('Z890', '2014-02-02', 'C');

INSERT INTO `dvTest`.`HUB_STORE` (`STORE_NUMBER`, `STORE_LOAD_DATE`, `STORE_REC_SOURCE`) VALUES ('13', '2014-01-01', 'A');
INSERT INTO `dvTest`.`HUB_STORE` (`STORE_NUMBER`, `STORE_LOAD_DATE`, `STORE_REC_SOURCE`) VALUES ('14', '2014-01-01', 'A');
INSERT INTO `dvTest`.`HUB_STORE` (`STORE_NUMBER`, `STORE_LOAD_DATE`, `STORE_REC_SOURCE`) VALUES ('15', '2014-01-01', 'A');

INSERT INTO `dvTest`.`SAT_STORE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('19', '2014-01-01', 'A', 'Münster');
INSERT INTO `dvTest`.`SAT_STORE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('20', '2014-01-01', 'B', 'Bielefeld');
INSERT INTO `dvTest`.`SAT_STORE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('21', '2014-01-01', 'A', 'Dortmund');

INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('19', 'FIAT 500', '2014-01-30', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('20', 'OPEL CORSA', '2014-01-30', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('21', 'FIAT PUNTO', '2014-01-30', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('22', 'OPEL ADAM', '2014-01-01', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('23', 'MERCEDES A', '2014-02-04', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('24', 'MERCEDES C', '2014-02-04', 'A', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('25', 'FUSSMATTE A', '2014-02-04', 'B', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('26', 'FUSSMATTE B', '2014-02-04', 'B', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('27', 'GPS A', '2014-02-04', 'C', null);
INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `NAME`, `LOAD_DATE`, `REC_SOURCE`, `LOAD_END_DATE`) VALUES ('28', 'GPS B', '2014-02-04', 'C', null);

INSERT INTO `dvTest`.`SAT_CAT` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('24', '2014-01-01', 'A', 'Autoteile');
INSERT INTO `dvTest`.`SAT_CAT` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('25', '2014-01-01', 'A', 'Zubehör');
INSERT INTO `dvTest`.`SAT_CAT` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('26', '2014-01-01', 'A', 'GPS');
INSERT INTO `dvTest`.`SAT_CAT` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('27', '2014-01-01', 'A', 'Fahrzeuge');


INSERT INTO `dvTest`.`HAL_LINK_CAT` (`HUB_CAT_SQN`, `HUB_CAT_PARENT_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('24', '25', '2014-01-01', 'A');
INSERT INTO `dvTest`.`HAL_LINK_CAT` (`HUB_CAT_SQN`, `HUB_CAT_PARENT_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('26', '25', '2014-01-01', 'A');


-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('19', '27', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('20', '27', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('21', '27', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('22', '27', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('23', '27', '20', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('24', '27', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('25', '24', '20', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('26', '24', '19', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('27', '26', '21', '2014-01-01', 'C');
-- INSERT INTO `dvTest`.`LINK_PRODUCT_CAT` (`HUB_PRODUCT_SQN`, `HUB_CAT_SQN`, `HUB_STORE_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('28', '26', '19', '2014-01-01', 'C');


INSERT INTO `dvTest`.`SAT_PRODUCT` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `NAME`) VALUES ('28', '2014-08-01', 'C', 'GPS B');
UPDATE `dvTest`.`SAT_PRODUCT` SET `LOAD_END_DATE`='2014-07-31' WHERE `SQN`='28' and`LOAD_DATE`='2014-02-04';

INSERT INTO `dvTest`.`TA_LINK_SALE` (`HUB_PRODUCT_SQN`, `HUB_STORE_SQN`, `HUB_CAT_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('28', '19', '26', '2014-05-01', 'A');
INSERT INTO `dvTest`.`TA_LINK_SALE` (`HUB_PRODUCT_SQN`, `HUB_STORE_SQN`, `HUB_CAT_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('28', '20', '26', '2014-08-03', 'A');
INSERT INTO `dvTest`.`TA_LINK_SALE` (`HUB_PRODUCT_SQN`, `HUB_STORE_SQN`, `HUB_CAT_SQN`, `LOAD_DATE`, `REC_SOURCE`) VALUES ('23', '19', '27', '2014-04-01', 'A');


INSERT INTO `dvTest`.`SAT_SALE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `BETRAG`) VALUES ('50', '2014-05-01', 'A', '79654.5');
INSERT INTO `dvTest`.`SAT_SALE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `BETRAG`) VALUES ('51', '2014-08-03', 'A', '17972.9');
INSERT INTO `dvTest`.`SAT_SALE` (`SQN`, `LOAD_DATE`, `REC_SOURCE`, `BETRAG`) VALUES ('52', '2014-04-01', 'A', '5000');