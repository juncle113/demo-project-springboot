package com.cpto.dapp.repository;

import com.cpto.dapp.domain.ProjectEvaluation;

/**
 * 项目评估Repository
 *
 * @author sunli
 * @date 2019/02/02
 */
public interface ProjectEvaluationRepository extends BaseRepository<ProjectEvaluation, Long> {

    /**
     * 取得项目评估信息
     *
     * @param projectId 项目id
     * @return 评估信息
     */
    ProjectEvaluation findByProjectId(Long projectId);
}