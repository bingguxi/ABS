package kopo.poly.controller;

import kopo.poly.dto.*;
import kopo.poly.service.ITyphoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/typhoon")
@RequiredArgsConstructor
@Controller
public class TyphoonController {

    @Autowired
    private ITyphoonService typhoonService;

    /**
     * 태풍 실시간 데이터 호출 후 파싱을 거쳐서
     * DB에 있는 기존 데이터 삭제하고 저장하는 로직
     */
    @ResponseBody
    @GetMapping(value = "typhoonLiveInfo")
    public List<TyphoonLiveDTO> typhoonLiveInfo() throws Exception{

        log.info(this.getClass().getName() + ".typhoonLiveInfo Start!!");

        List<TyphoonLiveDTO> rList = typhoonService.getTyphoonLiveList();


        log.info("rList : ", rList);

        for (TyphoonLiveDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("0(분석), 1(예측) : " + rDTO.getFt());
            log.info("태풍번호 : " + rDTO.getTyp());
            log.info("발표번호 : " + rDTO.getSeq());
            log.info("태풍 분석시각 (UTC) : " + rDTO.getTypTm());
            log.info("위도 : " + rDTO.getLat());
            log.info("경도 : " + rDTO.getLon());
            log.info("태풍진행방향 (16방위기호) : " + rDTO.getDir());
            log.info("태풍진향속도 (km/h) : " + rDTO.getSp());
            log.info("태풍중심기압 (hpa) : " + rDTO.getPs());
            log.info("태풍최대풍속 (m/s) : " + rDTO.getWs());
            log.info("15m/s 반경 (km) : " + rDTO.getRad15());
            log.info("25m/s 반경 (km) : " + rDTO.getRad25());
            log.info("위치 : " + rDTO.getLoc());
        }

        log.info(this.getClass().getName() + ".typhoonLiveInfo End!!");

        return rList;

    }

    /**
     * 태풍 과거 API 호출 후 값 저장하는 로직
     */
    @ResponseBody
    @GetMapping(value = "getTyphoon")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        typhoonService.setTyphoonUrl();

        return "/TyphoonInfo";
    }

    /* 지진 과거 정보 리스트 */
    @GetMapping("/getTyphoonList")
    public String getTyphoonList(HttpSession session, HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".getTyphoonList Start!");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<TyphoonDTO> rList = Optional.ofNullable(typhoonService.getTyphoonList())
                .orElseGet(ArrayList::new);

        /**페이징 시작 부분*/

        // 페이지당 보여줄 아이템 개수 정의
        int itemsPerPage = 10;

        // 페이지네이션을 위해 전체 아이템 개수 구하기
        int totalItems = rList.size();

        // 전체 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 아이템들만 선택하여 rList에 할당
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".페이지 번호 : " + page);

        /**페이징 끝부분*/

        // 들어온 값 확인
        log.info("rList : " + rList );

        log.info(this.getClass().getName() + ".getTyphoonList End!");

        return "typhoon/typhoonList";
    }

    /* 지진 과거 정보 리스트 */
    @GetMapping("/getTyphoonLiveList")
    public String getTyphoonLiveList(HttpSession session, HttpServletRequest request,
                                     ModelMap model, @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".getTyphoonLiveList Start!");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<TyphoonLiveDTO> rList = Optional.ofNullable(typhoonService.getTyphoonLiveList())
                .orElseGet(ArrayList::new);

        /**페이징 시작 부분*/

        // 페이지당 보여줄 아이템 개수 정의
        int itemsPerPage = 10;

        // 페이지네이션을 위해 전체 아이템 개수 구하기
        int totalItems = rList.size();

        // 전체 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 아이템들만 선택하여 rList에 할당
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".페이지 번호 : " + page);

        /**페이징 끝부분*/

        // 들어온 값 확인
        log.info("rList : " + rList );

        log.info(this.getClass().getName() + ".getTyphoonLiveList End!");

        return "typhoon/typhoonLiveList";
    }
}
