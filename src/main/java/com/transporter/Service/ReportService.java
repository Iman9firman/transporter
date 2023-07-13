package com.transporter.Service;

import com.transporter.DAO.ReportDao;
import com.transporter.Entity.CMSReport;
import com.transporter.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    ReportRepository repo;
    @Autowired
    ReportDao dao;

    public CMSReport findByID(String id){
        CMSReport report = repo.findById(id).get();
        return report;
    }

    public Page<CMSReport> disp(List<CMSReport> list, int pageNum){
        List<CMSReport> results = new ArrayList<>();

        HashMap<String, Integer> counts = new HashMap<String, Integer>();
//        System.out.println(list.toString());
        for (CMSReport temp : list) {
            String tes = temp.getDate();
            String[] arrOfStr = tes.split("-");
            String q = arrOfStr[0]+"-"+arrOfStr[1];
            List<CMSReport> hasil = dao.findTgl2(q, null);
            counts.put(q,hasil.size());
        }

        for (String i : counts.keySet()) {
//          System.out.println("key: " + i + " value: " + counts.get(i));
            CMSReport result = new CMSReport();
            result.setDate(i);
            result.setCount(String.valueOf(counts.get(i)));
            results.add(result);
        }

        Page<CMSReport> page = resultPage(results, pageNum,10);
//        System.out.println(page.getContent().toString());

        return page;
    }

//    @Cacheable("ReportCMSPage")
    private Page<CMSReport> resultPage(List<CMSReport> results, int page, int size){
        Pageable pageRequest = createPageRequestUsing(page, size);
        List<CMSReport> allReportCMSs = results;

        int start = (page-1)*size;
        int end = Math.min((start + pageRequest.getPageSize()), allReportCMSs.size());
        System.out.println("Showing Data from : " + start + " | " + end + " | of " + allReportCMSs.size());
        List<CMSReport> pageContent = allReportCMSs.subList(start, end);

        return new PageImpl<>(pageContent, pageRequest, allReportCMSs.size());
    }
    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page-1, size);
    }

//    @Cacheable("ReportCMSAll")
    public List<CMSReport> findAllReport(String keyword){
        List<CMSReport> dist = dao.findDistinctTable();
        if (keyword!= null){
            dist = dao.findDistinctTableFilter(keyword);
        }

        return dist;
    }


    /*
    @Caching(
            evict = {
                    @CacheEvict(value = {"ReportCMSAll", "ReportCMSPage"}, allEntries = true)},
            put = {
                    @CachePut(value = {"ReportCMSAll", "ReportCMSPage"})
            }
    )
    public ReportCMS deleteUpdateCache(ReportCMS result){
        return dao.saveReportCMS(result);
    }
    */
    
    public Map<String, Object> detailFilter( List<CMSReport> hasil){

        Map<String, Object> map = new HashMap<>();
        
        HashSet<String> dates = new HashSet<>();
        HashSet<String> dates2 = new HashSet<>();
        HashSet<String> msisdn = new HashSet<String>();
        HashMap<String, Integer> counts = new HashMap<>();
        HashMap<String, Integer> counts2 = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Boolean status = false;
        for (CMSReport s : hasil) {
            String[] arrOfStr = s.getDate().split("-");
            String q = arrOfStr[2];
//            System.out.println(arrOfStr +"|"+ q);
            dates.add(q);

            String stat = arrOfStr[0]+"-"+arrOfStr[1];
            msisdn.add(s.getMsisdn());
            dates2.add(s.getDate());
            counts.put(s.getDate()+ "_" + s.getMsisdn(), Integer.valueOf(s.getCount()));
        }

        List<String> dateList = new ArrayList<>(dates);
        List<String> dateList2 = new ArrayList<String>(dates2);
        Collections.sort(dateList);
        Collections.sort(dateList2);

        for (String p : msisdn) {
            Boolean stat = false;

            int columnCount = 1;
            for (String dt : dateList2) {
//                System.out.print(" " + dt + " ");
                Integer cnt = counts.get(dt +"_"+ p);
                counts2.put(dt +"_"+ p, cnt);
            }
        }

//        System.out.println(hasil.size());

        map.put("listDetails", hasil);
        map.put("listDetailStatus", msisdn);
        map.put("listDetailDate", dateList);
        map.put("listDetailDate2", dateList2);
        map.put("counts", counts2);

        return map;
    }
}
