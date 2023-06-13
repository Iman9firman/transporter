package com.transporter.Scheduler;

import com.transporter.DAO.TransportDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@Slf4j
public class ArchiveScheduler {
    @Autowired
    private TransportDAO dao;

    @Scheduled(fixedDelay = 50000)
//    @Scheduled(cron = "0 0 0 * * *")
    public void schedulerHari(){
        String yesterday = dao.previousDate();
        log.info("Archive Scheduler activated | Archive Data into : " + yesterday);
        dao.saveTableArchive();
        dao.archiveData();
    }
}
