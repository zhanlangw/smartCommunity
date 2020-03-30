package cn.bytecloud.smartCommunity.process.dao;

import cn.bytecloud.smartCommunity.base.dao.BaseDao;
import cn.bytecloud.smartCommunity.base.dto.PageModel;
import cn.bytecloud.smartCommunity.process.dto.ProcessPageDto;
import cn.bytecloud.smartCommunity.process.dto.UpdProcessStyleDto;
import cn.bytecloud.smartCommunity.process.entity.Process;
import cn.bytecloud.smartCommunity.util.EmptyUtil;
import cn.bytecloud.smartCommunity.util.StringUtil;
import cn.bytecloud.smartCommunity.util.UUIDUtil;
import cn.bytecloud.smartCommunity.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cn.bytecloud.smartCommunity.constant.ModelConstant.*;

@Repository
public class ProcessDaoImpl extends BaseDao<Process> implements ProcessDao {
    @Autowired
    private MongoTemplate template;
    @Autowired
    private ProcessRepository repository;

    @Override
    public Process save(Process process) {
        if (EmptyUtil.isEmpty(process.getId())) {
            process.setId(UUIDUtil.getUUID());
            process.setCreateTime(System.currentTimeMillis());
            process.setCreatorId(UserUtil.getUserId());
        } else {
            Process old = repository.findOne(process.getId());
            process.setCreateTime(old.getCreateTime());
            process.setCreatorId(old.getCreatorId());
        }
        process.setUpdateTime(System.currentTimeMillis());
        repository.save(process);
        return process;
    }

    /**
     * 修改流程图
     *
     * @param dto
     */
    @Override
    public void updateStyle(UpdProcessStyleDto dto) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ID).is(dto.getId()));
        Update update = new Update();
        update.set(PROCESS_STYLE, dto.getStyle());
        template.updateFirst(query, update, Process.class);
    }

    /**
     * 列表
     *
     * @param dto
     * @return
     */
    @Override
    public PageModel<Process> list(ProcessPageDto dto) {
        Query query = new Query();
        if (EmptyUtil.isNotEmpty(dto.getName())) {
            query.addCriteria(Criteria.where(PROCESS_NAME).regex(StringUtil.translat(dto.getName())));
        }
        query.with(new Sort(Sort.Direction.DESC, CREATE_TIME));
        return pageList(query, dto, CREATE_TIME);
    }

    @Override
    public Process findFirst() {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, CREATE_TIME));
        query.limit(1);
        return template.find(query,Process.class).get(0);
    }
}
