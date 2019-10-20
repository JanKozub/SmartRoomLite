package org.jk.smlite.services.connection;

public interface CommService {

    void connect(String topic);

    void sendMessage(String topic, String message);

    void register(MessageListener listener);
}
