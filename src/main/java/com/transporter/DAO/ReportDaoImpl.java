package com.transporter.DAO;

import com.transporter.Entity.CMSReport;
import com.transporter.Entity.ReportDetail;
import com.transporter.Entity.Transport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ReportDaoImpl implements ReportDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CMSReport> findDistinct(String table) {
//        String date = "_15062023";
        String sql = "SELECT `msisdn`, `sendto`, DATE(created_at) AS date, `status`, COUNT(`status`) AS COUNT " +
                    "FROM transport"+table+" \n" +
                    "GROUP BY `msisdn`";

        System.out.println(sql);
        return jdbcTemplate.query(sql,BeanPropertyRowMapper.newInstance(CMSReport.class));
    }
    @Override
    public List<CMSReport> findDistinctTable() {
        return jdbcTemplate.query("SELECT DISTINCT date FROM cms_report ORDER BY date desc", BeanPropertyRowMapper.newInstance(CMSReport.class));
    }

    @Override
    public List<CMSReport> findDistinctTableFilter(String filter) {
        String sql = "SELECT DISTINCT date FROM cms_report WHERE date LIKE '%" + filter + "%' ORDER BY date desc";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(CMSReport.class));
    }

    @Override
    public int saveCMSReport(CMSReport rpt) {
        List<ReportDetail> reportDetails = tesFindReportDetail("",rpt.getMsisdn());
        for(ReportDetail rptDetails : reportDetails){
//            tesSaveReportDetail(rptDetails, rpt.getId());
            System.out.println(rptDetails.toString());
            String status = rptDetails.getStatus();
            String count = rptDetails.getCount();
            rpt.addReportDetail(status, count);
        }
        
        String sql = "INSERT into cms_report(id,msisdn,sendto,date,status,count) VALUES (?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{
                rpt.getId(),
                rpt.getMsisdn(),
                rpt.getSendto(),
                rpt.getDate(),
                rpt.getStatus(),
                rpt.getCount(),
                });
    }

    @Override
    public List<CMSReport> findTgl2(String date, String keyword){
        String monthyear = new SimpleDateFormat("MMyyyy").format(new Date());
        String sql1 = "SELECT * FROM cms_report WHERE date LIKE '%" + date + "%'";
        if (keyword!=null){
            sql1 = "SELECT * FROM cms_report WHERE date LIKE '%" + date + "%' AND msisdn LIKE '%" + keyword + "%' ";
        }

//        System.out.println("tgl>> "+sql1);
        return jdbcTemplate.query(sql1,BeanPropertyRowMapper.newInstance(CMSReport.class));
    }

    @Override
    public List<ReportDetail> tesFindReportDetail(String table, String msisdn){
        String sql1 = "SELECT `status`, COUNT(`status`) AS COUNT\n" +
                "FROM `transport"+ table +"` WHERE `msisdn` LIKE '"+msisdn+"'\n" +
                "GROUP BY `msisdn`, `status` ; ";
        return jdbcTemplate.query(sql1,BeanPropertyRowMapper.newInstance(ReportDetail.class));
    }
    @Override
    public int tesSaveReportDetail(ReportDetail reportDetail, String id){

        String sql = "INSERT into report_details(id,status,count,report_id) VALUES (?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{
                reportDetail.getId(),
                reportDetail.getStatus(),
                reportDetail.getCount(),
                id
        });
    }
}
