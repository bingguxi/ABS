package kopo.poly.service;

import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.dto.TyphoonDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITyphoonService {

    String apiURL = "https://apihub.kma.go.kr/api/typ02/openApi/SfcYearlyInfoService/getTyphoonList";

    String liveApiURL = "https://apihub.kma.go.kr/api/typ01/url/typ_now.php";

//    List<EarthquakeResultDTO> getTyphoonList() throws Exception;

    void setTyphoonUrl() throws Exception;

//    List<TyphoonDTO> getTyphoonLiveInfo() throws Exception;

    void getTyphoonInfo(String apiParam, int year) throws Exception;
}
