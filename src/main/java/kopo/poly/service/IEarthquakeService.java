package kopo.poly.service;

import kopo.poly.dto.EarthquakeLiveDTO;
import kopo.poly.dto.EarthquakeResultDTO;

import java.util.List;

public interface IEarthquakeService {

    String apiURL = "https://apihub.kma.go.kr/api/typ09/url/eqk/urlNewNotiEqk.do";

    // 지진 과거 API URL 생성하기
    void setEarthquakeUrl() throws Exception;

    // 지진 과거 API 호출하기
    void getEarthquakeInfo(String apiParam) throws Exception;

    // 과거 지진 정보 조회하기
    List<EarthquakeResultDTO> getEarthquakeList() throws Exception;

    // 실시간 지진 정보 조회하기
    List<EarthquakeLiveDTO> getEarthquakeLiveInfo() throws Exception;
}
