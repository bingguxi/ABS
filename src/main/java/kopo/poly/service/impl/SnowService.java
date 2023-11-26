package kopo.poly.service.impl;

import kopo.poly.dto.SnowDTO;
import kopo.poly.persistance.mapper.ISnowMapper;
import kopo.poly.service.ISnowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnowService implements ISnowService {

    private final ISnowMapper snowMapper;

    @Override
    public List<SnowDTO> getSnowInfo() throws Exception {

        log.info(this.getClass().getName() + ".getSnowInfo Start!");
        log.info(this.getClass().getName() + ".getSnowInfo End!");

        return snowMapper.getSnowInfo();
    }
}
