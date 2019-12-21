package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.Feedback;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.enums.FeedbackStatusEnum;
import com.cpto.dapp.enums.FeedbackTypeEnum;
import com.cpto.dapp.exception.ActionException;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.RequestOverLimitException;
import com.cpto.dapp.pojo.dto.FeedbackDTO;
import com.cpto.dapp.pojo.vo.FeedbackVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.repository.FeedbackRepository;
import com.cpto.dapp.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 反馈ServiceImpl
 *
 * @author sunli
 * @date 2019/01/29
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Value("${custom.other.feedback-day-times}")
    private Integer feedbackDayTimes;

    /**
     * 添加反馈信息
     *
     * @param user        用户
     * @param feedbackDTO 反馈信息
     */
    @Override
    public void addFeedback(User user, FeedbackDTO feedbackDTO) {

        // 检查用户每日反馈次数（反馈不可超过每日限制次数）
        Long count;
        try {
            count = feedbackRepository.countByUserIdAndCreatedTimeGreaterThanEqual(user.getId(), DateUtil.todayTimeStamp());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ActionException();
        }
        if (count + 1 > feedbackDayTimes) {
            throw new RequestOverLimitException();
        }

        Feedback feedback = new Feedback();

        feedback.setUser(user);
        feedback.setFeedbackType(feedbackDTO.getFeedbackType());
        feedback.setContent(feedbackDTO.getContent());
        feedback.setStatus(FeedbackStatusEnum.UNTREATED.getCode());
        feedback.setCreatedTime(DateUtil.now());

        feedbackRepository.save(feedback);
    }

    /**
     * 修改反馈
     *
     * @param admin       管理员
     * @param feedbackId  反馈id
     * @param feedbackDTO 反馈信息
     */
    @Override
    public void modifyFeedback(ManagerAdmin admin, Long feedbackId, FeedbackDTO feedbackDTO) {

        /* 1.取得被修改的内容 */
        Feedback feedback = feedbackRepository.findNotNullById(feedbackId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(feedbackDTO.getModifiedTime(), feedback.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        feedback.setStatus(feedbackDTO.getStatus());
        feedback.setRemark(feedbackDTO.getRemark());
        feedback.setModifiedBy(admin);
        feedback.setModifiedTime(DateUtil.now());
        feedbackRepository.save(feedback);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_FEEDBACK, feedback.getId());
    }

    /**
     * 查询满足条件的反馈
     *
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
    @Override
    public PageVO<FeedbackVO> searchFeedback(Timestamp searchTime,
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
                                             String toCreatedTime) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<Feedback> specification = getSQLWhere(feedbackType, id, content, remark, status, userId, userName, fromCreatedTime, toCreatedTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<Feedback> feedbackPage = feedbackRepository.findAll(specification, pageable);

        List<FeedbackVO> feedbackVOList = new ArrayList<>();
        for (Feedback feedback : feedbackPage) {
            feedbackVOList.add(editFeedbackVO(feedback));
        }

        PageVO<FeedbackVO> feedbackPageVO = new PageVO();
        feedbackPageVO.setRows(feedbackVOList);
        feedbackPageVO.setTotal(feedbackPage.getTotalElements());
        feedbackPageVO.setTotalPage(feedbackPage.getTotalPages());
        feedbackPageVO.setHasNext(feedbackPage.hasNext());
        feedbackPageVO.setSearchTime(searchTime);

        return feedbackPageVO;
    }

    /**
     * 根据id取得反馈详情
     *
     * @param feedbackId 反馈id
     * @return 反馈详情
     */
    @Override
    public FeedbackVO findFeedback(Long feedbackId) {
        Feedback feedback = feedbackRepository.findNotNullById(feedbackId);
        return editFeedbackVO(feedback);
    }

    /**
     * 生成动态查询条件
     *
     * @param feedbackType    反馈类型
     * @param id              反馈id
     * @param content         内容
     * @param remark          备注
     * @param status          状态
     * @param userId          用户id
     * @param userName        用户名
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 动态查询条件
     */
    private Specification<Feedback> getSQLWhere(Integer feedbackType,
                                                Long id,
                                                String content,
                                                String remark,
                                                Integer status,
                                                Long userId,
                                                String userName,
                                                String fromCreatedTime,
                                                String toCreatedTime) {

        Specification<Feedback> specification = new Specification<Feedback>() {
            @Override
            public Predicate toPredicate(Root<Feedback> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 精确查询反馈类型
                if (ObjectUtil.isNotEmpty(feedbackType)) {
                    predicatesList.add(cb.equal(root.get("feedbackType"), feedbackType));
                }

                // 精确查询反馈id
                if (ObjectUtil.isNotEmpty(id)) {
                    predicatesList.add(cb.equal(root.get("id"), id));
                }

                // 模糊查询内容
                if (ObjectUtil.isNotEmpty(content)) {
                    predicatesList.add(cb.like(cb.lower(root.get("content")), "%" + content.toLowerCase() + "%"));
                }

                // 模糊查询备注
                if (ObjectUtil.isNotEmpty(remark)) {
                    predicatesList.add(cb.like(cb.lower(root.get("remark")), "%" + remark.toLowerCase() + "%"));
                }

                // 精确查询状态
                if (ObjectUtil.isNotEmpty(status)) {
                    predicatesList.add(cb.equal(root.get("status"), status));
                }

                // 精确查询用户id
                if (ObjectUtil.isNotEmpty(userId)) {
                    predicatesList.add(cb.equal(root.<User>get("user").get("id"), userId));
                }

                // 模糊查询用户名
                if (ObjectUtil.isNotEmpty(userName)) {
                    predicatesList.add(cb.like(cb.lower(root.<User>get("user").get("userName")), "%" + userName.toLowerCase() + "%"));
                }

                // 范围查询创建时间
                if (ObjectUtil.isNotEmpty(fromCreatedTime)) {
                    predicatesList.add(cb.greaterThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullFromTime(fromCreatedTime))));
                }
                if (ObjectUtil.isNotEmpty(toCreatedTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullFromTime(fromCreatedTime))));
                }

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 编辑反馈VO
     *
     * @param feedback 公告
     * @return 反馈VO
     */
    private FeedbackVO editFeedbackVO(Feedback feedback) {

        FeedbackVO feedbackVO = new FeedbackVO();

        feedbackVO.setId(feedback.getId());
        feedbackVO.setFeedbackType(feedback.getFeedbackType());
        feedbackVO.setFeedbackTypeName(FeedbackTypeEnum.getNameByCode(feedback.getFeedbackType()));
        feedbackVO.setContent(feedback.getContent());
        feedbackVO.setStatus(feedback.getStatus());
        feedbackVO.setStatusName(FeedbackStatusEnum.getNameByCode(feedback.getStatus()));
        feedbackVO.setRemark(feedback.getRemark());
        feedbackVO.setUserId(feedback.getUser().getId());
        feedbackVO.setUserName(feedback.getUser().getUserName());
        feedbackVO.setCreatedBy(ObjectUtil.isNotEmpty(feedback.getCreatedBy()) ? feedback.getCreatedBy().getName() : null);
        feedbackVO.setCreatedTime(feedback.getCreatedTime());
        feedbackVO.setModifiedBy(ObjectUtil.isNotEmpty(feedback.getCreatedBy()) ? feedback.getCreatedBy().getName() : null);
        feedbackVO.setModifiedTime(feedback.getModifiedTime());

        return feedbackVO;
    }
}