package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Source;
import com.cpto.dapp.enums.RelationTypeEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.pojo.dto.SourceDTO;
import com.cpto.dapp.pojo.vo.SourceVO;
import com.cpto.dapp.repository.SourceRepository;
import com.cpto.dapp.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源ServiceImpl
 *
 * @author sunli
 * @date 2019/01/31
 */
@Service
public class SourceServiceImpl extends BaseServiceImpl implements SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    /**
     * 查询资源信息列表
     *
     * @param relationType 关联类型
     * @param relationId   关联id
     * @return 资源信息列表
     */
    @Override
    public List<SourceVO> findSourceList(Integer relationType, Long relationId) {

        List<SourceVO> sourceVOList = new ArrayList<>();

        List<Source> sourceList = sourceRepository.findByRelationTypeAndRelationId(relationType, relationId);
        for (Source source : sourceList) {
            sourceVOList.add(editSourceVO(source));
        }

        return sourceVOList;
    }

    /**
     * 新增资源
     *
     * @param admin        管理员
     * @param addSourceDTO 新增的资源信息
     */
    @Override
    public void addSource(ManagerAdmin admin, SourceDTO addSourceDTO) {

        /* 1.设置资源信息 */
        Source source = new Source();

        source.setRelationId(addSourceDTO.getRelationId());
        source.setRelationType(addSourceDTO.getRelationType());
        source.setName(addSourceDTO.getName());
        source.setUrl(addSourceDTO.getUrl());
        source.setDeleted(false);
        source.setStatus(StatusEnum.VALID.getCode());
        source.setCreatedBy(admin);
        source.setCreatedTime(DateUtil.now());

        sourceRepository.save(source);

        /* 2.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_SOURCE, addSourceDTO.getRelationId());
    }

    /**
     * 删除资源
     *
     * @param admin    管理员
     * @param sourceId 资源id
     */
    @Override
    public void removeSource(ManagerAdmin admin, Long sourceId) {

        /* 1.获取删除信息 */
        Source source = sourceRepository.findNotNullById(sourceId);

        /* 2.删除资源 */
        source.setDeleted(true);
        source.setStatus(StatusEnum.INVALID.getCode());
        source.setModifiedBy(admin);
        source.setModifiedTime(DateUtil.now());
        sourceRepository.save(source);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.DELETE_SOURCE, source.getId());
    }

    /**
     * 编辑资源VO
     *
     * @param source 资源
     * @return 资源VO
     */
    private SourceVO editSourceVO(Source source) {

        SourceVO sourceVO = new SourceVO();

        sourceVO.setId(source.getId());
        sourceVO.setRelationId(source.getRelationId());
        sourceVO.setRelationType(source.getRelationType());
        sourceVO.setRelationTypeName(RelationTypeEnum.getNameByCode(source.getRelationType()));
        sourceVO.setName(source.getName());
        sourceVO.setUrl(source.getUrl());
        sourceVO.setCreatedBy(ObjectUtil.isNotEmpty(source.getCreatedBy()) ? source.getCreatedBy().getName() : null);
        sourceVO.setCreatedTime(source.getCreatedTime());
        sourceVO.setModifiedBy(ObjectUtil.isNotEmpty(source.getModifiedBy()) ? source.getModifiedBy().getName() : null);
        sourceVO.setModifiedTime(source.getModifiedTime());

        return sourceVO;
    }
}