package kopo.poly.persistance.mapper;

import kopo.poly.dto.ShelterDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IShelterMapper {

    // 대피소 정보 파싱 후 저장하기
    void insertShelter(ShelterDTO pDTO) throws Exception;

    // 대피소 정보 조회 리스트
    List<ShelterDTO> getShelterList() throws Exception;

    // 대피소 정보 조회하기
    ShelterDTO getShelter() throws Exception;

}
