package kopo.poly.persistance.mapper;

import kopo.poly.dto.CctvResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICctvMapper {

    // DB에 저장된 cctv 조회하기
    List<CctvResultDTO> getCctv() throws Exception;
}
