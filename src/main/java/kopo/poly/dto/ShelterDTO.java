package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShelterDTO {

    private String acmdfcltySn; // 시설일련번호
    private String vtAcmdfcltyNm; // 시설명
    private String xcord; // 위도
    private String ycord; // 경도

}
