package kopo.poly.persistance.mapper;

import kopo.poly.dto.DustDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDustMapper {

    // 수집된 황사 정보 DB에 등록

    int insertDustInfo(DustDTO pDTO) throws Exception;

    // DB에 저장된 황사 정보 삭제
    int deleteDustInfo() throws Exception;

    // 황사 정보 수정
    int updateDustInfo(DustDTO pDTO) throws Exception;

    // 수집된 황사 정보 조회
    List<DustDTO> getDustInfo() throws Exception;
}
