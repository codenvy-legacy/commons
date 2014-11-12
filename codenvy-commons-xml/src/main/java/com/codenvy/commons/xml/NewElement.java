package com.codenvy.commons.xml;

import java.util.ArrayList;
import java.util.List;

import static com.codenvy.commons.xml.Util.tabulate;
import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static java.util.Arrays.asList;

/**
 * @author Eugene Voevodin
 */
public final class NewElement extends PrefixedName {

    public static NewElement create(String name) {
        return new NewElement(name, null);
    }

    public static NewElement create(String name, String text) {
        return new NewElement(name, text);
    }

    public static NewElement create(String name, NewElement... children) {
        final NewElement newElement = create(name);
        newElement.children = new ArrayList<>(asList(children));
        return newElement;
    }

    private static final int EXPECTED_ATTRIBUTES_SIZE = 2;
    private static final int EXPECTED_CHILDREN_SIZE   = 3;

    private String                    text;
    private List<NewAttribute>        attributes;
    private List<NewElement>          children;

    private NewElement(String name, String text) {
        super(name);
        this.text = text;
    }

    public NewElement setText(String text) {
        this.text = text;
        return this;
    }

    public NewElement setAttributes(List<NewAttribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public NewElement setChildren(List<NewElement> children) {
        this.children = children;
        return this;
    }

    public NewElement appendChild(NewElement child) {
        getChildren().add(child);
        return this;
    }

    public NewElement setAttribute(String name, String value) {
        getAttributes().add(new NewAttribute(name, value));
        return this;
    }

    public String getText() {
        return text;
    }

    public List<NewAttribute> getAttributes() {
        if (attributes == null) {
            attributes = newArrayListWithExpectedSize(EXPECTED_ATTRIBUTES_SIZE);
        }
        return attributes;
    }

    public List<NewElement> getChildren() {
        if (children == null) {
            children = newArrayListWithExpectedSize(EXPECTED_CHILDREN_SIZE);
        }
        return children;
    }

    public boolean isVoid() {
        return text == null && !hasChildren();
    }

    public String asString() {
        final StringBuilder builder = new StringBuilder();
        builder.append('<')
               .append(name);
        if (attributes != null) {
            for (NewAttribute attribute : attributes) {
                builder.append(' ')
                       .append(attribute.asString());
            }
        }
        //if it is void element such as <tag attr="value"/>
        if (isVoid()) {
            return builder.append('/')
                          .append('>')
                          .toString();
        }
        builder.append('>')
               .append(getText());
        if (hasChildren()) {
            builder.append('\n');
            for (NewElement child : children) {
                builder.append(tabulate(child.asString(), 1))
                       .append('\n');
            }
        }
        builder.append('<')
               .append('/')
               .append(name)
               .append('>');
        return builder.toString();
    }

    @Override
    public String toString() {
        return asString();
    }

    private boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
}
