package xinrui.cloud.filter;

import com.google.common.base.CaseFormat;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * 自定义hibernate生的表格的名称格式，但是这边因为涉及到之前的项目，表名称这样生成就会有问题，所以把application.properties中的配置关闭了
 */
public class HbPhysicalNamingStrategy extends PhysicalNamingStrategyStandardImpl {


    private static final long serialVersionUID = 1L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalTableName(
                new Identifier(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name.getText()),
                        name.isQuoted()),
                context);
    }

}
