package kopo.poly.service.impl;

import kopo.poly.dto.CommunityDTO;
import kopo.poly.persistance.mapper.ICommunityMapper;
import kopo.poly.service.ICommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityService implements ICommunityService {

    private final ICommunityMapper CommunityMapper;

    @Override
    public List<CommunityDTO> getCommunityList() throws Exception {

        log.info(this.getClass().getName() + ".getCommunityList Start!!");

        return CommunityMapper.getCommunityList();
    }

    @Transactional
    @Override
    public CommunityDTO getCommunityInfo(CommunityDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getCommunityInfo Start!!");

        // 상세보기할 때마다, 조회수 증가하기(수정보기는 제외)
        if (type) {
            log.info("Update ReadCNT");
            CommunityMapper.updateCommunityReadCnt(pDTO);
        }
        return CommunityMapper.getCommunityInfo(pDTO);
    }

    @Transactional
    @Override
    public void insertCommunityInfo(CommunityDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".insertCommunityInfo Start!!");

        CommunityMapper.insertCommunityInfo(pDTO);

    }

    @Transactional
    @Override
    public void updateCommunityInfo(CommunityDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateCommunityInfo Start!!");

        CommunityMapper.updateCommunityInfo(pDTO);

    }

    @Transactional
    @Override
    public void deleteCommunityInfo(CommunityDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteCommunityInfo Start!!");

        CommunityMapper.deleteCommunityInfo(pDTO);

    }
}
