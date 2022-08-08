# database-ops

Simple tool to execute any script sql.

![](./coverage.png)
![](./branch.png)

# Requirements

- java >=8
- some database for tests. Supported engines: oracle, mysql

# Usage

- Download the latest jar
- Create scripts ending with .sql
- For each .sql file, you should add a .rollback file. This script is used to revert changes in the database caused by these wrong scripts.
- Run


```
java -Duser.timezone=GMT -jar /foo/database-ops.jar \
--database_host=$database_host \
--database_port=$database_port \
--database_name=$database_name \
--database_user=$database_user \
--database_password=$database_password \
--scripts_folder=/bar/baz/scripts \
--engine=mysql
```

For a quicky test you could use the internal sqlyog


mkdir -p /tmp/workspace
cp src/test/resources/real.databases/sqlite/chinook.db /tmp/workspace
cp src/test/resources/real.databases/sqlite/ddl_tables /tmp/workspace -r

```
java -Duser.timezone=GMT -jar target/database-ops.jar \
--database_host=foo \
--database_port=1 \
--database_name=/tmp/workspace/chinook.db \
--database_user=foo \
--database_password=foo \
--scripts_folder=/tmp/workspace/ddl_tables \
--engine=sqlite
```

# Build

```
mvn clean package
```

# Parameters

|parameter|sample value|description|
| --- | --- | --- |
|database_host|10.100.15.26|database host|
|database_port|3306|database port. 1521 for oracle|
|database_name|value|database name|
|database_user|value|database user|
|database_password|value|database password|
|scripts_folder|value|/foo/bar/scripts|
|engine|oracle|database engine. Supported values: oracle,mysql,sqlite|
|--verbose_log||enable a lot of log|

# Roadmap

- [ ] Solve compiler warnings
- [ ] Fix https://github.com/mockito/mockito-cglib/issues/1
- [ ] Publish to mvn repository


# Contributors

<table>
  <tbody>
    <td>
      <img src="https://avatars0.githubusercontent.com/u/3322836?s=460&v=4" width="100px;"/>
      <br />
      <label><a href="http://jrichardsz.github.io/">JRichardsz</a></label>
      <br />
    </td>    
  </tbody>
</table>
