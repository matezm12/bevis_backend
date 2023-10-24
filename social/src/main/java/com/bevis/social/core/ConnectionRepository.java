package com.bevis.social.core;


import com.bevis.social.core.Connection;
import com.bevis.social.core.dto.ConnectionKey;

public interface ConnectionRepository {

    void addConnection(Connection var1);

    void removeConnection(ConnectionKey var1);
}
