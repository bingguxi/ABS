package kopo.poly.service;

import kopo.poly.dto.FireDTO;

import java.util.List;

public interface IFireService {

    // 수집된 산불 정보 조회하기
    List<FireDTO> getFireInfo() throws Exception;

}
