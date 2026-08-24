/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2026 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2026 The OpenNMS Group, Inc.
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

package org.opennms.integration.api.v1.mcp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The result of executing a {@link McpToolProvider} tool: one or more text
 * content blocks and an error flag.
 *
 * @since 2.1.0
 */
public final class McpToolResult {
    private final List<String> textContents;
    private final boolean error;

    private McpToolResult(Builder builder) {
        this.textContents = Collections.unmodifiableList(new ArrayList<>(builder.textContents));
        this.error = builder.error;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * Convenience factory for a successful single text result.
     */
    public static McpToolResult text(String text) {
        return newBuilder().addText(text).build();
    }

    /**
     * Convenience factory for a failed single text result.
     */
    public static McpToolResult error(String text) {
        return newBuilder().addText(text).setError(true).build();
    }

    public List<String> getTextContents() {
        return textContents;
    }

    public boolean isError() {
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final McpToolResult that = (McpToolResult) o;
        return error == that.error && Objects.equals(textContents, that.textContents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textContents, error);
    }

    @Override
    public String toString() {
        return "McpToolResult{textContents=" + textContents + ", error=" + error + "}";
    }

    public static final class Builder {
        private final List<String> textContents = new ArrayList<>();
        private boolean error;

        private Builder() {
        }

        public Builder addText(String text) {
            this.textContents.add(Objects.requireNonNull(text));
            return this;
        }

        public Builder setError(boolean error) {
            this.error = error;
            return this;
        }

        public McpToolResult build() {
            if (textContents.isEmpty()) {
                throw new IllegalStateException("At least one text content block is required");
            }
            return new McpToolResult(this);
        }
    }
}
