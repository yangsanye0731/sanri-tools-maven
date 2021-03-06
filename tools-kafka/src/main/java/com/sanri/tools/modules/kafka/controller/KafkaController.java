package com.sanri.tools.modules.kafka.controller;

import com.sanri.tools.modules.kafka.dtos.*;
import com.sanri.tools.modules.kafka.service.KafkaDataService;
import com.sanri.tools.modules.kafka.service.KafkaService;
import com.sanri.tools.modules.core.dtos.param.KafkaConnectParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private KafkaDataService kafkaDataService;

    YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();

    /**
     * kafka 连接的创建需要依赖于 zookeeper
     * @param kafkaConnectParam
     */
    @PostMapping(value = "/connect/create",consumes = "application/yaml")
    public void createConnect(@RequestBody String yamlConfig) throws IOException {
        ByteArrayResource byteArrayResource = new ByteArrayResource(yamlConfig.getBytes());
        List<PropertySource<?>> load = yamlPropertySourceLoader.load("a",byteArrayResource);
        Iterable<ConfigurationPropertySource> from = ConfigurationPropertySources.from(load);
        Binder binder = new Binder(from);
        BindResult<KafkaConnectParam> bind = binder.bind("", KafkaConnectParam.class);
        KafkaConnectParam kafkaConnectParam = bind.get();
        kafkaService.createConnect(kafkaConnectParam);
    }

    @PostMapping("/topic/create")
    public void createTopic(String clusterName,String topic,int partitions,int replication) throws InterruptedException, ExecutionException, IOException {
        kafkaService.createTopic(clusterName,topic,partitions,replication);
    }

    @PostMapping("/topic/delete")
    public void deleteTopic(String clusterName,String topic) throws InterruptedException, ExecutionException, IOException {
        kafkaService.deleteTopic(clusterName,topic);
    }

    @GetMapping("/topics")
    public List<TopicInfo> listTopic(String clusterName) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.topics(clusterName);
    }

    @GetMapping("/topic/partitions")
    public int topicPartitions(String clusterName,String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.partitions(clusterName,topic);
    }

    @GetMapping("/topic/logSize")
    public List<TopicLogSize> topicLogSize(String clusterName, String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.logSizes(clusterName,topic);
    }

    @GetMapping("/topic/data/last")
    public List<KafkaData> topicLastDatas(DataConsumerParam dataConsumerParam) throws InterruptedException, ExecutionException, ClassNotFoundException, IOException {
        return kafkaDataService.lastDatas(dataConsumerParam);
    }

    @PostMapping("/topic/data/send/json")
    public void topicSendJsonData(@RequestBody SendJsonDataParam sendJsonDataParam) throws InterruptedException, ExecutionException, IOException {
        kafkaDataService.sendJsonData(sendJsonDataParam);
    }

    @PostMapping("/topic/data/send")
    public void topicSendData(@RequestBody SendObjectDataParam sendObjectDataParam) throws ClassNotFoundException, ExecutionException, InterruptedException, IOException {
        kafkaDataService.sendObjectData(sendObjectDataParam);
    }

    @GetMapping("/groups")
    public List<String> listGroups(String clusterName) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groups(clusterName);
    }

    @PostMapping("/group/delete")
    public void deleteGroup(String clusterName,String group) throws InterruptedException, ExecutionException, IOException {
        kafkaService.deleteGroup(clusterName,group);
    }

    @GetMapping("/group/topics")
    public Set<String> groupSubscribeTopics(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupSubscribeTopics(clusterName,group);
    }

    @GetMapping("/group/topic/data/nearby")
    public List<KafkaData> groupTopicNearbyData(NearbyDataConsumerParam nearbyDataConsumerParam) throws IOException, ClassNotFoundException {
        return kafkaDataService.nearbyDatas(nearbyDataConsumerParam);
    }

    @GetMapping("/group/subscribes")
    public ConsumerGroupInfo consumerGroupInfo(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.consumerGroupInfo(clusterName,group);
    }

    @GetMapping("/group/topic/offset")
    public List<OffsetShow> groupTopicConsumerInfo(String clusterName, String group, String topic) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupTopicConsumerInfo(clusterName,group,topic);
    }

    @GetMapping("/group/topics/offset")
    public List<TopicOffset> groupTopicConsumerInfos(String clusterName, String group) throws InterruptedException, ExecutionException, IOException {
        return kafkaService.groupTopicConsumerInfos(clusterName,group);
    }

    @GetMapping("/brokers")
    public List<String> brokers(String clusterName) throws IOException {
        List<BrokerInfo> brokers = kafkaService.brokers(clusterName);
        List<String> collect = brokers.stream().map(BrokerInfo::hostAndPort).collect(Collectors.toList());
        return collect;
    }
}
