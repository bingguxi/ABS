package kopo.poly.service;

import kopo.poly.dto.DustDTO;

import java.util.List;

public interface IDustService {

    // 수집된 황사 내용 조회
    List<DustDTO> getDustList() throws Exception;

}
