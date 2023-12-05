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

}
