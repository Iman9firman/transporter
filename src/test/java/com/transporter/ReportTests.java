package com.transporter;

import com.transporter.Entity.CMSReport;
import com.transporter.Entity.ReportDetail;
import com.transporter.Repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class ReportTests {
	@Autowired
	private ReportRepository reportRepository;

	@Test
	void testDetails(){
		CMSReport report = reportRepository.findById("2023-06-13#628111390310#1").get();
//		System.out.println(report.toString());
//		Hibernate.initialize(report.getDetails().toString());  // Initialize the details collection
		List<ReportDetail> tes = report.getDetails();
		for (ReportDetail q : tes) {
			System.out.println(q.toString());
		}
		System.out.println("test" + report.getDetails().toString());
	}

}
