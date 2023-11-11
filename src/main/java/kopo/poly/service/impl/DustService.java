package kopo.poly.service.impl;

import kopo.poly.dto.DustDTO;
import kopo.poly.persistance.mapper.IDustMapper;
import kopo.poly.service.IDustService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DustService implements IDustService {

    private final IDustMapper dustMapper;

    @Override
    public List<DustDTO> getDustInfo() throws Exception {

        log.info(this.getClass().getName() + ".getDustInfo Strart!!");

        log.info("DB 삭제 시작");

        dustMapper.deleteDustInfo();

        // 현재 시간을 가져옵니다.
        Date currentDate = new Date();

        // 날짜 및 시간 형식을 설정합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        // 현재 시간을 원하는 형식으로 변환합니다.
        String formattedDate = dateFormat.format(currentDate);

        // 변환된 날짜와 시간을 사용하여 URL을 생성합니다.
        String url = "https://apihub.kma.go.kr/api/typ01/url/dst_pm10_hr.php?tm=" + formattedDate + "&help=0&org=%27kma%27&authKey=0a-KCqUxRzuvigqlMbc7rg";

        // Jsoup 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
        Document doc = null;

        // 사이트 접속
        doc = Jsoup.connect(url).get();

        log.info("doc : " + doc);

        Elements element = doc.select("body");

        log.info("element : " + element);

        // 적설 정보 가져오기
        Iterator<Element> dust = element.select("body").iterator();

        DustDTO pDTO = null;

        log.info("dust : " + dust);

        List<DustDTO> pList = new ArrayList<>();

        int idx = 0;

        // pre 태그에서 추출한 텍스트를 처리하고 DustDTO 객체에 값을 담아 리스트에 추가하는 로직 추가
        while (dust.hasNext()) {

            if (idx++ > 6) {
                break;
            }
            pDTO = new DustDTO();

            log.info("pDTO : " + pDTO);

            // pre 태그에서 추출한 텍스트 한 줄씩 읽어오기
            String line = CmmUtil.nvl(dust.next().text());

            log.info("line : " + line);

            log.info("ug/m의 위치 : " + line.indexOf("ug/"));
            log.info("#7777END의 위치 : " + line.indexOf("#7777END"));
            line = line.substring(134, 1161);

            log.info("substring 결과 : " + line);

            String[] lines = line.split("\\n"); // 난 한줄씩
            String[] snowInfoArray = line.split(" ");


            log.info("lines : " + lines.length);


            // 3번째 줄부터 출력하기
            for (int i = 0; i < lines.length && (6 + 7 * i) < snowInfoArray.length; i++) {
                pDTO.setStnId(CmmUtil.nvl(snowInfoArray[3 + 7 * i]));
                pDTO.setMean(CmmUtil.nvl(snowInfoArray[4 + 7 * i]));

                log.info("-----------------------------------");

                log.info("stnId : " + pDTO.getStnId());
                log.info("sd : " + pDTO.getMean());

                // STN_ID가 이미 데이터베이스에 존재하는지 확인
                List<DustDTO> idList = dustMapper.getDustInfo();

                if (idList != null && !idList.isEmpty()) {
                    for (DustDTO dDTO : idList) {
                        // 나머지 코드는 그대로 유지
                        if (dDTO.getStnId().equals(dDTO.getStnId())) {
                            // STN_ID가 이미 존재한다면, Mean 값을 업데이트
                            dDTO.setMean(dDTO.getMean());
                            dustMapper.updateDustInfo(dDTO);
                            break; // 이미 존재하는 경우에는 반복을 종료합니다.
                        }
                    }
                } else {
                    // STN_ID가 존재하지 않는다면, 새로운 레코드 삽입
                    dustMapper.insertDustInfo(pDTO);
                }
                pList.add(pDTO);
            }
        }

        List<DustDTO> rList = dustMapper.getDustInfo();

        log.info(this.getClass().getName() + ".getDustInfo End!");

        return rList;
    }
}