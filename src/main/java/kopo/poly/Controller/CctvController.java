package kopo.poly.Controller;

import kopo.poly.service.impl.CctvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CctvController {

    @Autowired
    private CctvService cctvService;

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
}