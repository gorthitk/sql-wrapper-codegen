# sql-wrapper-codegen [![Gradle Status](https://gradleupdate.appspot.com/jetsasank/sql-wrapper-codegen/status.svg)](https://gradleupdate.appspot.com/jetsasank/sql-wrapper-codegen/status) [![CircleCI](https://circleci.com/gh/jetsasank/sql-wrapper-codegen/tree/master.svg?style=svg)](https://circleci.com/gh/jetsasank/sql-wrapper-codegen/tree/master) [![Build Status](https://travis-ci.org/jetsasank/sql-wrapper-codegen.svg?branch=master)](https://travis-ci.org/jetsasank/sql-wrapper-codegen)

![Language](https://img.shields.io/badge/language-Java-brightgreen.svg)&nbsp;

Gradle plugin to reduce boiler-plate code for running JDBC queries in java and improve [type safety](https://en.wikipedia.org/wiki/Type_safety) while running [PreparedStatements](https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html) in java.

This plugin requires simple YAML files and a gradle plugin to generate wrapper java classes that abstract away the boiler-plate code.

# Code generation

- Step 1

# Gradle plugin configuration

```groovy
// Add the gradle plugin.
plugins {
  id 'org.jet.sql.codegen' version '1.1.1'
}

sqlWrapperConfig {
    sources = fileTree(dir: 'src/main/resources/sql') // source directory for YAML sql configuration
    generatedFileDirectory = file('src/gen/java') // output directory for generated classes.
    userName = 'testUser' // user name required to connect to the database
    password = 'testPassword' // password required to connect to the database
    type = 'postgresql' or 'mysql' // type of the database to connect. Currently only supports postgres and mysql
    host = 'testHost' // hostname where the database server is deployed.
}

```

- Step 2 : Add the YAML files to the source directory.

Sample Yaml file

```yaml
packageName: org.jet.test
className: EmployeeQueries
queries:
  - name: 'get_employee_by_id'
    sql: 'SELECT ID, NAME FROM EMPLOYEE WHERE ID = arg_id'
```

- Step 3 : Generate required java classes by running the gradle plugin task

```bash
    ./gradlew generateSqlWrapper
```

- Step 4 : Use the generated classes

Once the plugin runs , it will auto generate all the required wrapper classes. Which in this example will be a class named 
``
 EmployeeQueries
``

under package 
```
    org.jet.test
```

you can then use it in your code to replace boiler plate code 
 
```java
package org.jet.example;

import org.jet.test.EmployeeQueries;

import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeDao
{
    public void getEmployeeById(int id, Connection connection) throws SQLException
    {
        EmployeeQueries.Runners.GetEmployeeByIdQueryRunner()
                .connection(connection)
                .ID(id) // set the id before executing.
                .executeAsStream() // can also use .execute() or .executeAndReturnIterator()
                .forEach(e -> {
                    System.out.println("Employee Id " + e.id() + ", Employee Name" + e.name());
                });
    }
}


```