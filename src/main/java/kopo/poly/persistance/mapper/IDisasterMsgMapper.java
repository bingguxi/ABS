package kopo.poly.persistance.mapper;

import kopo.poly.dto.CctvResultDTO;
import kopo.poly.dto.DisasterMsgResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDisasterMsgMapper {

    // 수집된 cctv 정보 DB에 등록
    int insertDisasterMsgInfo(DisasterMsgResultDTO pDTO) throws Exception;

    // DB에 저장된 cctv 정보 삭제하기
    int deleteDisasterMsgInfo() throws Exception;
    List<DisasterMsgResultDTO> getDisasterMsg() throws Exception;
}