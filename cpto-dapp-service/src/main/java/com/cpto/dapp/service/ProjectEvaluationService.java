package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectEvaluationDTO;
import com.cpto.dapp.pojo.vo.ProjectEvaluationVO;

import java.util.List;

/**
 * 项目评价Service
 *
 * @author sunli
 * @date 2019/01/13
 */
public interface ProjectEvaluationService extends BaseService {

    /**
     * 取得项目评估信息列表
     *
     * @return 项目评估信息列表
     */
    List<ProjectEvaluationVO> findProjectEvaluationList();

    /**
     * 取得项目评估信息
     *
     * @param projectId 项目id
     * @return 项目评估信息
     */
    ProjectEvaluationVO findProjectEvaluation(Long projectId);

    /**
     * 修改项目评估信息
     *
     * @param admin                管理员
     * @param projectId            项目id
     * @param projectEvaluationDTO 项目评估信息
     */
    void modifyProjectEvaluation(ManagerAdmin admin, Long projectId, ProjectEvaluationDTO projectEvaluationDTO);
}