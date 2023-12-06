package kopo.poly.controller;

import kopo.poly.dto.FireDTO;
import kopo.poly.service.IFireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/fire")
@Controller
public class FireController {

    private final IFireService fireService;

    @GetMapping(value = "/getFireList")
    public String getFireList(ModelMap model, @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".getFireList 컨트롤러 시작!");

        // 저장된 산불 정보 조회하기
        List<FireDTO> rList = Optional.ofNullable(fireService.getFireList()).orElseGet(ArrayList::new);

        /**페이징 시작 부분*/

        // 페이지당 보여줄 아이템 개수 정의
        int itemsPerPage = 10;

        // 페이지네이션을 위해 전체 아이템 개수 구하기
        int totalItems = rList.size();

        // 전체 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 아이템들만 선택하여 rList에 할당
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".페이지 번호 : " + page);

        /**페이징 끝부분*/

        // 들어온 값 확인
        log.info("rList : " + rList );

        log.info(this.getClass().getName() + ".getFireList 컨트롤러 끝!");

        return "/fire/fireList";
    }

}
