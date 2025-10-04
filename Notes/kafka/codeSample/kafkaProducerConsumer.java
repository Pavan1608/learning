// ============================= KAFKA PRODUCER EXAMPLES =============================

// 1. Basic Producer
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import java.util.concurrent.Future;

public class BasicKafkaProducer {
    private final Producer<String, String> producer;
    
    public BasicKafkaProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Reliability settings
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        // Performance settings
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        
        this.producer = new KafkaProducer<>(props);
    }
    
    // Fire and forget
    public void sendFireAndForget(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record);
    }
    
    // Synchronous send
    public void sendSync(String topic, String key, String value) throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        Future<RecordMetadata> future = producer.send(record);
        RecordMetadata metadata = future.get(); // This will block
        System.out.printf("Sent record to topic %s partition %d offset %d%n", 
                         metadata.topic(), metadata.partition(), metadata.offset());
    }
    
    // Asynchronous send with callback
    public void sendAsync(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    System.err.println("Error sending message: " + exception.getMessage());
                } else {
                    System.out.printf("Sent record to topic %s partition %d offset %d%n", 
                                     metadata.topic(), metadata.partition(), metadata.offset());
                }
            }
        });
    }
    
    public void close() {
        producer.close();
    }
    
    public static void main(String[] args) throws Exception {
        BasicKafkaProducer producer = new BasicKafkaProducer();
        
        // Send messages
        for (int i = 0; i < 10; i++) {
            producer.sendAsync("test-topic", "key-" + i, "message-" + i);
        }
        
        Thread.sleep(1000); // Allow async sends to complete
        producer.close();
    }
}

// 2. Custom Partitioner
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, 
                        byte[] valueBytes, Cluster cluster) {
        int numPartitions = cluster.partitionCountForTopic(topic);
        
        if (key == null) {
            // Round-robin for null keys
            return (int) (Math.random() * numPartitions);
        }
        
        // Hash-based partitioning for non-null keys
        return Math.abs(key.hashCode()) % numPartitions;
    }
    
    @Override
    public void close() {}
    
    @Override
    public void configure(Map<String, ?> configs) {}
}

// 3. Transactional Producer
public class TransactionalProducer {
    private final Producer<String, String> producer;
    
    public TransactionalProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Transactional settings
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-producer");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        
        this.producer = new KafkaProducer<>(props);
        producer.initTransactions();
    }
    
    public void sendInTransaction(String topic, String[] keys, String[] values) {
        producer.beginTransaction();
        try {
            for (int i = 0; i < keys.length; i++) {
                producer.send(new ProducerRecord<>(topic, keys[i], values[i]));
            }
            producer.commitTransaction();
        } catch (Exception e) {
            producer.abortTransaction();
            throw e;
        }
    }
    
    public void close() {
        producer.close();
    }
}

// ============================= KAFKA CONSUMER EXAMPLES =============================

// 4. Basic Consumer
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class BasicKafkaConsumer {
    private final Consumer<String, String> consumer;
    
    public BasicKafkaConsumer(String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        // Offset management
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual commit
        
        // Performance settings
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 500);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);
        
        this.consumer = new KafkaConsumer<>(props);
    }
    
    public void consume(String topic) {
        consumer.subscribe(Arrays.asList(topic));
        
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed record: key=%s, value=%s, partition=%d, offset=%d%n",
                                     record.key(), record.value(), record.partition(), record.offset());
                    
                    // Process the record
                    processRecord(record);
                }
                
                // Manual commit after processing
                consumer.commitSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
    
    private void processRecord(ConsumerRecord<String, String> record) {
        // Business logic here
        System.out.println("Processing: " + record.value());
    }
    
    public static void main(String[] args) {
        BasicKafkaConsumer consumer = new BasicKafkaConsumer("my-consumer-group");
        consumer.consume("test-topic");
    }
}

// 5. Consumer with Rebalance Listener
public class ConsumerWithRebalanceListener {
    private final Consumer<String, String> consumer;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    
    public ConsumerWithRebalanceListener(String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        
        this.consumer = new KafkaConsumer<>(props);
    }
    
    public void consume(String topic) {
        consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("Partitions revoked: " + partitions);
                // Commit current offsets before rebalancing
                consumer.commitSync(currentOffsets);
            }
            
            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("Partitions assigned: " + partitions);
                // Initialize any required state
            }
        });
        
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                    
                    // Track offset for manual commit
                    currentOffsets.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1)
                    );
                }
                
                consumer.commitAsync(currentOffsets, null);
            }
        } finally {
            consumer.close();
        }
    }
    
    private void processRecord(ConsumerRecord<String, String> record) {
        System.out.printf("Processing: %s%n", record.value());
    }
}

