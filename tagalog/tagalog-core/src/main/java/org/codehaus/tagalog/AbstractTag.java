/*
 * $Id: AbstractTag.java,v 1.6 2004-11-02 12:10:43 mhw Exp $
 */

package org.codehaus.tagalog;

import java.util.Map;

/**
 * AbstractTag
 *
 * @author <a href="mailto:mhw@kremvax.net">Mark Wilkinson</a>
 * @version $Revision: 1.6 $
 */
public abstract class AbstractTag implements Tag {
    private TagalogParser parser;

    private Map context;

    private Tag parent;

    public void setParser(TagalogParser parser) {
        this.parser = parser;
    }

    public TagalogParser getParser() {
        return parser;
    }

    public void setContext(Map context) {
        this.context = context;
    }

    protected Map getContext() {
        return context;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return parent;
    }

    public void begin(String elementName, Attributes attribute)
        throws TagException, TagalogParseException
    {
    }

    public void text(char[] characters, int start, int length)
        throws TagException, TagalogParseException
    {
    }

    public void child(Object child)
        throws TagException, TagalogParseException
    {
    }

    public Object end(String elementName)
        throws TagException, TagalogParseException
    {
        return null;
    }

    public boolean recycle() {
        return true;
    }

    //
    // Convenience methods to make tag implementation easier.
    //

    protected Location getLocation() {
        return parser.getLocation();
    }

    protected String requireAttribute(Attributes attributes,
                                      String elementName,
                                      String attributeName)
        throws TagException
    {
        return TagUtils.requireAttribute(attributes, elementName, attributeName);
    }

    protected Tag findAncestorWithClass(Class tagClass) {
        return TagUtils.findAncestorWithClass(getParent(), tagClass);
    }

    protected Tag requireAncestor(String tagName, Class tagClass)
        throws TagException
    {
        return TagUtils.requireAncestor(getParent(), tagName, tagClass);
    }
}
