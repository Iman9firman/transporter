package com.transporter;

import com.transporter.DAO.ReportDao;
import com.transporter.DAO.TransportDAO;
import com.transporter.Entity.CMSReport;
import com.transporter.Entity.ReportDetail;
import com.transporter.Repository.ReportRepository;
import com.transporter.Service.TransportService;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@SpringBootTest
class TransportApplicationTests {
	@Autowired
	private TransportDAO dao;
	@Autowired
	private ReportDao reportDao;

	@Autowired
	private ReportRepository reportRepository;
	@Autowired
	private TransportService service;

	@Test
	void contextLoads() {
//		dao.archiveData();
		LocalDate yesteday = LocalDate.now();
		yesteday.minusDays(1);
		System.out.println("yst "+yesteday);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		String tableName = "tableNamePrefix" + LocalDate.now().format(formatter);

	}

	@Test
	void tesRandString(){
		System.out.println(service.randCode());
		byte[] array = new byte[7]; // length is bounded by 7
		new Random().nextBytes(array);
		String generatedString = new String(array, Charset.forName("UTF-8"));

		System.out.println(generatedString);

		String generatedString2 = RandomStringUtils.randomAlphanumeric(16);

		System.out.println(generatedString2);

		String msisdn = dao.findRandData();
		System.out.println("msisdn :: "+msisdn);

	}

	@Test
	void verifyStatus(){
		int result =  dao.verifyData("1112345678", "62811411244", "fxbFqMTQAyX0F7Ae");
		System.out.println(result);
	}

	@Test
	void tesArchive(){
		System.out.println(previousDate());

		dao.saveTableArchive();
		dao.archiveData();
	}

	public String previousDate(){
		LocalDate yesteday = LocalDate.now().minusDays(1);
		String tableNamePrefix = "transport_";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
		String tableName = tableNamePrefix + yesteday.format(formatter);
		return tableName;
	}

	@Autowired
	private RestTemplate restTemplate;

	@Test
	void contextLoads2() {
		String fooResourceUrl = "http://localhost:8080/conn";
		ResponseEntity<String> response  = restTemplate.getForEntity(fooResourceUrl , String.class);
		System.out.println(response.getBody());

	}
	@Test
	void contextLoads3() throws ParseException {
		String table = "_15062023";
		List<CMSReport> test = reportDao.findDistinct(table);
		System.out.println(test.toString());

		for (CMSReport temp : test) {
			//            System.out.println("Sent Status :"+ temp.getSentStatus() + " || Count : " + temp.getCount()
			//                    + " || date : " + temp.getDate());
			String status = temp.getStatus() == null  ? "0" : temp.getStatus();

			CMSReport result = new CMSReport();
			result.setId(temp.getDate()+"#"+temp.getMsisdn()+"#"+status);
			result.setMsisdn(temp.getMsisdn());
			result.setSendto(temp.getSendto());
			result.setDate(temp.getDate());
			result.setStatus(status);
			result.setCount(temp.getCount());

			reportDao.saveCMSReport(result);
		}
	}

	@Test
	void tesReportDetail(){
		String table = "_15062023";
		List<CMSReport> test = reportDao.findDistinct(table);
		System.out.println(test.toString());

		for (CMSReport temp : test) {
			String status = temp.getStatus() == null  ? "0" : temp.getStatus();

			CMSReport result = new CMSReport();
			result.setId(temp.getDate()+"_"+temp.getMsisdn());
			result.setMsisdn(temp.getMsisdn());
			result.setSendto(temp.getSendto());
			result.setDate(temp.getDate());
			result.setStatus(status);
			result.setCount(temp.getCount());

//			table = "_" + temp.getMsisdn();
			List<ReportDetail> reportDetails = reportDao.tesFindReportDetail(table, temp.getMsisdn());
			for(ReportDetail rptDetails : reportDetails){
				System.out.println(rptDetails.toString());
				String statusDet = rptDetails.getStatus();
				String count = rptDetails.getCount();
				result.addReportDetail(statusDet, count);
			}

//			result.addReportDetail("tes", "123");

			reportRepository.save(result);
		}
	}

	@Test
	void testDetails(){
		CMSReport report = reportRepository.findById("2023-06-13#628111390310#1").get();
//		System.out.println(report.toString());
		Hibernate.initialize(report.getDetails().toString());  // Initialize the details collection

		System.out.println("test" + report.getDetails().get(1).toString());
	}

}
