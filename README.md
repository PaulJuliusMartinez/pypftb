# Website

How to start the Website application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/website-0.0.1-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Adding new Models to the Database
---

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
