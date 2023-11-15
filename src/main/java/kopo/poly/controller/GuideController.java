package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/guide")
public class GuideController {

    @GetMapping(value = "dust")
    public String dustGuide() {

        log.info(this.getClass().getName() + ".dustGuide 시작!");
        log.info(this.getClass().getName() + ".dustGuide 끝!");

        return "/guide/dustGuide";
    }

    @GetMapping(value = "storm")
    public String stormGuide() {

        log.info(this.getClass().getName() + ".dustGuide 시작!");
        log.info(this.getClass().getName() + ".dustGuide 끝!");

        return "/guide/stormGuide";
    }

    @GetMapping(value = "snow")
    public String snowGuide() {

        log.info(this.getClass().getName() + ".dustGuide 시작!");
        log.info(this.getClass().getName() + ".dustGuide 끝!");

        return "/guide/snowGuide";
    }

    @GetMapping(value = "earthquake")
    public String earthquakeGuide() {

        log.info(this.getClass().getName() + ".dustGuide 시작!");
        log.info(this.getClass().getName() + ".dustGuide 끝!");

        return "/guide/earthquakeGuide";
    }

}
