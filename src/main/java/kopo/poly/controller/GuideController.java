package kopo.poly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/guide")
public class GuideController {

    // TODO 재난별 행동요령 html 만들고 GetMapping 하기!!!
    @GetMapping(value = "dust")
    public String dustGuide() {

        log.info(this.getClass().getName() + ".dustGuide 시작!");
        log.info(this.getClass().getName() + ".dustGuide 끝!");

        return "/guide/dustGuide";
    }

}
