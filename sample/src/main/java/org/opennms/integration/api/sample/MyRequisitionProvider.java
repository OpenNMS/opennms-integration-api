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

package org.opennms.integration.api.sample;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;

import org.opennms.integration.api.v1.config.requisition.Requisition;
import org.opennms.integration.api.v1.config.requisition.SnmpPrimaryType;
import org.opennms.integration.api.v1.config.requisition.beans.RequisitionBean;
import org.opennms.integration.api.v1.config.requisition.beans.RequisitionInterfaceBean;
import org.opennms.integration.api.v1.config.requisition.beans.RequisitionNodeBean;
import org.opennms.integration.api.v1.requisition.RequisitionProvider;
import org.opennms.integration.api.v1.requisition.RequisitionRequest;

public class MyRequisitionProvider implements RequisitionProvider {
    public static final String TYPE = "MyRequisitionProvider";

    private final RequisitionTestContextManager requisitionManager;

    public MyRequisitionProvider(RequisitionTestContextManager requisitionManager) {
        this.requisitionManager = Objects.requireNonNull(requisitionManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public RequisitionRequest getRequest(Map<String, String> parameters) {
        return new MyRequisitonRequest(parameters);
    }

    @Override
    public Requisition getRequisition(RequisitionRequest genericRequest) {
        final MyRequisitonRequest request = (MyRequisitonRequest)genericRequest;
        requisitionManager.trackGetRequisitionForSession(request.getSessionId());
        final InetAddress loopback = InetAddress.getLoopbackAddress();
        return RequisitionBean.builder()
                .foreignSource(request.getForeignSource())
                .node(RequisitionNodeBean.builder()
                        .foreignId("n1")
                        .nodeLabel("n1")
                        .asset("serialnumber", "42")
                        .metaData("oai", "sn", "42")
                        .iface(RequisitionInterfaceBean.builder()
                                    .ipAddress(loopback)
                                    .snmpPrimary(SnmpPrimaryType.NOT_ELIGIBLE)
                                    .metaData("oai", "mac", "00aabbccddee")
                                    .build())
                        .build())
                .build();
    }

    @Override
    public byte[] marshalRequest(RequisitionRequest request) {
        throw new UnsupportedOperationException("No Minion support.");
    }

    @Override
    public RequisitionRequest unmarshalRequest(byte[] bytes) {
        throw new UnsupportedOperationException("No Minion support.");
    }

    private static class MyRequisitonRequest implements RequisitionRequest {
        private final Map<String, String> parameters;

        public MyRequisitonRequest(Map<String, String> parameters) {
            this.parameters = Objects.requireNonNull(parameters);
        }

        public String getForeignSource() {
            return parameters.getOrDefault("foreignSource", "fs");
        }

        public String getSessionId() {
            return parameters.get(RequisitionTestContextManager.SESSION_ID_PARM_NAME);
        }
    }
}
