Keycloak application created using Spring Boot, and using dependency management with Gradle.
For messaging use Apache Kafka - S (la 2.12  - kafka_2.12-2.2.0 dir) :;
Starting app - Gradlew bootRun

CMD commands for starting Apache Kafka (use cmd in kafka_2.12-2.2.0 dir) :
//zookeaper
1)bin\windows\zookeeper-server-start.bat config\zookeeper.properties

//kafka
2)bin\windows\kafka-server-start.bat config\server.properties

//create topic
3)bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic user-event

//check topic
4)bin\windows\kafka-topics.bat --list --zookeeper localhost:2181

//check what is in the topic
5)bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic user-event --from-beginning

