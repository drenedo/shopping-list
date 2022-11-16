DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS shopping_list;

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
    unit        text NOT NULL,
    status      char(1) NOT NULL,
    list        uuid NOT NULL REFERENCES shopping_list (id)
);
