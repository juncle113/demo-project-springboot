package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.domain.LogUserIncome;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserIncomeDetailLogVO;
import com.cpto.dapp.pojo.vo.UserIncomeLogVO;
import com.cpto.dapp.pojo.vo.UserIncomeProjectLogVO;
import com.cpto.dapp.repository.LogUserIncomeRepository;
import com.cpto.dapp.service.AccountService;
import com.cpto.dapp.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 用户收益ServiceImpl
 *
 * @author sunli
 * @date 2019/01/31
 */
@Service
public class UserIncomeServiceImpl extends BaseServiceImpl implements UserIncomeService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private LogUserIncomeRepository logUserIncomeRepository;

    /**
     * 查询用户的各项目收益信息
     *
     * @param userId 用户id
     * @return 收益列表
     */
    @Override
    public UserIncomeLogVO findUserIncomeProjectLogList(Long userId) {

        UserIncomeLogVO userIncomeLogVO = new UserIncomeLogVO();
        List<UserIncomeProjectLogVO> userIncomeProjectLogVOList = new ArrayList<>();
        UserIncomeProjectLogVO userIncomeProjectLogVO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());

        List<Map<String, Object>> logUserIncomeList = logUserIncomeRepository.sumByUserIdGroupByProjectId(userId, statusList);
        for (Map<String, Object> logUserIncome : logUserIncomeList) {
            userIncomeProjectLogVO = editUserIncomeProjectLogVO(logUserIncome);
            userIncomeProjectLogVOList.add(userIncomeProjectLogVO);
            totalAmount = totalAmount.add(userIncomeProjectLogVO.getAmount());
        }

        userIncomeLogVO.setDetails(userIncomeProjectLogVOList);
        userIncomeLogVO.setTotalAmount(totalAmount);

        return userIncomeLogVO;
    }

    /**
     * 查询用户的某项目详细收益信息
     *
     * @param userId     用户id
     * @param projectId  项目id
     * @param searchTime 查询时间
     * @param page       当前页数
     * @param pageSize   每页条数
     * @return 收益信息
     */
    @Override
    public PageVO<UserIncomeDetailLogVO> findUserIncomeDetailLogListByProject(Long userId,
                                                                              Long projectId,
                                                                              Timestamp searchTime,
                                                                              Integer page,
                                                                              Integer pageSize) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<LogUserIncome> specification = getSQLWhere(userId, projectId, searchTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<LogUserIncome> userIncomeLogPage = logUserIncomeRepository.findAll(specification, pageable);

        List<UserIncomeDetailLogVO> userIncomeDetailLogVOList = new ArrayList<>();
        for (LogUserIncome userIncomeLog : userIncomeLogPage) {
            userIncomeDetailLogVOList.add(editUserIncomeDetailLogVO(userIncomeLog));
        }

        PageVO<UserIncomeDetailLogVO> userIncomeDetailLogPageVO = new PageVO();
        userIncomeDetailLogPageVO.setRows(userIncomeDetailLogVOList);
        userIncomeDetailLogPageVO.setTotal(userIncomeLogPage.getTotalElements());
        userIncomeDetailLogPageVO.setTotalPage(userIncomeLogPage.getTotalPages());
        userIncomeDetailLogPageVO.setHasNext(userIncomeLogPage.hasNext());
        userIncomeDetailLogPageVO.setSearchTime(searchTime);

        return userIncomeDetailLogPageVO;
    }

    /**
     * 释放收益
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnIncome() {

        /* 1.取得未完成收益信息 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());

        List<LogUserIncome> logUserIncomeList = logUserIncomeRepository.findByStatusInOrderByIdDesc(statusList);
        for (LogUserIncome logUserIncome : logUserIncomeList) {

            /* 2.相关订单状态为已完成的场合，立即释放收益 */
            if (ObjectUtil.equals(logUserIncome.getOrderInfo().getStatus(), OrderStatusEnum.COMPLETED.getCode())) {

                /* 2.1.更新收益状态为完成 */
                logUserIncome.setStatus(StatusEnum.INVALID.getCode());
                logUserIncome.setModifiedBy(null);
                logUserIncome.setModifiedTime(DateUtil.now());
                logUserIncomeRepository.save(logUserIncome);

                /* 2.2.退回投资 */
                TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

                // 设置出账账户信息（用户id和账户类型（锁定账户））
                transferAccountDTO.setOutUserId(logUserIncome.getId());
                transferAccountDTO.setOutAccountType(AccountTypeEnum.LOCKED.getCode());

                // 设置入账账户信息（用户id和账户类型（可用账户））
                transferAccountDTO.setInUserId(logUserIncome.getId());
                transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

                // 设置转账数额（投资数额）
                transferAccountDTO.setAmount(logUserIncome.getAmount());

                // 设置转账类型和说明
                transferAccountDTO.setTransferType(TransferTypeEnum.RETURN.getCode());
                transferAccountDTO.setTransferMemoZh(logUserIncome.getProject().getNameZh());
                transferAccountDTO.setTransferMemoEn(logUserIncome.getProject().getNameEn());

                // 设置业务关联号（订单id）
                transferAccountDTO.setRelationId(logUserIncome.getOrderInfo().getId());
                transferAccountDTO.setRelationType(RelationTypeEnum.ORDER.getCode());

                accountService.transferAccount(transferAccountDTO);
            }
        }
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime 查询时间
     * @param userId     用户id
     * @param projectId  项目id
     * @return 动态查询条件
     */
    private Specification<LogUserIncome> getSQLWhere(Long userId,
                                                     Long projectId,
                                                     Timestamp searchTime) {

        Specification<LogUserIncome> specification = new Specification<LogUserIncome>() {
            @Override
            public Predicate toPredicate(Root<LogUserIncome> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 精确查询用户id
                if (ObjectUtil.isNotEmpty(userId)) {
                    predicatesList.add(cb.equal(root.get("userId"), userId));
                }

                // 精确查询项目id
                if (ObjectUtil.isNotEmpty(projectId)) {
                    predicatesList.add(cb.equal(root.get("project"), projectId));
                }

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询状态
                predicatesList.add(cb.equal(root.get("status"), StatusEnum.VALID.getCode()));

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 编辑用户收益项目记录详情VO
     *
     * @param logUserIncome 用户收益记录
     * @return 收益项目记录详情VO
     */
    private UserIncomeProjectLogVO editUserIncomeProjectLogVO(Map<String, Object> logUserIncome) {

        UserIncomeProjectLogVO userIncomeProjectLogVO = new UserIncomeProjectLogVO();

        userIncomeProjectLogVO.setProjectId(ObjectUtil.toLong(logUserIncome.get("project_id")));
        userIncomeProjectLogVO.setProjectNo(String.valueOf(logUserIncome.get("project_no")));
        userIncomeProjectLogVO.setProjectName(LanguageUtil.getTextByLanguage(String.valueOf(logUserIncome.get("project_name_zh")), String.valueOf(logUserIncome.get("project_name_en"))));
        userIncomeProjectLogVO.setOrderId(ObjectUtil.toLong(logUserIncome.get("order_id")));
        userIncomeProjectLogVO.setAmount(ObjectUtil.toBigDecimal(logUserIncome.get("amount_sum")));

        return userIncomeProjectLogVO;
    }

    /**
     * 编辑用户收益明细记录详情VO
     *
     * @param logUserIncome 用户收益记录
     * @return 收益明细记录详情VO
     */
    private UserIncomeDetailLogVO editUserIncomeDetailLogVO(LogUserIncome logUserIncome) {

        UserIncomeDetailLogVO userIncomeDetailLogVO = new UserIncomeDetailLogVO();

        userIncomeDetailLogVO.setId(logUserIncome.getId());
        userIncomeDetailLogVO.setProjectIncomeLogId(logUserIncome.getLogProjectIncome().getId());
        userIncomeDetailLogVO.setAmount(logUserIncome.getAmount());
        userIncomeDetailLogVO.setRemark(logUserIncome.getRemark());
        userIncomeDetailLogVO.setCreatedBy(ObjectUtil.isNotEmpty(logUserIncome.getCreatedBy()) ? logUserIncome.getCreatedBy().getName() : null);
        userIncomeDetailLogVO.setCreatedTime(logUserIncome.getCreatedTime());
        userIncomeDetailLogVO.setModifiedBy(ObjectUtil.isNotEmpty(logUserIncome.getCreatedBy()) ? logUserIncome.getCreatedBy().getName() : null);
        userIncomeDetailLogVO.setModifiedTime(logUserIncome.getModifiedTime());

        return userIncomeDetailLogVO;
    }
}