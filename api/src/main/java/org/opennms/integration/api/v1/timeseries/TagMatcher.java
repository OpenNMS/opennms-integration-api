package org.opennms.integration.api.v1.timeseries;

/**
 * A TagMatcher is used to search for Metrics.
 * A TagMatcher works in the following way:
 * The key defines to which Tags it can be applied. All Tags that have the same key as the TagMatcher are evaluated.
 * The value of a Tag is evaluated against the value of a TagMatcher.
 * The comparison is defined by the Type.
 *
 * Examples:
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myOtherKey, myValue) => no match
 * Tag(myKey, myValue) applied to TagMatcher(notEquals, myOtherKey, myValue) => no match
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myKey, myValue)      => match
 * Tag(myKey, myValue) applied to TagMatcher(equals,    myKey, myOtherValue) => no match
 * Tag(myKey, myValue) applied to TagMatcher(notEquals, myKey, myOtherValue) => match
 */
public interface TagMatcher {
    enum Type {
        equals,
        notEquals,
        equalsRegex,
        notEqualsRegex
    }

    Type getType();
    String getKey();
    String getValue();
}
