package com.cpto.dapp.repository;

import com.cpto.dapp.domain.Feedback;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;

/**
 * 反馈Repository
 *
 * @author sunli
 * @date 2019/01/29
 */
public interface FeedbackRepository extends BaseRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {

    /**
     * 统计当天用户反馈次数
     *
     * @param userId 用户id
     * @param today  当天日期
     * @return 反馈次数
     */
    Long countByUserIdAndCreatedTimeGreaterThanEqual(Long userId, Timestamp today);
}