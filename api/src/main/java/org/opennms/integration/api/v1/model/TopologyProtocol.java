/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019-2023 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2023 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
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
 * NOTE: If you modify this file, you <strong>must</strong> also update the <code>opennms-kafka-producer.proto</code> in ALEC,
 * as well as anything relying on this enum in Enlinkd and Topology in OpenNMS proper
 * (including the copy of <code>opennms-kafka-producer.proto</code> in features/kafka/producer).
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

