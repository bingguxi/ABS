package kopo.poly.service;

import kopo.poly.dto.CctvResultDTO;

import java.util.List;

public interface ICctvService {

    String url = "https://openapi.its.go.kr:9443/cctvInfo";

    // 수집된 cctv 정보 DB에 등록
    void insertCctvInfo(CctvResultDTO pDTO) throws Exception;

    // DB에 저장된 cctv 조회하기
    List<CctvResultDTO> getCctv() throws Exception;

}