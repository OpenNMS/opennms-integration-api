/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019-2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 
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

package org.opennms.integration.api.v1.model;

/**
 * The set of protocols supported by OpenNMS topology.
 *
 * NOTE: This header (unfortunately) does not match the order specified in the
 * <code>opennms-kafka-producer.proto</code> file, so it can not be used for determining
 * serialized protocols from protobuf streams.
 *
 * TODO normalize protocol definition/ordering in 2.0
 */
public enum TopologyProtocol {
    /**
     * A special value representing all protocols.
     */
    ALL,

    BRIDGE,
    CDP,
    ISIS,
    LLDP,
    NODES,
    OSPF,
    USERDEFINED,

    /**
     * @since 1.6
     */
    OSPFAREA,

    /**
     * @since 1.6
     */
    NETWORKROUTER;
}

