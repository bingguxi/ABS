package kopo.poly.controller;

import kopo.poly.persistance.mapper.IDisasterMsgMapper;
import kopo.poly.service.IWordAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller("WordController")
public class WordController {

    private final IDisasterMsgMapper disasterMsgMapper;

//    private Logger log = Logger.getLogger(String.valueOf(this.getClass()));

    @Resource(name = "WordAnalysisService")
    private IWordAnalysisService wordAnalysisService;

    @RequestMapping(value = "word/analysis")
    @ResponseBody
    public Map<String, Integer> analysis() throws Exception {
        log.info(this.getClass().getName() + ".inputForm !");

        // DB에서 값을 가져오도록 수정
        String text = getTextFromDatabase();

        // 분석 수행
        Map<String, Integer> wordFrequencies = wordAnalysisService.doWordAnalysis(text);

        if (wordFrequencies == null) {
            wordFrequencies = new HashMap<>();
        }

        return wordFrequencies;
    }

    // DB에서 텍스트를 가져오는 메서드
    private String getTextFromDatabase() {
        try {
            // 실제로는 여기에 DB에서 데이터를 가져오는 코드를 추가해야 합니다.
            // disasterMsgMapper를 이용하여 원하는 데이터를 가져오세요.
            // 아래는 간단한 예시입니다.
            return String.valueOf(disasterMsgMapper.getDisasterMsg());
        } catch (Exception e) {
            log.error("Error while fetching text from the database", e);
            return null;
        }
    }
}
