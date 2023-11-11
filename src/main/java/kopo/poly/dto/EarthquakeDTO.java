package kopo.poly.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
public class EarthquakeDTO {

    private String frDate;
    private String laDate;
    private String cntDiv;
    private String orderTy;

    private List<EarthquakeDTO> info;

    public List<EarthquakeDTO> getNDTO() throws Exception {

        return info;
    }

}
