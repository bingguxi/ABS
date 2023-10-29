package kopo.poly.service.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.ILoginMapper;
import kopo.poly.service.ILoginService;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import kopo.poly.util.RandomCodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService implements ILoginService {

    private final ILoginMapper loginMapper;

    private final IMailService mailService;


    // 일반 로그인
    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getLogin Start!");

        UserInfoDTO rDTO = Optional.ofNullable(loginMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        log.info("rDTO.toString() : " + rDTO.toString());

        if (CmmUtil.nvl(rDTO.getUserId()).length() > 0) {

            log.info("로그인 성공");

        }

        log.info(this.getClass().getName() + ".getLogin End!");

        return rDTO;
    }


    // 카카오 로그인
    public String getAccessToken(String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=6706621d8f90ce8e40e411e21d348bf7");
            sb.append("&redirect_uri=http://localhost:11000/login/kakaoLogin");
            sb.append("&code=" + authorize_code);
            sb.append("&client_secret=yeijSkDR2vOf3tmNZVFZpN0Nmk2aLFH5");
            bw.write(sb.toString());
            bw.flush();

            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);

            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);


            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            // import com.google.gson.JsonParser; <== 위에 요거 해줘야 빨간줄 안 뜸!!!
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token : " + access_Token);
            log.info("refresh_token : " + refresh_Token);

            br.close();
            bw.close();

            log.info("access_token : " + access_Token);
            log.info("refresh_token : " + refresh_Token);

            br.close();
            bw.close();

        } catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return access_Token;
    }


    public HashMap<String, Object> getUserInfo (String access_Token) {

        //    요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            //    요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String email = kakao_account.getAsJsonObject().get("email").getAsString();

            userInfo.put("email", email);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userInfo;
    }


    public void kakaoLogout(String access_Token) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public UserInfoDTO findIdOrPasswordProc(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc Start!");

        UserInfoDTO rDTO = loginMapper.getUserId(pDTO);

        log.info(this.getClass().getName() + ".searchUserIdOrPasswordProc End!");

        return rDTO;
    }

    @Override
    public int pwCode(UserInfoDTO pDTO) throws Exception {

        log.info(getClass().getName() + ".pwCode Start!");

        int res = 0;

        try {
            // DB 이메일이 존재하는지 SQL 쿼리 실행
            // SQL 쿼리에 COUNT()를 사용하기 때문에 반드시 조회 결과는 존재함
            UserInfoDTO rDTO = loginMapper.getUserExists(pDTO);
            log.info("rDTO.userExists : " + rDTO.getExistsYn());

            if (rDTO.getExistsYn().equals("Y")) {
                log.info("새 비밀번호 발급 시작!");

                res = 1;

                // 임시 비밀번호 생성하기
                String newPwd = CmmUtil.nvl(RandomCodeUtil.createKey());

                // 임시 비밀번호 설정 및 업데이트
                UserInfoDTO nDTO = new UserInfoDTO();

                nDTO.setUserId(pDTO.getUserId());
                nDTO.setUserName(pDTO.getUserName());
                nDTO.setEmail(EncryptUtil.decAES128CBC(pDTO.getEmail()));
                nDTO.setPassword(EncryptUtil.encHashSHA256(newPwd));

                loginMapper.updatePassword(nDTO);

                log.info("임시 비밀번호로 업데이트 성공");

                // 인증번호 발송 로직
                MailDTO dto = new MailDTO();

                dto.setTitle("임시 비밀번호 발송 메일");
                dto.setContents("회원님의 임시 비밀번호는 " + newPwd + " 입니다.\n로그인 후 반드시 비밀번호를 변경해주세요!");
                dto.setToWho(EncryptUtil.decAES128CBC(pDTO.getEmail()));

                mailService.doSendMail(dto); // 이메일 발송 서비스 호출

                dto = null;

            } else {
                log.info("업데이트 실패");
            }

        } catch (
                Exception e) {
            res = 0;
            log.info("[ERR0R] " + this.getClass().getName() + " doSendMail : " + e);

        }

        log.info(this.getClass().getName() + ".pwCode End!");

        return res;

    }
}
