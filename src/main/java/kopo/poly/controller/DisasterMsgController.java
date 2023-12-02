package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.ICctvService;
import kopo.poly.service.IDisasterMsgService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //    @Autowired
    private final IDisasterMsgService disasterMsgService;

//    @GetMapping("/findCctv")
//    public String findCctv(Model model) {
//
//        log.info(this.getClass().getName() + ".findCctv Start!");
//
//        double lat = 36.58629; // 고정값 (임의)
//        double lng = 128.186793; // 고정값 (임의)
//
//        String cctvData = cctvService.getCctvData(lat, lng);
//
//        model.addAttribute("cctvData", cctvData);
//
//        log.info(this.getClass().getName() + ".findCctv End!");
//
//        return "cctvResult";
//    }

    /**
     * 재난문자 API JSON 형태로 파싱한 후
     * 파싱이 잘 됐는지 확인하는 로직
     */
    @ResponseBody
    @GetMapping("getDisasterMsg")
    public List<DisasterMsgResultDTO> getDisasterMsg() throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsg Start!!");


        List<DisasterMsgResultDTO> rList = disasterMsgService.getDisasterMsg();

        log.info("rList : ", rList);

        for (DisasterMsgResultDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("생성일시 : " + rDTO.getCreateDate());
            log.info("지점번호 : " + rDTO.getLocationId());
            log.info("지점명 : " + rDTO.getLocationName());
            log.info("발생번호 : " + rDTO.getMd101Sn());
            log.info("내용 : " + rDTO.getMsg());
            log.info("발생위치 ? : " + rDTO.getSendPlatform());
        }

        log.info(this.getClass().getName() + ".getDisasterMsg End!!");

        return rList;
    }

    @GetMapping("disasterMsgInfo")
    public String disasterMsgInfo(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".disasterMsgInfo Start!");

        String md101Sn = CmmUtil.nvl(request.getParameter("md101Sn"));
        String msg = CmmUtil.nvl(request.getParameter("msg"));

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("md101Sn : " + md101Sn);
        log.info("msg : " + msg);
        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        DisasterMsgResultDTO pDTO = new DisasterMsgResultDTO();
        pDTO.setMd101Sn(md101Sn);
        pDTO.setMsg(msg);

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        DisasterMsgResultDTO rDTO = Optional.ofNullable(disasterMsgService.getDisasterMsgInfo(pDTO, true))
                .orElseGet(DisasterMsgResultDTO::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<DisasterMsgResultDTO> rList = Optional.ofNullable(disasterMsgService.getDisasterMsgList())
                .orElseGet(ArrayList::new);

//        log.info("rList : ", rList);
//
//        for (DisasterMsgResultDTO rDTO : rList) {
//            log.info("-------------------------------");
//            log.info("생성일시 : " + rDTO.getCreateDate());
//            log.info("지점번호 : " + rDTO.getLocationId());
//            log.info("지점명 : " + rDTO.getLocationName());
//            log.info("발생번호 : " + rDTO.getMd101Sn());
//            log.info("내용 : " + rDTO.getMsg());
//            log.info("발생위치 ? : " + rDTO.getSendPlatform());
//        }

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 들어온 값 확인
        log.info("rList : " + rList );

        log.info(this.getClass().getName() + ".disasterMsgInfo End!");

        return "/disasterMsg/disasterMsgInfo";
    }
}
