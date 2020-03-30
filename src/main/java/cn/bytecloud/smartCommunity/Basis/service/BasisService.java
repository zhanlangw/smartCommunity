package cn.bytecloud.smartCommunity.Basis.service;

import cn.bytecloud.smartCommunity.Basis.entity.Basis;

public interface BasisService {
    Basis findFirst();

    Basis save(Basis basis);

    Object item();
}
