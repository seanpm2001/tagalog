/*
 * $Id: Proc.java,v 1.5 2004-10-01 15:02:22 mhw Exp $
 */

package org.codehaus.tagalog.jdbc;

/**
 * Representation of a procedure, which attaches a name and a connection
 * to a {@link ProcStatement}.
 *
 * @author Mark H. Wilkinson
 * @version $Revision: 1.5 $
 */
public final class Proc {
    private String name;

    private String connectionName;

    private ProcStatement body;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setBody(ProcStatement body) {
        this.body = body;
    }

    public Object execute(Catalog catalog, ProcContext ctx)
        throws ProcException
    {
        Object result = null;

        ctx.setConnectionName(connectionName);
        try {
            ctx.begin();
            result = body.execute(catalog, this, ctx);
        } finally {
            ctx.end();
        }
        return result;
    }
}
