package kopo.poly.service;

import kopo.poly.dto.CctvResultDTO;

import java.util.List;

public interface ICctvService {

    String url = "https://openapi.its.go.kr:9443/cctvInfo";

    // cctv api호출하여 cctv 결과 받아오기
    List<CctvResultDTO> getCctv() throws Exception;

}