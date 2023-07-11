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
    public List<Transport> findDistinct2() {
        String created_atFormat = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String monthyear = new SimpleDateFormat("MMyyyy").format(new Date());
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

        LocalDate earlier = LocalDate.now();
        earlier = earlier.minusMonths(1);

        int day1 = earlier.getMonth().length(earlier.isLeapYear());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMYYYY");
        String created_atformat1 = formatter.format(earlier);

        String temp = "122022";

        List<String> test= new ArrayList<String>();
        day1 = day1-1;
//        day1=11;
        int i = 1;
        for (;i<=day1;){
            System.out.println(i);
            if (i<10){
//                String format = "0" +i + monthyear;
//                String format = "0" +i + temp;
                String format = "0" +i + created_atformat1;
                String sql3 = "SELECT `deliveredcreated_at` as created_at; `sentstatus`; COUNT(`sentstatus`) as count\n" +
                        " FROM messagetemp_backup_"+format +
                        "\n GROUP BY `sentstatus`\n" +
                        "UNION ALL\n" ;
                System.out.println("1-9 "+format);
                test.add(sql3);
            }else if(i == day1){
                String format = i + created_atformat1;
//                String format = i + temp;
                System.out.println("last "+format);

                String sql3 = "SELECT `deliveredcreated_at` as created_at; `sentstatus`; COUNT(`sentstatus`) as count\n" +
                        " FROM messagetemp_backup_"+format +
                        "\n GROUP BY `sentstatus`\n";
                System.out.println("1-9 "+format);
                test.add(sql3);
            }else{
                String format = i + created_atformat1;
//                String format = i + temp;
                System.out.println(">10 "+format);

                String sql3 = "SELECT `deliveredcreated_at` as created_at; `sentstatus`; COUNT(`sentstatus`) as count\n" +
                        " FROM messagetemp_backup_"+format +
                        "\n GROUP BY `sentstatus`\n" +
                        "UNION ALL\n" ;
                test.add(sql3);
            }
            i++;
        }

        System.out.println(test.size());
//        test.forEach(System.out::println);
        System.out.println(test.toString());

        String new1= test.toString().replace("[", "");
        new1.replace("]","");
        String new2 = new1.replace("]","");
        new2 = new2.replaceAll(",","");
        new2= new2.replaceAll(";",",");
        System.out.println("Transportant String: \n"+new2+" \nend");

        return jdbcTemplate.query(new2, BeanPropertyRowMapper.newInstance(Transport.class));
    }
    @Override
    public List<CMSReport> findDistinctTable() {
        return jdbcTemplate.query("SELECT DISTINCT date FROM cms_report ORDER BY date desc", BeanPropertyRowMapper.newInstance(CMSReport.class));
    }

    @Override
    public List<CMSReport> findDistinctTableFilter(String filter) {
        String sql = "SELECT DISTINCT date FROM cms_report WHERE created_at LIKE '%" + filter + "%' ORDER BY date desc";
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
            sql1 = "SELECT * FROM cms_report WHERE date LIKE '%" + date + "%' AND sentstatus LIKE '%" + keyword + "%' ";
        }

//        System.out.println("tgl>> "+sql1);
        return jdbcTemplate.query(sql1,BeanPropertyRowMapper.newInstance(CMSReport.class));
    }
  @Override
    public List<Transport> findTgl3(String created_at, String sentstatus){
            String sql1 = "SELECT * FROM transport WHERE created_at LIKE '%" + created_at + "%' AND sentstatus LIKE '%" + sentstatus + "%' ";
//      System.out.println(sql1);
        return jdbcTemplate.query(sql1,BeanPropertyRowMapper.newInstance(Transport.class));
    }

    @Override
    public List<Transport> findsentstatus(){
        String monthyear = new SimpleDateFormat("MMyyyy").format(new Date());

        String sql1 = "SELECT DISTINCT sentstatus FROM transport";
        return jdbcTemplate.query(sql1,BeanPropertyRowMapper.newInstance(Transport.class));
    }

    @Override
    public List<ReportDetail> tesFindReportDetail(String table, String msisdn){
        String sql1 = "SELECT `status`, COUNT(`status`) AS COUNT\n" +
                "FROM `transport"+ table +"` WHERE `msisdn` LIKE '%"+msisdn+"%'\n" +
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

/*    @Override
    public CMSReport findCMSReportById(String id) {
        try {
            CMSReport report = jdbcTemplate.queryForObject("SELECT * FROM cms_report WHERE id LIKE ?",
                    BeanPropertyRowMapper.newInstance(CMSReport.class), id);

            return report;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }*/
}
