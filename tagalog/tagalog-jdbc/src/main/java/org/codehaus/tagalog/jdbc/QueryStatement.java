/*
 * $Id: QueryStatement.java,v 1.5 2004-01-28 15:28:13 mhw Exp $
 *
 * Copyright (c) 2003 Fintricity Limited. All Rights Reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF FINTRICITY LIMITED
 * The copyright notice above does not evidence any actual or
 * intended publication of such source code.
 */

package com.fintricity.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Mark H. Wilkinson
 * @version $Revision: 1.5 $
 */
public final class QueryStatement extends SQLStatement {
    public void setQueryType(QueryType queryType) {
        super.setQueryType(queryType);
    }

    public Object execute(Catalog catalog, Proc proc, ProcContext ctx)
        throws ProcException
    {
        PreparedStatement stmt = super.prepareAndExecute(catalog, proc, ctx);
        ResultSetWrapper rs;

        if (stmt == null) {
            throw new ProcException("query created no result set", this);
        }
        try {
            rs = new ResultSetWrapper(this, ctx, stmt);
            if (!rs.advanceToFirstRow()) {
                rs.discard();
                return null;
            }
        } catch (SQLException e) {
            try {
                Connection connection = stmt.getConnection();
                stmt.close();
                ctx.returnConnection(connection);
            } catch (SQLException e2) {
                // ignore
            }
            throw new ProcException(e, this);
        }
        return rs;
    }
}
