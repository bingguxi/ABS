package kopo.poly.service.impl;


import kopo.poly.dto.DisasterMsgResultDTO;
import kopo.poly.persistance.mapper.IDisasterMsgMapper;
import kopo.poly.service.IDisasterMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class DisasterMsgService implements IDisasterMsgService {

    private final IDisasterMsgMapper disasterMsgMapper;

    @Override
    public List<DisasterMsgResultDTO> getDisasterMsg() throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsg Start!!");

        List<DisasterMsgResultDTO> rList = disasterMsgMapper.getDisasterMsg();

        log.info(this.getClass().getName() + ".getDisasterMsg End!!");

        return rList;
    }

    @Override
    public List<DisasterMsgResultDTO> getDisasterMsgList() throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsgList start!");

        return disasterMsgMapper.getDisasterMsgList();
    }

    @Transactional
    @Override
    public DisasterMsgResultDTO getDisasterMsgInfo(DisasterMsgResultDTO pDTO, boolean type) throws Exception {

        log.info(this.getClass().getName() + ".getDisasterMsgInfo start!");

        return disasterMsgMapper.getDisasterMsgInfo(pDTO);
    }
    @Override
    public List<DisasterMsgResultDTO> getLast3DisasterMsgList() throws Exception {

        log.info(this.getClass().getName() + ".getLast3DisasterMsgList start!");

        List<DisasterMsgResultDTO> rList = Optional.ofNullable(getDisasterMsgList())
                .orElseGet(ArrayList::new);

        // 'md101Sn' 속성을 기준으로 리스트를 오름차순으로 정렬
        rList.sort(Comparator.comparing(DisasterMsgResultDTO::getMd101Sn));

        // 리스트 크기가 3보다 작으면 모든 아이템을 가져옵니다.
        return rList.size() < 3 ? rList : rList.subList(rList.size() - 3, rList.size());
    }
}
