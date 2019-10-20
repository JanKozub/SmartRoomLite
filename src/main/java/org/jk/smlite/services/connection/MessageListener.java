package org.jk.smlite.services.connection;

import org.jk.smlite.services.Message;

public interface MessageListener {

    void messageArrived(Message message);

}
