package kopo.poly.controller;

import kopo.poly.dto.*;
import kopo.poly.service.IDisasterMsgService;
import kopo.poly.service.IEarthquakeService;
import kopo.poly.service.ITyphoonService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "")
@RequiredArgsConstructor
@Controller
public class MainController {

    private final IDisasterMsgService disasterMsgService;

    private final IEarthquakeService earthquakeService;

    private final ITyphoonService typhoonService;

    @GetMapping(value = "/index")
    public String index(ModelMap model) throws Exception{

        log.info(this.getClass().getName() + ".index Start!");

        /* --------------------- 재난문자 리스트 슬라이드 로직 -------------------------- */
        // 서비스 메서드를 호출하여 데이터를 가져옴
        List<DisasterMsgResultDTO> last3List = disasterMsgService.getLast3DisasterMsgList();

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", last3List);

        // 들어온 값 확인
        log.info("rList : " + last3List);
        /* ------------------------------- 끝 ----------------------------------- */

        /* ------------------------------------------------------------------------- */
        /* 지진 + 태풍 실시간 정보 */
        List<EarthquakeLiveDTO> earthquakeLiveTotal = earthquakeService.getEarthquakeLiveList();
        List<TyphoonLiveDTO> typhoonLiveTotal = typhoonService.getTyphoonLiveList();

        int elSize = earthquakeLiveTotal.size();
        int tlSize = typhoonLiveTotal.size();

        model.addAttribute("elSize", elSize); // 실시간 지진 카운트를 위해 배열의 사이즈를 재서 모델로 저장
        model.addAttribute("tlSize", tlSize);

        log.info("elSize ( 실시간 지진 ) : " + elSize);
        log.info("tlSize ( 실시간 태풍 ) : " + tlSize);
        /**/

        /* 지진 태풍 올해 총 건수 정보 */
        List<EarthquakeResultDTO> earthquakeTotal = earthquakeService.getEarthquakeList();
        List<TyphoonDTO> typhoonTotal = typhoonService.getTyphoonList();

        // tm_Issue 컬럼에 2023으로 시작하는 경우 필터링
        long eTyCount = earthquakeTotal.stream()
                .filter(dto -> dto.getTmIssue() != null && dto.getTmIssue().startsWith("2023"))
                .count();

        // year 컬럼에 2023으로 시작하는 경우 필터링
        long tTyCount = typhoonTotal.stream()
                .filter(dto -> dto.getYear() != null && dto.getYear().startsWith("2023"))
                .count();

        model.addAttribute("eTyCount", eTyCount);
        model.addAttribute("tTyCount", tTyCount);

        log.info("eTyCount ( 올해 지진 건수 ) : " + eTyCount);
        log.info("tTyCount ( 올해 태풍 건수 ) : " + tTyCount);
        /**/

        /* 지진 태풍 작년 총 건수 정보 */
        // tm_Issue 컬럼에 2023으로 시작하는 경우 필터링
        long eLyCount = earthquakeTotal.stream()
                .filter(dto -> dto.getTmIssue() != null && dto.getTmIssue().startsWith("2022"))
                .count();

        // year 컬럼에 2023으로 시작하는 경우 필터링
        long tLyCount = typhoonTotal.stream()
                .filter(dto -> dto.getYear() != null && dto.getYear().startsWith("2022"))
                .count();


        model.addAttribute("eLyCount", eLyCount);
        model.addAttribute("tLyCount", tLyCount);

        log.info("eLyCount ( 지난해 지진 건수 ) : " + eLyCount);
        log.info("tLyCount ( 지난해 태풍 건수 ) : " + tLyCount);
        /**/

        // 올해 두 카운트를 합쳐서 모델에 추가
        model.addAttribute("totalTyCount", eTyCount + tTyCount);

        // 작년 두 카운트를 합쳐서 모델에 추가
        model.addAttribute("totalLyCount", eLyCount + tLyCount);

        /* ------------------------------------------------------------------------- */

        log.info(this.getClass().getName() + ".index End!");

        return "/index";
    }

    @GetMapping(value = "/wordCloud")
    public String wordCloud() {

        log.info(this.getClass().getName() + ".wordCloud Start!");
        log.info(this.getClass().getName() + ".wordCloud End!");

        return "/index";
    }
}
