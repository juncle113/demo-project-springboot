package com.cpto.dapp.repository;

import com.cpto.dapp.domain.ProjectReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 项目报告Repository
 *
 * @author sunli
 * @date 2019/02/28
 */
public interface ProjectReportRepository extends BaseRepository<ProjectReport, Long>, JpaSpecificationExecutor<ProjectReport> {

    /**
     * 取得所有项目报告信息列表
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @param pageable   分页信息
     * @return 项目报告信息列表
     */
    Page<ProjectReport> findByProjectIdAndStatusInOrderByX1MonthDesc(Long projectId, List<Integer> statusList, Pageable pageable);

    /**
     * 检查项目报告是否存在
     *
     * @param projectId 项目id
     * @param x1Month   报告月份
     * @return 是否存在
     */
    boolean existsByProjectIdAndX1Month(Long projectId, String x1Month);
}