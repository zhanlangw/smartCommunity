package cn.bytecloud.smartCommunity.version.service;

import cn.bytecloud.smartCommunity.version.dao.VersionRepository;
import cn.bytecloud.smartCommunity.version.dao.Versiondao;
import cn.bytecloud.smartCommunity.version.entity.Version;
import cn.bytecloud.smartCommunity.version.entity.VersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VersionServiceImpl implements VersionService {
    @Autowired
    private Versiondao dao;

    @Autowired
    private VersionRepository repository;

    @Override
    public Version getInfo(VersionType type) {
        return repository.findOneByType(type);
    }
}
