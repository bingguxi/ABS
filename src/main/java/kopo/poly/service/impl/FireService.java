package kopo.poly.service.impl;

import kopo.poly.dto.FireDTO;
import kopo.poly.persistance.mapper.IFireMapper;
import kopo.poly.service.IFireService;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FireService implements IFireService {

    private final IFireMapper fireMapper;


    /*private final WebDriver webDriver; // 셀리니움 사용을 위한 webDriver 주입
    private String url = "http://forestfire.nifos.go.kr/menu.action?menuNum=1"; // 크롤링할 사이트 URL

    private static String WEB_DRIVER_ID = "webdriver.chrome.driver"; // property 키
    private static String WEB_DRIVER_PATH = "/chromedriver_win64/chromedriver.exe"; // property 값*/


    //생성자로 webDriver 초기설정 + 위스키 정보들을 담을 whiskDatas 초기화


    private WebDriver webDriver;
    private final String url = "http://forestfire.nifos.go.kr/menu.action?menuNum=1";

    public void crawlWebsite() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "/chromedriver-win32/chromedriver.exe");

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("headless");
        options.addArguments("--whitelisted-ips=127.0.0.1"); // GPT 추가
        options.addArguments("--remote-allow-origins=*"); // 구글링해서 추가


        this.webDriver = new ChromeDriver(options);

        webDriver.get("http://forestfire.nifos.go.kr/menu.action?menuNum=1");

        // 웹 페이지가 로딩될 때까지 기다릴 수 있는 코드 추가 (예: WebDriverWait 사용)
        Thread.sleep(2000);

        // 크롤링할 데이터를 찾는 코드 예시
        WebElement table = webDriver.findElement(By.cssSelector("ul.table"));
        for (WebElement row : table.findElements(By.cssSelector("li.tr"))) {
            WebElement locationElement = row.findElement(By.cssSelector("p.cell.w33"));
            WebElement gradeElement = row.findElement(By.cssSelector("img"));
            WebElement indexElement = row.findElement(By.cssSelector("p.cell.w33"));

            String location = locationElement.getText();
            String grade = gradeElement.getAttribute("alt");
            String index = indexElement.getText();

            System.out.println("지역: " + location);
            System.out.println("등급: " + grade);
            System.out.println("지수: " + index);
            System.out.println();

            // 웹 드라이버 종료
            webDriver.quit();
        }
    }


    // TODO 이거 살리기
   /* @Transactional
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


        List<FireDTO> fireInfoList;


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

        }*/


    // TODO 이건 Jsoup이라 나중에 지워도 됨
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

        }

        log.info(this.getClass().getName() + ".insertFireInfo 서비스 끝!");

        return res;
    }*/

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
