/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.collectors;

import java.net.InetAddress;

import org.opennms.integration.api.v1.annotations.Consumable;

/**
 * Persist a collection set.
 *
 * @since 1.0.0
 */
@Consumable
public interface CollectionSetPersistenceService {

    /**
     * Persist a collection set.
     *
     * If the system is using an RRD-based storage backend, then the default RRD
     * file settings will be used.
     *
     * @param nodeId node id of the node for which the collection set is related
     * @param iface IP address associated with the node for which the collection set is related
     * @param collectionSet the collection set containing the metrics to persist
     */
    void persist(int nodeId, InetAddress iface, CollectionSet collectionSet);

    /**
     * Persist a collection set using the given RRD file settings.
     *
     * If the system is not using an RRD-based storage backend i.e. Newts, then the
     * RRD file settings will be ignored.
     *
     * @param nodeId node id of the node for which the collection set is related
     * @param iface IP address associated with the node for which the collection set is related
     * @param collectionSet the collection set containing the metrics to persist
     * @param rrdRepository RRD settings to use when creating RRD files for the metrics in the collection set
     */
    void persist(int nodeId, InetAddress iface, CollectionSet collectionSet, RrdRepository rrdRepository);

}
