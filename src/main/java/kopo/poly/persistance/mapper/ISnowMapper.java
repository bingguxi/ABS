package kopo.poly.persistance.mapper;

import kopo.poly.dto.SnowDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISnowMapper {

    // 수집된 적설 정보 조회
    List<SnowDTO> getSnowList() throws Exception;

}
