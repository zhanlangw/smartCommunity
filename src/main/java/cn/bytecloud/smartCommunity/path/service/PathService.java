package cn.bytecloud.smartCommunity.path.service;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.path.dto.AddPathDto;
import cn.bytecloud.smartCommunity.path.dto.PathItemDto;
import cn.bytecloud.smartCommunity.path.entity.Path;

import java.util.List;

public interface PathService {
    PathItemDto add(AddPathDto dto) throws ByteException;

    PathItemDto item(String id);

    void del(String id);

    List<Path> findByProcessId(String id);

    Path findById(String afterPathId);

    List<Path> findPathByNodeId(String currNodeId, String id);
}