// ============================= KAFKA STREAMS EXAMPLES =============================

// 6. Basic Kafka Streams Application
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.common.serialization.Serdes;
import java.util.Properties;

public class BasicStreamsApplication {
    
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder();
        
        // Word count example
        KStream<String, String> textLines = builder.stream("text-input");
        
        KTable<String, Long> wordCounts = textLines
            .flatMapValues(textLine -> Arrays.asList(textLine.toLowerCase().split("\\W+")))
            .groupBy((key, word) -> word)
            .count(Materialized.as("counts-store"));
        
        wordCounts.toStream().to("wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

// 7. Advanced Streams with Windowing
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import java.time.Duration;

public class WindowedStreamsApplication {
    
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "windowed-count-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder();
        
        KStream<String, String> stream = builder.stream("page-views");
        
        // Count page views per user in 5-minute windows
        KTable<Windowed<String>, Long> windowedCounts = stream
            .groupByKey()
            .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
            .count();
        
        // Convert to stream and print
        windowedCounts.toStream().foreach((windowedKey, count) -> {
            System.out.printf("User: %s, Window: %s-%s, Count: %d%n",
                             windowedKey.key(),
                             windowedKey.window().startTime(),
                             windowedKey.window().endTime(),
                             count);
        });
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

// 8. Streams with State Stores
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

public class StatefulProcessor implements Processor<String, String, String, String> {
    private ProcessorContext<String, String> context;
    private KeyValueStore<String, String> store;
    
    @Override
    public void init(ProcessorContext<String, String> context) {
        this.context = context;
        this.store = context.getStateStore("my-store");
    }
    
    @Override
    public void process(Record<String, String> record) {
        String key = record.key();
        String value = record.value();
        
        // Get previous value
        String previousValue = store.get(key);
        
        // Update state
        store.put(key, value);
        
        // Forward processed record
        context.forward(new Record<>(key, "Previous: " + previousValue + ", Current: " + value, 
                                   record.timestamp()));
    }
    
    @Override
    public void close() {
        // Cleanup if needed
    }
}

// ============================= KAFKA CONNECT EXAMPLES =============================

// 9. Custom Source Connector
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.connector.ConnectorContext;
import org.apache.kafka.common.config.ConfigDef;
import java.util.List;
import java.util.Map;

public class CustomSourceConnector extends SourceConnector {
    private Map<String, String> configProps;
    
    @Override
    public String version() {
        return "1.0";
    }
    
    @Override
    public void start(Map<String, String> props) {
        this.configProps = props;
    }
    
    @Override
    public Class<? extends SourceTask> taskClass() {
        return CustomSourceTask.class;
    }
    
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return Arrays.asList(configProps);
    }
    
    @Override
    public void stop() {
        // Cleanup resources
    }
    
    @Override
    public ConfigDef config() {
        return new ConfigDef()
            .define("file.path", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "File path to read from")
            .define("topic", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Topic to write to");
    }
}

// 10. Custom Source Task
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.data.Schema;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class CustomSourceTask extends SourceTask {
    private String filePath;
    private String topic;
    private BufferedReader reader;
    private long offset = 0;
    
    @Override
    public String version() {
        return "1.0";
    }
    
    @Override
    public void start(Map<String, String> props) {
        this.filePath = props.get("file.path");
        this.topic = props.get("topic");
        
        try {
            this.reader = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not open file: " + filePath, e);
        }
    }
    
    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        try {
            String line = reader.readLine();
            if (line != null) {
                Map<String, Object> sourcePartition = Collections.singletonMap("file", filePath);
                Map<String, Object> sourceOffset = Collections.singletonMap("position", offset++);
                
                SourceRecord record = new SourceRecord(
                    sourcePartition,
                    sourceOffset,
                    topic,
                    Schema.STRING_SCHEMA,
                    line
                );
                
                return Collections.singletonList(record);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
        
        return null; // No data available
    }
    
    @Override
    public void stop() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            // Log error
        }
    }
}

// 11. Custom Sink Connector
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.connect.sink.SinkTask;

public class CustomSinkConnector extends SinkConnector {
    private Map<String, String> configProps;
    
    @Override
    public String version() {
        return "1.0";
    }
    
    @Override
    public void start(Map<String, String> props) {
        this.configProps = props;
    }
    
    @Override
    public Class<? extends SinkTask> taskClass() {
        return CustomSinkTask.class;
    }
    
    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        return Arrays.asList(configProps);
    }
    
    @Override
    public void stop() {
        // Cleanup
    }
    
    @Override
    public ConfigDef config() {
        return new ConfigDef()
            .define("output.file", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Output file path");
    }
}

// 12. Custom Sink Task
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class CustomSinkTask extends SinkTask {
    private String outputFile;
    private PrintWriter writer;
    
    @Override
    public String version() {
        return "1.0";
    }
    
    @Override
    public void start(Map<String, String> props) {
        this.outputFile = props.get("output.file");
        try {
            this.writer = new PrintWriter(new FileWriter(outputFile, true));
        } catch (IOException e) {
            throw new RuntimeException("Could not open output file: " + outputFile, e);
        }
    }
    
    @Override
    public void put(Collection<SinkRecord> records) {
        for (SinkRecord record : records) {
            writer.println(String.format("Topic: %s, Partition: %d, Offset: %d, Value: %s",
                                        record.topic(), record.kafkaPartition(), 
                                        record.kafkaOffset(), record.value()));
        }
        writer.flush();
    }
    
    @Override
    public void stop() {
        if (writer != null) {
            writer.close();
        }
    }
}

// ============================= UTILITY CLASSES =============================

// 13. Kafka Admin Client Operations
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdminOperations {
    private final AdminClient adminClient;
    
    public KafkaAdminOperations() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        this.adminClient = AdminClient.create(props);
    }
    
    public void createTopic(String topicName, int numPartitions, short replicationFactor) {
        NewTopic newTopic = new NewTopic(topicName, numPartitions, replicationFactor);
        
        CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
        
        try {
            result.all().get();
            System.out.println("Topic created successfully: " + topicName);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to create topic: " + e.getMessage());
        }
    }
    
    public void listTopics() {
        ListTopicsResult result = adminClient.listTopics();
        
        try {
            result.names().get().forEach(System.out::println);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to list topics: " + e.getMessage());
        }
    }
    
    public void describeTopic(String topicName) {
        DescribeTopicsResult result = adminClient.describeTopics(Collections.singletonList(topicName));
        
        try {
            TopicDescription description = result.values().get(topicName).get();
            System.out.println("Topic: " + description.name());
            System.out.println("Partitions: " + description.partitions().size());
            description.partitions().forEach(partition -> {
                System.out.println("  Partition " + partition.partition() + 
                                 " - Leader: " + partition.leader().id() + 
                                 " - Replicas: " + partition.replicas().size());
            });
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to describe topic: " + e.getMessage());
        }
    }
    
    public void deleteTopic(String topicName) {
        DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(topicName));
        
        try {
            result.all().get();
            System.out.println("Topic deleted successfully: " + topicName);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to delete topic: " + e.getMessage());
        }
    }
    
