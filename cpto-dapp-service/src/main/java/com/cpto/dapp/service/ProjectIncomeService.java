package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectIncomeDTO;
import com.cpto.dapp.pojo.vo.ProjectIncomeLogVO;

import java.util.List;

/**
 * 收益Service
 *
 * @author sunli
 * @date 2019/02/22
 */
public interface ProjectIncomeService extends BaseService {

    /**
     * 发放项目收益
     *
     * @param admin            管理员
     * @param projectId        项目id
     * @param projectIncomeDTO 放项目收益信息
     */
    void grantProjectIncome(ManagerAdmin admin, Long projectId, ProjectIncomeDTO projectIncomeDTO);

    /**
     * 取得项目收益记录列表
     *
     * @param projectId 项目id
     * @return 项目收益记录列表
     */
    List<ProjectIncomeLogVO> findProjectIncomeLogList(Long projectId);

    /**
     * 撤销项目收益
     *
     * @param admin     管理员
     * @param projectId 项目id
     * @param incomeId  收益id
     */
    void cancelProjectIncome(ManagerAdmin admin, Long projectId, Long incomeId);
}