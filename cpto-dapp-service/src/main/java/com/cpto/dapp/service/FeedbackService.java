package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.FeedbackDTO;
import com.cpto.dapp.pojo.vo.FeedbackVO;
import com.cpto.dapp.pojo.vo.PageVO;

import java.sql.Timestamp;

/**
 * 反馈Service
 *
 * @author sunli
 * @date 2019/01/29
 */
public interface FeedbackService extends BaseService {

    /**
     * 添加反馈信息
     *
     * @param user        用户
     * @param feedbackDTO 反馈信息
     */
    void addFeedback(User user, FeedbackDTO feedbackDTO);

    /**
     * 查询满足条件的反馈
     *
     * @param searchTime      查询时间
     * @param page            当前页数
     * @param pageSize        每页条数
     * @param feedbackType    反馈类型
     * @param id              反馈id
     * @param content         内容
     * @param remark          备注
     * @param status          状态
     * @param userId          用户id
     * @param userName        用户名
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 反馈列表
     */
    PageVO<FeedbackVO> searchFeedback(Timestamp searchTime,
                                      Integer page,
                                      Integer pageSize,
                                      Integer feedbackType,
                                      Long id,
                                      String content,
                                      String remark,
                                      Integer status,
                                      Long userId,
                                      String userName,
                                      String fromCreatedTime,
                                      String toCreatedTime);

    /**
     * 根据id取得反馈详情
     *
     * @param feedbackId 反馈id
     * @return 反馈详情
     */
    FeedbackVO findFeedback(Long feedbackId);

    /**
     * 修改反馈
     *
     * @param admin       管理员
     * @param feedbackId  反馈id
     * @param feedbackDTO 反馈信息
     */
    void modifyFeedback(ManagerAdmin admin, Long feedbackId, FeedbackDTO feedbackDTO);
}