package kopo.poly.persistance.mapper;

import kopo.poly.dto.FireDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFireMapper {

    // 수집된 산불 정보 DB에 등록하기
    int insertFireInfo(FireDTO pDTO) throws Exception;

    // DB에 저장된 산불 정보 삭제하기
    int deleteFireInfo(FireDTO pDTO) throws Exception;

    // 수집된 산불 정보 조회하기
    List<FireDTO> getFireInfo(FireDTO pDTO) throws Exception;

}
