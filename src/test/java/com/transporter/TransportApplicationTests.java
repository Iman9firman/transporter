package com.transporter;

import com.transporter.DAO.TransportDAO;
import com.transporter.Entity.Transport;
import com.transporter.Service.TransportService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class TransportApplicationTests {
	@Autowired
	private TransportDAO dao;

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
		dao.cekStatus("628111390310");
	}

}
