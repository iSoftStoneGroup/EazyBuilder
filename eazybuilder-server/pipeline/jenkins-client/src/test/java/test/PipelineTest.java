package test;

import com.eazybuilder.ci.service.PipelineServiceImpl;
import org.junit.Test;

import java.sql.SQLException;


public class PipelineTest {

    @Test
    public  void sqlTakeMinJdbc() throws SQLException {
        PipelineServiceImpl.sqlTakeMinJdbc("jdbc:mysql://mysql.eazybuilder-devops.cn:3306/ci_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&failOverReadOnly=false&serverTimezone=UTC",
                "root",
                "mysql_123",
                        "UPDATE ci_user \n" +
                        "SET \n" +
                        "    `email` = (IF(LENGTH(email) > 2,\n" +
                        "        CONCAT(LEFT(email, 1), '**'),\n" +
                        "        CONCAT(LEFT(email, 1), '*'))),\n" +
                        "    `name` = 'testtest',\n" +
                        "    `password` = '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2';");
    }
}
