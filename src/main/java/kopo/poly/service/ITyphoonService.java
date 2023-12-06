package kopo.poly.service;

import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.dto.TyphoonDTO;
import kopo.poly.dto.TyphoonLiveDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITyphoonService {

    String apiURL = "https://apihub.kma.go.kr/api/typ02/openApi/SfcYearlyInfoService/getTyphoonList";

    // 태풍 URL 정보 생성하기
    void setTyphoonUrl() throws Exception;

    // 태풍 API 호출해서 과거 정보 가져오기
    void insertTyphoonInfo(String apiParam, int year) throws Exception;

    // 태풍 과거 정보 조회하기
    List<TyphoonDTO> getTyphoonList() throws Exception;

    // 태풍 실시간 정보 조회하기
    List<TyphoonLiveDTO> getTyphoonLiveList() throws Exception;

}
