package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Project;
import com.cpto.dapp.domain.ProjectEvaluation;
import com.cpto.dapp.enums.ProjectStatusEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.ProjectEvaluationDTO;
import com.cpto.dapp.pojo.vo.ProjectEvaluationVO;
import com.cpto.dapp.repository.ProjectEvaluationRepository;
import com.cpto.dapp.service.ProjectEvaluationService;
import com.cpto.dapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目ServiceImpl
 *
 * @author sunli
 * @date 2019/01/13
 */
@Service
public class ProjectEvaluationServiceImpl extends BaseServiceImpl implements ProjectEvaluationService {

    @Autowired
    private ProjectEvaluationRepository projectEvaluationRepository;

    @Autowired
    private ProjectService projectService;

    /**
     * 取得项目评估信息
     *
     * @param projectId 项目id
     */
    @Override
    public ProjectEvaluationVO findProjectEvaluation(Long projectId) {
        ProjectEvaluation projectEvaluation = projectEvaluationRepository.findByProjectId(projectId);
        return editProjectEvaluationVO(projectEvaluation);
    }

    /**
     * 取得项目评估信息列表
     *
     * @return 项目评估信息列表
     */
    @Override
    public List<ProjectEvaluationVO> findProjectEvaluationList() {

        List<ProjectEvaluationVO> projectEvaluationVOList = new ArrayList<>();
        ProjectEvaluationVO projectEvaluationVO;
        ProjectEvaluation projectEvaluation;

        List<Integer> statusList = new ArrayList<>();
        statusList.add(ProjectStatusEnum.S1.getCode());
        statusList.add(ProjectStatusEnum.S1_END.getCode());
        statusList.add(ProjectStatusEnum.S2.getCode());
        statusList.add(ProjectStatusEnum.S2_END.getCode());
        statusList.add(ProjectStatusEnum.S3.getCode());
        statusList.add(ProjectStatusEnum.S3_END.getCode());
        statusList.add(ProjectStatusEnum.SUCCESS_2.getCode());

        List<Project> projectList = projectService.findProjectByStatus(statusList);
        for (Project project : projectList) {

            projectEvaluation = projectEvaluationRepository.findByProjectId(project.getId());
            projectEvaluationVO = editProjectEvaluationVO(projectEvaluation);
            projectEvaluationVO.setProjectId(project.getId());
            projectEvaluationVO.setProjectName(LanguageUtil.getTextByLanguage(project.getNameZh(), project.getNameEn()));
            projectEvaluationVOList.add(projectEvaluationVO);
        }

        return projectEvaluationVOList;
    }

    /**
     * 修改项目评估信息
     *
     * @param admin                管理员
     * @param projectId            项目id
     * @param projectEvaluationDTO 项目评估信息
     */
    @Override
    public void modifyProjectEvaluation(ManagerAdmin admin, Long projectId, ProjectEvaluationDTO projectEvaluationDTO) {

        /* 1.取得被修改的内容 */
        ProjectEvaluation projectEvaluation = projectEvaluationRepository.findByProjectId(projectId);
        if (ObjectUtil.isEmpty(projectEvaluation)) {
            throw new DataNotFoundException();
        }

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(projectEvaluationDTO.getModifiedTime(), projectEvaluation.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        projectEvaluation.setS1ExpectRange(projectEvaluationDTO.getS1ExpectRange());
        projectEvaluation.setS2ExpectRange(projectEvaluationDTO.getS2ExpectRange());
        projectEvaluation.setS3ExpectRange(projectEvaluationDTO.getS3ExpectRange());
        projectEvaluation.setS1ExpectReturnZh(projectEvaluationDTO.getS1ExpectReturnZh());
        projectEvaluation.setS1ExpectReturnEn(projectEvaluationDTO.getS1ExpectReturnEn());
        projectEvaluation.setS2ExpectReturnZh(projectEvaluationDTO.getS2ExpectReturnZh());
        projectEvaluation.setS2ExpectReturnEn(projectEvaluationDTO.getS2ExpectReturnEn());
        projectEvaluation.setS3ExpectReturnZh(projectEvaluationDTO.getS3ExpectReturnZh());
        projectEvaluation.setS3ExpectReturnEn(projectEvaluationDTO.getS3ExpectReturnEn());
        projectEvaluation.setS1Zh(projectEvaluationDTO.getS1Zh());
        projectEvaluation.setS1En(projectEvaluationDTO.getS1En());
        projectEvaluation.setS1EvaluatorZh(projectEvaluationDTO.getS1EvaluatorZh());
        projectEvaluation.setS1EvaluatorEn(projectEvaluationDTO.getS1EvaluatorEn());
        projectEvaluation.setS1ReturnZh(projectEvaluationDTO.getS1ReturnZh());
        projectEvaluation.setS1ReturnEn(projectEvaluationDTO.getS1ReturnEn());
        projectEvaluation.setS2Zh(projectEvaluationDTO.getS2Zh());
        projectEvaluation.setS2En(projectEvaluationDTO.getS2En());
        projectEvaluation.setS2EvaluatorZh(projectEvaluationDTO.getS2EvaluatorZh());
        projectEvaluation.setS2EvaluatorEn(projectEvaluationDTO.getS2EvaluatorEn());
        projectEvaluation.setS2ReturnZh(projectEvaluationDTO.getS2ReturnZh());
        projectEvaluation.setS2ReturnEn(projectEvaluationDTO.getS2ReturnEn());
        projectEvaluation.setS3Zh(projectEvaluationDTO.getS3Zh());
        projectEvaluation.setS3En(projectEvaluationDTO.getS3En());
        projectEvaluation.setS3EvaluatorZh(projectEvaluationDTO.getS3EvaluatorZh());
        projectEvaluation.setS3EvaluatorEn(projectEvaluationDTO.getS3EvaluatorEn());
        projectEvaluation.setS3ReturnZh(projectEvaluationDTO.getS3ReturnZh());
        projectEvaluation.setS3ReturnEn(projectEvaluationDTO.getS3ReturnEn());
        projectEvaluation.setModifiedBy(admin);
        projectEvaluation.setModifiedTime(DateUtil.now());

        projectEvaluationRepository.save(projectEvaluation);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.EVALUATION_PROJECT, projectEvaluation.getId());
    }

