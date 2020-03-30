package cn.bytecloud.smartCommunity.path.dao;

import cn.bytecloud.smartCommunity.node.entity.Node;
import cn.bytecloud.smartCommunity.path.dto.PathItemDto;
import cn.bytecloud.smartCommunity.path.entity.Path;

public interface PathDao {
    Path save(Path path);
}
