package kopo.poly.controller;

import kopo.poly.dto.DustDTO;
import kopo.poly.service.IDustService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping(value = "/dust")
@RequiredArgsConstructor
@Controller
public class DustController {

    private final IDustService dustService;

    @GetMapping(value = "getDustList")
    public String getDustList(ModelMap model, @RequestParam(defaultValue = "1") int page) throws Exception {

        log.info(this.getClass().getName() + ".dustInfo Start!!");

        List<DustDTO> rList = dustService.getDustList();

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

        log.info(this.getClass().getName() + ".getSnowList 컨트롤러 끝!");

        return "/dust/dustList";
    }
}
