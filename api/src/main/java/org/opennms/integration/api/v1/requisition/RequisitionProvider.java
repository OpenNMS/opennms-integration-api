/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.requisition;

import java.util.Map;

import org.opennms.integration.api.v1.annotations.Exposable;
import org.opennms.integration.api.v1.config.requisition.Requisition;

/**
 * Used to generate {@link Requisition}s from some source.
 *
 * @author jwhite
 */
@Exposable
public interface RequisitionProvider {

    /**
     * A string used to identify the provider type.
     *
     * This string should be unique for every implementation.
     *
     * @return the type string
     */
    String getType();

    /**
     * Generate a request.
     *
     * @param parameters
     * @return
     */
    RequisitionRequest getRequest(Map<String, String> parameters);

    /**
     * Execute the request.
     *
     * @param request
     * @return
     */
    Requisition getRequisition(RequisitionRequest request);

    /**
     * Marshals the request to a byte array.
     *
     * This will be used before sending the request to a Minion.
     *
     * @param request request to marshall
     * @return byte array
     */
    byte[] marshalRequest(RequisitionRequest request);

    /**
     * Unmarshals the request from a byte array.
     *
     * Used when receiving the request on a Minion.
     *
     * @param bytes byte array from a previous call to {@link #marshalRequest(RequisitionRequest)}
     * @return the unmarshaled requisition
     */
    RequisitionRequest unmarshalRequest(byte[] bytes);

}
