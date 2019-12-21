package com.cpto.dapp.repository;


import com.cpto.dapp.domain.LogUserIncome;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 个人收益记录Repository
 *
 * @author sunli
 * @date 2019/02/12
 */
public interface LogUserIncomeRepository extends BaseRepository<LogUserIncome, Long>, JpaSpecificationExecutor<LogUserIncome> {

    /**
     * 查询用户的各项目收益列表
     *
     * @param userId     用户id
     * @param statusList 状态
     * @return 收益列表
     */
    @Query(nativeQuery = true, value = "select log_user_income.project_id, project.no as project_no, project.name_zh as project_name_zh, project.name_en as project_name_en, order_id, ifnull(sum(log_user_income.amount), 0) as amount_sum " +
            "from log_user_income, project where log_user_income.user_id = :userId and log_user_income.status in (:statusList) and log_user_income.project_id = project.id group by log_user_income.project_id, project.no, project.name_zh, project.name_en, order_id order by project_id desc")
    List<Map<String, Object>> sumByUserIdGroupByProjectId(Long userId, List<Integer> statusList);

    /**
     * 根据状态查询收益信息
     *
     * @param statusList 状态
     * @return 收益信息
     */
    List<LogUserIncome> findByStatusInOrderByIdDesc(List<Integer> statusList);
}