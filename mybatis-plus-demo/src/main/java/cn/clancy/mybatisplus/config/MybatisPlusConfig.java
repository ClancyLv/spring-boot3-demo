package cn.clancy.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * @Author ClancyLv
 * @Date 2025/6/23 14:54
 * @Description 配置类--Mybatis-Plus配置
 * 使用多个插件时，需要注意它们的顺序。建议的顺序是：<br>
 * 多租户、动态表名、分页、乐观锁、SQL 性能规范、防止全表更新与删除<br>
 * 总结：对 SQL 进行单次改造的插件应优先放入，不对 SQL 进行改造的插件最后放入。<br>
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件（如果配置多个插件, 切记分页最后添加）
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件<br>
     */
    private PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        // 分页合理化
        paginationInnerInterceptor.setOverflow(true);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件<br>
     * 使用方式：在实体类中，需要在表示版本号的字段上添加 @Version 注解<br>
     */
    private OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 多租户插件<br>
     */
    private TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                // 假设有一个租户上下文，能够从中获取当前用户的租户
                // Long tenantId = TenantContextHolder.getCurrentTenantId();
                // 返回租户ID的表达式，LongValue 是 JSQLParser 中表示 bigint 类型的 class
                return new LongValue(1);
            }

            @Override
            public String getTenantIdColumn() {
                return TenantLineHandler.super.getTenantIdColumn();
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // 根据需要返回是否忽略该表
                return false;
            }
        });
    }

    /**
     * 数据权限插件<br>
     */
    private DataPermissionInterceptor dataPermissionInterceptor() {
        return new DataPermissionInterceptor((MultiDataPermissionHandler) (table, where, mappedStatementId) -> {
            // 在此处编写自定义数据权限逻辑
            try {
                // 数据权限相关的 SQL 片段
                String sqlSegment = "...";
                return CCJSqlParserUtil.parseCondExpression(sqlSegment);
            } catch (JSQLParserException e) {
                return null;
            }
        });
    }

    /**
     * 动态表名插件<br>
     */
    private DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        return new DynamicTableNameInnerInterceptor((sql, tableName) -> {
            // 获取参数方法
            Map<String, Object> paramMap = Collections.singletonMap("param1", "value1");
            paramMap.forEach((k, v) -> System.err.println(k + "----" + v));

            String year = "_2018";
            int random = new Random().nextInt(10);
            if (random % 2 == 1) {
                year = "_2019";
            }
            return tableName + year;
        });
    }

    /**
     * 防全表更新与删除插件<br>
     * 专门用于防止恶意的全表更新和删除操作。该插件通过拦截 update 和 delete 语句，确保这些操作不会无意中影响到整个数据表，从而保护数据的完整性和安全性。<br>
     * 拦截规则：插件默认拦截没有指定条件的 update 和 delete 语句。<br>
     */
    private BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }
}
