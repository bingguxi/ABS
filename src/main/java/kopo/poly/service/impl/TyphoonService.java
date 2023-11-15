package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import kopo.poly.dto.SnowDTO;
import kopo.poly.dto.TyphoonDTO;
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
            getTyphoonInfo(apiParam, year);

            i++;
            year++;
        }
        log.info(this.getClass().getName() + ".setTyphoonUrl End !");
    }

//    @Override
//    public List<TyphoonDTO> getTyphoonLiveInfo() throws Exception {
//
//        log.info(this.getClass().getName() + ".getSnowInfo Start!!");
//
//        log.info("DB 삭제 시작");
//
//        typhoonMapper.deleteTyphoonLiveInfo();
//
//
//        // 현재 시간을 가져옵니다.
//        Date currentDate = new Date();
//
//        // 날짜 및 시간 형식을 설정합니다.
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
//
//        // 현재 시간을 원하는 형식으로 변환합니다.
//        String formattedDate = dateFormat.format(currentDate);
//
//        // 변환된 날짜와 시간을 사용하여 URL을 생성합니다.
//        String url = "https://apihub.kma.go.kr/api/typ01/url/kma_snow1.php?sd=tot&tm=" + formattedDate + "&help=0&authKey=ELIfl6hfTHeyH5eoXwx3oA";
//
//        // Jsoup 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
//        Document doc = null;
//
//        // 사이트 접속
//        doc = Jsoup.connect(url).get();
//
//        log.info("doc : " + doc);
//
//        Elements element = doc.select("body");
//
//        log.info("element : " + element);
//
//        // 적설 정보 가져오기
//        Iterator<Element> snow = element.select("body").iterator();
//
//        SnowDTO pDTO = null;
//
//
//        log.info("snow : " + snow);
//
//        List<SnowDTO> pList = new ArrayList<>();
//
//        int idx = 0;
//
//
//// pre 태그에서 추출한 텍스트를 처리하고 SnowDTO 객체에 값을 담아 리스트에 추가하는 로직 추가
//        while (snow.hasNext()) {
//
//            if (idx++ > 6) {
//                break;
//            }
//            pDTO = new SnowDTO();
//
//            log.info("pDTO : " + pDTO);
//
//            // pre 태그에서 추출한 텍스트 한 줄씩 읽어오기
//            String line = CmmUtil.nvl(snow.next().text());
//
//            log.info("line : " + line);
//
//            log.info("cm의 위치 : " + line.indexOf("(cm)"));
//            log.info("#7777END의 위치 : " + line.indexOf("#7777END"));
//            line = line.substring(79, 36001);
//
//            log.info("substring 결과 : " + line);
//
//            String[] lines = line.split("="); // 난 한줄씩
//            String[] snowInfoArray = line.split(",");
//
//
//            log.info("lines : " + lines.length);
//
//
//            // 3번째 줄부터 출력하기
//            for (int i = 0; i < lines.length && (6 + 7 * i) < snowInfoArray.length; i++) {
//                pDTO.setDt(CmmUtil.nvl(snowInfoArray[0 + 7 * i].replaceAll("= ", "")));
//                pDTO.setStnId(CmmUtil.nvl(snowInfoArray[1 + 7 * i]));
//                pDTO.setStnKo(CmmUtil.nvl(snowInfoArray[2 + 7 * i]));
//                pDTO.setLon(CmmUtil.nvl(snowInfoArray[3 + 7 * i]));
//                pDTO.setLat(CmmUtil.nvl(snowInfoArray[4 + 7 * i]));
//                pDTO.setSd(CmmUtil.nvl(snowInfoArray[6 + 7 * i]));
//
//                log.info("-----------------------------------");
//                log.info("DT : " + pDTO.getDt());
//                log.info("stnId : " + pDTO.getStnId());
//                log.info("stnKo : " + pDTO.getStnKo());
//                log.info("Lon : " + pDTO.getLon());
//                log.info("Lat : " + pDTO.getLat());
//                log.info("sd : " + pDTO.getSd());
//
//
//                snowMapper.insertSnowInfo(pDTO);
//
//                pList.add(pDTO);
//            }
//        }
//        List<SnowDTO> rList = snowMapper.getSnowInfo();
//
//        log.info(this.getClass().getName() + ".getSnowInfo End!");
//
//        return rList;
//    }

    @Override
    public void getTyphoonInfo(String apiParam, int year) throws Exception {

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
                    // body에서 items 추출
//                    Map<String, Object> responseBody = (Map<String, Object>) body.get("body");
                    Map<String, Object> items = (Map<String, Object>) body.get("items");

                    // items에서 item 추출
                    Map<String, Object> item = (Map<String, Object>) items.get("item");

                    //  item에서 ann 추출
                    Map<String, Object> ann = (Map<String, Object>) item.get("ann");

                    // ann에서 info 추출
                    List<Map<String, Object>> infoList = (List<Map<String, Object>>) ann.get("info");

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

                    // DB에 저장 (주석 해제하여 활성화)
                    // typhoonMapper.insertTyphoonInfo(typhoonList);

                    log.info("매핑된 TyphoonDTO 리스트: " + tList);
                } else {
                    log.error("API 호출 결과 오류: " + resultCode);
                }
                // ... (이후 코드는 그대로 유지)
            } catch (Exception e) {
                log.error("Error in getTyphoonInfo: " + e.getMessage(), e);
            }
        } finally {
            // Ensure proper cleanup in case of exceptions or successful execution
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
