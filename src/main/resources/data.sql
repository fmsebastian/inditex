--CREATE TABLE prices (
--   BRAND_ID INT NOT NULL,
--   START_DATE TIMESTAMP NOT NULL,
--   END_DATE TIMESTAMP NOT NULL,
--   PRICE_LIST INT NOT NULL,
--   PRODUCT_ID NUMERIC NOT NULL,
--   PRIORITY INT NOT NULL,
--   PRICE DOUBLE NOT NULL,
--   CURR VARCHAR(3) NOT NULL,
--   PRIMARY KEY(BRAND_ID, PRODUCT_ID, START_DATE, END_DATE)
--);

--BRAND_ID         START_DATE                                    END_DATE                        PRICE_LIST                   PRODUCT_ID  PRIORITY                 PRICE           CURR
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--1         2020-06-14-00.00.00                        2020-12-31-23.59.59                        1                        35455                0                        35.50            EUR
--1         2020-06-14-15.00.00                        2020-06-14-18.30.00                        2                        35455                1                        25.45            EUR
--1         2020-06-15-00.00.00                        2020-06-15-11.00.00                        3                        35455                1                        30.50            EUR
--1         2020-06-15-16.00.00                        2020-12-31-23.59.59                        4                        35455                1                        38.95            EUR

INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-14 00.00.00', '2020-12-31 23.59.59', 1, 35455, 0, 35.50, 'EUR');
INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-14 15.00.00', '2020-06-14 18.30.00', 2, 35455, 1, 25.45, 'EUR');
INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-15 00.00.00', '2020-06-15 11.00.00', 3, 35455, 1, 30.50, 'EUR');
INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-15 16.00.00', '2020-12-31 23.59.59', 4, 35455, 1, 38.95, 'EUR');
INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-15 10.00.00', '2020-06-15 17.30.00', 4, 35455, 2, 41.99, 'EUR');
