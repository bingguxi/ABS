package kopo.poly.service.impl;

import kopo.poly.dto.FireDTO;
import kopo.poly.persistance.mapper.IFireMapper;
import kopo.poly.service.IFireService;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FireService implements IFireService {

    private final IFireMapper fireMapper;

    @Override
    public List<FireDTO> getFireList() throws Exception {

        log.info(this.getClass().getName() + ".getFireList 서비스 시작!");
        log.info(this.getClass().getName() + ".getFireList 서비스 끝!");

        return fireMapper.getFireList();
    }
}