    public void close() {
        adminClient.close();
    }
}

// 14. Custom Serializer/Deserializer
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

// User POJO
class User {
    private String id;
    private String name;
    private String email;
    
    public User() {}
    
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}

// Custom Serializer
public class UserSerializer implements Serializer<User> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}
    
    @Override
    public byte[] serialize(String topic, User data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing User", e);
        }
    }
    
    @Override
    public void close() {}
}

// Custom Deserializer
public class UserDeserializer implements Deserializer<User> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}
    
    @Override
    public User deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, User.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing User", e);
        }
    }
    
    @Override
    public void close() {}
}

// 15. Producer with Custom Serializer
public class UserProducer {
    private final Producer<String, User> producer;
    
    public UserProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName());
        
        this.producer = new KafkaProducer<>(props);
    }
    
    public void sendUser(String topic, User user) {
        ProducerRecord<String, User> record = new ProducerRecord<>(topic, user.getId(), user);
        
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Error sending user: " + exception.getMessage());
            } else {
                System.out.println("User sent successfully: " + user);
            }
        });
    }
    
    public void close() {
        producer.close();
    }
    
    public static void main(String[] args) {
        UserProducer producer = new UserProducer();
        
        User user1 = new User("1", "John Doe", "john@example.com");
        User user2 = new User("2", "Jane Smith", "jane@example.com");
        
        producer.sendUser("users", user1);
        producer.sendUser("users", user2);
        
        producer.close();
    }
}

