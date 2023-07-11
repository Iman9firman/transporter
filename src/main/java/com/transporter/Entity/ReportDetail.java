package com.transporter.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String status;
	private String count;
	@ManyToOne
	@JoinColumn(name = "report_id")
	private CMSReport report;

	@Override
	public String toString() {
		return "ReportDetail{" +
				"id=" + id +
				", status='" + status + '\'' +
				", count='" + count + '\'' +
				'}';
	}

	public ReportDetail(String status, String count, CMSReport report) {
		this.status = status;
		this.count = count;
		this.report = report;
	}

}
