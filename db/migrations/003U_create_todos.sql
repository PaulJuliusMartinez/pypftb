CREATE TABLE todos (
  id 				serial PRIMARY KEY,
  date_created  	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_completed 	timestamp,
  body				text NOT NULL,
  completed			boolean DEFAULT false,
  parent_id			integer REFERENCES todos (id) ON DELETE cascade,
  user_id 			integer NOT NULL REFERENCES users (id) ON DELETE cascade
);