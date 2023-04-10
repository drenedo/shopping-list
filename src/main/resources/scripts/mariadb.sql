DROP TABLE IF EXISTS LINE;
DROP TABLE IF EXISTS ITEM;
DROP TABLE IF EXISTS RECEIPT;
DROP TABLE IF EXISTS SHOPPING_LIST;
DROP TABLE IF EXISTS TERM;

CREATE TABLE IF NOT EXISTS SHOPPING_LIST
(
    ID          CHAR(36) PRIMARY KEY,
    NAME        TEXT NOT NULL,
    DATETIME    DATETIME NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    STATUS      CHAR(1) NOT NULL
    );
CREATE INDEX SHOPPING_LIST_DATETIME ON SHOPPING_LIST (DATETIME);

CREATE TABLE IF NOT EXISTS ITEM
(
    ID          CHAR(36) PRIMARY KEY,
    NAME        TEXT NOT NULL,
    amount      INT NOT NULL,
    unit        TEXT,
    brand       TEXT,
    STATUS      CHAR(1) NOT NULL,
    LIST        CHAR(36) NOT NULL REFERENCES SHOPPING_LIST (ID)
    );

CREATE TABLE IF NOT EXISTS TERM
(
    ID          CHAR(36) PRIMARY KEY,
    NAME        TEXT NOT NULL,
    TIMES       INT NOT NULL,
    TYPE        CHAR(1) NOT NULL,
    UPDATED     DATETIME NOT NULL
    );
CREATE INDEX TERM_NAME_TYPE ON TERM (NAME(255), TYPE);
CREATE FULLTEXT INDEX TERM_NAME_SEARCH ON TERM (NAME, TYPE);

CREATE TABLE IF NOT EXISTS RECEIPT
(
    ID          CHAR(36) PRIMARY KEY,
    LIST        CHAR(36) REFERENCES SHOPPING_LIST (ID),
    SITE        TEXT NOT NULL,
    CONTENT     TEXT NOT NULL,
    TOTAL       INT NOT NULL,
    CREATED     DATETIME NOT NULL
);
CREATE INDEX RECEIPT_LIST ON RECEIPT(LIST);

CREATE TABLE IF NOT EXISTS LINE
(
    ID          CHAR(36) PRIMARY KEY,
    RECEIPT     CHAR(36) NOT NULL REFERENCES RECEIPT (ID),
    ITEM        CHAR(36) REFERENCES ITEM (ID),
    NAME        TEXT,
    AMOUNT      INT,
    TOTAL       INT,
    CREATED     timestamp NOT NULL
);
CREATE INDEX LINE_RECEIPT ON LINE (RECEIPT);

ALTER TABLE RECEIPT ADD COLUMN cash BOOLEAN;
CREATE INDEX RECEIPT_CREATED ON RECEIPT (CREATED);


ALTER TABLE RECEIPT ADD COLUMN LINE_NUMBER INT;
