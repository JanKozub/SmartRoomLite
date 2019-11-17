package org.jk.smlite.services.connection;

import org.jk.smlite.model.Message;

public interface MessageListener {

    void messageArrived(Message message);

}
