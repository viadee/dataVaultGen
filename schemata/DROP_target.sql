use dvTarget;
-- use dvTargetHier;
drop table if exists DIM_KND_ADDRESS_MAT, DIM_GEO_MAT,DIM_KND_MAT, DIM_KTO_MAT, DIM_UMS_ART_MAT, FACT_KONTOSTAND_BEWERTUNGSZAHL_MAT, FACT_UMS_MAT;
drop view if exists DIM_KND_ADDRESS,DIM_GEO_MAT, DIM_KND_MAT, DIM_KTO_MAT, DIM_UMS_ART_MAT, FACT_KONTOSTAND_BEWERTUNGSZAHL_ENH, FACT_UMS_ENH;
drop view if exists DIM_KND, DIM_KTO, DIM_UMS_ART, DIM_GEO, FACT_KONTOSTAND_BEWERTUNGSZAHL, FACT_UMS, DIM_GEO_HELPER;
drop table if exists DIM_GEO_FLAT_MAT,FACT_KONTOSTAND_BEWERTUNGSZAHL_ENH_MAT, FACT_UMS_ENH_MAT, DIM_KTO, DIM_UMS_ART, DIM_GEO, FACT_KONTOSTAND_BEWERTUNGSZAHL, FACT_UMS, DIM_GEO_HELPER, DIM_GEO_ENH_MAT, DIM_KND_ENH_MAT, DIM_KTO_ENH_MAT;

drop view if exists  DIM_GEO_TEST, DIM_GEO_TEST_FLAT, DIM_KND_ADDRESS_ENH,DIM_GEO_ENH, DIM_KND_ENH, DIM_KTO_ENH, DIM_UMS_ART_ENH, DIM_GEO_FLAT;
drop table if exists DIM_GEO_TEST_FLAT_MAT ,DIM_GEO_TEST_ENH_MAT, DIM_KND_ADDRESS_ENH_MAT, DIM_UMS_ART_ENH_MAT, DIM_GEO_MAT, DIM_KND_MAT, DIM_KTO_MAT, DIM_UMS_ART_MAT;



drop table if exists DIM_CAT_ENH_MAT, DIM_PRODUCT_ENH_MAT, DIM_STORE_ENH_MAT, FACT_SALE_ENH_MAT;

drop view if exists DIM_CAT,A DIM_CAT_ENH, DIM_PRODUCT, DIM_PRODUCT_ENH, DIM_STORE, DIM_STORE_ENH, FACT_SALE, FACT_SALE_ENH;