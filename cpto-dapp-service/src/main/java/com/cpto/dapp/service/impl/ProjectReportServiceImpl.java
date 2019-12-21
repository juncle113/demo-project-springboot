package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Project;
import com.cpto.dapp.domain.ProjectReport;
import com.cpto.dapp.enums.ProjectStatusEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataExistedException;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.ProjectReportDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.ProjectReportDetailVO;
import com.cpto.dapp.pojo.vo.ProjectReportVO;
import com.cpto.dapp.repository.ProjectReportRepository;
import com.cpto.dapp.service.ProjectReportService;
import com.cpto.dapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 项目ServiceImpl
 *
 * @author sunli
 * @date 2019/01/13
 */
@Service
public class ProjectReportServiceImpl extends BaseServiceImpl implements ProjectReportService {

    @Autowired
    private ProjectReportRepository projectReportRepository;

    @Autowired
    private ProjectService projectService;

    /**
     * 取得所有正在运营的项目报告信息
     *
     * @return 项目报告信息
     */
    @Override
    public List<ProjectReportVO> findProjectReportList() {

        /* 1.查询正在运营的项目 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatusEnum.OPERATING.getCode());
        List<Project> projectList = projectService.findProjectByStatus(statusList);

        List<ProjectReportVO> projectReportVOList = new ArrayList<>();
        ProjectReportVO projectReportVO;

        List<ProjectReportDetailVO> projectReportDetailVOList;
        Page<ProjectReport> projectReportPage;

        /* 2.设置分页 */
        Pageable pageable = PageRequest.of(0, 12);

        for (Project project : projectList) {

            projectReportVO = new ProjectReportVO();
            projectReportVO.setProjectId(project.getId());
            projectReportVO.setProjectName(LanguageUtil.getTextByLanguage(project.getNameZh(), project.getNameEn()));
            projectReportDetailVOList = new ArrayList<>();

            /* 3.查询运营项目的报告信息 */
            List<Integer> projectReportStatusList = new ArrayList<>();
            projectReportStatusList.add(StatusEnum.VALID.getCode());

            projectReportPage = projectReportRepository.findByProjectIdAndStatusInOrderByX1MonthDesc(project.getId(), projectReportStatusList, pageable);
            for (ProjectReport projectReport : projectReportPage) {
                projectReportDetailVOList.add(editProjectReportDetailVO(projectReport));
            }

            ObjectUtil.reverse(projectReportDetailVOList);

            projectReportVO.setDetails(projectReportDetailVOList);
            projectReportVOList.add(projectReportVO);
        }

        return projectReportVOList;
    }

    /**
     * 取得某项目报告信息列表
     *
     * @param searchTime 查询时间
     * @param page       当前页数
     * @param pageSize   每页条数
     * @param projectId  项目id
     * @return 项目报告信息列表
     */
    @Override
    public PageVO<ProjectReportDetailVO> findProjectReportListByProject(Timestamp searchTime, Integer page, Integer pageSize, Long projectId) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<ProjectReport> specification = getSQLWhere(searchTime, projectId);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<ProjectReport> projectReportPage = projectReportRepository.findAll(specification, pageable);

        List<ProjectReportDetailVO> projectReportVOList = new ArrayList<>();
        for (ProjectReport projectReport : projectReportPage) {
            projectReportVOList.add(editProjectReportDetailVO(projectReport));
        }

        PageVO<ProjectReportDetailVO> projectReportPageVO = new PageVO();
        projectReportPageVO.setRows(projectReportVOList);
        projectReportPageVO.setTotal(projectReportPage.getTotalElements());
        projectReportPageVO.setTotalPage(projectReportPage.getTotalPages());
        projectReportPageVO.setHasNext(projectReportPage.hasNext());
        projectReportPageVO.setSearchTime(searchTime);

