package kopo.poly.service.impl;

import kopo.poly.dto.DustDTO;
import kopo.poly.persistance.mapper.IDustMapper;
import kopo.poly.service.IDustService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DustService implements IDustService {

    private final IDustMapper dustMapper;

    @Override
    public List<DustDTO> getDustInfo() throws Exception {

        log.info(this.getClass().getName() + ".getDustInfo Start!");
        log.info(this.getClass().getName() + ".getDustInfo End!");

        return dustMapper.getDustInfo();
    }
}