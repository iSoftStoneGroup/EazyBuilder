package test;

import com.eazybuilder.ci.service.PipelineServiceImpl;
import org.junit.Test;

import java.sql.SQLException;


public class PipelineTest {

    @Test
    public  void sqlTakeMinJdbc() throws SQLException {
        PipelineServiceImpl.sqlTakeMinJdbc("jdbc:mysql://xxxxxxxx:3306/ci_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&failOverReadOnly=false&serverTimezone=UTC",
                "root",
                "xx",
                        "UPDATE ci_user \n" +
                        "SET \n" +
                        "    `email` = (IF(LENGTH(email) > 2,\n" +
                        "        CONCAT(LEFT(email, 1), '**'),\n" +
                        "        CONCAT(LEFT(email, 1), '*'))),\n" +
                        "    `name` = 'testtest',\n" +
                        "    `password` = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx';");
    }
}
