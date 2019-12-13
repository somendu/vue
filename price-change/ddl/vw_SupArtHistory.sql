alter view vw_SupArtHistory as
select   SUPPLIER_ARTICLE.SK_SUPP_ART_NO as id
,        SUPPLIER_ARTICLE.DA_INDICATOR as status -- Status
,        SUPPLIER_ARTICLE.DA_REMARK as remark -- Remark
,        ARTICLE.DN_SAP_ART_NO as sapArticleNumber -- SAP article number
,        ARTICLE.DA_ART_DESC as articleDescriptionNL -- Article description NL (concatenated version)
,        SUPPLIER_ARTICLE.DA_SUPPLIER_ARTICLE_NO as supplierArticleNumber -- Supplier article number
,        SUPPLIER_ARTICLE.DA_SUPPLIER as supplier -- Supplier

,        PRICE_MASTER_NET.DH_PRICE as netPriceSupplierOld -- Net price supplier (old)
,        PRICE_SUPPLIER_NET.DH_PRICE as netPriceSupplierNew -- 'Net price supplier (new)
--       Percentage change (new-old/old) = (Net price supplier (new) - Net price supplier (old)) / Net price supplier (old)
,        (PRICE_SUPPLIER_NET.DH_PRICE - PRICE_MASTER_NET.DH_PRICE) / NullIf(PRICE_MASTER_NET.DH_PRICE, 0) as netPriceSupplierChange

,        PRICE_MASTER_GROSS.DH_PRICE as grossPriceSupplierOld -- Gross price supplier (old)
,        PRICE_SUPPLIER_GROSS.DH_PRICE as grossPriceSupplierNew -- 'Gross price supplier (new)
--       Percentage change (new-old/old) = (Gross price supplier (new) - Gross price supplier (old)) / Gross price supplier (old) * 100%
,        (PRICE_SUPPLIER_GROSS.DH_PRICE - PRICE_MASTER_GROSS.DH_PRICE) / NullIf(PRICE_MASTER_GROSS.DH_PRICE, 0) as grossPriceSupplierChange

--       Total revaluation = Revaluation outlets + Revaluation depots + Revaluation CM
,        revaluation.outlets + revaluation.depots as revaluationTotal
--       Back-office margin % (difference) = Back-office margin % (new) - Back-office margin % (old)
,        margins.backOfficePctNew - margins.backOfficePctOld as backOfficeMarginPctDiff
--       Outlet margin % (difference) = Back-office margin % (new) - Back-office margin % (old)
,        margins.outletPctNew - margins.outletPctOld as outletMarginPctDiff

,        ARTICLE.DA_GOODS_GROUP_DESC as goodsDescription -- Goederengroep omschrijving
,        ARTICLE.DA_ART_GROUP_DESC as articleGroupDescription -- Artikelgroep omschrijving
,        ARTICLE.DA_PRODUCT_HIERARCHY_DESC as prodHierDescription -- Merk/merklijn omschrijving
,        ARTICLE.DA_POPCODE_NL as popCodeNL -- Popcode NL
,        SUPPLIER_ARTICLE.DA_MAINSUPPLIER_IND as mainSupplier -- Main Supplier
,        ARTICLE.DA_POPCODE_BE as popCodeBE -- Popcode BE
,        SUPPLIER_ARTICLE.DA_DISCOUNT_GROUP as discountGroup -- Discount group
,        SUPPLIER_ARTICLE.DA_DISCOUNT_RATE as discountGroupPercent -- Discount group percentage
,        STOCK.DH_GROSS_PRICE as transferPrice -- Transfer price
,        STOCK.DH_CURRENT_TRANSFER_PRC as outletDiscount -- Outlet discount
,        STOCK.DH_ZKGM_MARKUP_PERC as outletMarkup -- Outlet markup
,        ARTICLE.DN_PIM_MSTR_ART_NO as pimArticleNumberMaster -- PIM article number (master)
,        SUPPLIER_ARTICLE.DN_PIM_SPPLR_ART_NO as pimArticleNumberSupplier -- PIM article number (supplier)
,        ARTICLE.DN_AX_ART_NO as axArticleNumber -- AX_article number
,        ARTICLE.DA_PRODUCT_MANAGER as productManager -- Buyer / PM
,        PRICE_SUPPLIER_NET.DH_PRICE_START_DT as netPriceSupplierStartDateNew -- Net price supplier start date (new)
,        PRICE_MASTER_NET.DH_PRICE_START_DT as netPriceSupplierStartDateOld -- Net price supplier start date (old)
,        PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT as grossPriceSupplierStartDateNew -- Gross price supplier start date (new)
,        PRICE_MASTER_GROSS.DH_PRICE_START_DT as grossPriceSupplierStartDateOld -- Gross price supplier start date (old)
,        PRICE_SUPPLIER_OWN.DH_PRICE as grossPriceOwnPriceNew -- 'Gross price own price (new)
,        PRICE_SUPPLIER_OWN.DH_PRICE_START_DT as grossPriceOwnPriceStartDateNew -- Gross price own price start date (new)
,        PRICE_MASTER_OWN.DH_PRICE as grossPriceOwnPriceOld -- Gross price own price (old)
,        PRICE_MASTER_OWN.DH_PRICE_START_DT as grossPriceOwnPriceStartDateOld -- Gross price own price start date (old)
--       Percentage change (new-old/old) = (Gross price own (new) - Gross price own (old)) / Gross price own (old) * 100%
,        (PRICE_SUPPLIER_OWN.DH_PRICE - PRICE_MASTER_OWN.DH_PRICE) / NullIf(PRICE_MASTER_OWN.DH_PRICE, 0) as grossPriceOwnPriceChange

