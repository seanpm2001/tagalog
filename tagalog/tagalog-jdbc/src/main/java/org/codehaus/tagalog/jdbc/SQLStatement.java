/*
 * $Id: SQLStatement.java,v 1.3 2004-01-28 10:27:33 mhw Exp $
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
import java.util.Iterator;

import org.codehaus.plexus.util.StringUtils;

import com.fintricity.jdbc.ProcContext.NameValue;

/**
 * @author Mark H. Wilkinson
 * @version $Revision: 1.3 $
 */
public class SQLStatement implements ProcStatement {
    private String dialect;

    private String sql;

    private QueryType queryType = QueryType.ZERO;

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDialect() {
        return dialect;
    }

    public void setSQL(String sql) {
        this.sql = sql;
    }

    public String getSQL() {
        return sql;
    }

    protected void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public String toString() {
        if (dialect != null) {
            return "[" + dialect + "] " + sql;
        } else {
            return sql;
        }
    }

    public Object execute(Catalog catalog, Proc proc, ProcContext ctx)
        throws ProcException
    {
        Connection conn;
        PreparedStatement stmt;
        String expandedSql;

        try {
            if (dialect != null && !ctx.getDialect(catalog).equals(dialect))
                return null;
            conn = ctx.getConnection(catalog);
            expandedSql = expand(sql, ctx);
            stmt = conn.prepareStatement(expandedSql);
            if (stmt.execute()) {
                return new ResultSetWrapper(queryType, ctx, stmt);
            } else {
                stmt.close();
                ctx.returnConnection(conn);
                return null;
            }
        } catch (Exception e) {
            throw new ProcException(e, this);
        }
    }

    private String expand(String sql, ProcContext ctx) {
        Iterator iter = ctx.attributeIterator();
        
        while (iter.hasNext()) {
            NameValue attr = (NameValue) iter.next();
            String pattern = "${" + attr.name + "}";
            sql = StringUtils.replace(sql, pattern, attr.value);
        }
        return sql;
    }
}
