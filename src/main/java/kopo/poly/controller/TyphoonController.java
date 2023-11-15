package kopo.poly.controller;

import kopo.poly.service.ITyphoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(value = "/typhoon")
@RequiredArgsConstructor
@RestController
@Controller
public class TyphoonController {

    @Autowired
    private ITyphoonService typhoonService;

    @GetMapping(value = "getTyphoon")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        typhoonService.setTyphoonUrl();

        return "/TyphoonInfo";
    }
}
