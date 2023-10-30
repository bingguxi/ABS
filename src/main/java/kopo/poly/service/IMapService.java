package kopo.poly.service;

import kopo.poly.dto.MapApiDTO;

import java.util.List;

public interface IMapService {
    List<MapApiDTO> shelterMap()throws Exception;
}
