package com.transporter.DAO;

import com.transporter.Entity.MSISDN_Ref;
import com.transporter.Entity.Transport;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransportDAO {
    int saveTableArchive();
    int alterTablename();
    int archiveData();
    String findRandData();
    Integer verifyData(String origin, String to, String msg);
    String previousDate();
    Integer cekStatus(String msisdn, String to, String msg);
}
