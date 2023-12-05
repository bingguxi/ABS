package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.CctvResultDTO;
import kopo.poly.persistance.mapper.ICctvMapper;
import kopo.poly.service.ICctvService;
import kopo.poly.util.NetworkUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CctvService implements ICctvService {

    private final ICctvMapper cctvMapper;

    @Value("${cctvResult.api.key}")
    private String apiKey;

    @Override
    public List<CctvResultDTO> getCctv() throws Exception {

        log.info(this.getClass().getName() + ".getCctv Start!");
        log.info(this.getClass().getName() + ".getCctv End!");

        return cctvMapper.getCctv();
    }

}
