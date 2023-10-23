package kopo.poly.controller;

import kopo.poly.dto.CommunityDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.ICommunityService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/community")
@RequiredArgsConstructor
@Controller
public class CommunityController {

    private final ICommunityService communityService;


     //커뮤니티 리스트 보여주기
    @GetMapping(value = "communityList")
    public String communityList(HttpSession session, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".communityList Start!");

        // TODO: 2023-10-23 session_user_id 변경하기
        session.setAttribute("SESSION_USER_ID", "USER01");

        // 커뮤니티 리스트 조회하기
        List<CommunityDTO> rList = Optional.ofNullable(communityService.getCommunityList()).orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        modelMap.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".communityList End!");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "community/communityList";
    }

    // 커뮤니티 작성 페이지 이동
    @GetMapping(value = "communityReg")
    public String communityReg(){

        log.info(this.getClass().getName() + ".communityReg Start!");

        log.info(this.getClass().getName() + ".communityReg End!");

        return "community/communityReg";
    }

    // 커뮤니티 글 등록
    @ResponseBody
    @PostMapping(value = "communityInsert")
    public MsgDTO communityInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".communityInsert Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            // 로그인된 사용자 아이디 가져오기
            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String communityYn = CmmUtil.nvl(request.getParameter("communityYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            log.info("session user_id : " + userId);
            log.info("title : " + title);
            log.info("communityYn : " + communityYn);
            log.info("contents : " + contents);

            // 데이터 저장하기 위해 DTO에 저장하기
            CommunityDTO pDTO = new CommunityDTO();
            pDTO.setUserId(userId);
            pDTO.setTitle(title);
            pDTO.setCommunityYn(communityYn);
            pDTO.setContents(contents);

            // 게시글 등록하기 위한 비즈니스 로직 호출
            communityService.insertCommunityInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
//            msg = "등록되었습니다.";
        } catch (Exception e) {

            // 저장이 실패되면 사용자에게 보여줄 메시지
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".communityInsert End!");
        }
        return dto;
    }
}
