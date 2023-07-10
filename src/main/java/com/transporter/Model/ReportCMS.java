package com.transporter.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCMS {
    private Integer id;
    private String msisdn;
    private String sendto;
    private String status;
    private String count;
    private String date;


}
