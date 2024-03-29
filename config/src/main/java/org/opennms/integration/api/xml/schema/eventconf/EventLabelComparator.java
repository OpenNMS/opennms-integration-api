/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2013-2023 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.xml.schema.eventconf;

import java.io.Serializable;
import java.util.Comparator;

public class EventLabelComparator implements Comparator<Event>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Event e1, final Event e2) {
        final var e1label = e1 == null? null : e1.getEventLabel();
        final var e2label = e2 == null? null : e2.getEventLabel();
        if (e1label == null && e2label == null) return 0;
        if (e1label == null) return -1;
        if (e2label == null) return 1;
        if (e1label.equals(e2label)) return 0;
        return e1label.compareToIgnoreCase(e2label);
    }
}