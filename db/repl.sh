psql postgres://`grep User secrets.yml | cut -d " " -f 2`:`grep Password secrets.yml | cut -d " " -f 2`@pauljuliusmartinez-test.cllhowwyaqxl.us-west-1.rds.amazonaws.com/postgres
