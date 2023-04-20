INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a39c', 'list1', 'list-description', '2022-10-12 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a31c', 'list2', 'list-description', '2022-10-13 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a32c', 'list3', 'list-description', '2022-10-14 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a33c', 'list4', 'list-description', '2022-10-15 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a34c', 'list5', 'list-description', '2022-10-16 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a35c', 'list6', 'list-description', '2022-10-17 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a36c', 'list7', 'list-description', '2022-10-18 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a37c', 'list8', 'list-description', '2022-10-19 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a38c', 'list9', 'list-description', '2022-10-20 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a31a', 'list10', 'list-description', '2022-10-21 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a32a', 'list11', 'list-description', '2022-10-22 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a33a', 'list12', 'list-description', '2022-10-23 10:00:01', 'A');
INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a34a', 'list13', 'list-description', '2022-10-24 10:00:01', 'A');

INSERT INTO SHOPPING_LIST (id, name, description, datetime, status)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a340', 'list', 'list-description', CURRENT_DATE, 'A');
INSERT INTO ITEM (id, name, amount, unit, status, list)
VALUES ('d44f860a-0d91-4529-9f91-ac9f5f29a35c', 'item', 1, 'Unit', 'A', 'd44f860a-0d91-4529-9f91-ac9f5f29a340');

INSERT INTO TERM (ID, NAME, TIMES, TYPE, UPDATED)
values ('d44f860a-0d91-0529-9f91-ac9f5f29a35c', 'item', 3, 'P', CURRENT_DATE);
INSERT INTO TERM (ID, NAME, TIMES, TYPE, UPDATED)
values ('d223860a-0d91-0529-9f91-ac9f5f29a35c', 'itelo', 1, 'P', CURRENT_DATE);
INSERT INTO TERM (ID, NAME, TIMES, TYPE, UPDATED)
values ('1223860a-0d91-0529-9f91-ac9f5f29a35c', 'text', 1, 'P', CURRENT_DATE);

INSERT INTO RECEIPT (ID, LIST, SITE, CONTENT, TOTAL, CREATED, CATEGORY)
values ('d44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'ALCAMPO 1', 'ALCAMPO 1', 30.5, '2023-2-16 10:00:01', 'H');
INSERT INTO RECEIPT (ID, LIST, SITE, CONTENT, TOTAL, CREATED, CATEGORY)
values ('d14f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'ALCAMPO 3', 'ALCAMPO 2', 30.5, '2023-2-16 11:00:01', 'E');
INSERT INTO RECEIPT (ID, LIST, SITE, CONTENT, TOTAL, CREATED, CATEGORY)
values ('d14f860a-0d91-0529-9f91-ac9f5f29a45c', null, 'ALCAMPO 3', 'ALCAMPO 3', 30.5, '2023-2-16 12:00:01', 'F');
INSERT INTO RECEIPT (ID, LIST, SITE, CONTENT, TOTAL, CREATED, CATEGORY)
values ('d14f860a-0d91-0529-9f91-ac9f5f27a45c', null, 'ALCAMPO 4', 'ALCAMPO 4', 30.5, '2023-2-16 13:00:01', 'C');

INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0529-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0539-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0549-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0559-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0569-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0579-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0589-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');
INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0599-9f91-ac9f5f29a35c', 'd44f860a-0d91-0529-9f91-ac9f5f29a35c', null, 'NAME A', 1, 30.5, '2023-2-16 10:00:01');


INSERT INTO LINE (ID, RECEIPT, ITEM, NAME, AMOuNT, TOTAL, CREATED)
values ('d44f860a-0d91-0599-9f91-ac9f5f29135c', 'd14f860a-0d91-0529-9f91-ac9f5f27a45c', null, 'ITEM A', 1, 30.5, '2023-2-16 10:00:01');
