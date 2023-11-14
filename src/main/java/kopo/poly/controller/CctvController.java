package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.service.impl.CctvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("cctv")
@RestController
public class CctvController {

//    @Autowired
    private final CctvService cctvService;

    @GetMapping("/findCctv")
    public String findCctv(Model model) {

        log.info(this.getClass().getName() + ".findCctv Start!");

        double lat = 36.58629; // 고정값 (임의)
        double lng = 128.186793; // 고정값 (임의)

        String cctvData = cctvService.getCctvData(lat, lng);

        model.addAttribute("cctvData", cctvData);

        log.info(this.getClass().getName() + ".findCctv End!");

        return "cctvResult";
    }

    @ResponseBody
    @GetMapping("getCctv")
    public List<CctvResultDTO> getCctv(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!!");

        List<CctvResultDTO> rList = cctvService.getCctv();

        log.info("rList : ", rList);

        for (CctvResultDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("날짜 : " + rDTO.getCctvName());
            log.info("국내 지점번호 : " + rDTO.getCctvUrl());
            log.info("지점명 : " + rDTO.getCoordX());
            log.info("경도 : " + rDTO.getCoordY());
        }

//        String type = CmmUtil.nvl(request.getParameter("type")); // 도로 유형(ex: 고속도로 / its: 국도)
//        String CctvType = CmmUtil.nvl(request.getParameter("CctvType"));  // CCTV 유형(1: 실시간 스트리밍(HLS) / 2: 동영상 파일 / 3: 정지 영상)
//        String MinX = CmmUtil.nvl(request.getParameter("MinX")); // 최소 경도 영역
//        String MaxX = CmmUtil.nvl(request.getParameter("MaxX")); // 최대 경도 영역
//        String MinY = CmmUtil.nvl(request.getParameter("MinY")); // 최소 위도 영역
//        String MaxY = CmmUtil.nvl(request.getParameter("MaxY")); // 최대 위도 영역
//        String GetType = CmmUtil.nvl(request.getParameter("GetType")); // 출력 결과 형식(xml, json / 기본: xml)
//
//        pDTO.setType(type);
//        pDTO.setCctvType(CctvType);
//        pDTO.setMinX(MinX);
//        pDTO.setMaxX(MaxX);
//        pDTO.setMinY(MinY);
//        pDTO.setMaxY(MaxY);
//        pDTO.setGetType(GetType);


//        List<CctvResultDTO> rList = cctvService.getCctv(pDTO);

        return rList;
    }
}