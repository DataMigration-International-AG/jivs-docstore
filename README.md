# Jivs-Docstore
### Requirements
- Java 21
- MSSQL Database

### Dependency
```xml
<dependency>
  <groupId>com.datamigration</groupId>
  <artifactId>jivs-docstore</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Configuration
The following config values need to be:
```properties
# Database username
jds.mssql.username=
# Database password
jds.mssql.password=
# Database jdbc url
jds.mssql.url=
```

### How to use
The class DocStoreManager can be used to use the functionalities from this lib.
```java
DocStoreManager docStoreManager = DocStoreManager.getInstance();
JivsDocument insertedDocument = docStoreManager.create(jivsDocument);
```