        return projectReportPageVO;
    }

    /**
     * 根据报告id取得项目报告详情
     *
     * @param reportId 报告id
     * @return 报告详情
     */
    @Override
    public ProjectReportDetailVO findProjectReport(Long reportId) {
        ProjectReport projectReport = projectReportRepository.findNotNullById(reportId);
        return editProjectReportDetailVO(projectReport);
    }

    @Override
    public void addProjectReport(ManagerAdmin admin, Long projectId, ProjectReportDTO projectReportDTO) {

        /* 1.检查重复数据 */
        boolean exists = projectReportRepository.existsByProjectIdAndX1Month(projectId, projectReportDTO.getX1Month());
        if (exists) {
            throw new DataExistedException();
        }

        /* 2.设置新增公告详情 */
        ProjectReport projectReport = new ProjectReport();

        projectReport.setProjectId(projectId);
        projectReport.setTitleZh(projectReportDTO.getTitleZh());
        projectReport.setTitleEn(projectReportDTO.getTitleEn());
        projectReport.setReturnMemoZh(projectReportDTO.getReturnMemoZh());
        projectReport.setReturnMemoEn(projectReportDTO.getReturnMemoEn());
        projectReport.setX1Month(projectReportDTO.getX1Month());
        projectReport.setY1USD(projectReportDTO.getY1USD());
        projectReport.setY2cpto(projectReportDTO.getY2CPTO());
        projectReport.setRemark(projectReportDTO.getRemark());
        projectReport.setStatus(projectReportDTO.getStatus());
        projectReport.setDeleted(false);
        projectReport.setCreatedBy(admin);
        projectReport.setCreatedTime(DateUtil.now());
        projectReport = projectReportRepository.save(projectReport);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_REPORT, projectReport.getId());
    }

    /**
     * 修改项目报告
     *
     * @param admin            管理员
     * @param projectId        项目id
     * @param reportId         报告id
     * @param projectReportDTO 项目报告信息
     */
    @Override
    public void modifyProjectReport(ManagerAdmin admin, Long projectId, Long reportId, ProjectReportDTO projectReportDTO) {

        /* 1.取得被修改的内容 */
        ProjectReport projectReport = projectReportRepository.findNotNullById(reportId);

        /* 2.检查信息 */
        /* 2.1.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(projectReportDTO.getModifiedTime(), projectReport.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 2.2.检查项目id */
        if (ObjectUtil.notEquals(projectReport.getProjectId(), projectId)) {
            throw new DataNotFoundException();
        }

        /* 2.3.检查重复数据 */
        boolean exists = projectReportRepository.existsByProjectIdAndX1Month(projectId, projectReportDTO.getX1Month());
        if (exists) {
            throw new DataExistedException();
        }

        /* 3.设置修改内容 */
        projectReport.setTitleZh(projectReportDTO.getTitleZh());
        projectReport.setTitleEn(projectReportDTO.getTitleEn());
        projectReport.setReturnMemoZh(projectReportDTO.getReturnMemoZh());
        projectReport.setReturnMemoEn(projectReportDTO.getReturnMemoEn());
        projectReport.setX1Month(projectReportDTO.getX1Month());
        projectReport.setY1USD(projectReportDTO.getY1USD());
        projectReport.setY2cpto(projectReportDTO.getY2CPTO());
        projectReport.setRemark(projectReportDTO.getRemark());
        projectReport.setStatus(projectReportDTO.getStatus());
        projectReport.setModifiedBy(admin);
        projectReport.setModifiedTime(DateUtil.now());

        projectReportRepository.save(projectReport);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_NOTICE, projectReport.getId());
    }

    /**
     * 删除项目报告
     *
     * @param admin     管理员
     * @param projectId 项目id
     * @param reportId  报告id
     */
    @Override
    public void removeProjectReport(ManagerAdmin admin, Long projectId, Long reportId) {

        /* 1.检查删除信息 */
        ProjectReport projectReport = projectReportRepository.findNotNullById(reportId);
        if (ObjectUtil.notEquals(projectReport.getProjectId(), projectId)) {
            throw new DataNotFoundException();
        }

        /* 2.删除项目报告 */
        projectReport.setDeleted(true);
        projectReport.setStatus(StatusEnum.INVALID.getCode());
        projectReport.setModifiedBy(admin);
        projectReport.setModifiedTime(DateUtil.now());
        projectReportRepository.save(projectReport);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.DELETE_REPORT, projectReport.getId());
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime 查询时间
     * @param projectId  项目id
     * @return 动态查询条件
     */
    private Specification<ProjectReport> getSQLWhere(Timestamp searchTime, Long projectId) {

        Specification<ProjectReport> specification = new Specification<ProjectReport>() {
            @Override
            public Predicate toPredicate(Root<ProjectReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询公告类型
                if (ObjectUtil.isNotEmpty(projectId)) {
                    predicatesList.add(cb.equal(root.get("projectId"), projectId));
                }

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 编辑项目报告VO
     *
     * @param projectReport 项目报告
     * @return 项目报告VO
     */
    private ProjectReportDetailVO editProjectReportDetailVO(ProjectReport projectReport) {

        ProjectReportDetailVO projectReportDetailVO = new ProjectReportDetailVO();

        projectReportDetailVO.setId(projectReport.getId());
        projectReportDetailVO.setProjectId(projectReport.getProjectId());
        projectReportDetailVO.setTitle(LanguageUtil.getTextByLanguage(projectReport.getTitleZh(), projectReport.getTitleEn()));
        projectReportDetailVO.setTitleZh(projectReport.getTitleZh());
        projectReportDetailVO.setTitleEn(projectReport.getTitleEn());
        projectReportDetailVO.setReturnMemo(LanguageUtil.getTextByLanguage(projectReport.getReturnMemoZh(), projectReport.getReturnMemoEn()));
        projectReportDetailVO.setReturnMemoZh(projectReport.getReturnMemoZh());
        projectReportDetailVO.setReturnMemoEn(projectReport.getReturnMemoEn());
        projectReportDetailVO.setX1Month(projectReport.getX1Month());
        projectReportDetailVO.setY1USD(projectReport.getY1USD());
        projectReportDetailVO.setY2CPTO(projectReport.getY2cpto());
        projectReportDetailVO.setRemark(projectReport.getRemark());
        projectReportDetailVO.setStatus(projectReport.getStatus());
        projectReportDetailVO.setStatusName(StatusEnum.getNameByCode(projectReport.getStatus()));
        projectReportDetailVO.setCreatedBy(ObjectUtil.isNotEmpty(projectReport.getCreatedBy()) ? projectReport.getCreatedBy().getName() : null);
        projectReportDetailVO.setCreatedTime(projectReport.getCreatedTime());
        projectReportDetailVO.setModifiedBy(ObjectUtil.isNotEmpty(projectReport.getCreatedBy()) ? projectReport.getCreatedBy().getName() : null);
        projectReportDetailVO.setModifiedTime(projectReport.getModifiedTime());

        return projectReportDetailVO;
    }
}