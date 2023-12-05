package kopo.poly.persistance.mapper;

import kopo.poly.dto.DisasterMsgResultDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDisasterMsgMapper {

    List<DisasterMsgResultDTO> getDisasterMsg() throws Exception;

    List<DisasterMsgResultDTO> getDisasterMsgList() throws Exception;

    //게시판 상세보기
    DisasterMsgResultDTO getDisasterMsgInfo(DisasterMsgResultDTO pDTO) throws Exception;
}
