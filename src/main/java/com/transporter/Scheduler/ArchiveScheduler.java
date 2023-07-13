package com.transporter.Scheduler;

import com.transporter.DAO.TransportDAO;
import com.transporter.Service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@Slf4j
public class ArchiveScheduler {
    @Autowired
    private TransportDAO dao;
    @Autowired
    private ReportService reportService;

//    @Scheduled(fixedDelay = 50000)
    @Scheduled(cron = "0 0 0 * * *")
    public void schedulerHariArchive() throws InterruptedException {
//        String table = "transport_12072023";
        String yesterday = dao.previousDate();
        log.info("Archive Scheduler activated | Archive Data into : " + yesterday);
        dao.saveTableArchive();
        dao.archiveData();

        Thread.sleep(5000);
        reportService.reportDetail(yesterday);
    }


}
