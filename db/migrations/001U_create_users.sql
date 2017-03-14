CREATE SEQUENCE users__id_seq;
CREATE TABLE users (
  id 		integer PRIMARY KEY DEFAULT nextval('users__id_seq'),
  email 	text NOT NULL UNIQUE,
  password 	text NOT NULL
);