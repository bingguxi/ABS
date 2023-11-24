package kopo.poly.controller;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.service.ICctvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("cctv")
@RestController
public class CctvController {

    private final ICctvService cctvService;

    @ResponseBody
    @GetMapping("getCctv")
    public List<CctvResultDTO> getCctv() throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!!");

        List<CctvResultDTO> rList = cctvService.getCctv();

        log.info("rList : " + rList);

        for (CctvResultDTO rDTO : rList) {
            log.info("-------------------------------");
            log.info("CCTV명 : " + rDTO.getCctvName());
            log.info("CCTV URL : " + rDTO.getCctvUrl());
            log.info("위도 : " + rDTO.getCoordX());
            log.info("경도 : " + rDTO.getCoordY());
        }

        return rList;
    }
}