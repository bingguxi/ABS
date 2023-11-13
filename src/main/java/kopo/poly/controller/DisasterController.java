package kopo.poly.controller;

import kopo.poly.dto.EarthquakeDTO;
import kopo.poly.service.IEarthquakeService;
import kopo.poly.service.impl.EarthquakeService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequestMapping(value = "/disaster")
@RequiredArgsConstructor
@RestController
@Controller
public class DisasterController {
    @Autowired
    private IEarthquakeService earthquakeService;

//    @GetMapping(value = "getEarthquake")
//    public EarthquakeDTO getWeather(HttpServletRequest request) throws Exception {
//
//        log.info(this.getClass().getName() + ".getWeather Start!");
//
//        String lat = CmmUtil.nvl(request.getParameter("eqDt"));
//        String lon = CmmUtil.nvl(request.getParameter("eqLt"));
//
//        EarthquakeDTO pDTO = new EarthquakeDTO();
//        pDTO.setEqDt(lat);
//        pDTO.setEqLt(lon);
//
//        EarthquakeDTO rDTO = earthquakeService.getEarthquake(pDTO);
//
//        if(rDTO == null) {
//            rDTO = new EarthquakeDTO();
//        }
//
//        log.info(this.getClass().getName() + ".getWeather End!");
//
//        return rDTO;
//    }


    @GetMapping(value = "getEarthquake")
    public String getEarthquakeApi() throws Exception {
        log.info("test called");
        earthquakeService.setEarthquakeUrl();

        return "/erathquakeInfo";
    }
}
