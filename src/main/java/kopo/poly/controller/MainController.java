package kopo.poly.controller;

import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.service.IDisasterMsgService;
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
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "")
@RequiredArgsConstructor
@Controller
public class MainController {

    private final IDisasterMsgService disasterMsgService;

    @GetMapping(value = "/index")
    public String index(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception{

        log.info(this.getClass().getName() + ".index Start!");
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
        List<DisasterMsgResultDTO> rList = Optional.ofNullable(disasterMsgService.getDisasterMsgList())
                .orElseGet(ArrayList::new);

        // 'md101Sn' 속성을 기준으로 리스트를 오름차순으로 정렬
        rList.sort(Comparator.comparing(DisasterMsgResultDTO::getMd101Sn));

        // 리스트 크기가 3보다 작으면 모든 아이템을 가져옵니다.
        List<DisasterMsgResultDTO> last3List = rList.size() < 3 ? rList : rList.subList(rList.size() - 3, rList.size());

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", last3List);

        // 들어온 값 확인
        log.info("rList : " + rList );

        log.info(this.getClass().getName() + ".disasterMsgInfo End!");
        log.info(this.getClass().getName() + ".index End!");

        return "/index";
    }

//    @GetMapping("/index")
//    public String disasterMsgInfo(HttpSession session, HttpServletRequest request, ModelMap model) throws Exception {
//
//        log.info(this.getClass().getName() + ".disasterMsgInfo Start!");
//
//        String md101Sn = CmmUtil.nvl(request.getParameter("md101Sn"));
//        String msg = CmmUtil.nvl(request.getParameter("msg"));
//
//        /*
//         * ####################################################################################
//         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
//         * ####################################################################################
//         */
//        log.info("md101Sn : " + md101Sn);
//        log.info("msg : " + msg);
//        /*
//         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
//         */
//        DisasterMsgResultDTO pDTO = new DisasterMsgResultDTO();
//        pDTO.setMd101Sn(md101Sn);
//        pDTO.setMsg(msg);
//
//        // 공지사항 상세정보 가져오기
//        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
//        DisasterMsgResultDTO rDTO = Optional.ofNullable(disasterMsgService.getDisasterMsgInfo(pDTO, true))
//                .orElseGet(DisasterMsgResultDTO::new);
//
//        // 조회된 리스트 결과값 넣어주기
//        model.addAttribute("rDTO", rDTO);
//
//        // 공지사항 리스트 조회하기
//        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
//        List<DisasterMsgResultDTO> rList = Optional.ofNullable(disasterMsgService.getDisasterMsgList())
//                .orElseGet(ArrayList::new);
//
////        log.info("rList : ", rList);
////
////        for (DisasterMsgResultDTO rDTO : rList) {
////            log.info("-------------------------------");
////            log.info("생성일시 : " + rDTO.getCreateDate());
////            log.info("지점번호 : " + rDTO.getLocationId());
////            log.info("지점명 : " + rDTO.getLocationName());
////            log.info("발생번호 : " + rDTO.getMd101Sn());
////            log.info("내용 : " + rDTO.getMsg());
////            log.info("발생위치 ? : " + rDTO.getSendPlatform());
////        }
//
//        // 조회된 리스트 결과값 넣어주기
//        model.addAttribute("rList", rList);
//
//        // 들어온 값 확인
//        log.info("rList : " + rList );
//
//        log.info(this.getClass().getName() + ".disasterMsgInfo End!");
//
//        return "/index";
//    }
}
