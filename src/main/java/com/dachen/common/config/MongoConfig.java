package com.dachen.common.config;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 钟良
 * @desc
 * @date:2017/6/29 10:32 Copyright (c) 2017, DaChen All Rights Reserved.
 */
@Configuration
public class MongoConfig {
    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private MongoProperties properties;

    @Bean(name = "dsForRW")
    public Datastore datastore() {
        Morphia morphia = new Morphia();

        Datastore datastore = morphia.createDatastore(mongoClient, properties.getDatabase());
        datastore.ensureIndexes();
        datastore.ensureCaps();
        return datastore;
    }
}
