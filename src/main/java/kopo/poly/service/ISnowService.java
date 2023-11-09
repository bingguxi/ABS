package kopo.poly.service;

import kopo.poly.dto.MapApiDTO;
import kopo.poly.dto.SnowDTO;

import java.util.List;

public interface ISnowService {

    // 수집된 적설 내용 조회하기
    List<SnowDTO> getSnowInfo() throws Exception;
}
