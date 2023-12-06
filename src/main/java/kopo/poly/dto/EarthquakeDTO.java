package kopo.poly.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class EarthquakeDTO {

    /* 지진 API URL에 필요한 파라미터 값을 위한 DTO인데 기간을 지정하는 방식으로 하고 있어서 추후에 수정할수도 있음 */
    /* 아마 추후에 정리하면서 삭제할 가능성이 농후함 없어도 잘 돌아감 */

    private String frDate;
    private String laDate;
    private String cntDiv;
    private String orderTy;

    private List<EarthquakeDTO> info;

    public List<EarthquakeDTO> getNDTO() throws Exception {

        return info;
    }

}
