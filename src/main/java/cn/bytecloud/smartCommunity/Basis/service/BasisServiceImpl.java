package cn.bytecloud.smartCommunity.Basis.service;

import cn.bytecloud.smartCommunity.Basis.dao.BasisDao;
import cn.bytecloud.smartCommunity.Basis.dao.BasisRepository;
import cn.bytecloud.smartCommunity.Basis.entity.Basis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasisServiceImpl implements BasisService {
    @Autowired
    private BasisRepository repository;
    @Autowired
    private BasisDao dao;

    @Override
    public Basis findFirst() {
       return dao.findFirst();
    }

    @Override
    public Basis save(Basis basis) {
        repository.save(basis);
        return basis;
    }

    @Override
    public Object item() {
        Basis basis = dao.findFirst();
        if (null == basis.getSoundNum()) {
            basis.setSoundNum(1);
        }
        return dao.findFirst();
    }
}
