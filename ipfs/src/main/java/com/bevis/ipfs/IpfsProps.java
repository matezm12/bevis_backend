package com.bevis.ipfs;

import com.bevis.ipfs.cluster.dto.ClusterProps;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ipfs")
public class IpfsProps {
    private ClusterProps cluster;

    public ClusterProps getCluster() {
        return cluster;
    }

    public void setCluster(ClusterProps cluster) {
        this.cluster = cluster;
    }

}
