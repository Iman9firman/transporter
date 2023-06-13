package com.transporter.Scheduler;

import com.transporter.DAO.TransportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
public class ArchiveScheduler {
    @Autowired
    private TransportDAO dao;
//        @Scheduled(cron = "0 */1 * * * *")

//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedDelay = 50000)
    public void schedulerHari(){
        System.out.println("sched");
       dao.saveTableArchive();
       dao.archiveData();
    }

}