,        PRICE_SUPPLIER_GOING.DH_PRICE as goingPriceNew -- 'Going price (new)
,        PRICE_SUPPLIER_GOING.DH_PRICE_START_DT as goingPriceStartDateNew -- Going price start date (new)
,        PRICE_MASTER_GOING.DH_PRICE as goingPriceOld -- Going price (old)
,        PRICE_MASTER_GOING.DH_PRICE_START_DT as goingPriceStartDateOld -- Going price start date (old)
--       Percentage change (new-old/old) = (Going price (new) - Going price (old)) / Going price (old) * 100%
,        (PRICE_SUPPLIER_GOING.DH_PRICE - PRICE_MASTER_GOING.DH_PRICE) / NullIf(PRICE_MASTER_GOING.DH_PRICE, 0) as goingPriceChange

,        PRICE_SUPPLIER_WHOLESALE.DH_PRICE as wholesalePriceNew -- 'Wholesale price (new)
,        PRICE_SUPPLIER_WHOLESALE.DH_PRICE_START_DT as wholesalePriceStartDateNew -- Wholesale price start date (new)
,        PRICE_MASTER_WHOLESALE.DH_PRICE as wholesalePriceOld -- Wholesale price (old)
,        PRICE_MASTER_WHOLESALE.DH_PRICE_START_DT as wholesalePriceStartDateOld -- Wholesale price start date (old)
--       Percentage change (new-old/old) = (Wholesale price (new) - Wholesale price (old)) / Wholesale price (old) * 100%
,        (PRICE_SUPPLIER_WHOLESALE.DH_PRICE - PRICE_MASTER_WHOLESALE.DH_PRICE) / NullIf(PRICE_MASTER_WHOLESALE.DH_PRICE, 0) as wholesalePriceChange

,        STOCK.DH_STOCK_DC as stockPositionCM -- Stock position CM (Ede + Oosterhout + Depots)
,        STOCK.DH_STOCK_VKP as stockPositionOutlet -- Stock position Outlets (verkooppunten)

,        revaluation.outlets revaluationOutlets
,        revaluation.depots revaluationDepots

,        margins.backOfficeNew as backOfficeMarginNew
,        margins.backOfficeOld as backOfficeMarginOld
--       Back-office margin (difference) = Back-office margin (new) - Back-office margin (old)
,        margins.backOfficeNew - margins.backOfficeOld as backOfficeMarginDiff

--       Back-office margin % (new) = (1 - (Net price supplier (new) / Transfer price)) * 100%
,        margins.backOfficePctNew as backOfficeMarginPctNew
--       Back-office margin % (old) = (1 - (Net price supplier (old) / Transfer price)) * 100%
,        margins.backOfficePctOld as backOfficeMarginPctOld

