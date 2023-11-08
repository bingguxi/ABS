package kopo.poly.persistance.mapper;

import kopo.poly.dto.SnowDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISnowMapper {


    // 수집된 적설 정보 DB에 등록
    int insertSnowInfo(SnowDTO pDTO) throws Exception;

    // DB에 저장된 적설 정보 삭제하기
    int deleteSnowInfo() throws Exception;

    // 수집된 적설 정보 조회
    List<SnowDTO> getSnowInfo() throws Exception;

}
