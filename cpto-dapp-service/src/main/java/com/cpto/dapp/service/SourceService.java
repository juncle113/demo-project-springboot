package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.SourceDTO;
import com.cpto.dapp.pojo.vo.SourceVO;

import java.util.List;

/**
 * 资源Service
 *
 * @author sunli
 * @date 2019/01/31
 */
public interface SourceService extends BaseService {

    /**
     * 查询资源信息列表
     *
     * @param relationType 关联类型
     * @param relationId   关联id
     * @return 资源信息列表
     */
    List<SourceVO> findSourceList(Integer relationType, Long relationId);

    /**
     * 新增资源
     *
     * @param admin        管理员
     * @param addSourceDTO 资源信息
     */
    void addSource(ManagerAdmin admin, SourceDTO addSourceDTO);

    /**
     * 删除资源
     *
     * @param admin    管理员
     * @param sourceId 资源id
     */
    void removeSource(ManagerAdmin admin, Long sourceId);
}