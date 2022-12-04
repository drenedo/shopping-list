DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS shopping_list;
DROP TABLE IF EXISTS term;

CREATE TABLE IF NOT EXISTS shopping_list
(
    id          uuid PRIMARY KEY,
    name        text NOT NULL,
    datetime    timestamp NOT NULL,
    description text NOT NULL,
    status      char(1) NOT NULL
);
CREATE INDEX shopping_list_datetime ON shopping_list (datetime);

CREATE TABLE IF NOT EXISTS item
(
    id          uuid PRIMARY KEY,
    name        text NOT NULL,
    amount      int NOT NULL,
    unit        text,
    brand       text,
    status      char(1) NOT NULL,
    list        uuid NOT NULL REFERENCES shopping_list (id)
);

CREATE TABLE IF NOT EXISTS term
(
    id          uuid PRIMARY KEY,
    name        text NOT NULL,
    times       int NOT NULL,
    type        char(1) NOT NULL,
    updated     timestamp NOT NULL
);
--TODO h2 not support index on clob
--CREATE INDEX term_name_type ON term (name, type);
