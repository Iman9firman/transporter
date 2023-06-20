package com.transporter.Service;

import com.transporter.DAO.TransportDAO;
import com.transporter.Entity.Transport;
import com.transporter.ModelResponse.RequestResponse;
import com.transporter.Repository.TransportRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class TransportService {
    @Autowired
    TransportDAO dao;
    @Autowired
    TransportRepository repository;

    public RequestResponse saveMsisdn(String num){
        String key = randCode();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String to = dao.findRandData();

        Transport transport = new Transport();
        transport.setMsisdn(num);
        transport.setKeyword(randCode());
        transport.setStatus(100);
        transport.setCreated_at(date);
        transport.setKeyword(key);
        transport.setSendto(to);
        repository.save(transport);

        RequestResponse response = new RequestResponse(to, key);
        return response;
    }

    public Integer getStatus(String num){
        Integer status = 0;

        List<Transport> listTransport = repository.findByMsisdn(num);
        if (listTransport.isEmpty() || listTransport==null) {
            status = 0;
        }else {
            Transport transport = listTransport.get(listTransport.size()-1);
            status = transport.getStatus();
        }
        return status;
    }

    public Integer getStatus2(String num){
        return dao.cekStatus(num);
    }

    public String randCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        String rndString = String.format("%06d", number);

        //using apache common lang
        String randString = RandomStringUtils.randomAlphanumeric(16);
        return randString;
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

    public Integer verifyData(String origin, String to, String msg){
        int result =  dao.verifyData(origin, to, msg);
        return result;
    }

}
