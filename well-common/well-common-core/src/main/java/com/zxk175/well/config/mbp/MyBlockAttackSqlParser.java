package com.zxk175.well.config.mbp;

import com.baomidou.mybatisplus.core.parser.AbstractJsqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.reflection.MetaObject;

/**
 * @author zxk175
 * @since 2019/03/22 11:05
 */
public class MyBlockAttackSqlParser extends AbstractJsqlParser {

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        if (this.allowProcess(metaObject)) {
            try {
                // 解决调用存储过程或函数时报错
                if (sql.startsWith("{call") || sql.contains("IF")) {
                    return null;
                }

                StringBuilder sqlStringBuilder = new StringBuilder();
                Statements statements = CCJSqlParserUtil.parseStatements(sql);
                int i = 0;
                for (Statement statement : statements.getStatements()) {
                    if (null != statement) {
                        if (i++ > 0) {
                            sqlStringBuilder.append(';');
                        }
                        sqlStringBuilder.append(this.processParser(statement).getSql());
                    }
                }
                if (sqlStringBuilder.length() > 0) {
                    return SqlInfo.newInstance().setSql(sqlStringBuilder.toString());
                }
            } catch (JSQLParserException e) {
                throw ExceptionUtils.mpe("Failed to process, please exclude the tableName or statementId.\n Error SQL: %s", e, sql);
            }
        }
        return null;
    }

    @Override
    public SqlInfo processParser(Statement statement) {
        if (statement instanceof Insert) {
            this.processInsert((Insert) statement);
        } else if (statement instanceof Select) {
            this.processSelectBody(((Select) statement).getSelectBody());
        } else if (statement instanceof Update) {
            this.processUpdate((Update) statement);
        } else if (statement instanceof Delete) {
            this.processDelete((Delete) statement);
        }
        return SqlInfo.newInstance().setSql(statement.toString());
    }

    @Override
    public void processInsert(Insert insert) {
        // to do nothing
    }

    @Override
    public void processDelete(Delete delete) {
        Assert.notNull(delete.getWhere(), "Prohibition of full table deletion");
    }

    @Override
    public void processUpdate(Update update) {
        Assert.notNull(update.getWhere(), "Prohibition of table update operation");
    }

    @Override
    public void processSelectBody(SelectBody selectBody) {
        // to do nothing
    }
}
