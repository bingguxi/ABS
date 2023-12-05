package kopo.poly.service;

import kopo.poly.dto.CctvResultDTO;

import java.util.List;

public interface ICctvService {

    String url = "https://openapi.its.go.kr:9443/cctvInfo";

    // DB에 저장된 cctv 조회하기
    List<CctvResultDTO> getCctv() throws Exception;

}