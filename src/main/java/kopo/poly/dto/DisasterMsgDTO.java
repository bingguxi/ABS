package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DisasterMsgDTO {

    /** 일단은 재난문자 API URL 파라미터 값인데
     * 바인딩 하는 경우 필요없기 때문에 추후에 수정할 수도 있음
     */
    private String pageNo;
    private String numOfRows;
    private String type;

    // 재난문자 결과 값
    private List<DisasterMsgResultDTO> msgList;
}
