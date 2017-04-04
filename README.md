# Website

## How to start Mission Control application

- To run in Eclipse
  1. Click green play button in menu bar, then "Run Mission Control"
  1. If you get an error "Address already in use" this means you have another
     instance of the server running
  1. To stop this other instance click the bug in the top right (Debug menu)
  1. Select the running instance in the top left pane
  1. Click the red stop button to stop the instance, then start the server again
- To run from the command line
  1. Run `mvn clean install` to build your application
  1. Start application with `java -jar target/website-0.0.1-SNAPSHOT.jar server config.yml`
  1. To check that your application is running enter url `http://localhost:8080`

## Helpful Utils

### `db/repl.sh`

This runs the command `psql` to connect to the database, giving you a SQL repl.
Use this to see what objects exists in the database.

Common commands:
- `\dt` Lists all tables. (`databasechangelog` and `databasechangeloglock` are
  managed by Liquibase to migrate the database)
- `\d <table_name>` Describes a table, listing the columns and their types. Also
  lists any indices and foreign key constraints on the table.
- `SELECT * FROM <table_name>` Selects all rows from a table.

### `./test-api.sh`

The file `api-tester.html` is a helpful script to test API endpoints. Our server
authenticates user requests by looking at the `Cookie` header in requests. In
order to get Chrome to send the cookie, you can't simply open the file in Chrome
at `file:///path/to/api-tester.html`, but must load it over HTTP.
`./test-api.sh` starts a simple Python server that serves the files in the
project root directory at `localhost:8082`. Once the page is open you can stop
the script by hitting CTRL-C, but if you ever change `api-tester.html`, you must
rerun the script to be able to refresh the page.

## Adding new Models to the Database

To create a new model `Model`
- Write up and down migrations in `db/migrations` to create your schema
  - `0XXU_create_models.sql`
  - `0XXD_create_models.sql`
- Add references to the migrations in `liquibase-migrations.xml`
- Migrate the database by running `./migrate.py up`
- Create model file `src/main/java/com/abcanthur/website/models/Model.java`
- Have `jooq` generate the `ModelsRecord` classes by running "jooq Generate
  Models" in the green play button menu
- Create `ModelResource` class and register it in `WebsiteApplication.java`

If you need to change your migration, make sure to migrate down before changing
the `sql` files otherwise Liquibase will yell at you!
