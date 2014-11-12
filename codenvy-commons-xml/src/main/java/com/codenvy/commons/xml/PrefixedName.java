package com.codenvy.commons.xml;

/**
 * @author Eugene Voevodin
 */
public class PrefixedName {

    protected String prefix;
    protected String name;

    public PrefixedName(String name) {
        applyName(name);
    }

    public boolean hasPrefix() {
        return prefix != null && !prefix.isEmpty();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void applyName(String newName) {
        final int separator = newName.indexOf(':');
        if (separator != -1) {
            name = newName;
        } else {
            name = newName.substring(separator + 1);
            prefix = newName.substring(0, separator);
        }
    }
}
