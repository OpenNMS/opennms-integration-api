/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.config.events;

import java.io.File;
import java.util.List;

/**
 * CollectionGroup to use for event performance data.
 *
 * @since 1.0.0
 */
public interface CollectionGroup {
    String getName();

    String getResourceType();

    String getInstance();

    Rrd getRrd();

    List<Collection> getCollection();

    interface Rrd {
        Integer getStep();

        int getHeartBeat();

        List<String> getRras();

        File getBaseDir();
    }

    interface Collection {
        String getName();

        String getRename();

        AttributeType getType();

        List<ParamValue> getParamValue();
    }

    interface ParamValue {
        String getName();

        Double getValue();
    }
}