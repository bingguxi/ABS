package kopo.poly.service;

import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.dto.TyphoonDTO;
import kopo.poly.dto.TyphoonLiveDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITyphoonService {

    String apiURL = "https://apihub.kma.go.kr/api/typ02/openApi/SfcYearlyInfoService/getTyphoonList";

//    String liveApiURL = "https://apihub.kma.go.kr/api/typ01/url/typ_now.php";

//    List<EarthquakeResultDTO> getTyphoonList() throws Exception;

    // 태풍 실시간 정보 가져오기
    List<TyphoonLiveDTO> getTyphoonLiveInfo() throws Exception;

    // 태풍 URL 정보 호출하기 ?
    void setTyphoonUrl() throws Exception;

    // 태풍 과거 정보 가져오기
    void getTyphoonInfo(String apiParam, int year) throws Exception;
}