// 16. Consumer with Custom Deserializer
public class UserConsumer {
    private final Consumer<String, User> consumer;
    
    public UserConsumer(String groupId) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        this.consumer = new KafkaConsumer<>(props);
    }
    
    public void consume(String topic) {
        consumer.subscribe(Arrays.asList(topic));
        
        try {
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(100));
                
                for (ConsumerRecord<String, User> record : records) {
                    User user = record.value();
                    System.out.println("Received user: " + user);
                    
                    // Process user
                    processUser(user);
                }
            }
        } finally {
            consumer.close();
        }
    }
    
    private void processUser(User user) {
        // Business logic for processing user
        System.out.println("Processing user: " + user.getName());
    }
    
    public static void main(String[] args) {
        UserConsumer consumer = new UserConsumer("user-consumer-group");
        consumer.consume("users");
    }
}

// 17. Kafka Health Check and Monitoring
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class KafkaHealthCheck {
    
    public static void checkConsumerLag(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            consumer.poll(Duration.ofMillis(0)); // Trigger assignment
            
            for (TopicPartition partition : consumer.assignment()) {
                long currentOffset = consumer.position(partition);
                
                Map<TopicPartition, Long> endOffsets = consumer.endOffsets(Arrays.asList(partition));
                long endOffset = endOffsets.get(partition);
                
                long lag = endOffset - currentOffset;
                
                System.out.printf("Partition %d: Current Offset=%d, End Offset=%d, Lag=%d%n",
                                 partition.partition(), currentOffset, endOffset, lag);
            }
        }
    }
    
    public static void main(String[] args) {
        checkConsumerLag("localhost:9092", "my-consumer-group", "test-topic");
    }
}

// 18. Kafka Security Configuration Example
public class SecureKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // SSL Configuration
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", "/path/to/kafka.client.truststore.jks");
        props.put("ssl.truststore.password", "password");
        props.put("ssl.keystore.location", "/path/to/kafka.client.keystore.jks");
        props.put("ssl.keystore.password", "password");
        props.put("ssl.key.password", "password");
        
        // SASL Configuration (alternative to SSL)
        // props.put("security.protocol", "SASL_PLAINTEXT");
        // props.put("sasl.mechanism", "PLAIN");
        // props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username='user' password='password';");
        
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = new ProducerRecord<>("secure-topic", "key", "secure-message");
            producer.send(record);
            System.out.println("Secure message sent successfully");
        }
    }
}

// 19. Kafka Streams Join Example
public class StreamsJoinExample {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-join-example");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        
        StreamsBuilder builder = new StreamsBuilder();
        
        // Create streams
        KStream<String, String> orders = builder.stream("orders");
        KStream<String, String> customers = builder.stream("customers");
        
        // Stream-Stream join
        KStream<String, String> enrichedOrders = orders.join(
            customers,
            (orderValue, customerValue) -> "Order: " + orderValue + ", Customer: " + customerValue,
            JoinWindows.of(Duration.ofMinutes(5))
        );
        
        enrichedOrders.to("enriched-orders");
        
        // Create tables
        KTable<String, String> customerTable = builder.table("customer-table");
        
        // Stream-Table join
        KStream<String, String> orderWithCustomer = orders.join(
            customerTable,
            (orderValue, customerValue) -> orderValue + " - " + customerValue
        );
        
        orderWithCustomer.to("order-with-customer");
        
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

// 20. Performance Testing Utility
public class KafkaPerformanceTest {
    
    public static void performanceTest(String topic, int numMessages, int messageSize) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        // Performance optimizations
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 67108864);
        
        String message = "x".repeat(messageSize);
        
        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < numMessages; i++) {
                producer.send(new ProducerRecord<>(topic, "key-" + i, message));
            }
            
            producer.flush();
            long endTime = System.currentTimeMillis();
            
            double throughput = (double) numMessages / ((endTime - startTime) / 1000.0);
            double mbPerSecond = (throughput * messageSize) / (1024 * 1024);
            
            System.out.printf("Sent %d messages in %d ms%n", numMessages, (endTime - startTime));
            System.out.printf("Throughput: %.2f messages/sec%n", throughput);
            System.out.printf("Throughput: %.2f MB/sec%n", mbPerSecond);
        }
    }
    
    public static void main(String[] args) {
        performanceTest("performance-test", 100000, 1024);
    }
}