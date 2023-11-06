package kopo.poly.service.impl;

import kopo.poly.dto.FireDTO;
import kopo.poly.persistance.mapper.IFireMapper;
import kopo.poly.service.IFireService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.WebDriverUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class FireService implements IFireService {

    private final IFireMapper fireMapper;

    @Transactional
    @Override
    public int insertFireInfo() throws Exception {

        log.info(this.getClass().getName() + ".insertFireInfo 서비스 시작!");

        String collectTime = DateUtil.getDateTime("yyyyMMdd"); // 수집날짜 = 오늘날짜

        FireDTO pDTO = new FireDTO();
        pDTO.setCollectTime(collectTime); // 수집날짜 담기

        // 기존에 수집된 산불 정보 데이터 삭제하기
        fireMapper.deleteFireInfo(pDTO);

        pDTO = null; // 기존 저장된 산불 정보 삭제 후, pDTO 비우기

        int res = 0;


        WebDriver driver = new ChromeDriver();
        List<WebElement> webElementList = new ArrayList<>();
        String url = "http://forestfire.nifos.go.kr/menu.action?menuNum=1";
        String query = "#id";

        if (!ObjectUtils.isEmpty(driver)) {

            driver.get(url);
            driver.getTitle();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            webElementList = driver.findElements(By.xpath("//*[@id=\"gradeList\"]/ul"));
            log.info("webElementList : " + webElementList);

            WebElement parentElement = webElementList.get(0);
            log.info("parentElement : " + parentElement);

            List<WebElement> childElement = parentElement.findElements(By.className("tr"));
            log.info("childElement : " + childElement);

            log.info("childElement.get(0).getText() : " + childElement.get(0).getText());

            driver.quit();

        }


/*
        // 산불 정보 가져올 사이트 주소
        String url = "http://forestfire.nifos.go.kr/menu.action?menuNum=1";

        // JSOUP 라이브러리를 통해 사이트에 접속되면, 그 사이트의 전체 HTML 소스를 저장할 변수
        Document doc = null;

        // 사이트 접속
        doc = Jsoup.connect(url).get();
        log.info("사이트 접속");

        Element secondTabContent = doc.select("div.tab-content").get(1);
        log.info("secondTabContent : " + secondTabContent);

        Iterator<Element> iterator = elements.iterator();

        while (iterator.hasNext()) {

            log.info("반복문 시작이요오옹");

            Element item = iterator.next();
            String city = item.select("p.cell.w33").first().text(); // 지역은 p 태그 중 첫 번째로 나타나는 텍스트입니다.
            String grade = item.select("img").attr("alt"); // 등급은 img 태그의 alt 속성 값입니다.
            String score = item.select("p.cell.w33").last().text(); // 지수는 p 태그 중 마지막으로 나타나는 텍스트입니다.

            log.info("city : " + city);
            log.info("grade : " + grade);
            log.info("score : " + score);

            // 수집된 데이터를 DTO에 저장하고 DB에 저장하기
            pDTO = new FireDTO();

            // 수집시간을 기본키(pk)로 사용
            pDTO.setCollectTime(collectTime);

            // 산불 지역, 등급, 지수를 DTO에 저장
            pDTO.setCity(city.trim());
            pDTO.setGrade(grade.trim());
            pDTO.setScore(score.trim());

            res += fireMapper.insertFireInfo(pDTO);

        } */

        log.info(this.getClass().getName() + ".insertFireInfo 서비스 끝!");

        return res;
    }

    @Override
    public List<FireDTO> getFireInfo() throws Exception {

        log.info(this.getClass().getName() + ".getFireInfo 서비스 시작!");

        String collectTime = DateUtil.getDateTime("yyyyMMdd"); // 수집날짜 = 오늘날짜

        FireDTO pDTO = new FireDTO();
        pDTO.setCollectTime(collectTime);

        // DB에서 조회하기
        List<FireDTO> rList = fireMapper.getFireInfo(pDTO);

        log.info(this.getClass().getName() + ".getFireInfo 서비스 끝!");

        return rList;
    }
}
