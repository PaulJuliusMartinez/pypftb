psql postgres://`grep User secrets.yml | cut -d " " -f 2`:`grep Password secrets.yml | cut -d " " -f 2`@`grep Url secrets.yml | cut -d "/" -f 3,4`
