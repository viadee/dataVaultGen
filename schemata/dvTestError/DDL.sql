CREATE SCHEMA IF NOT EXISTS `dvTestError` ;
use dvTestError;
CREATE TABLE `HUB_PRODUCT` (
  `SQN` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_NUMBER` VARCHAR(45) NOT NULL,
  `PRODUCT_LOAD_DATE` date NOT NULL,
  `PRODUCT_REC_SOURCE` varchar(45) NOT NULL,
  PRIMARY KEY (`SQN`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

CREATE TABLE `HUB_CAT` (
  `SQN` int(11) NOT NULL AUTO_INCREMENT,
  `CAT_NUMBER` varchar(45) NOT NULL,
  `CAT_LOAD_DATE` date NOT NULL,
  `CAT_REC_SOURCE` varchar(45) NOT NULL,
  PRIMARY KEY (`SQN`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

CREATE TABLE `HUB_STORE` (
  `SQN` int(11) NOT NULL AUTO_INCREMENT,
  `STORE_NUMBER` int(11) NOT NULL,
  `STORE_LOAD_DATE` date NOT NULL,
  `STORE_REC_SOURCE` varchar(45) NOT NULL,
  PRIMARY KEY (`SQN`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;

CREATE SCHEMA IF NOT EXISTS dvTestError;
use dvTestError;
CREATE TABLE `TA_LINK_SALE` (
  `SQN` int(11) NOT NULL AUTO_INCREMENT,
  `HUB_PRODUCT_SQN` int(11) NOT NULL,
  `HUB_STORE_SQN` int(11) NOT NULL,
  `HUB_CAT_SQN` int(11) NOT NULL,
  `LOAD_DATE` date NOT NULL,
  `REC_SOURCE` varchar(45) NOT NULL,
  PRIMARY KEY (`SQN`),
  KEY `fk_TA_LINK_UMS_HUB_UMS_idx` (`HUB_PRODUCT_SQN`),
  KEY `fk_TA_LINK_UMS_HUB_KTO1_idx` (`HUB_STORE_SQN`),
  KEY `fk_TA_HUB_CAT` (`HUB_CAT_SQN`),
  CONSTRAINT `fk_LINK_UMS_HUB_KTO` FOREIGN KEY (`HUB_PRODUCT_SQN`) REFERENCES `HUB_PRODUCT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_LINK_UMS_HUB_UMS_ART` FOREIGN KEY (`HUB_STORE_SQN`) REFERENCES `HUB_STORE` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_TA_LINK_CAT` FOREIGN KEY (`HUB_CAT_SQN`) REFERENCES `HUB_CAT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;

CREATE TABLE `HAL_LINK_CAT` (
  `SQN` int(11) NOT NULL AUTO_INCREMENT,
  `HUB_CAT_SQN` int(11) NOT NULL,
  `HUB_CAT_PARENT_SQN` int(11) NOT NULL,
  `LOAD_DATE` date NOT NULL,
  `REC_SOURCE` varchar(45) NOT NULL,
  PRIMARY KEY (`SQN`),
  KEY `fk_LINK_KND_PRT_HUB_GEO_idx` (`HUB_CAT_SQN`),
  KEY `fk_LINK_KND_PRT_HUB_GEO_PAR_idx` (`HUB_CAT_PARENT_SQN`),
  CONSTRAINT `fk_LINK_GEO_HUB_GEO` FOREIGN KEY (`HUB_CAT_SQN`) REFERENCES `HUB_CAT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_LINK_GEO_HUB_GEP_PARENT` FOREIGN KEY (`HUB_CAT_PARENT_SQN`) REFERENCES `HUB_CAT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- CREATE TABLE `LINK_PRODUCT_CAT` (
  -- `SQN` int(11) NOT NULL AUTO_INCREMENT,
  -- `HUB_PRODUCT_SQN` int(11) NOT NULL,
  -- `HUB_CAT_SQN` int(11) NOT NULL,
  -- `HUB_STORE_SQN` int(11) NOT NULL,
  -- `LOAD_DATE` date DEFAULT NULL,
  -- `REC_SOURCE` varchar(45) DEFAULT NULL,
  -- PRIMARY KEY (`SQN`),
  -- KEY `fk_LINK_HUB_PRODUCT_idx` (`HUB_PRODUCT_SQN`),
  -- KEY `fk_LINK_HUB_CAT_idx` (`HUB_CAT_SQN`),
  -- KEY `fk_LINK_HUB_STORE_idx` (`HUB_STORE_SQN`),
  -- CONSTRAINT `fk_HUB_PRODUCT` FOREIGN KEY (`HUB_PRODUCT_SQN`) REFERENCES `HUB_PRODUCT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `fk_HUB_CAT` FOREIGN KEY (`HUB_CAT_SQN`) REFERENCES `HUB_CAT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  -- CONSTRAINT `fk_HUB_STORE` FOREIGN KEY (`HUB_STORE_SQN`) REFERENCES `HUB_STORE` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
-- ) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `SAT_PRODUCT` (
  `SQN` int(11) NOT NULL,
  `LOAD_DATE` date NOT NULL,
  `LOAD_END_DATE` date,
  `REC_SOURCE` varchar(45) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SQN`,`LOAD_DATE`),
  KEY `fk_SAT_UMS_LINK_UMS1_idx` (`SQN`),
  CONSTRAINT `fk_SAT_UMS_LINK_UMS1` FOREIGN KEY (`SQN`) REFERENCES `HUB_PRODUCT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `SAT_CAT` (
  `SQN` int(11) NOT NULL,
  `LOAD_END_DATE` date DEFAULT NULL,
  `REC_SOURCE` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SQN`),
  CONSTRAINT `fk_HUB_CATSAT` FOREIGN KEY (`SQN`) REFERENCES `HUB_CAT` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `SAT_STORE` (
  `SQN` int(11) NOT NULL,
  `LOAD_DATE` date NOT NULL,
  `LOAD_END_DATE` date DEFAULT NULL,
  `REC_SOURCE` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SQN`,`LOAD_DATE`),
  CONSTRAINT `FK_SAT_STORE` FOREIGN KEY (`SQN`) REFERENCES `HUB_STORE` (`SQN`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
