package com.cpto.dapp.repository;

import com.cpto.dapp.domain.Project;

import java.util.List;

/**
 * 项目Repository
 *
 * @author sunli
 * @date 2019/01/16
 */
public interface ProjectRepository extends BaseRepository<Project, Long> {

    /**
     * 取得所有项目信息
     *
     * @return 项目信息
     */
    List<Project> findByOrderByIdDesc();

    /**
     * 根据状态查询项目信息
     *
     * @param statusList 状态
     * @return 项目信息
     */
    List<Project> findByStatusInOrderByIdDesc(List<Integer> statusList);

    /**
     * 根据项目编号查询项目信息
     *
     * @param no 项目编号
     * @return 项目信息
     */
    Project findByNo(String no);

    /**
     * 检查项目编号是否存在
     *
     * @param no 项目编号
     * @return 是否存在
     */
    boolean existsByNo(String no);
}