package com.transporter.DAO;

import com.transporter.Entity.CMSReport;
import com.transporter.Entity.Report;
import com.transporter.Entity.ReportDetail;
import com.transporter.Entity.Transport;

import java.util.List;

public interface ReportDao {
    List<CMSReport> findDistinct(String table);
    List<Transport> findDistinct2();
    List<CMSReport> findDistinctTable();
    List<CMSReport> findDistinctTableFilter(String filter);
    List<CMSReport> findTgl2(String date, String keyword);
    List<Transport> findTgl3(String date, String sentstatus);
    int saveCMSReport(CMSReport rpt);
    List<Transport> findsentstatus();
    List<ReportDetail> tesFindReportDetail(String table, String msisdn);
    int tesSaveReportDetail(ReportDetail reportDetail, String id);
//    CMSReport findCMSReportById(String id);

}
