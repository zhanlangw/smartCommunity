package cn.bytecloud.smartCommunity.version.service;

import cn.bytecloud.smartCommunity.version.entity.VersionType;

public interface VersionService {
    Object getInfo(VersionType type);
}
