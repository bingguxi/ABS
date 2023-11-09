package kopo.poly.service;

import kopo.poly.dto.FireDTO;

import java.util.List;

public interface IFireService {

    void crawlWebsite() throws InterruptedException;

    // 웹 상에서 산불 정보 가져오기
    // int insertFireInfo() throws Exception;

    // 수집된 산불 정보 조회하기
    List<FireDTO> getFireInfo() throws Exception;

}
