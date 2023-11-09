package kopo.poly.service.impl;

import kopo.poly.dto.SnowDTO;
import kopo.poly.persistance.mapper.ISnowMapper;
import kopo.poly.service.ISnowService;
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
public class SnowService implements ISnowService {

    private final ISnowMapper snowMapper;


    @Override
    public List<SnowDTO> getSnowInfo() throws Exception {

        log.info(this.getClass().getName() + ".getSnowInfo Start!!");

        log.info("DB 삭제 시작");

        snowMapper.deleteSnowInfo();


        // 현재 시간을 가져옵니다.
        Date currentDate = new Date();

        // 날짜 및 시간 형식을 설정합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        // 현재 시간을 원하는 형식으로 변환합니다.
        String formattedDate = dateFormat.format(currentDate);

        // 변환된 날짜와 시간을 사용하여 URL을 생성합니다.
        String url = "https://apihub.kma.go.kr/api/typ01/url/kma_snow1.php?sd=tot&tm=" + formattedDate + "&help=0&authKey=ELIfl6hfTHeyH5eoXwx3oA";

        // Jsoup 라이브러리를 통해 사이트 접속되면, 그 사이트의 전체 HTML소스 저장할 변수
        Document doc = null;

        // 사이트 접속
        doc = Jsoup.connect(url).get();

        log.info("doc : " + doc);

        Elements element = doc.select("body");

        log.info("element : " + element);

        // 적설 정보 가져오기
        Iterator<Element> snow = element.select("body").iterator();

        SnowDTO pDTO = null;


        log.info("snow : " + snow);

        List<SnowDTO> pList = new ArrayList<>();

        int idx = 0;


// pre 태그에서 추출한 텍스트를 처리하고 SnowDTO 객체에 값을 담아 리스트에 추가하는 로직 추가
        while (snow.hasNext()) {

            if (idx++ > 6) {
                break;
            }
            pDTO = new SnowDTO();

            log.info("pDTO : " + pDTO);

            // pre 태그에서 추출한 텍스트 한 줄씩 읽어오기
            String line = CmmUtil.nvl(snow.next().text());

            log.info("line : " + line);

            log.info("cm의 위치 : " + line.indexOf("(cm)"));
            log.info("#7777END의 위치 : " + line.indexOf("#7777END"));
            line = line.substring(79, 36001);

            log.info("substring 결과 : " + line);

            String[] lines = line.split("="); // 난 한줄씩
            String[] snowInfoArray = line.split(",");


            log.info("lines : " + lines.length);


            // 3번째 줄부터 출력하기
            for (int i = 0; i < lines.length && (6 + 7 * i) < snowInfoArray.length; i++) {
                pDTO.setDt(CmmUtil.nvl(snowInfoArray[0 + 7 * i]));
                pDTO.setStnId(CmmUtil.nvl(snowInfoArray[1 + 7 * i]));
                pDTO.setStnKo(CmmUtil.nvl(snowInfoArray[2 + 7 * i]));
                pDTO.setLon(CmmUtil.nvl(snowInfoArray[3 + 7 * i]));
                pDTO.setLat(CmmUtil.nvl(snowInfoArray[4 + 7 * i]));
                pDTO.setSd(CmmUtil.nvl(snowInfoArray[6 + 7 * i]));

                log.info("-----------------------------------");
                log.info("DT : " + pDTO.getDt());
                log.info("stnId : " + pDTO.getStnId());
                log.info("stnKo : " + pDTO.getStnKo());
                log.info("Lon : " + pDTO.getLon());
                log.info("Lat : " + pDTO.getLat());
                log.info("sd : " + pDTO.getSd());


                snowMapper.insertSnowInfo(pDTO);

                pList.add(pDTO);
            }
        }
        List<SnowDTO> rList = snowMapper.getSnowInfo();

        log.info(this.getClass().getName() + ".getSnowInfo End!");

        return rList;
    }
}
