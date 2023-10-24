package com.bevis.social.core;

import java.util.List;

public interface UsersConnectionRepository {
    List<String> findUserIdsWithConnection(Connection var1);

    ConnectionRepository createConnectionRepository(String var1);
}
