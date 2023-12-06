package kopo.poly.persistance.mapper;

import kopo.poly.dto.DustDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDustMapper {

    // 수집된 황사 정보 조회
    List<DustDTO> getDustList() throws Exception;

}
