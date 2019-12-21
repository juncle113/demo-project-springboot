package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.MathUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.*;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.pojo.dto.ProjectIncomeDTO;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.vo.ProjectIncomeLogVO;
import com.cpto.dapp.repository.LogProjectIncomeRepository;
import com.cpto.dapp.repository.LogUserIncomeRepository;
import com.cpto.dapp.repository.ProjectRepository;
import com.cpto.dapp.service.AccountService;
import com.cpto.dapp.service.OrderService;
import com.cpto.dapp.service.ProjectIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 收益ServiceImpl
 *
 * @author sunli
 * @date 2019/02/22
 */
@Service
public class ProjectIncomeServiceImpl extends BaseServiceImpl implements ProjectIncomeService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LogUserIncomeRepository logUserIncomeRepository;

    @Autowired
    private LogProjectIncomeRepository logProjectIncomeRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    /**
     * 发放项目收益
     *
     * @param admin            管理员
     * @param projectId        项目id
     * @param projectIncomeDTO 项目收益信息
     */
    @Override
    public void grantProjectIncome(ManagerAdmin admin, Long projectId, ProjectIncomeDTO projectIncomeDTO) {

        /* 1.取得项目信息 */
        Project project = findProjectById(projectId);

        /* 2.检查发放信息 */
        checkInfo(project);

        /* 3.保存发放项目收益记录 */
        LogProjectIncome logProjectIncome = saveProjectIncomeLog(admin, project, projectIncomeDTO);

        /* 4.发放奖励 */
        grantIncome(admin, project, projectIncomeDTO.getAmount(), logProjectIncome);

        /* 5.更新发放项目收益记录 */
        updateProjectIncomeLog(logProjectIncome);

        /* 6.记录管理日志 */
        saveManagerLog(admin, logProjectIncome.getId());
    }

    /**
     * 取得项目收益记录列表
     *
     * @param projectId 项目id
     */
    @Override
    public List<ProjectIncomeLogVO> findProjectIncomeLogList(Long projectId) {

        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());
        statusList.add(StatusEnum.INVALID.getCode());

        List<LogProjectIncome> logProjectIncomeList = logProjectIncomeRepository.findByProjectIdAndStatusInOrderByIdDesc(projectId, statusList);

        List<ProjectIncomeLogVO> projectIncomeLogVOList = new ArrayList<>();
        for (LogProjectIncome logProjectIncome : logProjectIncomeList) {
            projectIncomeLogVOList.add(editProjectIncomeLogVO(logProjectIncome));
        }

        return projectIncomeLogVOList;
    }

    /**
     * 撤销项目收益
     *
     * @param admin     管理员
     * @param projectId 项目id
     * @param incomeId  收益id
     */
    @Override
    public void cancelProjectIncome(ManagerAdmin admin, Long projectId, Long incomeId) {

        // TODO【改善】撤销项目收益
        // 根据项目收益记录id，查询用户收益记录表；判断用户收益状态（是否已领取），根据用户收益记录的数额，从未发放的锁定账户余额中扣除
    }

    /**
     * 取得项目信息
     *
     * @param projectId 项目id
     * @return 项目信息
     */
    private Project findProjectById(Long projectId) {
        return projectRepository.findNotNullById(projectId);
    }

    /**
     * 检查发放信息
     *
     * @param project 项目信息
     */
    private void checkInfo(Project project) {

        // 未经历S1完成状态，无法发放
        if (ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.READY.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.START.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.SUCCESS_1.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.FAILURE_1.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.S1.getCode()) ||
                ObjectUtil.equals(project.getStatus(), ProjectStatusEnum.INVALID.getCode())) {
            throw new BusinessException(ErrorEnum.GRANT_INCOME_FAILED_1);
        }
    }

    /**
     * 保存发放项目收益记录
     *
     * @param admin            管理员
     * @param project          项目信息
     * @param projectIncomeDTO 项目收益信息
     * @return 项目收益记录
     */
    private LogProjectIncome saveProjectIncomeLog(ManagerAdmin admin, Project project, ProjectIncomeDTO projectIncomeDTO) {

        // 保存发放项目收益记录
        LogProjectIncome logProjectIncome = new LogProjectIncome();

        logProjectIncome.setProjectId(project.getId());
        logProjectIncome.setProjectStatus(projectIncomeDTO.getProjectStatus());
        logProjectIncome.setAmount(projectIncomeDTO.getAmount());
        logProjectIncome.setRemark(projectIncomeDTO.getRemark());
        logProjectIncome.setStatus(IncomeStatusEnum.START.getCode());
        logProjectIncome.setCreatedBy(admin);
        logProjectIncome.setCreatedTime(DateUtil.now());

        return logProjectIncomeRepository.save(logProjectIncome);
    }

    /**
     * 发放收益
     *
     * @param admin             管理员
     * @param project           项目信息
     * @param incomeTotalAmount 项目收益总额
     * @param logProjectIncome  项目收益记录
     */
    private void grantIncome(ManagerAdmin admin, Project project, BigDecimal incomeTotalAmount, LogProjectIncome logProjectIncome) {

        /* 1.查询投资总额（状态为有效订单的投资数额合计） */
        BigDecimal payAmountSum = orderService.sumPayAmountByProject(project.getId());
        if (ObjectUtil.equals(payAmountSum, BigDecimal.ZERO)) {
            throw new BusinessException(ErrorEnum.GRANT_INCOME_FAILED_2);
        }

        /* 2.发放项目收益 */
        // 个人投资占比
        BigDecimal payRate;
        // 个人收益数额
        BigDecimal incomeAmount;
        // 根据每笔投资订单发放个人收益
        TransferAccountDTO transferAccountDTO;

        /* 2.1.查询有效的订单信息 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatusEnum.PAID.getCode());
        statusList.add(OrderStatusEnum.COMPLETED.getCode());
        List<OrderInfo> orderInfoList = orderService.findOrderListByProject(project.getId(), statusList);

        if (ObjectUtil.isEmptyCollection(orderInfoList)) {
            throw new BusinessException(ErrorEnum.GRANT_INCOME_FAILED_2);
        }

        for (OrderInfo orderInfo : orderInfoList) {

            /* 2.2.计算个人收益 */
            // 个人投资占比 = 个人投资数额 / 该项目投资总额
            payRate = MathUtil.divideRoundDown(orderInfo.getPayAmount(), payAmountSum);
            // 个人收益数额 = 项目收益总额 * 个人投资占比
            incomeAmount = MathUtil.multiplyRoundDown(incomeTotalAmount, payRate);

            /* 2.3.保存用户收益记录 */
            LogUserIncome logUserIncome = new LogUserIncome();
            logUserIncome.setLogProjectIncome(logProjectIncome);
            logUserIncome.setProject(project);
            logUserIncome.setUserId(orderInfo.getUser().getId());
            logUserIncome.setOrderInfo(orderInfo);
            logUserIncome.setAmount(incomeAmount);
            logUserIncome.setRemark(logProjectIncome.getRemark());
            logUserIncome.setCreatedBy(admin);
            logUserIncome.setCreatedTime(DateUtil.now());
            logUserIncome.setStatus(StatusEnum.VALID.getCode());
            logUserIncome = logUserIncomeRepository.save(logUserIncome);

            /* 2.4.转账处理 */
            transferAccountDTO = new TransferAccountDTO();

            // 因系统发放收益，所以无需设置出账账户
            transferAccountDTO.setOutUserId(orderInfo.getUser().getId());
            transferAccountDTO.setOutAccountType(null);

            // 设置入账账户信息（用户id和账户类型（锁定账户））
            transferAccountDTO.setInUserId(orderInfo.getUser().getId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.LOCKED.getCode());

            // 设置转账数额
            transferAccountDTO.setAmount(incomeAmount);

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.INCOME.getCode());
            transferAccountDTO.setTransferMemoZh(project.getNameZh());
            transferAccountDTO.setTransferMemoEn(project.getNameEn());

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(logUserIncome.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.INCOME.getCode());

            // 不存在出账账户
            accountService.transferAccount(transferAccountDTO);
        }
    }

    /**
     * 更新发放项目收益记录
     *
     * @param logProjectIncome 项目收益记录
     */
    private void updateProjectIncomeLog(LogProjectIncome logProjectIncome) {

        logProjectIncome.setStatus(IncomeStatusEnum.END.getCode());
        logProjectIncome.setModifiedBy(null);
        logProjectIncome.setModifiedTime(DateUtil.now());

        logProjectIncomeRepository.save(logProjectIncome);
    }

    /**
     * 记录管理日志
     *
     * @param admin              管理员
     * @param logProjectIncomeId 项目收益记录id
     */
    private void saveManagerLog(ManagerAdmin admin, Long logProjectIncomeId) {
        managerLogService.saveManagerLog(admin, ManagerLogConstant.SEND_PROJECT_INCOME, logProjectIncomeId);
    }

    /**
     * 编辑项目收益记录VO
     *
     * @param logProjectIncome 项目收益记录
     * @return 项目收益记录VO
     */
    private ProjectIncomeLogVO editProjectIncomeLogVO(LogProjectIncome logProjectIncome) {

        ProjectIncomeLogVO projectIncomeLogVO = new ProjectIncomeLogVO();

        projectIncomeLogVO.setId(logProjectIncome.getId());
        projectIncomeLogVO.setProjectId(logProjectIncome.getProjectId());
        projectIncomeLogVO.setProjectStatus(logProjectIncome.getProjectStatus());
        projectIncomeLogVO.setProjectStatusName(ProjectStatusEnum.getNameByCode(logProjectIncome.getProjectStatus()));
        projectIncomeLogVO.setAmount(logProjectIncome.getAmount());
        projectIncomeLogVO.setStatus(logProjectIncome.getStatus());
        projectIncomeLogVO.setStatusName(IncomeStatusEnum.getNameByCode(logProjectIncome.getStatus()));
        projectIncomeLogVO.setRemark(logProjectIncome.getRemark());
        projectIncomeLogVO.setCreatedBy(ObjectUtil.isNotEmpty(logProjectIncome.getCreatedBy()) ? logProjectIncome.getCreatedBy().getName() : null);
        projectIncomeLogVO.setCreatedTime(logProjectIncome.getCreatedTime());
        projectIncomeLogVO.setModifiedBy(ObjectUtil.isNotEmpty(logProjectIncome.getCreatedBy()) ? logProjectIncome.getCreatedBy().getName() : null);
        projectIncomeLogVO.setModifiedTime(logProjectIncome.getModifiedTime());

        return projectIncomeLogVO;
    }
}