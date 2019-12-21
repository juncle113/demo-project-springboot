package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.ProjectReportDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.ProjectReportDetailVO;
import com.cpto.dapp.pojo.vo.ProjectReportVO;

import java.sql.Timestamp;
import java.util.List;

/**
 * 项目报告Service
 *
 * @author sunli
 * @date 2019/03/13
 */
public interface ProjectReportService extends BaseService {

    /**
     * 取得项目报告信息列表
     *
     * @return 项目报告信息列表
     */
    List<ProjectReportVO> findProjectReportList();

    /**
     * 取得某项目报告信息列表
     *
     * @param searchTime 查询时间
     * @param page       当前页数
     * @param pageSize   每页条数
     * @param projectId  项目id
     * @return 项目报告信息列表
     */
    PageVO<ProjectReportDetailVO> findProjectReportListByProject(Timestamp searchTime, Integer page, Integer pageSize, Long projectId);

    /**
     * 根据报告id取得项目报告详情
     *
     * @param reportId 报告id
     * @return 报告详情
     */
    ProjectReportDetailVO findProjectReport(Long reportId);

    /**
     * 新增项目报告
     *
     * @param admin            管理员
     * @param projectId        项目id
     * @param projectReportDTO 项目报告信息
     */
    void addProjectReport(ManagerAdmin admin, Long projectId, ProjectReportDTO projectReportDTO);

    /**
     * 修改项目报告
     *
     * @param admin            管理员
     * @param projectId        项目id
     * @param reportId         报告id
     * @param projectReportDTO 项目报告信息
     */
    void modifyProjectReport(ManagerAdmin admin, Long projectId, Long reportId, ProjectReportDTO projectReportDTO);

    /**
     * 删除项目报告
     *
     * @param admin     管理员
     * @param projectId 项目id
     * @param reportId  报告id
     */
    void removeProjectReport(ManagerAdmin admin, Long projectId, Long reportId);
}