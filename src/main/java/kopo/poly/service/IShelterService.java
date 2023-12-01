package kopo.poly.service;

import kopo.poly.dto.ShelterDTO;

import java.util.List;

public interface IShelterService {

    // 대피소 정보 파싱 후 저장하기
    void insertShelter() throws Exception;

    // 대피소 정보 조회 리스트
    List<ShelterDTO> getShelterList() throws Exception;

    // 대피소 정보 조회하기
    ShelterDTO getShelter() throws Exception;

}
