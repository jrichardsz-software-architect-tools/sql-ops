# dbvops

Do you want to automatize your sql scripts execution as you do with your source code?

# Requirements

- java >=8

# Usage

- Download the latest jar
- Create scripts ending with .sql
- For each .sql file, you should add a .rollback file. This script is used to revert changes in the database caused by these wrong scripts.
- Run

```
java -Duser.timezone=GMT -jar /foo/dbvops.jar \
--database_host=$database_host \
--database_port=$database_port \
--database_name=$database_name \
--database_user=$database_user \
--database_password=$database_password \
--scripts_folder=/bar/baz/scripts \
--engine=mysql
```

# build

```
mvn clean package -DskipTests=true -Dcobertura.skip=true
```