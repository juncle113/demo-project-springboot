package com.cpto.dapp.repository;

import com.cpto.dapp.domain.Source;

import java.util.List;

/**
 * 资源Repository
 *
 * @author sunli
 * @date 2019/01/31
 */
public interface SourceRepository extends BaseRepository<Source, Long> {

    /**
     * 查询资源信息列表
     *
     * @param relationType 关联类型
     * @param relationId   关联id
     * @return 资源信息列表
     */
    List<Source> findByRelationTypeAndRelationId(Integer relationType, Long relationId);
}