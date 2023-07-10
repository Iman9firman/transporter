package com.transporter.Service;

import com.transporter.DAO.ReportDao;
import com.transporter.Entity.Transport;
import com.transporter.Model.ReportCMS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    ReportDao dao;

    public Page<ReportCMS> disp(List<Transport> list, int pageNum){
        List<ReportCMS> results = new ArrayList<>();

        HashMap<String, Integer> counts = new HashMap<String, Integer>();
//        System.out.println(list.toString());
        for (Transport temp : list) {
            String tes = temp.getCreated_at();
            String[] arrOfStr = tes.split("-");
            String q = arrOfStr[0]+"-"+arrOfStr[1];
            List<Transport> hasil = dao.findTgl2(q, null);
            counts.put(q,hasil.size());
        }

        for (String i : counts.keySet()) {
//            System.out.println("key: " + i + " value: " + counts.get(i));
            ReportCMS result = new ReportCMS();
            result.setDate(i);
            result.setCount(String.valueOf(counts.get(i)));
            results.add(result);
        }

        Page<ReportCMS> page = resultPage(results, pageNum,10);
//        System.out.println(page.getContent().toString());

        return page;
    }

//    @Cacheable("ReportCMSPage")
    private Page<ReportCMS> resultPage(List<ReportCMS> results, int page, int size){
        Pageable pageRequest = createPageRequestUsing(page, size);
        List<ReportCMS> allReportCMSs = results;

//        int start = (int) pageRequest.getOffset();
        int start = (page-1)*size;
        int end = Math.min((start + pageRequest.getPageSize()), allReportCMSs.size());

        System.out.println("Showing Data from : " + start + " | " + end + " | of " + allReportCMSs.size());
        List<ReportCMS> pageContent = allReportCMSs.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, allReportCMSs.size());
    }
    private Pageable createPageRequestUsing(int page, int size) {
        return PageRequest.of(page, size);
    }

//    @Cacheable("ReportCMSAll")
    public List<Transport> findAllReport(String keyword){
        List<Transport> dist = dao.findDistinctTable();
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
    
    public Map<String, Object> detailFilter( List<Transport> hasil){

        Map<String, Object> map = new HashMap<>();
        
        HashSet<String> dates = new HashSet<>();
        HashSet<String> dates2 = new HashSet<>();
        HashSet<String> sendstatus = new HashSet<String>();
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        HashMap<String, Integer> counts2 = new HashMap<String, Integer>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Boolean status = false;
        for (Transport s : hasil) {
            String[] arrOfStr = s.getCreated_at().split(" ")[0].split("-");
            String q = arrOfStr[2];
            System.out.println(arrOfStr +"|"+ q);
            dates.add(q);

            String stat = arrOfStr[0]+"-"+arrOfStr[1];
            sendstatus.add(s.getMsisdn());
            dates2.add(s.getCreated_at());
            counts.put(s.getCreated_at()+ "#" + s.getMsisdn(), Integer.valueOf(s.getStatus()));
        }

        List<String> dateList = new ArrayList<>(dates);
        List<String> dateList2 = new ArrayList<String>(dates2);
        Collections.sort(dateList);
        Collections.sort(dateList2);

        for (String p : sendstatus) {
            Boolean stat = false;

            int columnCount = 1;
            for (String dt : dateList2) {
//                System.out.print(" " + dt + " ");
                Integer cnt = counts.get(dt +"#"+ p);
                if (cnt == null) {
                    cnt = null;
                    stat = true;
                }
                else if (stat == true && cnt!=null){
                    cnt = null;
                    stat = false;
                }
//                System.out.println(stat);
                counts2.put(dt +"#"+ p,cnt);
            }
        }

        System.out.println(hasil.size());

        map.put("listDetails", hasil);
        map.put("listDetailStatus", sendstatus);
        map.put("listDetailDate", dateList);
        map.put("listDetailDate2", dateList2);
        map.put("counts", counts2);

        return map;
    }
}
