package kopo.poly.persistance.mapper;

import kopo.poly.dto.EarthquakeLiveDTO;
import kopo.poly.dto.EarthquakeResultDTO;
import org.apache.ibatis.annotations.Mapper;
import org.bouncycastle.pqc.crypto.ExchangePair;

import java.util.List;

@Mapper
public interface IEarthquakeMapper {

    // 수집된 지진 과거 정보 DB에 등록
    void insertEarthquakeInfo(EarthquakeResultDTO pDTO) throws Exception;

    // 과거 지진 정보 조회하기
    List<EarthquakeResultDTO> getEarthquakeList() throws Exception;

    // 수집된 지진 실시간 정보 조회
    List<EarthquakeLiveDTO> getEarthquakeLiveInfo() throws Exception;

}
