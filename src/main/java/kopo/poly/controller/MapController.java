package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.ICctvService;
import kopo.poly.service.IShelterService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "map")
public class MapController {

    private final IShelterService shelterService;
    private final ICctvService cctvService;

   /* @GetMapping(value = "/getCurrentPosition")
    public String getCurrentPosition() {

        log.info(this.getClass().getName() + ".getCurrentPosition Start!");
        log.info(this.getClass().getName() + ".getCurrentPosition End!");

        return "getCurrentPosition";
    }

    @GetMapping(value = "/watchPosition")
    public String watchPosition() {

        log.info(this.getClass().getName() + ".watchPosition Start!");
        log.info(this.getClass().getName() + ".watchPosition End!");

        return "watchPosition";
    }*/

    @GetMapping(value = "insertShelter")
    @ResponseBody
    public String insertShelter() throws Exception {

        log.info(this.getClass().getName() + ".insertShelter 시작!");
        log.info(this.getClass().getName() + ".insertShelter 끝!");

        shelterService.insertShelter();

        return "끝났당";
    }

    @GetMapping(value = "shelter")
    public String shelter(ModelMap model, HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".shelter 시작!");

        //ShelterDTO rDTO = shelterService.getShelter();

        List<ShelterDTO> rList = Optional.ofNullable(shelterService.getShelterList()).orElseGet(ArrayList::new);

        //model.addAttribute("rDTO", rDTO);
        model.addAttribute("rList", rList);

//        log.info("rList : " + rList);

        log.info(this.getClass().getName() + ".shelter 끝!");

        return "/map/shelterMap";
    }

    @GetMapping(value = "cctv")
    public String cctv(ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".cctv 시작!");

        //ShelterDTO rDTO = shelterService.getShelter();

        List<CctvResultDTO> rList = Optional.ofNullable(cctvService.getCctv()).orElseGet(ArrayList::new);

        //model.addAttribute("rDTO", rDTO);
        model.addAttribute("rList", rList);

//        log.info("rList : " + rList);

        log.info(this.getClass().getName() + ".cctv 끝!");

        return "/map/cctvMap";
    }

    @GetMapping(value = "cctv/video")
    public String cctvVideo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".cctvVideo 시작!");

        String cctvName = CmmUtil.nvl(request.getParameter("cctvName"));

        log.info("cctvName : " + cctvName);

        CctvResultDTO pDTO = new CctvResultDTO();
        pDTO.setCctvName(cctvName);

        CctvResultDTO rDTO = Optional.ofNullable(cctvService.getCctvURL(pDTO)).orElseGet(CctvResultDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".cctvVideo 끝!");

        return "/map/cctvVideo";

    }


    @GetMapping(value = "dust")
    public String dust() {



        return "/map/dustMap";

    }

    @GetMapping(value = "typhoon")
    public String typhoon() {



        return "/map/typhoonMap";

    }

    @GetMapping(value = "fire")
    public String fire() {



        return "/map/fireMap";

    }

    @GetMapping(value = "snow")
    public String snonw() {



        return "/map/snowMap";

    }

    @GetMapping(value = "earthquake")
    public String earthquake() {



        return "/map/earthquakeMap";

    }



    @GetMapping(value = "test")
    public String test() {

        log.info(this.getClass().getName() + "test 열림!!!");

        return "/map/test";
    }

}
