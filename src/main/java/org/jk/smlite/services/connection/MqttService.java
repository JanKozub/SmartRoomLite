package org.jk.smlite.services.connection;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jk.smlite.exceptions.DeviceNotRecognizedException;
import org.jk.smlite.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MqttService implements MqttCallback, CommService {
    private static final Logger log = LoggerFactory.getLogger(MqttService.class);
    private final List<MessageListener> listeners = new CopyOnWriteArrayList<>();

    private final MqttClient client;
    private @Autowired
    ApplicationContext context;

    public MqttService() {
        String broker = "tcp://10.0.98.125:1883";
        String clientId = "user";

        try {
            MemoryPersistence persistence = new MemoryPersistence();

            client = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            client.connect(connOpts);

            client.setCallback(this);
        } catch (MqttException ex) {
            throw new RuntimeException("Cannot connect to broker " + broker + ": " + ex.getMessage(), ex);
        }
    }

    public void connect(String topic) {
        try {
            log.debug("{}: Subscribed", topic);
            client.subscribe(topic);
        } catch (MqttException ex) {
            log.error("Cannot subscribe to topic {}", topic, ex);
            throw new RuntimeException("Cannot subscribe to " + topic + ": " + ex.getMessage(), ex);
        }
    }

    public void sendMessage(String topic, String msg) {
        try {
            log.debug("{}: Message send: {}", topic, msg);
            MqttMessage message = new MqttMessage(msg.getBytes());
            message.setQos(2);
            client.publish(topic, message);
        } catch (MqttException ex) {
            log.error("Cannot send, message to topic {}", topic, ex);
            throw new RuntimeException("Cannot send message: " + ex.getMessage(), ex);
        }
    }


    public void register(MessageListener client) {
        listeners.add(client);
    }

    public void unregister(MessageListener client) {
        listeners.remove(client);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log.error("Connection lost rebooting system!");
        ((ConfigurableApplicationContext) context).close();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        log.debug("{}: Received message: {}", topic, message);
        listeners.forEach(l -> {
            try {
                l.messageArrived(new Message(Instant.now(), topic, message.toString()));
            } catch (DeviceNotRecognizedException ex) {
                log.error("message arrived with error syntax {}", ex.getMessage());
            }
        });
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.debug("{}: Delivery completed", (Object) iMqttDeliveryToken.getTopics());
    }
}

