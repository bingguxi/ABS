package kopo.poly.controller;

import com.google.gson.Gson;
import kopo.poly.dto.ShelterDTO;
import kopo.poly.service.IShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "map")
public class MapController {

    private final IShelterService shelterService;

    @GetMapping(value = "/getCurrentPosition")
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
    }

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

        log.info(this.getClass().getName() + ".shelter 끝!");

        return "/map/shelterMap";
    }

}
