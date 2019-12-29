# sql-wrapper-codegen [![Gradle Status](https://gradleupdate.appspot.com/jetsasank/sql-wrapper-codegen/status.svg)](https://gradleupdate.appspot.com/jetsasank/sql-wrapper-codegen/status) [![CircleCI](https://circleci.com/gh/jetsasank/sql-wrapper-codegen/tree/master.svg?style=svg)](https://circleci.com/gh/jetsasank/sql-wrapper-codegen/tree/master) [![Build Status](https://travis-ci.org/jetsasank/sql-wrapper-codegen.svg?branch=master)](https://travis-ci.org/jetsasank/sql-wrapper-codegen)

Gradle plugin to generate boiler-plate code for running JDBC queries in java.

This plugin requires simple YAML files and a gradle plugin to generate wrapper java classes that abstract away the boiler-plate code.

# Code generation

- Step 1

# Gradle plugin configuration

```groovy
// Add the gradle plugin.
plugins {
  id 'org.jet.sql.codegen' version '' // TODO
}

sqlWrapperConfig {
    sources = fileTree(dir: 'src/main/resources/sql') // source directory for YAML sql configuration
    generatedFileDirectory = file('src/gen/java') // output directory for generated classes.
}

```

- Step 2 : Add the YAML files to the source directory.

Sample Yaml file

```yaml
packageName: org.jet.test
className: EmployeeQueries
queries:
  - name: 'get_all_employees'
    sql: 'SELECT ID, NAME FROM EMPLOYEE WHERE ID = arg_id'
    arguments:
      - name: arg_id
      - type: integer
    results:
      - name: name
        type: varchar
      - name: id
        type: integer
```

- Step 3 : Generate required java classes by running the gradle plugin task

```bash
    ./gradlew generateSqlWrapper
```

