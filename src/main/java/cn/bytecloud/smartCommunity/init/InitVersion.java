package cn.bytecloud.smartCommunity.init;

import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.version.dao.VersionRepository;
import cn.bytecloud.smartCommunity.version.entity.Version;
import cn.bytecloud.smartCommunity.version.entity.VersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitVersion {
    @Autowired
    private VersionRepository repository;

    @PostConstruct
    public void initVersion(){
        if (repository.findOneByType(VersionType.IOS) == null) {
            Version version = new Version();
            version.setId(UUIDUtil.getUUID());
            version.setType(VersionType.IOS);
            version.setNum("1");
            version.setUrl("https://apps.apple.com/app/id1485197505");
            repository.save(version);
        }
        if (repository.findOneByType(VersionType.ANDROID) == null) {
            Version version = new Version();
            version.setId(UUIDUtil.getUUID());
            version.setType(VersionType.ANDROID);
            version.setNum("1");
            version.setUrl("/uploads/smartCommunity.apk");
            repository.save(version);
        }
    }
}
