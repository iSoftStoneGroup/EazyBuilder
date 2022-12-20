package com.eazybuilder.ga.controller;

import com.eazybuilder.ga.pojo.FlywayQueryPojo;
import com.eazybuilder.ga.pojo.SqlCheckPojo;
import com.eazybuilder.ga.untils.JDBCUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/flyway")
public class FlywayController {

    private static Logger logger = LoggerFactory.getLogger(FlywayController.class);

    @ApiOperation(value = "获取日期列表")
    @PostMapping("/findDateList")
    
    public List<String> findDateList(@RequestBody FlywayQueryPojo data){

        Connection conn = null;

        PreparedStatement pstmt  = null;

        ResultSet rs = null;
        try{
            conn = JDBCUtil.getConnection(data.getUrl(), data.getUserName(), data.getPassword());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = data.getDate();
            if(null==date){
                date=new Date();
            }
            String dateStr = sdf.format(date);

            String sql = "SELECT t.installed_on FROM flyway_schema_history t WHERE t.installed_on>=?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dateStr);
            rs = pstmt.executeQuery();
            List<String> resultList = new ArrayList<String>();
            while(rs.next()){
                resultList.add(rs.getString("installed_on"));
            }
            return resultList;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtil.release(conn,pstmt,rs);
        }
        return null;
    }

    @ApiOperation(value = "根据日期删除flyway")
    @PostMapping("/deleteByDate")
    
    public Integer deleteByDate(@RequestBody FlywayQueryPojo data){
        Connection conn = null;

        PreparedStatement pstmt  = null;

        ResultSet rs = null;
        try{
            conn = JDBCUtil.getConnection(data.getUrl(), data.getUserName(), data.getPassword());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = data.getDate();
            if(null==date){
                date=new Date();
            }
            String dateStr = sdf.format(date);

            String sql = "DELETE FROM flyway_schema_history t WHERE t.installed_on>=?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dateStr);
            int row = pstmt.executeUpdate();
            return row;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtil.release(conn,pstmt,rs);
        }
        return -1;
    }

}
