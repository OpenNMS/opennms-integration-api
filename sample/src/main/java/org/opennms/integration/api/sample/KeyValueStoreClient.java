/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License Version 2.0 for more details.
 *
 * You should have received a copy of the Apache License Version 2.0
 * along with OpenNMS(R).  If not, see:
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.sample;

import java.util.concurrent.TimeUnit;

import org.opennms.integration.api.v1.distributed.KeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyValueStoreClient {
    private static final Logger LOG = LoggerFactory.getLogger(KeyValueStoreClient.class);
    private final KeyValueStore<String> store;
    private String value = "{\"company\": \"OpenNMS\", \"location\": \"Canada\"}";

    private String context1 = "OIA-Test-one";

    public KeyValueStoreClient(KeyValueStore store) {
        this.store = store;
        LOG.debug("KeyValue store is {}", store.getName());
        keyValueOperation();
    }

    public void keyValueOperation() {
        testPutWithoutTTL();
        testPUtWithTTL();
    }

    private void testPutWithoutTTL() {
        long now = System.currentTimeMillis();
        long tmp = store.put("k1", value, context1);
        if(tmp < now) {
            LOG.warn("Wrong timestamp when put the key value");
        }
        String result = store.get("k1", context1).orElse(null);
        if(!value.equals(result)) {
            LOG.warn("Wrong value retrieved from key-value store {}, {}", "k1",result);
        }
    }

    private void testPUtWithTTL(){
        store.put("k1", value, context1, 1);
        String result = store.get("k1", context1).orElse(null);
        if(!value.equals(result)) {
            LOG.warn("Wrong value retrieved from key-value store {}, {}", "k1",result);
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = store.get("k1", context1).orElse(null);
        if(result != null) {
            LOG.warn("Value should be null {}, {}", "k1",result);
        }
    }


}
