package kopo.poly.controller;

import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.dto.EarthquakeLiveDTO;
import kopo.poly.dto.EarthquakeResultDTO;
import kopo.poly.service.IEarthquakeService;
import kopo.poly.util.CmmUtil;
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
@RequestMapping(value = "/earthquake")
@RequiredArgsConstructor
//@RestController
@Controller
public class EarthquakeController {
    @Autowired
    private IEarthquakeService earthquakeService;

    /**
     * 지진 실시간 데이터 호출 후 파싱을 거쳐서
     * DB에 있는 기존 데이터 삭제하고 저장한 데이터를 확인하는 로직
     */
    @ResponseBody
    @GetMapping(value = "earthquakeLiveInfo")
    public List<EarthquakeLiveDTO> earthquakeLiveInfo() throws Exception{

        log.info(this.getClass().getName() + ".earthquakeLiveInfo Start!!");

        List<EarthquakeLiveDTO> rList = earthquakeService.getEarthquakeLiveList();


        log.info("rList : ", rList);

        for (EarthquakeLiveDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("참조번호 : " + rDTO.getCnt());
            log.info("통보종류 : " + rDTO.getFcTp());
            log.info("지도 이미지 : " + rDTO.getImg());
            log.info("진도 (시범서비스) : " + rDTO.getInT());
            log.info("위도(deg.) : " + rDTO.getLat());
            log.info("진앙 위치 : " + rDTO.getLoc());
            log.info("경도(deg.) : " + rDTO.getLon());
            log.info("규모 : " + rDTO.getMt());
            log.info("참고사항 : " + rDTO.getRem());
            log.info("지점코드 - 108(전국)으로 고정 : " + rDTO.getStnId());
            log.info("진앙시(년월일시분초)" + "- 지진 발생 시각 : " + rDTO.getTmEqk());
            log.info("발표시각(년월일시분)" + "- 통보문 발표 시각 : " + rDTO.getTmFc());
            log.info("발표 일련번호(월별) : " + rDTO.getTmSeq());
            log.info("발생깊이(km, 시범 운영) : " + rDTO.getDep());
        }

        log.info(this.getClass().getName() + ".earthquakeLiveInfo End!!");

        return rList;

    }

    /**
     * 지진 과거 데이터 API 호출해서 DB에 저장하는 로직
     */
    @GetMapping(value = "getEarthquake")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        earthquakeService.setEarthquakeUrl();

        return "/erathquakeInfo";
    }

    /* 지진 과거 정보 리스트 */
    @GetMapping("/getEarthquakeList")
    public String getEarthquakeList(HttpSession session, HttpServletRequest request, ModelMap model,
                                 @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".earthquakeList Start!");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<EarthquakeResultDTO> rList = Optional.ofNullable(earthquakeService.getEarthquakeList())
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

        log.info(this.getClass().getName() + ".getEarthquakeList End!");

        return "earthquake/earthquakeList";
    }

    /* 지진 과거 정보 리스트 */
    @GetMapping("/getEarthquakeLiveList")
    public String getEarthquakeLiveList(HttpSession session, HttpServletRequest request,
                                     ModelMap model, @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".getEarthquakeLiveList Start!");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<EarthquakeLiveDTO> rList = Optional.ofNullable(earthquakeService.getEarthquakeLiveList())
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

        // img 값을 가져와서 처리
        for (EarthquakeLiveDTO dto : rList) {
            // img 값을 가져와서 반절로 자르기
            String img = dto.getImg();
            if (img != null && img.length() > 1) {
                int halfLength = img.length() / 2;
                String firstHalf = img.substring(0, halfLength);
                String secondHalf = img.substring(halfLength);
                // 줄바꿈 문자를 추가하여 model에 다시 담기
                dto.setImg(firstHalf + "\n" + secondHalf);
            }
        }

        log.info(this.getClass().getName() + ".getEarthquakeLiveList End!");

        return "earthquake/earthquakeLiveList";
    }
}
