package kopo.poly.controller;

import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.service.IDisasterMsgService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/disasterMsg")
//@RestController
@Controller
public class DisasterMsgController {

    private final IDisasterMsgService disasterMsgService;

    /**
     * 재난문자 API JSON 형태로 파싱한 후
     * 파싱이 잘 됐는지 확인하는 로직
     */
    @ResponseBody
    @GetMapping("getDisasterMsg")
    public List<DisasterMsgResultDTO> getDisasterMsg() throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsg Start!!");


        List<DisasterMsgResultDTO> rList = disasterMsgService.getDisasterMsg();

        log.info("rList : " + rList);

        for (DisasterMsgResultDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("생성일시 : " + rDTO.getCreateDate());
            log.info("지점번호 : " + rDTO.getLocationId());
            log.info("지점명 : " + rDTO.getLocationName());
            log.info("발생번호 : " + rDTO.getMd101Sn());
            log.info("내용 : " + rDTO.getMsg());
            log.info("발생위치 : " + rDTO.getSendPlatform());
        }

        log.info(this.getClass().getName() + ".getDisasterMsg End!!");

        return rList;
    }

    /* 재난 문자 리스트 페이지 */
    @GetMapping("getDisasterMsgList")
    public String getDisasterMsgList(HttpSession session, HttpServletRequest request, ModelMap model,
                                     @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsgList Start!");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<DisasterMsgResultDTO> rList = Optional.ofNullable(disasterMsgService.getDisasterMsgList())
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

        log.info(this.getClass().getName() + ".getDisasterMsgList End!");

        return "disasterMsg/disasterMsgList";
    }
}
