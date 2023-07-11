package com.transporter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportDetail> details = new ArrayList<>();

    public void addReportDetail(String name, String value) {
        this.details.add(new ReportDetail(name, value, this));
    }
    public void addReportDetail(Integer id,String name, String value) {
        this.details.add(new ReportDetail(id, name, value, this));
    }

    @Override
    public String toString() {
        return "CMSReport{" +
                "id='" + id + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", sendto='" + sendto + '\'' +
                ", status='" + status + '\'' +
                ", count='" + count + '\'' +
                ", date='" + date + '\'' +
                ", details=" + details +
                '}';
    }
}
