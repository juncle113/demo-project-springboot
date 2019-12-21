package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Project;
import com.cpto.dapp.pojo.dto.ProjectDTO;
import com.cpto.dapp.pojo.vo.ProjectDetailVO;
import com.cpto.dapp.pojo.vo.ProjectSummaryVO;
import com.cpto.dapp.pojo.vo.ProjectVO;

import java.util.List;

/**
 * 项目Service
 *
 * @author sunli
 * @date 2019/01/13
 */
public interface ProjectService extends BaseService {

    /**
     * 取得项目详细信息
     *
     * @param projectId 项目id
     * @return 项目详细信息
     */
    ProjectDetailVO findProject(Long projectId);

    /**
     * 取得所有正在募集的项目概要信息
     *
     * @return 项目概要信息
     */
    List<ProjectSummaryVO> findProjectSummaryList();

    /**
     * 根据状态取得项目信息
     *
     * @param statusList 状态
     * @return 项目信息
     */
    List<Project> findProjectByStatus(List<Integer> statusList);

    /**
     * 新增项目
     *
     * @param admin      管理员
     * @param projectDTO 新增的项目信息
     */
    void addProject(ManagerAdmin admin, ProjectDTO projectDTO);

    /**
     * 修改项目
     *
     * @param admin      管理员
     * @param projectId  被修改的项目id
     * @param projectDTO 项目信息
     */
    void modifyProject(ManagerAdmin admin, Long projectId, ProjectDTO projectDTO);

    /**
     * 删除项目
     *
     * @param admin     管理员
     * @param projectId 被删除的项目id
     */
    void removeProject(ManagerAdmin admin, Long projectId);

    /**
     * 取得全部项目信息
     *
     * @return 项目列表
     */
    List<ProjectVO> findProjectList();

    /**
     * 根据项目编号查询项目详细信息
     *
     * @param no 项目编号
     * @return 项目详细信息
     */
    ProjectDetailVO findProjectByNo(String no);
}