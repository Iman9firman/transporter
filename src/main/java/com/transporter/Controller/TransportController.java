package com.transporter.Controller;

import com.transporter.Model.RequestResponse;
import com.transporter.Service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
public class TransportController {
    @Autowired
    private TransportService service;

    @PostMapping("/request")
    private ResponseEntity<?> reiceveFile(@RequestParam("msisdn") String num){
        try {
            //Jadi to itu nomor random dari database || msg itu generate random 16 string
            RequestResponse response = service.saveMsisdn(num);
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }catch (Exception e){
            System.out.println("error request : " + e.getMessage());
            return new ResponseEntity<>("saveDb Failed cause : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    public HashMap<String, Date> numStat = new HashMap<String, Date>();
    @GetMapping("/status")
    public ResponseEntity<?> getStatusScheduler(@RequestParam("msisdn") String num){
        Integer response = service.getStatus(num);
        // Get the current date and time
        Date currentDate = new Date();

        if (numStat.get(num) == null) {
            numStat.put(num, currentDate);
        }else {
            currentDate = numStat.get(num);
            System.out.println("udah ada " + currentDate);
        }
        Boolean oneMinute = service.oneMinute(currentDate);

        if (response==1){
            if (oneMinute){
                numStat.remove(num);
                return new ResponseEntity<>("failed",HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("pending",HttpStatus.ACCEPTED);
        }else {
            return new ResponseEntity<>("success",HttpStatus.ACCEPTED);
        }

    }

    @PostMapping("/report")
    private ResponseEntity<?> sendReport(@RequestParam("origin") String origin, @RequestParam("to") String to, @RequestParam("msg") String msg){
        String response = "";
        try {
            //search semua param ke database yg status nya 1 kalo ada return 2 success
            int result =  service.verifyData(origin, to, msg);
            System.out.println("report result :: "+result);

            response = (result==1) ? "failed" : "success";
            if (result==0) response="Cannot find data";
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>("report failed cause : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
