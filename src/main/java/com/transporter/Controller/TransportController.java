package com.transporter.Controller;

import com.transporter.ModelRequest.ApiReport;
import com.transporter.ModelRequest.ApiRequest;
import com.transporter.ModelRequest.ApiStatus;
import com.transporter.ModelResponse.ApiReportResponse;
import com.transporter.ModelResponse.RequestResponse;
import com.transporter.Service.TransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

@RestController
@Slf4j
public class TransportController {
    @Autowired
    private TransportService service;

    @PostMapping("/request")
    private ResponseEntity<?> requestNumber(@RequestBody ApiRequest request){
        String num = request.getMsisdn();
        try {
            //Jadi to itu nomor random dari database || msg itu generate random 16 string

            RequestResponse response = service.saveMsisdn(num);
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.info("Request Number "+ num +" error, cause: " + e.getMessage());
            return new ResponseEntity<>("Request Number Failed cause : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    public HashMap<String, Date> numStat = new HashMap<String, Date>();
    @GetMapping("/status")
    public ResponseEntity<?> getStatusScheduler(@RequestParam("msisdn") String num){
        try {
            ApiStatus status = new ApiStatus();
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
                    status.setResult("failed");
                    return new ResponseEntity<>(status,HttpStatus.ACCEPTED);
                }
                status.setResult("pending");
                return new ResponseEntity<>(status,HttpStatus.ACCEPTED);
            }else {
                status.setResult("success");
                return new ResponseEntity<>(status,HttpStatus.ACCEPTED);
            }
        }catch (Exception e){
            log.info("Request Statuts "+ num +" error, cause: " + e.getMessage());
            return new ResponseEntity<>("Request Status Failed cause : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/report")
    private ResponseEntity<?> sendReport(@RequestBody ApiReport report){
        ApiReportResponse response = new ApiReportResponse();
        String responseString = "";
        String origin = report.getOrigin();
        String to = report.getTo();
        String msg = report.getMsg();
        try {
            //search semua param ke database yg status nya 1 kalo ada return 2 success
            int result =  service.verifyData(origin, to, msg);
            System.out.println("report result :: "+result);

            responseString = (result==1) ? "failed" : "success";
            if (result==0) responseString="Cannot find data";
            response.setResult(responseString);
            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }catch (Exception e){
            log.info("Request Report error, cause: " + e.getMessage());
            return new ResponseEntity<>("Report Failed cause : "+e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
