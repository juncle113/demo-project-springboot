package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.*;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Project;
import com.cpto.dapp.domain.ProjectEvaluation;
import com.cpto.dapp.enums.ErrorEnum;
import com.cpto.dapp.enums.ProjectStatusEnum;
import com.cpto.dapp.enums.RelationTypeEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.ProjectDTO;
import com.cpto.dapp.pojo.vo.ProjectDetailVO;
import com.cpto.dapp.pojo.vo.ProjectSummaryVO;
import com.cpto.dapp.pojo.vo.ProjectVO;
import com.cpto.dapp.pojo.vo.SourceVO;
import com.cpto.dapp.repository.ProjectEvaluationRepository;
import com.cpto.dapp.repository.ProjectRepository;
import com.cpto.dapp.service.OrderService;
import com.cpto.dapp.service.ProjectService;
import com.cpto.dapp.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目ServiceImpl
 *
 * @author sunli
 * @date 2019/01/13
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectEvaluationRepository projectEvaluationRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SourceService sourceService;

    /**
     * 取得全部项目信息
     *
     * @return 项目列表
     */
    @Override
    public List<ProjectVO> findProjectList() {

        /* 1.取得数据 */
        List<Project> projectList = projectRepository.findByOrderByIdDesc();

        /* 2.编辑返回的数据格式 */
        List<ProjectVO> projectVOList = new ArrayList<>();
        for (Project project : projectList) {
            projectVOList.add(editProjectVO(project));
        }

        return projectVOList;
    }

    /**
     * 取得项目详细信息
     *
     * @param projectId 项目id
     * @return 项目详细信息
     */
    @Override
    public ProjectDetailVO findProject(Long projectId) {
        Project project = projectRepository.findNotNullById(projectId);
        return editProjectDetailVO(project);
    }

    /**
     * 根据状态取得项目信息
     *
     * @param statusList 状态
     * @return 项目信息
     */
    @Override
    public List<Project> findProjectByStatus(List<Integer> statusList) {
        return projectRepository.findByStatusInOrderByIdDesc(statusList);
    }

    /**
     * 取得所有正在募集的项目概要信息
     *
     * @return 项目概要信息
     */
    @Override
    public List<ProjectSummaryVO> findProjectSummaryList() {

        List<ProjectSummaryVO> projectSummaryVOList = new ArrayList<>();

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatusEnum.START.getCode());
        statusList.add(ProjectStatusEnum.SUCCESS_1.getCode());

        List<Project> projectList = findProjectByStatus(statusList);
        for (Project project : projectList) {
            projectSummaryVOList.add(editProjectSummaryVO(project));
        }

        return projectSummaryVOList;
    }

    /**
     * 根据项目编号查询项目信息
     *
     * @param no 项目编号
     * @return 项目信息
     */
    @Override
    public ProjectDetailVO findProjectByNo(String no) {

        /* 1.取得项目信息 */
        Project project = projectRepository.findByNo(no);
        if (ObjectUtil.isEmpty(project)) {
            throw new DataNotFoundException();
        }

        /* 2.设置项目信息 */
        return editProjectDetailVO(project);
    }

    /**
     * 新增项目
     *
     * @param admin      管理员
     * @param projectDTO 新增的项目详情
     */
    @Override
    public void addProject(ManagerAdmin admin, ProjectDTO projectDTO) {

        /* 1.检查项目编号不可重复 */
        checkExistsNo(projectDTO.getNo());

        /* 2.设置新增项目详情 */
        Project project = new Project();

        project.setId(IdUtil.generateIdByCurrentTime());
        project.setNo(projectDTO.getNo());
        project.setNameZh(projectDTO.getNameZh());
        project.setNameEn(projectDTO.getNameEn());
        project.setSummaryZh(projectDTO.getSummaryZh());
        project.setSummaryEn(projectDTO.getSummaryEn());
        project.setDescriptionZh(projectDTO.getDescriptionZh());
        project.setDescriptionEn(projectDTO.getDescriptionEn());
        project.setRecruitmentEndTime(projectDTO.getRecruitmentEndTime());
        project.setStartTime(projectDTO.getStartTime());
        project.setTotalAmount(projectDTO.getTotalAmount());
        project.setInitiatorZh(projectDTO.getInitiatorZh());
        project.setInitiatorEn(projectDTO.getInitiatorEn());
        project.setInitiatorPayAmount(projectDTO.getInitiatorPayAmount());
        project.setLockDays(projectDTO.getLockDays());
        project.setConditionMaxJoinNumber(projectDTO.getConditionMaxJoinNumber());
        project.setConditionMinLockedAmount(projectDTO.getConditionMinLockedAmount());
        project.setConditionMinPayAmount(projectDTO.getConditionMinPayAmount());
        project.setConditionMinRegisterDays(projectDTO.getConditionMinRegisterDays());
        project.setDeleted(false);
        project.setRemark(projectDTO.getRemark());
        project.setStatus(projectDTO.getStatus());
        project.setCreatedBy(admin);
        project.setCreatedTime(DateUtil.now());

        project = projectRepository.save(project);

        /* 3.初始化项目评估信息 */
        ProjectEvaluation projectEvaluation = new ProjectEvaluation();

        projectEvaluation.setProjectId(project.getId());
        projectEvaluation.setDeleted(false);
        projectEvaluation.setStatus(StatusEnum.VALID.getCode());
        projectEvaluation.setCreatedBy(admin);
        projectEvaluation.setCreatedTime(DateUtil.now());

        projectEvaluationRepository.save(projectEvaluation);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_PROJECT, project.getId());
    }

    /**
     * 修改项目
     *
     * @param admin      管理员
     * @param projectId  被修改的项目id
     * @param projectDTO 管理员信息
     */
    @Override
    public void modifyProject(ManagerAdmin admin, Long projectId, ProjectDTO projectDTO) {

        /* 1.取得被修改的内容 */
        Project project = projectRepository.findNotNullById(projectId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(projectDTO.getModifiedTime(), project.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        project.setNameZh(projectDTO.getNameZh());
        project.setNameEn(projectDTO.getNameEn());
        project.setSummaryZh(projectDTO.getSummaryZh());
        project.setSummaryEn(projectDTO.getSummaryEn());
        project.setDescriptionZh(projectDTO.getDescriptionZh());
        project.setDescriptionEn(projectDTO.getDescriptionEn());
        project.setRecruitmentEndTime(projectDTO.getRecruitmentEndTime());
        project.setStartTime(projectDTO.getStartTime());
        project.setTotalAmount(projectDTO.getTotalAmount());
        project.setInitiatorZh(projectDTO.getInitiatorZh());
        project.setInitiatorEn(projectDTO.getInitiatorEn());
        project.setInitiatorPayAmount(projectDTO.getInitiatorPayAmount());
        project.setLockDays(projectDTO.getLockDays());
        project.setConditionMaxJoinNumber(projectDTO.getConditionMaxJoinNumber());
        project.setConditionMinLockedAmount(projectDTO.getConditionMinLockedAmount());
        project.setConditionMinPayAmount(projectDTO.getConditionMinPayAmount());
        project.setConditionMinRegisterDays(projectDTO.getConditionMinRegisterDays());
        project.setRemark(projectDTO.getRemark());
        project.setStatus(projectDTO.getStatus());
        project.setModifiedBy(admin);
        project.setModifiedTime(DateUtil.now());

        projectRepository.save(project);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_PROJECT, project.getId());

        /* 5.修改项目状态后续处理 */
        modifyProjectStatusPostHandle(admin, project);
    }

    /**
     * 删除项目
     *
     * @param admin     管理员
     * @param projectId 被删除的项目id
     */
    @Override
    public void removeProject(ManagerAdmin admin, Long projectId) {

        /* 1.取得删除信息 */
        Project project = projectRepository.findNotNullById(projectId);

        /* 2.检查能否删除(无法删除已投资的项目) */
        Integer count = orderService.countOrderByProject(projectId);
        if (count > 0) {
            throw new BusinessException(ErrorEnum.DELETE_PROJECT_FAILED);
        }

        /* 3.删除项目信息 */
        project.setDeleted(true);
        project.setStatus(StatusEnum.INVALID.getCode());
        project.setModifiedBy(admin);
        project.setModifiedTime(DateUtil.now());
        projectRepository.save(project);

        /* 4.检查删除信息 */
        ProjectEvaluation projectEvaluation = projectEvaluationRepository.findByProjectId(projectId);
        if (ObjectUtil.isEmpty(projectEvaluation)) {
            throw new DataNotFoundException();
        }

        /* 5.删除项目评估信息 */
        projectEvaluation.setDeleted(true);
        projectEvaluation.setStatus(StatusEnum.INVALID.getCode());
        projectEvaluation.setModifiedBy(admin);
        projectEvaluation.setModifiedTime(DateUtil.now());
        projectEvaluationRepository.save(projectEvaluation);

        /* 6.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.REMOVE_PROJECT, project.getId());
    }

    /**
     * 统计参与该项目的投资募集进度
     *
     * @param payAmountSum 投资数额合计
     * @param totalAmount  预定总募集数额
     * @return 募集进度
     */
    public BigDecimal countProgress(BigDecimal payAmountSum, BigDecimal totalAmount) {
        // 募集进度(.2%) = 投资数额合计 / 预定总募集数额
        return MathUtil.divideRoundDown(2, payAmountSum, totalAmount);
    }

    /**
     * 检查项目编号是否存在
     *
     * @param no 项目编号
     */
    public void checkExistsNo(String no) {
        boolean existsByNo = projectRepository.existsByNo(no);
        if (existsByNo) {
            throw new BusinessException(ErrorEnum.PROJECT_NO_EXISTED);
        }
    }

    /**
     * 修改项目状态后续处理
     *
     * @param admin   管理员
     * @param project 项目
     */
    private void modifyProjectStatusPostHandle(ManagerAdmin admin, Project project) {

        //【募集失败】、【投资失败】或【无效】的场合，作废订单，退还用户投资
        if (ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.FAILURE_1.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.FAILURE_2.getCode()) || // TODO 【必须】投资失败时，需要扣除一定的投资额
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.INVALID.getCode())) {
            orderService.invalidateOrderByProjectFailure(admin, project.getId());
        }

        //【投资成功】的场合，设置订单锁仓时间
        if (ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.SUCCESS_2.getCode())) {
            orderService.updateOrderByProjectSuccess(admin, project.getId());
        }
    }

    /**
     * 编辑项目概要VO
     *
     * @param project 项目
     * @return 项目概要VO
     */
    private ProjectSummaryVO editProjectSummaryVO(Project project) {

        // 投资数额合计
        BigDecimal payAmountSum = orderService.sumPayAmountByProject(project.getId());

        ProjectSummaryVO projectSummaryVO = new ProjectSummaryVO();

        projectSummaryVO.setId(project.getId());
        projectSummaryVO.setName(LanguageUtil.getTextByLanguage(project.getNameZh(), project.getNameEn()));
        projectSummaryVO.setInitiator(LanguageUtil.getTextByLanguage(project.getInitiatorZh(), project.getInitiatorEn()));
        projectSummaryVO.setJoinNumber(orderService.countJoinNumberByProject(project.getId()));
        projectSummaryVO.setLockDays(project.getLockDays());
        projectSummaryVO.setTotalAmount(project.getTotalAmount());
        projectSummaryVO.setProgress(countProgress(payAmountSum, project.getTotalAmount()));
        projectSummaryVO.setConditionMaxJoinNumber(project.getConditionMaxJoinNumber());
        projectSummaryVO.setConditionMinLockedAmount(project.getConditionMinLockedAmount());
        projectSummaryVO.setConditionMinPayAmount(project.getConditionMinPayAmount());
        projectSummaryVO.setConditionMinRegisterDays(project.getConditionMinRegisterDays());
        projectSummaryVO.setRecruitmentEndTime(project.getRecruitmentEndTime());

        return projectSummaryVO;
    }

    /**
     * 编辑项目VO
     *
     * @param project 项目
     * @return 项目VO
     */
    private ProjectVO editProjectVO(Project project) {

        ProjectVO projectVO = new ProjectVO();

        projectVO.setId(project.getId());
        projectVO.setNo(project.getNo());
        projectVO.setName(LanguageUtil.getTextByLanguage(project.getNameZh(), project.getNameEn()));
        projectVO.setNameZh(project.getNameZh());
        projectVO.setNameEn(project.getNameEn());
        projectVO.setInitiator(LanguageUtil.getTextByLanguage(project.getInitiatorZh(), project.getInitiatorEn()));
        projectVO.setInitiatorZh(project.getInitiatorZh());
        projectVO.setInitiatorEn(project.getInitiatorEn());
        projectVO.setRecruitmentEndTime(project.getRecruitmentEndTime());
        projectVO.setStartTime(project.getStartTime());
        projectVO.setTotalAmount(project.getTotalAmount());
        projectVO.setLockDays(project.getLockDays());
        projectVO.setStatus(project.getStatus());
        projectVO.setStatusName(ProjectStatusEnum.getNameByCode(project.getStatus()));
        projectVO.setCreatedBy(ObjectUtil.isNotEmpty(project.getCreatedBy()) ? project.getCreatedBy().getName() : null);
        projectVO.setCreatedTime(project.getCreatedTime());
        projectVO.setModifiedBy(ObjectUtil.isNotEmpty(project.getCreatedBy()) ? project.getCreatedBy().getName() : null);
        projectVO.setModifiedTime(project.getModifiedTime());
        projectVO.setPayAmountSum(orderService.sumPayAmountByProject(project.getId()));
        projectVO.setProgress(countProgress(projectVO.getPayAmountSum(), project.getTotalAmount()));
        projectVO.setOrderCount(orderService.countOrderByProject(project.getId()));

        return projectVO;
    }

    /**
     * 编辑项目详情VO
     *
     * @param project 项目
     * @return 项目详情VO
     */
    private ProjectDetailVO editProjectDetailVO(Project project) {

        ProjectDetailVO projectDetailVO = new ProjectDetailVO();

        projectDetailVO.setId(project.getId());
        projectDetailVO.setNo(project.getNo());
        projectDetailVO.setName(LanguageUtil.getTextByLanguage(project.getNameZh(), project.getNameEn()));
        projectDetailVO.setNameZh(project.getNameZh());
        projectDetailVO.setNameEn(project.getNameEn());
        projectDetailVO.setSummary(LanguageUtil.getTextByLanguage(project.getSummaryZh(), project.getSummaryEn()));
        projectDetailVO.setSummaryZh(project.getSummaryZh());
        projectDetailVO.setSummaryEn(project.getSummaryEn());
        projectDetailVO.setDescription(LanguageUtil.getTextByLanguage(project.getDescriptionZh(), project.getDescriptionEn()));
        projectDetailVO.setDescriptionZh(project.getDescriptionZh());
        projectDetailVO.setDescriptionEn(project.getDescriptionEn());
        projectDetailVO.setInitiator(LanguageUtil.getTextByLanguage(project.getInitiatorZh(), project.getInitiatorEn()));
        projectDetailVO.setInitiatorZh(project.getInitiatorZh());
        projectDetailVO.setInitiatorEn(project.getInitiatorEn());
        projectDetailVO.setJoinNumber(orderService.countJoinNumberByProject(project.getId()));
        projectDetailVO.setRecruitmentEndTime(project.getRecruitmentEndTime());
        projectDetailVO.setStartTime(project.getStartTime());
        projectDetailVO.setTotalAmount(project.getTotalAmount());
        projectDetailVO.setInitiatorPayAmount(project.getInitiatorPayAmount());
        projectDetailVO.setPayAmountSum(orderService.sumPayAmountByProject(project.getId()));
        projectDetailVO.setProgress(countProgress(projectDetailVO.getPayAmountSum(), project.getTotalAmount()));
        projectDetailVO.setLockDays(project.getLockDays());
        projectDetailVO.setConditionMaxJoinNumber(project.getConditionMaxJoinNumber());
        projectDetailVO.setConditionMinLockedAmount(project.getConditionMinLockedAmount());
        projectDetailVO.setConditionMinPayAmount(project.getConditionMinPayAmount());
        projectDetailVO.setConditionMinRegisterDays(project.getConditionMinRegisterDays());
        projectDetailVO.setStatus(project.getStatus());
        projectDetailVO.setStatusName(ProjectStatusEnum.getNameByCode(project.getStatus()));
        projectDetailVO.setRemark(project.getRemark());
        projectDetailVO.setCreatedBy(ObjectUtil.isNotEmpty(project.getCreatedBy()) ? project.getCreatedBy().getName() : null);
        projectDetailVO.setCreatedTime(project.getCreatedTime());
        projectDetailVO.setModifiedBy(ObjectUtil.isNotEmpty(project.getCreatedBy()) ? project.getCreatedBy().getName() : null);
        projectDetailVO.setModifiedTime(project.getModifiedTime());

        List<SourceVO> sourceVOList = sourceService.findSourceList(RelationTypeEnum.PROJECT.getCode(), project.getId());
        projectDetailVO.setSources(sourceVOList);

        projectDetailVO.setPayable(ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.START.getCode()) ? true : false);
        projectDetailVO.setInviteable(ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.START.getCode()) ? true : false);
        projectDetailVO.setSucceed(ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.SUCCESS_1.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S1.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S1_END.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S2.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S2_END.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S3.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S3_END.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.SUCCESS_2.getCode()) ? true : false);
        projectDetailVO.setFailed(ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.FAILURE_1.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.FAILURE_2.getCode())
                || ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.INVALID.getCode()) ? true : false);

        return projectDetailVO;
    }
}

// TODO【改善】针对用户订单发送消息