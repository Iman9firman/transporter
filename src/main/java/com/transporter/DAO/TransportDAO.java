package com.transporter.DAO;

import com.transporter.Entity.MSISDN_Ref;
import org.springframework.web.bind.annotation.RequestParam;

public interface TransportDAO {
    int saveTableArchive();
    int alterTablename();
    int archiveData();
    String findRandData();
    Integer verifyData(String origin, String to, String msg);
    String previousDate();
}
