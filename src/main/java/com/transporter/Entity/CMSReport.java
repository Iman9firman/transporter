package com.transporter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "cms_report")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CMSReport {
    @Id
    private String id;
    private String msisdn;
    private String sendto;
    private String status;
    private String count;
    private String date;

}
