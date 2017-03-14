#!/usr/bin/env python
import sys
import os
import yaml

SECRETS_RELATIVE_PATH = '../secrets.yml'
CHANGELOG_FILE = 'db/liquibase-migrations.xml'

def get_secrets():
    secrets = os.path.join(os.path.dirname(__file__), SECRETS_RELATIVE_PATH)
    with open(secrets, 'r') as f:
        return yaml.load(f)

def migrate(command):
    secrets = get_secrets()
    database_url = secrets['databaseUrl']
    database_password = secrets['databasePassword']
    database_user = secrets['databaseUser']

    print 'Migrating...'
    full_command = 'liquibase'
    full_command += ' --driver=org.postgresql.Driver'
    full_command += ' --classpath=db/postgresql-9.4.1212.jre6.jar '
    full_command += ' --changeLogFile=' + CHANGELOG_FILE
    full_command += ' --url=' + database_url
    full_command += ' --username=' + database_user
    full_command += ' --password=' + database_password
    full_command += ' ' + command
    os.system(full_command)

def usage():
    print 'USAGE:'
    print '  db/migrate.py up           to migrate forward 1 change set.'
    print '  db/migrate.py down         to rollback 1 change set.'
    sys.exit()

def main():
    if len(sys.argv) == 1:
        usage()

    if sys.argv[1] == 'up':
        migrate('updateCount 1')
    elif sys.argv[1] == 'down':
        migrate('rollbackCount 1')
    else:
        print 'Unknown command:', sys.argv[1]

if __name__ == "__main__":
    main()
