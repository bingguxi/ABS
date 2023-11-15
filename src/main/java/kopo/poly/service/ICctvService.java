package kopo.poly.service;

import kopo.poly.dto.CctvResultDTO;

import java.util.List;

public interface ICctvService {

    String apiURL = "https://openapi.its.go.kr:9443/cctvInfo?apiKey=test&type=ex&cctvType=1&minX=127.100000&maxX=128.890000&minY=34.100000&maxY=39.100000&getType=json";

    String url = "https://openapi.its.go.kr:9443/cctvInfo";


    // cctv api호출하여 cctv 결과 받아오기
    List<CctvResultDTO> getCctv() throws Exception;



    // CCTV 정보 가져오기
    String getCctvData(double lat, double lng);
}
