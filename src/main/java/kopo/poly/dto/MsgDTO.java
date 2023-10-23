package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // 결과값을 JSON 형태로 받을 DTO는 해당 어노테이션을 무조건 달아라
public class MsgDTO {

    private int result; // 성공 : 1 / 실패 : 그 외
    private String msg; // 메시지
}
