package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kopo.poly.dto.SnowDTO;
import kopo.poly.dto.TyphoonDTO;
import kopo.poly.dto.TyphoonLiveDTO;
import kopo.poly.persistance.mapper.ITyphoonMapper;
import kopo.poly.service.ITyphoonService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TyphoonService implements ITyphoonService {

    private final ITyphoonMapper typhoonMapper;

    @Value("${bhg.api.key}")
    private String apiKey;

    /**
     * 태풍 과거 API 접근을 위한 URL 정보 생성 로직
     */
    @Override
    public void setTyphoonUrl() throws Exception {
        log.info(this.getClass().getName() + ".setTyphoonUrl Start !");

        String pageNo = "1";
        String numOfRows = "100";
        String dataType = "XML";
        int year = 2015;

        int i = 0;  // 수정된 부분: 무한루프를 돌지 않도록 조건 수정
        while (i < 8) {
            log.info("현재 i : " + i);

            String apiParam = "?pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&dataType=" + dataType + "&year=" + year + "&authKey=" + apiKey;
            log.info("apiParam : " + apiParam);

            // 수정된 부분: API 호출과 결과 처리를 별도의 메서드로 분리
            insertTyphoonInfo(apiParam, year);

            i++;
            year++;
        }
        log.info(this.getClass().getName() + ".setTyphoonUrl End !");
    }

    /**
     * 위에서 받은 URL 정보를 가지고 태풍 과거 API 호출 후
     * XML 파싱 후 DB에 담는 로직
     */
    @Override
    public void insertTyphoonInfo(String apiParam, int year) throws Exception {

        HttpURLConnection urlConnection = null;

        try {
            // URL 객체를 생성하고 웹 주소를 넣습니다.
            URL url = new URL(ITyphoonService.apiURL + apiParam);
            log.info(url.toString());

            // HttpURLConnection 객체를 생성합니다.
            urlConnection = (HttpURLConnection) url.openConnection();
            // 요청 방식을 GET으로 설정합니다.
            urlConnection.setRequestMethod("GET");
            log.info("url 요청 전달 완료");

            // BufferedReader 객체를 생성하고 HttpURLConnection의 InputStream을 넣습니다.
            try (InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                StringBuilder response = new StringBuilder();

                // 서버로부터 받은 데이터를 줄단위로 읽습니다.
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // API 응답 결과를 문자열로 변환
                String resultXml = response.toString();
                log.info("API 호출 결과 (시작태그는 <response>): " + resultXml);

                // XmlMapper를 사용하여 XML을 Java 객체로 변환
                XmlMapper xmlMapper = new XmlMapper();

                // TypeReference를 사용하여 제네릭 타입 지정
                TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
                Map<String, Object> resultMap = xmlMapper.readValue(resultXml, typeRef);

                // header와 body를 추출
                Map<String, Object> header = (Map<String, Object>) resultMap.get("header");
                Map<String, Object> body = (Map<String, Object>) resultMap.get("body");

                // header의 결과 코드 확인
                String resultCode = (String) header.get("resultCode");
                if ("00".equals(resultCode)) {
//                    Map<String, Object> responseBody = (Map<String, Object>) body.get("body");
                    Map<String, Object> items = (Map<String, Object>) body.get("items"); // body에서 items 추출
                    Map<String, Object> item = (Map<String, Object>) items.get("item"); // items에서 item 추출
                    Map<String, Object> ann = (Map<String, Object>) item.get("ann"); //  item에서 ann 추출
                    List<Map<String, Object>> infoList = (List<Map<String, Object>>) ann.get("info"); // ann에서 info 추출

                    // DTO로 변환하여 DB에 저장
                    List<TyphoonDTO> tList = new ArrayList<>();

                        for (Map<String, Object> info : infoList) {
                            TyphoonDTO tDTO = new TyphoonDTO();

                            tDTO.setTypSeq(String.valueOf(info.get("typ_seq")));
                            tDTO.setTypEn(String.valueOf(info.get("typ_en")));
                            tDTO.setYear(String.valueOf(year));
                            tDTO.setTmSt(String.valueOf(info.get("tm_st")));
                            tDTO.setTmEd(String.valueOf(info.get("tm_ed")));
                            tDTO.setTypPs(String.valueOf(info.get("typ_ps")));
                            tDTO.setTypWs(String.valueOf(info.get("typ_ws")));
                            tDTO.setTypName(String.valueOf(info.get("typ_name")));
                            tDTO.setEff(String.valueOf(info.get("eff")));

                            tList.add(tDTO);

                            log.info("typhoonDTO : " + tDTO);
                            log.info("typhoonList : " + tList);

                            typhoonMapper.insertTyphoonInfo(tDTO);

                            tDTO = null;
                        }

                    log.info("매핑된 TyphoonDTO 리스트: " + tList);
                } else {
                    log.error("API 호출 결과 오류: " + resultCode);
                }

            } catch (Exception e) {
                log.error("Error in getTyphoonInfo: " + e.getMessage(), e);
            }
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        log.info(this.getClass().getName() + ".getTyphoonInfo End !");
    }

    @Override
    public List<TyphoonDTO> getTyphoonList() throws Exception {

        log.info(this.getClass().getName() + ".getTyphoonList 태풍 과거 정보 조회 시작!");
        log.info(this.getClass().getName() + ".getTyphoonList 태풍 과거 정보 조회 끝!");

        return typhoonMapper.getTyphoonList();
    }

    @Override
    public List<TyphoonLiveDTO> getTyphoonLiveList() throws Exception {

        log.info(this.getClass().getName() + ".getTyphoonLiveList 태풍 실시간 정보 조회 시작!");
        log.info(this.getClass().getName() + ".getTyphoonLiveList 태풍 실시간 정보 조회 끝!");

        return typhoonMapper.getTyphoonLiveList();
    }

}
