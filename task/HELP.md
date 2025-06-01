# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/servlet.html)
* [Spring Reactive Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/reactive.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### MongoDB Setup and Usage

Follow these steps to set up and use the MongoDB database:

1. **Start the MongoDB Container**
   ```bash
   docker-compose up -d
   ```

2. **Verify Container Status**
   ```bash
   docker-compose ps
   # or
   docker ps
   ```

3. **Check Container Logs (Optional)**
   ```bash
   docker-compose logs mongodb
   ```

4. **Connect to MongoDB CLI (Optional)**
   ```bash
   docker exec -it mongodb mongosh
   ```
   Useful commands in MongoDB shell:
   ```javascript
   // List databases
   show dbs
   
   // Use task database
   use task
   
   // List collections
   show collections
   ```

5. **Stop the Container**
   ```bash
   docker-compose down
   ```

6. **Reset Database (If needed)**
   ```bash
   docker-compose down -v
   ```

**Important Notes:**
- MongoDB runs on default port 27017
- Data is persisted through the `data_mongodb` volume
- The container has automatic restart configured
- Spring Boot is pre-configured to connect to this MongoDB instance

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
