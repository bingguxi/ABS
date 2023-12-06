package kopo.poly.persistance.mapper;

import kopo.poly.dto.TyphoonDTO;
import kopo.poly.dto.TyphoonLiveDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ITyphoonMapper {

    // 수집된 태풍 과거 정보 DB에 등록
    void insertTyphoonInfo(TyphoonDTO pDTO) throws Exception;

    // DB에 저장된 태풍 과거 정보 삭제하기
    void deleteTyphoonInfo() throws Exception;

    // 수집된 태풍 과거 정보 조회
    List<TyphoonDTO> getTyphoonList() throws Exception;

    // 수집된 태풍 실시간 정보 조회
    List<TyphoonLiveDTO> getTyphoonLiveList() throws Exception;

}
