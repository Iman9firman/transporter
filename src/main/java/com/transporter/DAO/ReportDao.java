package com.transporter.DAO;

import com.transporter.Entity.CMSReport;
import com.transporter.Entity.Report;
import com.transporter.Entity.Transport;

import java.util.List;

public interface ReportDao {
    List<CMSReport> findDistinct();
    List<Transport> findDistinct2();
    List<Transport> findDistinctTable();
    List<Transport> findDistinctTableFilter(String filter);
    List<Transport> findTgl2(String date, String keyword);
    List<Transport> findTgl3(String date, String sentstatus);
    int saveCMSReport(CMSReport rpt);
    List<Transport> findsentstatus();
}
