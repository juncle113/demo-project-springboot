package com.cpto.dapp.repository;


import com.cpto.dapp.domain.LogProjectIncome;

import java.util.List;

/**
 * 项目收益记录Repository
 *
 * @author sunli
 * @date 2019/02/12
 */
public interface LogProjectIncomeRepository extends BaseRepository<LogProjectIncome, Long> {

    /**
     * 查询某个项目收益记录列表
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 收益记录列表
     */
    List<LogProjectIncome> findByProjectIdAndStatusInOrderByIdDesc(Long projectId, List<Integer> statusList);
}