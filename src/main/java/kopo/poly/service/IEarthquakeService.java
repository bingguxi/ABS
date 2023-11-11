package kopo.poly.service;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import kopo.poly.dto.EarthquakeDTO;
import kopo.poly.service.impl.EarthquakeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IEarthquakeService {

    String apiURL = "https://apihub.kma.go.kr/api/typ09/url/eqk/urlNewNotiEqk.do";

    // 지진 API를 호출하여 날씨 결과 받아오기
//    EarthquakeDTO getEarthquake(EarthquakeDTO pDTO) throws Exception;

//    List<EarthquakeDTO> getEarthquakeList(EarthquakeDTO pDTO) throws Exception;

    void getEarthquakeApi() throws Exception;
}
