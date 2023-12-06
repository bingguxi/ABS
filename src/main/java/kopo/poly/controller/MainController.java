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


    @GetMapping(value = "/index")
    public String index(ModelMap model) throws Exception{

        log.info(this.getClass().getName() + ".index Start!");

        /* 재난문자 리스트 슬라이드 로직 */
        // 서비스 메서드를 호출하여 데이터를 가져옴
        List<DisasterMsgResultDTO> last3List = disasterMsgService.getLast3DisasterMsgList();

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", last3List);

        // 들어온 값 확인
        log.info("rList : " + last3List);
        /* 여기까지 */

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
