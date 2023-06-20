package com.transporter.DAO;

import com.transporter.Entity.MSISDN_Ref;
import com.transporter.Entity.Transport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TransportDAOImpl implements TransportDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int saveTableArchive() {
        String tableName = previousDate();

        // Create the SQL statement for table creation
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "`msisdn` varchar(255) DEFAULT NULL, " +
                "`sendto` varchar(20) DEFAULT NULL, " +
                "`keyword` varchar(255) DEFAULT NULL, " +
                "`status` int(11) DEFAULT NULL, " +
                "`created_at` varchar(255) DEFAULT NULL, " +
                "`updated_at` varchar(255) DEFAULT NULL " +
                ")";

        // Execute the SQL statement
        jdbcTemplate.execute(createTableSql);

        return 0;
    }

    @Override
    public int alterTablename() {
        String tableName = previousDate();

        // Create the SQL statement for table creation
        String createTableSql = "ALTER TABLE transport RENAME TO "+tableName+";";

        // Execute the SQL statement
        jdbcTemplate.execute(createTableSql);

        return 0;
    }

    @Override
    public int archiveData() {
        String tableName = previousDate();
        // Fetch data from the original table
        String selectQuery = "SELECT * FROM transport";
        List<Map<String, Object>> dataToArchive = jdbcTemplate.queryForList(selectQuery);

        // Insert the data into the archive table
        String insertQuery = "INSERT INTO "+ tableName +" (msisdn, sendto, keyword, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        for (Map<String, Object> row : dataToArchive) {
            jdbcTemplate.update(insertQuery, row.get("msisdn"), row.get("sendto"), row.get("keyword"), row.get("status"), row.get("created_at"), row.get("updated_at"));
        }
        // Delete the archived data from the original table
        String deleteQuery = "DELETE FROM transport";
        jdbcTemplate.update(deleteQuery);

        return 0;
    }

    @Override
    public String findRandData() {
        String query = "SELECT msisdn FROM msisdn ORDER BY RAND() LIMIT 1";
        String result = jdbcTemplate.queryForObject(query, String.class);
//        jdbcTemplate.execute("DELETE FROM msisdn where msisdn="+result);
        return result;
    }

    @Override
    public Integer verifyData(String origin, String to, String msg) {
        try {
            String query = "SELECT status FROM transport WHERE msisdn=? AND sendto=? AND keyword=? ";
            Integer result =  jdbcTemplate.queryForObject(query, Integer.class, origin, to, msg);
            return result;
        }catch (Exception e){
            log.info("Report Failed cause : " + e.getMessage());
            return 0;
        }
    }


    public String previousDate(){
        LocalDate yesteday = LocalDate.now().minusDays(1);
        yesteday.minusDays(1);
        String tableNamePrefix = "transport_";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String tableName = tableNamePrefix + yesteday.format(formatter);
        return tableName;
    }

    @Override
    public Integer cekStatus(String msisdn) {
        Integer status = 0;
        String query = "SELECT * FROM transport where msisdn=?";
        String dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Boolean oneMinute = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Transport> listTransport = jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(Transport.class), msisdn);
        if (listTransport.isEmpty() || listTransport == null) {
            status = 0;
        } else {
            Transport transport = listTransport.get(listTransport.size() - 1);
            status = transport.getStatus();
            System.out.println(transport.toString());
            Date date = null;
            try {
                date = dateFormat.parse(transport.getCreated_at());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            oneMinute = oneMinute(date);
            if (status == 100 && oneMinute){
                transport.setStatus(200);
                transport.setUpdated_at(dateNow);
                jdbcTemplate.update("UPDATE transport SET status=?, updated_at=?  WHERE id=?",
                        new Object[] { transport.getStatus(), transport.getUpdated_at(), transport.getId() });
                status = 200;
                System.out.println("failed "+transport.toString());
            }
        }
        return status;
    }


    public Boolean oneMinute(Date currentDate ){
        // Create a calendar instance
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Add 1 minute to the current date
        calendar.add(Calendar.MINUTE, 1);
        Date dateAfter = calendar.getTime();
        currentDate = new Date();
//        System.out.println(currentDate + " || " + dateAfter);
        // Compare the two dates
        if (currentDate.before(dateAfter)) {
            return false;
        }else {
            return true;
        }
    }

}
