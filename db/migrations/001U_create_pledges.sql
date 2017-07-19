CREATE TABLE pledges (
  id 		           serial PRIMARY KEY,
  age                      integer NOT NULL,
  name                     text NOT NULL,
  location                 text NOT NULL,
  favorite_brewer          text NOT NULL,
  last_pee_your_pants      text NOT NULL,
  county_stadium_memory    text NOT NULL,
  best_part_of_brewers_fan text NOT NULL,
  email                    text NOT NULL UNIQUE
);
