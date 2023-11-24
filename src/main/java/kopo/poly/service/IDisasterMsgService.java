package kopo.poly.service;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.DisasterMsgResultDTO;

import java.util.List;

public interface IDisasterMsgService {

    String apiURL = "https://apis.data.go.kr/1741000/DisasterMsg3/getDisasterMsg1List";

//    String url = "https://openapi.its.go.kr:9443/cctvInfo";


    // cctv api호출하여 cctv 결과 받아오기
    List<DisasterMsgResultDTO> getDisasterMsg() throws Exception;



    // CCTV 정보 가져오기
//    String getCctvData(double lat, double lng);
}
