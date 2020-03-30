package cn.bytecloud.smartCommunity.init;

import cn.bytecloud.smartCommunity.bigCategory.dao.BigCategoryRepository;
import cn.bytecloud.smartCommunity.bigCategory.entity.BigCategory;
import cn.bytecloud.smartCommunity.blacklist.dao.BlacklistRepository;
import cn.bytecloud.smartCommunity.blacklist.entity.Blacklist;
import cn.bytecloud.smartCommunity.constant.SystemConstant;
import cn.bytecloud.smartCommunity.smallCategory.dao.SmallCategoryRepository;
import cn.bytecloud.smartCommunity.smallCategory.entity.SmallCategory;
import cn.bytecloud.smartCommunity.smallCategory.entity.TimeType;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.work.entity.WorkType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static cn.bytecloud.smartCommunity.constant.SystemConstant.*;


@Component
public class InitBlacklist {
    @Autowired
    private BlacklistRepository blacklistRepository;
    @Autowired
    private BigCategoryRepository bigCategoryRepository;

    @Autowired
    private SmallCategoryRepository smallCategoryRepository;

    @PostConstruct
    public void init() {
        SmallCategory smallCategory = smallCategoryRepository.findOneByName(INIT_SMALL_CATEGORY_NAME);
        if (null == smallCategory) {
            BigCategory bigCategory = bigCategoryRepository.findOneByName(INIT_BIG_CATEGORY_NAME);
            if (null == bigCategory) {
                bigCategory = new BigCategory();
                bigCategory.setId(UUIDUtil.getUUID());
                bigCategory.setNum(0);
                bigCategory.setAbbre("jm");
                bigCategory.setName(INIT_BIG_CATEGORY_NAME);
                bigCategory.setCreatorId(INIT_SYSTEM_ID);
                bigCategory.setCreateTime(System.currentTimeMillis());
                bigCategoryRepository.save(bigCategory);
            }

            smallCategory = new SmallCategory();
            smallCategory.setId(UUIDUtil.getUUID());
            smallCategory.setName(INIT_SMALL_CATEGORY_NAME);
            smallCategory.setBigCategoryId(bigCategory.getId());
            smallCategory.setAbbre("rr");
            smallCategory.setTimeType(TimeType.DAY);
            smallCategory.setTime(1L);
            smallCategory.setWorkType(WorkType.ORDINARY);
            smallCategory.setCreatorId(INIT_SYSTEM_ID);
            smallCategory.setCreateTime(System.currentTimeMillis());
            smallCategoryRepository.save(smallCategory);

        }
        for (String modelId : SystemConstant.MODEL_IDS) {
            Blacklist model = blacklistRepository.findOne(modelId);
            if (model == null) {
                model = new Blacklist();
                model.setId(modelId);
                model.setName(MODEL_NAME);
                model.setTypeId(smallCategory.getId());
                model.setCreateTime(System.currentTimeMillis());
                model.setCreatorId(INIT_SYSTEM_ID);
                blacklistRepository.save(model);
            }
        }
    }

    @PostConstruct
    public void initFence() {
        for (String string : FENCE_NAME) {
            String[] items = string.split("-");
            BigCategory bigCategory = bigCategoryRepository.findOneByName(items[0]);
            if (null == bigCategory) {
                bigCategory = new BigCategory();
                bigCategory.setId(UUIDUtil.getUUID());
                bigCategory.setNum(0);
                bigCategory.setAbbre("jm");
                bigCategory.setName(items[0]);
                bigCategory.setCreatorId(INIT_SYSTEM_ID);
                bigCategory.setCreateTime(System.currentTimeMillis());
                bigCategoryRepository.save(bigCategory);
            }
            SmallCategory smallCategory = smallCategoryRepository.findOneByName(items[1]);
            if (null == smallCategory) {
                smallCategory = new SmallCategory();
                smallCategory.setId(UUIDUtil.getUUID());
                smallCategory.setName(items[1]);
                smallCategory.setBigCategoryId(bigCategory.getId());
                smallCategory.setAbbre("rr");
                smallCategory.setTimeType(TimeType.DAY);
                smallCategory.setTime(1L);
                smallCategory.setWorkType(WorkType.ORDINARY);
                smallCategory.setCreatorId(INIT_SYSTEM_ID);
                smallCategory.setCreateTime(System.currentTimeMillis());
                smallCategoryRepository.save(smallCategory);
            }
            for (int j = 2;j<items.length;j++) {
                String item = items[j];
                String modelId = item.split(",")[1];
                String name = item.split(",")[0];
                Blacklist model = blacklistRepository.findOne(modelId);
                if (model == null) {
                    model = new Blacklist();
                    model.setId(modelId);
                    model.setName(name);
                    model.setTypeId(smallCategory.getId());
                    model.setCreateTime(System.currentTimeMillis());
                    model.setCreatorId(INIT_SYSTEM_ID);
                    blacklistRepository.save(model);
                }
            }
        }
    }
}
