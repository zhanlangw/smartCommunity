package cn.bytecloud.smartCommunity.work.dto;

import cn.bytecloud.smartCommunity.exception.ByteException;
import cn.bytecloud.smartCommunity.address.service.AddressService;
import cn.bytecloud.smartCommunity.process.service.ProcessService;
import cn.bytecloud.smartCommunity.smallCategory.service.SmallCategoryService;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.MD5Util;
import cn.bytecloud.smartCommunity.util.SpringUtils;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.entity.Work;
import cn.bytecloud.smartCommunity.work.entity.WorkSource;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
public class AddWorkDto {
    @NotEmpty
    @Length(max = 40)
    private String title;

    @NotEmpty
    @Length(max = 100)
    private String address;

    @NotNull
    private WorkSource source;

    @NotEmpty
    private String typeId;

    private WorkType workType;

    private String desc;

    private String latitude;

    private String longitude;

    private String unitId;

    private String blacklistId;

    private String deviceId;

    private String eventId;


    //处理前照片地质集合
    @Size(min = 1,max = 5)
    @NotNull
    private List<String> beforeImagePaths = new ArrayList<>();

    //处理前视屏地质集合
    private List<String> beforeVideoPaths;

    public Work toData() throws ByteException {
        Work work = new Work();
        work.setBlacklistId(blacklistId);
        work.setDeviceId(deviceId);
        work.setTitle(title);
        work.setProcessId(SpringUtils.getBean(ProcessService.class).findFirst().getId());
        work.setAddress(address);
        work.setSource(source);
        work.setTypeId(typeId);
        work.setWorkType(SpringUtils.getBean(SmallCategoryService.class).findById(typeId).getWorkType());
        if (source != WorkSource.SYSTEM) {
            work.setAcceptFlag(true);
        }
        if (source == WorkSource.APP) {
            if (EmptyUtil.isEmpty(latitude)) {
                throw new ByteException("经度不可以为空");
            }
            if (EmptyUtil.isEmpty(longitude)) {
                throw new ByteException("维度不能为空!");
            }
            work.setLatitude(latitude);
            work.setLongitude(longitude);
            //根据经纬度或者对应的组织机构信息
            work.setUnitId(SpringUtils.getBean(AddressService.class).findUnitIdByLatitudeAndLongitude(latitude, longitude));
        } else {
            if (EmptyUtil.isEmpty(unitId)) {
                throw new ByteException("组织机构为空!");
            }
            work.setLatitude(latitude);
            work.setLongitude(longitude);
            work.setUnitId(unitId);
        }
        work.setAcceptFlag(source != WorkSource.SYSTEM);
        work.setBeforeImagePaths(beforeImagePaths);
        work.setBeforeVideoPaths(beforeVideoPaths);
        work.setDesc(desc);
        work.setEventId(eventId);
        return work;
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5("JTQ)OKMnji9"));
    }
}
