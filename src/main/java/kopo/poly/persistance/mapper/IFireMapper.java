package kopo.poly.persistance.mapper;

import kopo.poly.dto.FireDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFireMapper {

    // 수집된 산불 정보 조회하기
    List<FireDTO> getFireList() throws Exception;

}