    /**
     * 编辑项目评估VO
     *
     * @param projectEvaluation 项目评估
     * @return 项目评估VO
     */
    private ProjectEvaluationVO editProjectEvaluationVO(ProjectEvaluation projectEvaluation) {

        ProjectEvaluationVO projectEvaluationVO = new ProjectEvaluationVO();

        projectEvaluationVO.setId(projectEvaluation.getId());
        projectEvaluationVO.setProjectId(projectEvaluation.getProjectId());
        projectEvaluationVO.setS1ExpectRange(projectEvaluation.getS1ExpectRange());
        projectEvaluationVO.setS2ExpectRange(projectEvaluation.getS2ExpectRange());
        projectEvaluationVO.setS3ExpectRange(projectEvaluation.getS3ExpectRange());

        projectEvaluationVO.setS1ExpectReturn(LanguageUtil.getTextByLanguage(projectEvaluation.getS1ExpectReturnZh(), projectEvaluation.getS1ExpectReturnEn()));
        projectEvaluationVO.setS2ExpectReturn(LanguageUtil.getTextByLanguage(projectEvaluation.getS2ExpectReturnZh(), projectEvaluation.getS2ExpectReturnEn()));
        projectEvaluationVO.setS3ExpectReturn(LanguageUtil.getTextByLanguage(projectEvaluation.getS3ExpectReturnZh(), projectEvaluation.getS3ExpectReturnEn()));

        projectEvaluationVO.setS1(LanguageUtil.getTextByLanguage(projectEvaluation.getS1Zh(), projectEvaluation.getS1En()));
        projectEvaluationVO.setS1Evaluator(LanguageUtil.getTextByLanguage(projectEvaluation.getS1EvaluatorZh(), projectEvaluation.getS1EvaluatorEn()));
        projectEvaluationVO.setS1Return(LanguageUtil.getTextByLanguage(projectEvaluation.getS1ReturnZh(), projectEvaluation.getS1ReturnEn()));

        projectEvaluationVO.setS2(LanguageUtil.getTextByLanguage(projectEvaluation.getS2Zh(), projectEvaluation.getS2En()));
        projectEvaluationVO.setS2Evaluator(LanguageUtil.getTextByLanguage(projectEvaluation.getS2EvaluatorZh(), projectEvaluation.getS2EvaluatorEn()));
        projectEvaluationVO.setS2Return(LanguageUtil.getTextByLanguage(projectEvaluation.getS2ReturnZh(), projectEvaluation.getS2ReturnEn()));

        projectEvaluationVO.setS3(LanguageUtil.getTextByLanguage(projectEvaluation.getS3Zh(), projectEvaluation.getS3En()));
        projectEvaluationVO.setS3Evaluator(LanguageUtil.getTextByLanguage(projectEvaluation.getS3EvaluatorZh(), projectEvaluation.getS3EvaluatorEn()));
        projectEvaluationVO.setS3Return(LanguageUtil.getTextByLanguage(projectEvaluation.getS3ReturnZh(), projectEvaluation.getS3ReturnEn()));

        projectEvaluationVO.setStatus(projectEvaluation.getStatus());
        projectEvaluationVO.setStatusName(StatusEnum.getNameByCode(projectEvaluation.getStatus()));
        projectEvaluationVO.setRemark(projectEvaluation.getRemark());
        projectEvaluationVO.setCreatedBy(ObjectUtil.isNotEmpty(projectEvaluation.getCreatedBy()) ? projectEvaluation.getCreatedBy().getName() : null);
        projectEvaluationVO.setCreatedTime(projectEvaluation.getCreatedTime());
        projectEvaluationVO.setModifiedBy(ObjectUtil.isNotEmpty(projectEvaluation.getModifiedBy()) ? projectEvaluation.getModifiedBy().getName() : null);
        projectEvaluationVO.setModifiedTime(projectEvaluation.getModifiedTime());

        return projectEvaluationVO;
    }
}