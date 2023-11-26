package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.service.ICctvService;
import kopo.poly.service.IDisasterMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("disasterMsg")
@RestController
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
}
