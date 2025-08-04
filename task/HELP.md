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


### Kafka Setup and Usage

Este projeto utiliza o Apache Kafka para comunicação assíncrona e processamento de eventos. Siga estas etapas para configurar e usar o Kafka:

1. **Iniciar o Container do Kafka**
   ```bash
   docker-compose up -d kafka zookeeper
   ```

2. **Verificar o Status dos Containers**
   ```bash
   docker-compose ps
   # ou
   docker ps | grep -E 'kafka|zookeeper'
   ```

3. **Verificar Logs dos Containers (Opcional)**
   ```bash
   docker-compose logs -f kafka
   # ou
   docker-compose logs -f zookeeper
   ```

4. **Criar um Tópico (se necessário)**
   ```bash
   docker exec -it kafka kafka-topics --create --topic task-notification-v1 --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
   ```

5. **Listar Tópicos Existentes**
   ```bash
   docker exec -it kafka kafka-topics --list --bootstrap-server localhost:9092
   ```

6. **Monitorar Mensagens em um Tópico (útil para depuração)**
   ```bash
   docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic task-notification-v1 --from-beginning
   ```

7. **Publicar Mensagens de Teste (útil para depuração)**
   ```bash
   docker exec -it kafka kafka-console-producer --bootstrap-server localhost:9092 --topic task-notification-v1
   ```

8. **Parar os Containers**
   ```bash
   docker-compose down
   ```

**Configuração do Kafka no Projeto:**

- O projeto utiliza o Spring Cloud Stream com Kafka Binder para abstração da comunicação com o Kafka
- O tópico principal usado é `task-notification-v1` (configurado em `kafka.task.notification-output`)
- O grupo de consumidores é `task-notification-v1-group` (configurado em `kafka.task.notification-group-id`)
- Mensagens são serializadas/desserializadas como JSON
- O pacote `com.gabryel.task.entity` está configurado como confiável para desserialização

**Exemplo de Produção de Mensagens:**
```java
@Autowired
private StreamBridge streamBridge;

public void notifyTaskCreation(TaskEntity task) {
    streamBridge.send("task-notification-v1-out", task);
}
```

**Exemplo de Consumo de Mensagens:**
```java
@Bean
public Consumer<TaskEntity> processTaskNotification() {
    return task -> {
        log.info("Recebida notificação para a tarefa: {}", task.getTitle());
        // Processamento adicional aqui
    };
}
```

**Dependências Relevantes:**
- spring-cloud-stream
- spring-cloud-stream-binder-kafka
- spring-kafka

**Importante:**
- Certifique-se de que o Kafka esteja em execução antes de iniciar a aplicação
- Verifique as configurações no arquivo `application.yaml` para ajustar parâmetros do Kafka
- Para ambiente de produção, considere configurar múltiplas instâncias do Kafka para alta disponibilidade
