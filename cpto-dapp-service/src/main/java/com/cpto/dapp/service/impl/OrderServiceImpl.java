package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.IdUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.OrderInfo;
import com.cpto.dapp.domain.Project;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.exception.AuthorizedException;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.pojo.dto.OrderDTO;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.vo.AccountAssetsVO;
import com.cpto.dapp.pojo.vo.OrderVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.repository.OrderInfoRepository;
import com.cpto.dapp.repository.ProjectRepository;
import com.cpto.dapp.service.AccountService;
import com.cpto.dapp.service.OrderService;
import com.cpto.dapp.service.UserService;
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

/**
 * 订单ServiceImpl
 *
 * @author sunli
 * @date 2019/01/16
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl implements OrderService {

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    /**
     * 创建订单
     *
     * @param user     用户
     * @param orderDTO 创建订单信息
     * @return 订单信息
     */
    @Override
    public void addOrder(User user, OrderDTO orderDTO) {

        /* 1.取得用户和项目信息 */
        Project project = projectRepository.findNotNullById(orderDTO.getProjectId());

        // 取得邀请人信息
        User inviter = null;
        if (ObjectUtil.isNotEmpty(orderDTO.getInviteCode())) {
            inviter = userService.findByInviteCode(orderDTO.getInviteCode());
        }

        /* 2.检查项目参与条件 */
        checkProjectCondition(user, project, orderDTO.getPayAmount());

        /* 3.追加投资已有订单 */
        OrderInfo orderInfo = updateOrder(user.getId(), project.getId(), orderDTO.getPayAmount());
        if (ObjectUtil.isEmpty(orderInfo)) {
            /* 4.不存在已有订单的场合，生成新订单 */
            orderInfo = createOrder(user, inviter, project, orderDTO.getPayAmount());
        }

        /* 5.扣除账户投资数额 */
        // 用户参与项目募集投资的场合，进行转账操作（可用账户 -> 锁定账户）
        payOrder(orderInfo, orderDTO.getPayAmount());

        /* 6.发放邀请人奖励 */
        if (ObjectUtil.isNotEmpty(inviter)) {
            // TODO【待定】发放邀请人奖励，等待客户确认规则

        }
    }

    /**
     * 查询用户订单列表
     *
     * @param user 用户
     * @return 订单列表
     */
    @Override
    public List<OrderVO> findOrderListByUser(User user) {

        List<OrderVO> orderVOList = new ArrayList<>();

        List<OrderInfo> orderInfoList = orderInfoRepository.findByUserIdOrderByIdDesc(user.getId());
        for (OrderInfo orderInfo : orderInfoList) {
            orderVOList.add(editOrderVO(orderInfo));
        }

        return orderVOList;
    }

    /**
     * 查询项目全部订单列表
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 订单列表
     */
    @Override
    public List<OrderInfo> findOrderListByProject(Long projectId, List<Integer> statusList) {
        return orderInfoRepository.findByProjectIdAndStatusInOrderByIdDesc(projectId, statusList);
    }

    /**
     * 查询满足条件的订单
     *
     * @param searchTime      查询时间
     * @param page            当前页数
     * @param pageSize        每页条数
     * @param id              订单id
     * @param userId          用户id
     * @param userName        用户名
     * @param projectNo       项目编号
     * @param projectName     项目名称
     * @param status          状态
     * @param remark          备注
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 订单列表
     */
    @Override
    public PageVO<OrderVO> searchOrder(Timestamp searchTime,
                                       Integer page,
                                       Integer pageSize,
                                       Long id,
                                       Long userId,
                                       String userName,
                                       String projectNo,
                                       String projectName,
                                       Integer status,
                                       String remark,
                                       String fromCreatedTime,
                                       String toCreatedTime) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<OrderInfo> specification = getSQLWhere(searchTime, id, userId, userName, projectNo, projectName, status, remark, fromCreatedTime, toCreatedTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<OrderInfo> orderInfoPage = orderInfoRepository.findAll(specification, pageable);

        List<OrderVO> orderVOList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfoPage) {
            orderVOList.add(editOrderVO(orderInfo));
        }

        PageVO<OrderVO> orderPageVO = new PageVO();
        orderPageVO.setRows(orderVOList);
        orderPageVO.setTotal(orderInfoPage.getTotalElements());
        orderPageVO.setTotalPage(orderInfoPage.getTotalPages());
        orderPageVO.setHasNext(orderInfoPage.hasNext());
        orderPageVO.setSearchTime(searchTime);

        return orderPageVO;
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime      查询时间
     * @param id              订单id
     * @param userId          用户id
     * @param userName        用户名
     * @param projectNo       项目编号
     * @param projectName     项目名称
     * @param status          状态
     * @param remark          备注
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 动态查询条件
     */
    private Specification<OrderInfo> getSQLWhere(Timestamp searchTime,
                                                 Long id,
                                                 Long userId,
                                                 String userName,
                                                 String projectNo,
                                                 String projectName,
                                                 Integer status,
                                                 String remark,
                                                 String fromCreatedTime,
                                                 String toCreatedTime) {

        Specification<OrderInfo> specification = new Specification<OrderInfo>() {
            @Override
            public Predicate toPredicate(Root<OrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询订单id
                if (ObjectUtil.isNotEmpty(id)) {
                    predicatesList.add(cb.equal(root.get("id"), id));
                }

                // 精确查询用户id
                if (ObjectUtil.isNotEmpty(userId)) {
                    predicatesList.add(cb.equal(root.<User>get("user").get("id"), userId));
                }

                // 模糊查询用户名
                if (ObjectUtil.isNotEmpty(userName)) {
                    predicatesList.add(cb.like(cb.lower(root.<User>get("user").get("userName")), "%" + userName.toLowerCase() + "%"));
                }

                // 模糊查询项目编号
                if (ObjectUtil.isNotEmpty(projectNo)) {
                    predicatesList.add(cb.like(cb.lower(root.<Project>get("project").get("no")), "%" + projectNo.toLowerCase() + "%"));
                }

                // 模糊查询项目名称（条件为或者的关系）
                if (ObjectUtil.isNotEmpty(projectName)) {
                    predicatesList.add(cb.or(
                            cb.like(cb.lower(root.<Project>get("project").get("nameZh")), "%" + projectName.toLowerCase() + "%"),
                            cb.like(cb.lower(root.<Project>get("project").get("nameEn")), "%" + projectName.toLowerCase() + "%")));
                }

                // 精确查询订单状态
                if (ObjectUtil.isNotEmpty(status)) {
                    predicatesList.add(cb.equal(root.get("status"), status));
                }

                // 模糊查询备注
                if (ObjectUtil.isNotEmpty(remark)) {
                    predicatesList.add(cb.like(cb.lower(root.get("remark")), "%" + remark.toLowerCase() + "%"));
                }

                // 范围查询创建时间
                if (ObjectUtil.isNotEmpty(fromCreatedTime)) {
                    predicatesList.add(cb.greaterThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullFromTime(fromCreatedTime))));
                }
                if (ObjectUtil.isNotEmpty(toCreatedTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), Timestamp.valueOf(DateUtil.fullToTime(toCreatedTime))));
                }

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 检查项目参与条件
     *
     * @param user      用户信息
     * @param project   项目信息
     * @param payAmount 投资数额
     */
    private void checkProjectCondition(User user, Project project, BigDecimal payAmount) {

        /* 1.项目状态必须为开始募集 */
        if (ObjectUtil.notEquals(project.getStatus(), ProjectStatusEnum.START.getCode())) {
            throw new BusinessException(ErrorEnum.CREATE_ORDER_FAILED_1);
        }

        /* 2.最低投资数额 */
        if (ObjectUtil.isNotEmpty(project.getConditionMinPayAmount())
                && ObjectUtil.notEquals(project.getConditionMinPayAmount(), BigDecimal.ZERO)) {

            if (payAmount.compareTo(project.getConditionMinPayAmount()) < 0) {
                throw new BusinessException(ErrorEnum.CREATE_ORDER_FAILED_2);
            }
        }

        /* 3.持仓数额 */
        if (ObjectUtil.isNotEmpty(project.getConditionMinLockedAmount())
                && ObjectUtil.notEquals(project.getConditionMinLockedAmount(), BigDecimal.ZERO)) {

            AccountAssetsVO accountAssetsVO = accountService.findAssets(user.getId());

            if (accountAssetsVO.getLockedAmount().compareTo(project.getConditionMinLockedAmount()) < 0) {
                throw new BusinessException(ErrorEnum.CREATE_ORDER_FAILED_3);
            }
        }

        /* 4.注册时间 */
        if (ObjectUtil.isNotEmpty(project.getConditionMinRegisterDays())
                && project.getConditionMinRegisterDays() != 0) {

            Timestamp minRegisterTime = DateUtil.timestampSubDay(DateUtil.now(), project.getConditionMinRegisterDays());

            if (user.getCreatedTime().compareTo(minRegisterTime) > 0) {
                throw new BusinessException(ErrorEnum.CREATE_ORDER_FAILED_4);
            }
        }

        /* 5.募集人数 */
        if (ObjectUtil.isNotEmpty(project.getConditionMaxJoinNumber())
                && project.getConditionMaxJoinNumber() != 0) {

            Integer joinNumber = countJoinNumberByProject(project.getId());

            if (project.getConditionMaxJoinNumber() - joinNumber <= 0) {
                throw new BusinessException(ErrorEnum.CREATE_ORDER_FAILED_5);
            }
        }
    }

    /**
     * 生成订单
     *
     * @param user      用户信息
     * @param inviter   邀请人信息
     * @param project   项目信息
     * @param payAmount 支付数额
     * @return 订单信息
     */
    private OrderInfo createOrder(User user, User inviter, Project project, BigDecimal payAmount) {

        OrderInfo orderInfo = new OrderInfo();

        orderInfo.setId(IdUtil.generateIdByCurrentTime());
        orderInfo.setUser(user);
        orderInfo.setInviter(inviter);
        orderInfo.setProject(project);
        orderInfo.setPayAmount(payAmount);
        orderInfo.setStatus(OrderStatusEnum.PAID.getCode());
        orderInfo.setCreatedBy(null);
        orderInfo.setCreatedTime(DateUtil.now());

        return orderInfoRepository.save(orderInfo);
    }

    /**
     * 追加投资已存在的订单
     *
     * @param userId    用户信息
     * @param projectId 邀请人信息
     * @param payAmount 投资数额
     * @return 订单信息
     */
    private OrderInfo updateOrder(Long userId, Long projectId, BigDecimal payAmount) {

        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());

        // 查询已支付状态的订单，超过1条记录的场合报错
        List<OrderInfo> orderInfoList = orderInfoRepository.findByUserIdAndProjectIdAndStatusInOrderByIdDesc(userId, projectId, statusList);
        if (ObjectUtil.isNotEmptyCollection(orderInfoList)) {
            if (orderInfoList.size() > 1) {
                throw new BusinessException(ErrorEnum.ORDER_ERROR);
            } else {
                // 更新订单的投资数额
                OrderInfo orderInfo = orderInfoList.get(0);
                orderInfo.setPayAmount(orderInfo.getPayAmount().add(payAmount));
                orderInfo.setModifiedBy(null);
                orderInfo.setModifiedTime(DateUtil.now());
                return orderInfoRepository.save(orderInfo);
            }
        }

        return null;
    }

    /**
     * 扣除账户投资数额
     *
     * @param orderInfo 订单信息
     * @param payAmount 投资数额
     */
    private void payOrder(OrderInfo orderInfo, BigDecimal payAmount) {

        /* 1.转账处理 */
        TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

        // 设置出账账户信息（用户id和账户类型（可用账户））
        transferAccountDTO.setOutUserId(orderInfo.getUser().getId());
        transferAccountDTO.setOutAccountType(AccountTypeEnum.AVAILABLE.getCode());

        // 设置入账账户信息（用户id和账户类型（锁定账户））
        transferAccountDTO.setInUserId(orderInfo.getUser().getId());
        transferAccountDTO.setInAccountType(AccountTypeEnum.LOCKED.getCode());

        // 设置转账数额
        transferAccountDTO.setAmount(payAmount);

        // 设置转账类型和说明
        transferAccountDTO.setTransferType(TransferTypeEnum.PAY_ORDER.getCode());
        transferAccountDTO.setTransferMemoZh(orderInfo.getProject().getNameZh());
        transferAccountDTO.setTransferMemoEn(orderInfo.getProject().getNameEn());

        // 设置业务关联号（订单id）
        transferAccountDTO.setRelationId(orderInfo.getId());
        transferAccountDTO.setRelationType(RelationTypeEnum.ORDER.getCode());

        accountService.transferAccount(transferAccountDTO);
    }

    /**
     * 退出投资
     *
     * @param user    用户
     * @param orderId 订单id
     */
    @Override
    public void cancelOrder(User user, Long orderId) {

        /* 1.取得订单信息 */
        OrderInfo orderInfo = orderInfoRepository.findNotNullById(orderId);
        if (ObjectUtil.notEquals(orderInfo.getUser().getId(), user.getId())) {
            throw new AuthorizedException();
        }

        /* 2.检查信息 */
        /* 2.1.检查项目状态（非评估完成状态不可退出投资） */
        Project project = projectRepository.findNotNullById(orderInfo.getProject().getId());
        if (ObjectUtil.notEquals(project.getStatus(), ProjectStatusEnum.S1_END.getCode())
                && ObjectUtil.notEquals(project.getStatus(), ProjectStatusEnum.S2_END.getCode())) {
            throw new BusinessException(ErrorEnum.CANCEL_ORDER_FAILED_1);
        }

        /* 2.2.检查订单状态（只有已支付状态可以退出） */
        if (ObjectUtil.notEquals(orderInfo.getStatus(), OrderStatusEnum.PAID.getCode())) {
            throw new BusinessException(ErrorEnum.CANCEL_ORDER_FAILED_2);
        }

        /* 3.更改订单状态（3：已取消） */
        orderInfo.setStatus(OrderStatusEnum.CANCELED.getCode());
        orderInfo.setModifiedBy(null);
        orderInfo.setModifiedTime(DateUtil.now());
        orderInfoRepository.save(orderInfo);

        /* 4.撤回投资 */
        TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

        // 设置出账账户信息（用户id和账户类型（锁定账户））
        transferAccountDTO.setOutUserId(orderInfo.getUser().getId());
        transferAccountDTO.setOutAccountType(AccountTypeEnum.LOCKED.getCode());

        // 设置入账账户信息（用户id和账户类型（可用账户））
        transferAccountDTO.setInUserId(orderInfo.getUser().getId());
        transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

        // 设置转账数额（投资数额）
        transferAccountDTO.setAmount(orderInfo.getPayAmount());

        // 设置转账类型和说明
        transferAccountDTO.setTransferType(TransferTypeEnum.CANCEL_ORDER.getCode());
        transferAccountDTO.setTransferMemoZh(orderInfo.getProject().getNameZh());
        transferAccountDTO.setTransferMemoEn(orderInfo.getProject().getNameEn());

        // 设置业务关联号（订单id）
        transferAccountDTO.setRelationId(orderInfo.getId());
        transferAccountDTO.setRelationType(RelationTypeEnum.ORDER.getCode());

        accountService.transferAccount(transferAccountDTO);
    }

    /**
     * 保存订单信息
     *
     * @param orderInfo 订单信息
     */
    @Override
    public void saveOrder(OrderInfo orderInfo) {
        orderInfoRepository.save(orderInfo);
    }

    /**
     * 项目失败时，作废投资订单
     *
     * @param admin     管理员
     * @param projectId 项目id
     */
    @Override
    public void invalidateOrderByProjectFailure(ManagerAdmin admin, Long projectId) {

        /* 1.取得相关订单 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());

        List<OrderInfo> orderInfoList = orderInfoRepository.findByProjectIdAndStatusInOrderByIdDesc(projectId, statusList);
        for (OrderInfo orderInfo : orderInfoList) {

            /* 2.更新订单状态为无效 */
            orderInfo.setStatus(OrderStatusEnum.INVALID.getCode());
            orderInfo.setModifiedBy(admin);
            orderInfo.setModifiedTime(DateUtil.now());
            orderInfoRepository.save(orderInfo);

            /* 3.退回投资 */
            TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

            // 设置出账账户信息（用户id和账户类型（锁定账户））
            transferAccountDTO.setOutUserId(orderInfo.getUser().getId());
            transferAccountDTO.setOutAccountType(AccountTypeEnum.LOCKED.getCode());

            // 设置入账账户信息（用户id和账户类型（可用账户））
            transferAccountDTO.setInUserId(orderInfo.getUser().getId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

            // 设置转账数额（投资数额）
            transferAccountDTO.setAmount(orderInfo.getPayAmount());

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.INVALIDATE_ORDER.getCode());
            transferAccountDTO.setTransferMemoZh(orderInfo.getProject().getNameZh());
            transferAccountDTO.setTransferMemoEn(orderInfo.getProject().getNameEn());

            // 设置业务关联号（订单id）
            transferAccountDTO.setRelationId(orderInfo.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.ORDER.getCode());

            accountService.transferAccount(transferAccountDTO);
        }
    }

    /**
     * 项目成功时，更新投资订单
     *
     * @param admin     管理员
     * @param projectId 项目id
     */
    @Override
    public void updateOrderByProjectSuccess(ManagerAdmin admin, Long projectId) {

        /* 1.取得项目信息 */
        Project project = projectRepository.findNotNullById(projectId);

        /* 2.取得项目相关订单 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());

        List<OrderInfo> orderInfoList = orderInfoRepository.findByProjectIdAndStatusInOrderByIdDesc(projectId, statusList);
        for (OrderInfo orderInfo : orderInfoList) {

            /* 3.设置订单锁仓释放时间（项目投资成功时间 + 锁仓天数） */
            orderInfo.setLockEndTime(DateUtil.timestampAddDay(DateUtil.now(), project.getLockDays()));
            orderInfo.setModifiedBy(admin);
            orderInfo.setModifiedTime(DateUtil.now());
            orderInfoRepository.save(orderInfo);
        }
    }

    /**
     * 完成订单（订单锁仓释放时间到期后，释放投资）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeOrder() {

        /* 1.取得未完成订单信息 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());

        List<OrderInfo> orderInfoList = orderInfoRepository.findByStatusInOrderByIdDesc(statusList);
        for (OrderInfo orderInfo : orderInfoList) {

            /* 2.锁仓释放时间到期的场合，释放投资 */
            if (DateUtil.isExpires(orderInfo.getLockEndTime())) {

                /* 2.1.更新订单状态为完成 */
                orderInfo.setStatus(OrderStatusEnum.COMPLETED.getCode());
                orderInfo.setModifiedBy(null);
                orderInfo.setModifiedTime(DateUtil.now());
                orderInfoRepository.save(orderInfo);

                /* 2.2.退回投资 */
                TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

                // 设置出账账户信息（用户id和账户类型（锁定账户））
                transferAccountDTO.setOutUserId(orderInfo.getUser().getId());
                transferAccountDTO.setOutAccountType(AccountTypeEnum.LOCKED.getCode());

                // 设置入账账户信息（用户id和账户类型（可用账户））
                transferAccountDTO.setInUserId(orderInfo.getUser().getId());
                transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

                // 设置转账数额（投资数额）
                transferAccountDTO.setAmount(orderInfo.getPayAmount());

                // 设置转账类型和说明
                transferAccountDTO.setTransferType(TransferTypeEnum.RETURN.getCode());
                transferAccountDTO.setTransferMemoZh(orderInfo.getProject().getNameZh());
                transferAccountDTO.setTransferMemoEn(orderInfo.getProject().getNameEn());

                // 设置业务关联号（订单id）
                transferAccountDTO.setRelationId(orderInfo.getId());
                transferAccountDTO.setRelationType(RelationTypeEnum.ORDER.getCode());

                accountService.transferAccount(transferAccountDTO);
            }
        }
    }

    /**
     * 统计投资某项目的全部订单数量
     *
     * @param projectId 项目id
     */
    @Override
    public Integer countOrderByProject(Long projectId) {
        return orderInfoRepository.countByProjectId(projectId);
    }

    /**
     * 统计参与该项目的投资人数
     *
     * @param projectId 项目id
     * @return 投资人数
     */
    @Override
    public Integer countJoinNumberByProject(Long projectId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());
        statusList.add(OrderStatusEnum.COMPLETED.getCode());
        return orderInfoRepository.countByProjectIdAndStatusIn(projectId, statusList);
    }

    /**
     * 统计该项目的投资数额合计
     *
     * @param projectId 项目id
     * @return 投资数额合计
     */
    @Override
    public BigDecimal sumPayAmountByProject(Long projectId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());
        statusList.add(OrderStatusEnum.COMPLETED.getCode());
        return orderInfoRepository.sumPayAmountByProjectIdAndStatusIn(projectId, statusList);
    }


    /**
     * 编辑订单VO
     *
     * @param orderInfo 订单
     * @return 订单VO
     */
    private OrderVO editOrderVO(OrderInfo orderInfo) {

        OrderVO orderVO = new OrderVO();

        orderVO.setId(orderInfo.getId());
        orderVO.setUserId(orderInfo.getUser().getId());
        orderVO.setUserName(orderInfo.getUser().getUserName());
        orderVO.setProjectId(orderInfo.getProject().getId());
        orderVO.setProjectNo(orderInfo.getProject().getNo());
        orderVO.setProjectName(LanguageUtil.getTextByLanguage(orderInfo.getProject().getNameZh(), orderInfo.getProject().getNameEn()));
        orderVO.setProjectNameZh(orderInfo.getProject().getNameZh());
        orderVO.setProjectNameEn(orderInfo.getProject().getNameEn());
        orderVO.setPayAmount(orderInfo.getPayAmount());
        orderVO.setLockDays(orderInfo.getProject().getLockDays());
        orderVO.setLockEndTime(orderInfo.getLockEndTime());
        orderVO.setStatus(orderInfo.getStatus());
        orderVO.setStatusName(OrderStatusEnum.getNameByCode(orderInfo.getStatus()));
        orderVO.setProjectStatus(orderInfo.getProject().getStatus());
        orderVO.setProjectStatusName(ProjectStatusEnum.getNameByCode(orderInfo.getProject().getStatus()));
        orderVO.setRemark(orderInfo.getRemark());
        orderVO.setCreatedBy(ObjectUtil.isNotEmpty(orderInfo.getCreatedBy()) ? orderInfo.getCreatedBy().getName() : null);
        orderVO.setCreatedTime(orderInfo.getCreatedTime());
        orderVO.setModifiedBy(ObjectUtil.isNotEmpty(orderInfo.getCreatedBy()) ? orderInfo.getCreatedBy().getName() : null);
        orderVO.setModifiedTime(orderInfo.getModifiedTime());

        /* 项目是否可见 */
        // 项目状态为准备和无效以外的场合，可以
        if (ObjectUtil.notEquals(orderInfo.getProject().getStatus(), ProjectStatusEnum.INVALID.getCode()) &&
                ObjectUtil.notEquals(orderInfo.getProject().getStatus(), ProjectStatusEnum.READY.getCode())) {
            orderVO.setReviewable(true);
        } else {
            orderVO.setReviewable(false);
        }

        /* 项目是否可投资 */
        /* 项目是否可邀请 */
        // 项目状态为开始募集的场合，并且订单状态为已支付的场合，可以
        if (ObjectUtil.equals(orderInfo.getProject().getStatus(), ProjectStatusEnum.START.getCode()) &&
                ObjectUtil.equals(orderInfo.getStatus(), OrderStatusEnum.PAID.getCode())) {
            orderVO.setPayable(true);
            orderVO.setInviteable(true);
        } else {
            orderVO.setPayable(false);
            orderVO.setInviteable(false);
        }

        /* 项目是否可退出 */
        // 项目状态为S1完成和S2完成的场合，并且订单状态为已支付的场合，可以
        if ((ObjectUtil.equals(orderInfo.getProject().getStatus(), ProjectStatusEnum.S1_END.getCode()) || ObjectUtil.equals(orderInfo.getProject().getStatus(), ProjectStatusEnum.S2_END.getCode())) &&
                ObjectUtil.equals(orderInfo.getStatus(), OrderStatusEnum.PAID.getCode())) {
            orderVO.setCancelable(true);
        } else {
            orderVO.setCancelable(false);
        }

        return orderVO;
    }
}