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
 * OpenNMS(R) Licensing <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.v1.scv.immutables;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.opennms.integration.api.v1.scv.Credentials;

public class ImmutableCredentials implements Credentials, Serializable {
    static final long serialVersionUID = -1241293670886186178L;

    private final String username;
    private final String password;
    private final Map<String, String> attributes;

    public ImmutableCredentials(String username, String password) {
        this(username, password, Collections.emptyMap());
    }

    public ImmutableCredentials(String username, String password, Map<String, String> attributes) {
        this.username = username;
        this.password = password;
        if (attributes == null) {
            this.attributes = Collections.unmodifiableMap(Collections.emptyMap());
        } else {
            this.attributes = Collections.unmodifiableMap(Objects.requireNonNull(attributes));
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Set<String> getAttributeKeys() {
        return attributes.keySet();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes, password, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Credentials other = (Credentials) obj;
        return Objects.equals(this.attributes, other.getAttributes()) &&
                Objects.equals(this.password, other.getPassword()) &&
                Objects.equals(this.username, other.getUsername());
    }

    @Override
    public String toString() {
        return String.format("Credentials[username=%s,password=XXXXXX]", username);
    }
}