package com.transporter.DAO;

import com.transporter.Entity.MSISDN_Ref;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
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
            System.out.println("daoImpl verify failed cause : " + e.getMessage());
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
}