,        margins.outletNew as outletMarginNew
,        margins.outletOld as outletMarginOld
--       Outlet margin (difference) = Outlet margin (new) - Outlet margin (old)
,        margins.outletNew - margins.outletOld as outletMarginDiff

--       Outlet margin % (new) = (1 - (Net price supplier (new) / (Transfer price - Outlet discount + Outlet markup)) * 100%
,        margins.outletPctNew as outletMarginPctNew
--       Outlet margin % (old) = (1 - (Net price supplier (old) / (Transfer price - Outlet discount + Outlet markup)) * 100%
,        margins.outletPctOld as outletMarginPctOld

,        ARTICLE.DA_GOODS_GROUP_SAP as goodsGroup -- Goods group (SAP goederengroep)
,        ARTICLE.DA_ART_GROUP_AX as articleGroup -- Article groep (AX artikelgroep)
,        ARTICLE.DA_PRODUCT_HIERARCHY as prodHierarchy -- Product hierarchy (from brand/brand line)
from     ARTICLE
         inner join SUPPLIER_ARTICLE
            on ARTICLE.SK_ART_NO = SUPPLIER_ARTICLE.SK_ART_NO
         inner join PRICE_HISTORY as PRICE_SUPPLIER_GROSS
            on ARTICLE.SK_ART_NO = PRICE_SUPPLIER_GROSS.SK_ART_NO
            and PRICE_SUPPLIER_GROSS.DH_PRICETYPE = 'Gross price supplier'
            and PRICE_SUPPLIER_GROSS.DH_CATALOG = 'Supplier'
         left join PRICE_HISTORY as PRICE_MASTER_NET
            on ARTICLE.SK_ART_NO = PRICE_MASTER_NET.SK_ART_NO
            and PRICE_MASTER_NET.DH_PRICETYPE = 'Net price supplier'
            and PRICE_MASTER_NET.DH_CATALOG = 'Master'
            and PRICE_MASTER_NET.DH_PRICE_END_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_SUPPLIER_NET
            on ARTICLE.SK_ART_NO = PRICE_SUPPLIER_NET.SK_ART_NO
            and PRICE_SUPPLIER_NET.DH_PRICETYPE = 'Net price supplier'
            and PRICE_SUPPLIER_NET.DH_CATALOG = 'Supplier'
            and PRICE_SUPPLIER_NET.DH_PRICE_START_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_MASTER_GROSS
            on ARTICLE.SK_ART_NO = PRICE_MASTER_GROSS.SK_ART_NO
            and PRICE_MASTER_GROSS.DH_PRICETYPE = 'Gross price supplier'
            and PRICE_MASTER_GROSS.DH_CATALOG = 'Master'
            and PRICE_MASTER_GROSS.DH_PRICE_END_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_MASTER_OWN
            ON ARTICLE.SK_ART_NO = PRICE_MASTER_OWN.SK_ART_NO
            AND PRICE_MASTER_OWN.DH_PRICETYPE = 'Gross price - own'
            AND PRICE_MASTER_OWN.DH_CATALOG = 'Master'
            and PRICE_MASTER_OWN.DH_PRICE_END_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_SUPPLIER_OWN
            on ARTICLE.SK_ART_NO = PRICE_SUPPLIER_OWN.SK_ART_NO
            and PRICE_SUPPLIER_OWN.DH_PRICETYPE = 'Gross price - own'
            and PRICE_SUPPLIER_OWN.DH_CATALOG = 'Supplier'
            and PRICE_SUPPLIER_OWN.DH_PRICE_START_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_MASTER_GOING
            on ARTICLE.SK_ART_NO = PRICE_MASTER_GOING.SK_ART_NO
            and PRICE_MASTER_GOING.DH_PRICETYPE = 'Going price'
            and PRICE_MASTER_GOING.DH_CATALOG = 'Master'
            and PRICE_MASTER_GOING.DH_PRICE_END_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_SUPPLIER_GOING
            on ARTICLE.SK_ART_NO = PRICE_SUPPLIER_GOING.SK_ART_NO
            and PRICE_SUPPLIER_GOING.DH_PRICETYPE = 'Going price'
            and PRICE_SUPPLIER_GOING.DH_CATALOG = 'Supplier'
            and PRICE_SUPPLIER_GOING.DH_PRICE_START_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_MASTER_WHOLESALE
            on ARTICLE.SK_ART_NO = PRICE_MASTER_WHOLESALE.SK_ART_NO
            and PRICE_MASTER_WHOLESALE.DH_PRICETYPE = 'Wholesale price'
            and PRICE_MASTER_WHOLESALE.DH_CATALOG = 'Master'
            and PRICE_MASTER_WHOLESALE.DH_PRICE_END_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join PRICE_HISTORY as PRICE_SUPPLIER_WHOLESALE
            on ARTICLE.SK_ART_NO = PRICE_SUPPLIER_WHOLESALE.SK_ART_NO
            and PRICE_SUPPLIER_WHOLESALE.DH_PRICETYPE = 'Wholesale price'
            and PRICE_SUPPLIER_WHOLESALE.DH_CATALOG = 'Supplier'
            and PRICE_SUPPLIER_WHOLESALE.DH_PRICE_START_DT = PRICE_SUPPLIER_GROSS.DH_PRICE_START_DT
         left join STOCK_HISTORY STOCK
            on ARTICLE.DN_SAP_ART_NO = STOCK.DN_SAP_ART_NO
            or ARTICLE.DN_PIM_MSTR_ART_NO = STOCK.DN_PIM_NO
         cross apply
         (  select
            --       Revaluation outlets = Alleen voor main supplier uitrekenen: (Net price supplier (new) - Net price supplier (old)) * Stock position Outlets
                     case SUPPLIER_ARTICLE.DA_MAINSUPPLIER_IND
                        when 'Y' then (PRICE_SUPPLIER_NET.DH_PRICE - PRICE_MASTER_NET.DH_PRICE) * STOCK.DH_STOCK_VKP
                        else null
                     end as outlets

            --       Revaluation CM = Alleen voor main supplier uitrekenen: (Net price supplier (new) - Net price supplier (old)) * Stock position CM
            ,        case SUPPLIER_ARTICLE.DA_MAINSUPPLIER_IND
                        when 'Y' then (PRICE_SUPPLIER_NET.DH_PRICE - PRICE_MASTER_NET.DH_PRICE) * STOCK.DH_STOCK_DC
                        else null
                     end as depots
         ) as revaluation

         cross apply
         (  select
            --       Back-office margin (new) = (1 - (Net price supplier (new) / Transfer price)) * (Revaluation depots + Revaluation CM)
                     (1 - PRICE_SUPPLIER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) * revaluation.depots as backOfficeNew
            --       Back-office margin (old) = (1 - (Net price supplier (old) / Transfer price)) * (Revaluation depots + Revaluation CM)
            ,        (1 - PRICE_MASTER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) * revaluation.depots as backOfficeOld
            --       Back-office margin % (new) = (1 - (Net price supplier (new) / Transfer price)) * 100%
            ,        (1 - PRICE_SUPPLIER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) as backOfficePctNew
            --       Back-office margin % (old) = (1 - (Net price supplier (old) / Transfer price)) * 100%
            ,        (1 - PRICE_MASTER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) as backOfficePctOld

            --       Outlet margin (new) = (1 - (Net price supplier (new) / Transfer price)) * (Revaluation outlets)
            ,        (1 - PRICE_SUPPLIER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) * revaluation.outlets as outletNew
            --       Outlet margin (old) = (1 - (Net price supplier (old) / Transfer price)) * (Revaluation outlets)
            ,        (1 - PRICE_MASTER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE, 0)) * revaluation.outlets as outletOld
            --       Outlet margin % (new) = (1 - (Net price supplier (new) / (Transfer price - Outlet discount + Outlet markup)) * 100%
            ,        (1 - PRICE_SUPPLIER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE - STOCK.DH_CURRENT_TRANSFER_PRC + STOCK.DH_ZKGM_MARKUP_PERC, 0)) as outletPctNew
            --       Outlet margin % (old) = (1 - (Net price supplier (old) / (Transfer price - Outlet discount + Outlet markup)) * 100%
            ,        (1 - PRICE_MASTER_NET.DH_PRICE / NullIf(STOCK.DH_GROSS_PRICE - STOCK.DH_CURRENT_TRANSFER_PRC + STOCK.DH_ZKGM_MARKUP_PERC, 0)) as outletPctOld

         ) as margins;