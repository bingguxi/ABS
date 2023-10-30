package kopo.poly.controller;

import kopo.poly.dto.*;
import kopo.poly.service.ICommentService;
import kopo.poly.service.ICommunityService;
import kopo.poly.service.ILoginService;
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
    private final ICommentService commentService;

    /** 커뮤니티 리스트 보여주기 */
    @GetMapping(value = "communityList")
    public String communityList(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".communityList Start!");

        String userId = (String) session.getAttribute("SS_USER_ID"); // 아이디 세션값

        log.info("세션에 저장 되어있는 아이디('SS_USER_ID') : " + userId);

        CommunityDTO pDTO = new CommunityDTO();
        pDTO.setUserId(userId);

        CommunityDTO rDTO = Optional.ofNullable(communityService.getCommunityInfo(pDTO, true))
                .orElseGet(CommunityDTO::new);

        model.addAttribute("rDTO", rDTO);

        // 커뮤니티 리스트 조회하기
        List<CommunityDTO> rList = Optional.ofNullable(communityService.getCommunityList()).orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        log.info(this.getClass().getName() + ".communityList End!");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "community/communityList";
    }

    /** 커뮤니티 작성 페이지 이동*/
    @GetMapping(value = "communityReg")
    public String communityReg(){

        log.info(this.getClass().getName() + ".communityReg Start!");

        log.info(this.getClass().getName() + ".communityReg End!");

        return "community/communityReg";
    }

    /** 커뮤니티 글 등록 */
    @ResponseBody
    @PostMapping(value = "communityInsert")
    public MsgDTO communityInsert(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".communityInsert Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            // 로그인된 사용자 아이디 가져오기
            String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            log.info("ss_user_id : " + userId);
            log.info("title : " + title);
            log.info("contents : " + contents);

            // 데이터 저장하기 위해 DTO에 저장하기
            CommunityDTO pDTO = new CommunityDTO();
            pDTO.setUserId(userId);
            pDTO.setTitle(title);
            pDTO.setContents(contents);

            // 게시글 등록하기 위한 비즈니스 로직 호출
            communityService.insertCommunityInfo(pDTO);

            // 저장이 완료되면 사용자에게 보여줄 메시지
            msg = "등록되었습니다.";

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

    /** 게시판 상세보기 */
    @GetMapping(value = "communityInfo")
    public String communityInfo(HttpSession session, HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".communityInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글 번호(PK)

        log.info("nSeq : " + nSeq);

        // 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
        CommunityDTO pDTO = new CommunityDTO();
        pDTO.setCommunitySeq(nSeq);

        // 공지사항 상세정보 가져오기
        CommunityDTO rDTO = Optional.ofNullable(communityService.getCommunityInfo(pDTO, true)).orElseGet(CommunityDTO::new);

        CommentDTO cDTO = new CommentDTO();
        cDTO.setCommunitySeq(nSeq);

        // 댓글 리스트 조회하기
        List<CommentDTO> rList = Optional.ofNullable(commentService.getCommentList(cDTO)).orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        modelMap.addAttribute("rList", rList);

        for (CommentDTO dto : rList) {
            log.info("commentSeq" + dto.getCommentSeq());
        }

        // 조회된 리스트 결과값 넣어주기
        modelMap.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".communityInfo End!");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/community/communityInfo";
    }

    /** 게시판 수정을 위한 페이지*/
    @GetMapping(value = "communityEditInfo")
    public String communityEditInfo(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".communityEditInfo Start!");

        String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 공지글 번호

        log.info("nSeq : " + nSeq);

        // 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
        CommunityDTO pDTO = new CommunityDTO();
        pDTO.setCommunitySeq(nSeq);

        CommunityDTO rDTO = Optional.ofNullable(communityService.getCommunityInfo(pDTO, false)).orElseGet(CommunityDTO::new);

        // 조회된 리스트 결과값 넣어주기
        modelMap.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".communityEditInfo End!");

        // 함수 처리가 끝나고 보여줄 html 파일명
        return "/community/communityEditInfo";
    }

    /** 게시판 글 수정 */
    @ResponseBody
    @PostMapping(value = "communityUpdate")
    public MsgDTO communityUpdate(HttpSession session, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".communityUpdate Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID")); // 아이디
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)
            String title = CmmUtil.nvl(request.getParameter("title")); // 제목
//            String communityYn = CmmUtil.nvl(request.getParameter("communityYn")); // 공지글 여부
            String contents = CmmUtil.nvl(request.getParameter("contents")); // 내용

            log.info("userId : " + userId);
            log.info("nSeq : " + nSeq);
            log.info("title : " + title);
//            log.info("communityYn : " + communityYn);
            log.info("contents : " + contents);

            CommunityDTO pDTO = new CommunityDTO();
            pDTO.setUserId(userId);
            pDTO.setCommunitySeq(nSeq);
            pDTO.setTitle(title);
//            pDTO.setCommunityYn(communityYn);
            pDTO.setContents(contents);

            // 게시글 수정하기 DB
            communityService.updateCommunityInfo(pDTO);

            msg = "수정되었습니다.";
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            // 결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".communityUpdate End!");
        }
        return dto;
    }

    /** 커뮤니티 글 삭제 */
    @ResponseBody
    @PostMapping(value = "communityDelete")
    public MsgDTO communityDelete(HttpServletRequest request) {

        log.info(this.getClass().getName() + ".communityDelete Start!");

        String msg = ""; // 메시지 내용
        MsgDTO dto = null; // 결과 메시지 구조

        try {
            String nSeq = CmmUtil.nvl(request.getParameter("nSeq")); // 글번호(PK)

            log.info("nSeq : " + nSeq);

            CommunityDTO pDTO = new CommunityDTO();
            pDTO.setCommunitySeq(nSeq);

            // 게시글 삭제하기 DB
            communityService.deleteCommunityInfo(pDTO);

            msg = "삭제되었습니다.";

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            //결과 메시지 전달하기
            dto = new MsgDTO();
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".communityDelete End!");
        }
        return dto;

    }


}
