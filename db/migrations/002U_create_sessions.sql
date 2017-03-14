CREATE SEQUENCE sessions__id_seq;
CREATE TABLE sessions (
  id 			integer PRIMARY KEY DEFAULT nextval('sessions__id_seq'),
  token 		text NOT NULL UNIQUE,
  user_id 		integer NOT NULL REFERENCES users (id) ON DELETE cascade,
  expires_at 	timestamp DEFAULT CURRENT_TIMESTAMP + INTERVAL '30 days'
);