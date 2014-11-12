package com.codenvy.commons.xml;

/**
 * @author Eugene Voevodin
 */
public final class NewAttribute extends PrefixedName {

    private String value;

    public NewAttribute(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String asString() {
        final StringBuilder sb = new StringBuilder();
        if (hasPrefix()) {
            sb.append(prefix).append(':');
        }
        return sb.append(getName())
                 .append('=')
                 .append('"')
                 .append(value)
                 .append('"')
                 .toString();
    }

    @Override
    public String toString() {
        return asString();
    }
}
