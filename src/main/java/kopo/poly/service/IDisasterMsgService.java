package kopo.poly.service;

import kopo.poly.dto.DisasterMsgResultDTO;

import java.util.List;

public interface IDisasterMsgService {

    // cctv api호출하여 cctv 결과 받아오기
    List<DisasterMsgResultDTO> getDisasterMsg() throws Exception;

    // CCTV 정보 가져오기
    List<DisasterMsgResultDTO> getDisasterMsgList() throws Exception;

    // 상세보기
    DisasterMsgResultDTO getDisasterMsgInfo(DisasterMsgResultDTO pDTO, boolean type) throws Exception;

    List<DisasterMsgResultDTO> getLast3DisasterMsgList() throws Exception;
}
