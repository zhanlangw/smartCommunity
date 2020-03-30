package cn.bytecloud.smartCommunity.station.service;

import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.station.dao.StationDao;
import cn.bytecloud.smartCommunity.station.dao.StationRepository;
import cn.bytecloud.smartCommunity.station.dto.AddStationDto;
import cn.bytecloud.smartCommunity.station.dto.StationItemDto;
import cn.bytecloud.smartCommunity.station.dto.StationPageDto;
import cn.bytecloud.smartCommunity.station.dto.UpdStationDto;
import cn.bytecloud.smartCommunity.station.entity.Station;
import cn.bytecloud.smartCommunity.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationDao dao;
    @Autowired
    private StationRepository repository;

    @Override
    public StationItemDto add(AddStationDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).size() > 0) {
            throw new ByteException("名字已存在");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), StationItemDto.class);
    }

    @Override
    public StationItemDto item(String id) {
        return EntityUtil.entityToDto(repository.findOne(id), StationItemDto.class);
    }

    @Override
    public StationItemDto upd(UpdStationDto dto) throws ByteException {
        if (repository.findByName(dto.getName()).stream().anyMatch(station -> !station.getId().equals(dto.getId()))) {
            throw new ByteException("名字已存在");
        }
        return EntityUtil.entityToDto(dao.save(dto.toData()), StationItemDto.class);
    }

    @Override
    public void del(String ids) {
        for (String id : ids.split(",")) {
            repository.delete(id);
        }
    }

    @Override
    public Object list(StationPageDto dto) {
        PageModel pageModel = dao.list(dto);
        pageModel.setValue(EntityUtil.entityListToDtoList(pageModel.getValue(), StationItemDto.class));
        return pageModel;
    }

    @Override
    public Object mapList() {
        return EntityUtil.entityListToDtoList(repository.findAll(), StationItemDto.class);
    }
}
