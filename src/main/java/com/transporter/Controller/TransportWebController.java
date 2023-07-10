package com.transporter.Controller;

import com.transporter.DAO.ReportDao;
import com.transporter.Entity.Report;
import com.transporter.Entity.Transport;
import com.transporter.Model.ReportCMS;
import com.transporter.ModelRequest.ApiReport;
import com.transporter.ModelRequest.ApiRequest;
import com.transporter.ModelRequest.ApiStatus;
import com.transporter.ModelResponse.ApiReportResponse;
import com.transporter.ModelResponse.RequestResponse;
import com.transporter.Service.ReportService;
import com.transporter.Service.TransportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class TransportWebController {
    @Autowired
    private ReportDao dao;

    @Autowired
    private ReportService service;

    @GetMapping(value = {"/", "/home"})
    public String home(Model map, @Param("keyword") String keyword) {
        return homePage(map,1, "desc", keyword);
    }
    @GetMapping("/home/{pageNum}")
    public String homePage(Model map, @PathVariable(name = "pageNum") int pageNum,
                           @Param("sortDir") String sortDir,
                           @Param("keyword") String keyword) {
        int size = 10;
        long startCount = (pageNum-1) * size + 1;
        long endCount = startCount + size - 1;
        Pageable pageable = PageRequest.of(pageNum - 1, size);
        String reverseSortDirString = sortDir.equals("desc") ? "asc" : "desc";

        List<Transport> resultAll = service.findAllReport(keyword);
        Page<ReportCMS> resultPage = service.disp(resultAll, pageNum);
        List<ReportCMS> results = resultPage.getContent();

        System.out.println(results.toString());
        System.out.println(results.size());

        map.addAttribute("listReports", results);
        map.addAttribute("keyword", keyword);
        map.addAttribute("currentPage", pageNum);
        map.addAttribute("totalPages", resultPage.getTotalPages());
        map.addAttribute("startCount", startCount);
        map.addAttribute("endCount", endCount);
        map.addAttribute("totalItems", resultPage.getTotalElements());
        map.addAttribute("sortDir", sortDir);
        map.addAttribute("reverseSortDirString", reverseSortDirString);

        return "index";
    }


    @GetMapping("/detail/{date}")
    public String pageDetail(Model map, @PathVariable("date")String date, @Param("keyword") String keyword) throws ParseException {
        List<Transport> hasil = dao.findTgl2(date, keyword);
        Map<String, Object> attr = service.detailFilter(hasil);

        map.addAttribute("keyword", keyword);
        map.addAllAttributes(attr);
        return "detail";
    }

}
