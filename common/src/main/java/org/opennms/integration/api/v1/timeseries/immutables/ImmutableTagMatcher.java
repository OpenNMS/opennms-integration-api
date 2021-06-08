package org.opennms.integration.api.v1.timeseries.immutables;

import java.util.Objects;
import java.util.StringJoiner;

import org.opennms.integration.api.v1.timeseries.Tag;
import org.opennms.integration.api.v1.timeseries.TagMatcher;

public class ImmutableTagMatcher implements TagMatcher {
    private final Type type;
    private final String key;
    private final String value;

    public ImmutableTagMatcher(Type type, String key, String value) {
        this.type = Objects.requireNonNull(type);
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableTagMatcher that = (ImmutableTagMatcher) o;
        return type == that.type && Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key, value);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ImmutableTagMatcher.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("key='" + key + "'")
                .add("value='" + value + "'")
                .toString();
    }

    public static TagMatcherBuilder builder() {
        return new TagMatcherBuilder();
    }

    public final static class TagMatcherBuilder {
        private Type type = Type.equals; // default
        private String key;
        private String value;

        public TagMatcherBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public TagMatcherBuilder key(String key) {
            this.key = key;
            return this;
        }

        public TagMatcherBuilder value(String value) {
            this.value = value;
            return this;
        }

        /** Create a matcher that equals this tag */
        public TagMatcherBuilder tag(final Tag tag) {
            this.type = Type.equals;
            this.key = tag.getKey();
            this.value = tag.getValue();
            return this;
        }

        public TagMatcher build() {
            return new ImmutableTagMatcher(this.type, this.key, this.value);
        }
    }
}
