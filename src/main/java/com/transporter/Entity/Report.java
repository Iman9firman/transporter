package com.transporter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "check_report")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String msisdn;
    @Column(name = "sendto", length = 20)
    private String sendto;
    @Column(name = "msg", length = 255, nullable = true)
    private String keyword;
    private String created_at;


    public Report(String msisdn, String sendto, String keyword, String created_at) {
        this.msisdn = msisdn;
        this.sendto = sendto;
        this.keyword = keyword;
        this.created_at = created_at;
    }
}
