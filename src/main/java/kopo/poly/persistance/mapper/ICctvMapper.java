package kopo.poly.persistance.mapper;

import kopo.poly.dto.CctvResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICctvMapper {


    // 수집된 cctv 정보 DB에 등록
    int insertCctvInfo(CctvResultDTO pDTO) throws Exception;

    // DB에 저장된 cctv 정보 삭제하기
    int deleteCctvInfo() throws Exception;
    List<CctvResultDTO> getCctv() throws Exception;
}