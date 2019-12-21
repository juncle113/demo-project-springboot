package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.domain.Account;
import com.cpto.dapp.domain.LogAccount;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.vo.AccountAssetsVO;
import com.cpto.dapp.pojo.vo.AccountLogVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.repository.AccountRepository;
import com.cpto.dapp.repository.LogAccountRepository;
import com.cpto.dapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 账户ServiceImpl
 *
 * @author sunli
 * @date 2019/01/15
 */
@Service
public class AccountServiceImpl extends BaseServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LogAccountRepository logAccountRepository;

    /**
     * 初始化账户
     *
     * @param userId 用户id
     */
    @Override
    public void initAccount(Long userId) {

        // 锁定账户
        accountRepository.save(new Account(userId, AccountTypeEnum.LOCKED.getCode()));

        // 可用账户
        accountRepository.save(new Account(userId, AccountTypeEnum.AVAILABLE.getCode()));

        // 待审核账户
        accountRepository.save(new Account(userId, AccountTypeEnum.APPROVED.getCode()));
    }

    /**
     * 转账处理
     *
     * @param transferAccountDTO 转账信息
     */
    @Override
    public void transferAccount(TransferAccountDTO transferAccountDTO) {

        /* 1.出账处理 */
        if (ObjectUtil.isNotEmpty(transferAccountDTO.getOutAccountType())) {
            /* 1.1.判断余额是否充足 */
            outAccountBeforeCheck(transferAccountDTO);

            /* 1.2.出账处理 */
            outAccount(transferAccountDTO);

            /* 1.3.记录出账日志 */
            saveOutAccountLog(transferAccountDTO);
        }

        /* 2.入账处理 */
        if (ObjectUtil.isNotEmpty(transferAccountDTO.getInAccountType())) {

            /* 2.1.入账处理 */
            inAccount(transferAccountDTO);

            /* 2.2.记录入账日志 */
            saveInAccountLog(transferAccountDTO);
        }
    }

    /**
     * 根据用户id查询账户记录列表
     *
     * @param searchTime        查询时间
     * @param page              当前页数
     * @param pageSize          每页条数
     * @param userId            用户id
     * @param transferTypeGroup 转账类型分组
     * @param fromCreatedTime   创建开始时间
     * @param toCreatedTime     创建结束时间
     * @return 账户记录列表
     */
    @Override
    public PageVO<AccountLogVO> searchAccountLog(Timestamp searchTime,
                                                 Integer page,
                                                 Integer pageSize,
                                                 Long userId,
                                                 Integer transferTypeGroup,
                                                 String fromCreatedTime,
                                                 String toCreatedTime) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<LogAccount> specification = getSQLWhere(searchTime, userId, transferTypeGroup, fromCreatedTime, toCreatedTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<LogAccount> logAccountPage = logAccountRepository.findAll(specification, pageable);

        List<AccountLogVO> accountLogVOList = new ArrayList<>();
        for (LogAccount logAccount : logAccountPage) {
            accountLogVOList.add(editAccountLogVO(logAccount));
        }

        PageVO<AccountLogVO> accountLogPageVO = new PageVO();
        accountLogPageVO.setRows(accountLogVOList);
        accountLogPageVO.setTotal(logAccountPage.getTotalElements());
        accountLogPageVO.setTotalPage(logAccountPage.getTotalPages());
        accountLogPageVO.setHasNext(logAccountPage.hasNext());
        accountLogPageVO.setSearchTime(searchTime);

        return accountLogPageVO;
    }

    /**
     * 根据用户id查询账户资产信息
     *
     * @param userId 用户id
     * @return 账户资产信息
     */
    @Override
    public AccountAssetsVO findAssets(Long userId) {

        AccountAssetsVO accountAssetsVO = new AccountAssetsVO();

        Account availableAccount = findAccount(userId, AccountTypeEnum.AVAILABLE.getCode());
        Account lockedAccount = findAccount(userId, AccountTypeEnum.LOCKED.getCode());
        Account approvedAccount = findAccount(userId, AccountTypeEnum.APPROVED.getCode());

        accountAssetsVO.setAvailableAmount(availableAccount.getAmount());
        accountAssetsVO.setLockedAmount(lockedAccount.getAmount());
        accountAssetsVO.setApprovedAmount(approvedAccount.getAmount());
        accountAssetsVO.setIncomeAmountSum(sumIncomeAmount(userId));
        accountAssetsVO.setTotalAmount(accountAssetsVO.getAvailableAmount().add(accountAssetsVO.getLockedAmount()));

        return accountAssetsVO;
    }

    /**
     * 根据用户id和账户类型查询账户信息
     *
     * @param accountType 账户类型
     * @return 账户信息
     */
    @Override
    public Account findAccount(Long userId, Integer accountType) {
        Account account = accountRepository.findByUserIdAndAccountType(userId, accountType);
        if (ObjectUtil.isEmpty(account)) {
            throw new DataNotFoundException();
        }
        return account;
    }

    /**
     * 计算该用户的累计收益
     *
     * @param userId 项目id
     * @return 累计收益
     */
    public BigDecimal sumIncomeAmount(Long userId) {

        List<Integer> transferList = new ArrayList<>();
        transferList.add(TransferTypeEnum.INCOME.getCode());
        transferList.add(TransferTypeEnum.REWARD.getCode());

        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());

        return logAccountRepository.sumIncomeAmountByUserId(userId, transferList, statusList);
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime        查询时间
     * @param userId            用户id
     * @param transferTypeGroup 转账类型分组
     * @param fromCreatedTime   创建开始时间
     * @param toCreatedTime     创建结束时间
     * @return 动态查询条件
     */
    private Specification<LogAccount> getSQLWhere(Timestamp searchTime,
                                                  Long userId,
                                                  Integer transferTypeGroup,
                                                  String fromCreatedTime,
                                                  String toCreatedTime) {

        Specification<LogAccount> specification = new Specification<LogAccount>() {
            @Override
            public Predicate toPredicate(Root<LogAccount> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询用户id
                if (ObjectUtil.isNotEmpty(userId)) {
                    predicatesList.add(cb.equal(root.get("userId"), userId));
                }

                // 精确查询转账类型
                if (ObjectUtil.isNotEmpty(transferTypeGroup)) {

                    List<Integer> transferTypeList = getTransferType(transferTypeGroup);

                    CriteriaBuilder.In<Integer> in = cb.in(root.get("transferType"));
                    for (Integer transferType : transferTypeList) {
                        in.value(transferType);
                    }

                    predicatesList.add(in);
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
     * 取得转账类型
     *
     * @param transferTypeGroup 转账类型分组
     * @return 转账类型
     */
    private List<Integer> getTransferType(Integer transferTypeGroup) {

        List<Integer> transferTypeList = new ArrayList<>();

        // 支付相关
        if (ObjectUtil.equals(transferTypeGroup, TransferTypeGroupEnum.PAY.getCode())) {

            // 投资项目、退出投资、释放锁定
            transferTypeList.add(TransferTypeEnum.PAY_ORDER.getCode());
            transferTypeList.add(TransferTypeEnum.CANCEL_ORDER.getCode());
            transferTypeList.add(TransferTypeEnum.RETURN.getCode());
        }
        // 收益相关
        else if (ObjectUtil.equals(transferTypeGroup, TransferTypeGroupEnum.INCOME.getCode())) {

            // 项目收益、大盘奖励
            transferTypeList.add(TransferTypeEnum.INCOME.getCode());
            transferTypeList.add(TransferTypeEnum.REWARD.getCode());
        }
        // 充值相关
        else if (ObjectUtil.equals(transferTypeGroup, TransferTypeGroupEnum.TOP_UP.getCode())) {

            // 充值
            transferTypeList.add(TransferTypeEnum.TOP_UP.getCode());
        }
        // 提币相关
        else if (ObjectUtil.equals(transferTypeGroup, TransferTypeGroupEnum.WITHDRAW.getCode())) {

            // 提币申请、提币申请-手续费、提币失败、提币失败-手续费
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_APPLICATION.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_APPLICATION_FEE.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_FAILURE.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_FAILURE_FEE.getCode());
        }
        // 全部
        else {
            transferTypeList.add(TransferTypeEnum.PAY_ORDER.getCode());
            transferTypeList.add(TransferTypeEnum.CANCEL_ORDER.getCode());
            transferTypeList.add(TransferTypeEnum.RETURN.getCode());
            transferTypeList.add(TransferTypeEnum.INCOME.getCode());
            transferTypeList.add(TransferTypeEnum.REWARD.getCode());
            transferTypeList.add(TransferTypeEnum.TOP_UP.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_APPLICATION.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_APPLICATION_FEE.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_FAILURE.getCode());
            transferTypeList.add(TransferTypeEnum.WITHDRAWAL_FAILURE_FEE.getCode());
        }

        return transferTypeList;
    }

    /**
     * 转账前检查
     *
     * @param transferAccountDTO 转账信息
     */
    private void outAccountBeforeCheck(TransferAccountDTO transferAccountDTO) {

        /* 1.取得出账前账户信息 */
        Account outAccount = findAccount(transferAccountDTO.getOutUserId(), transferAccountDTO.getOutAccountType());

        /* 2.检查余额是否充足 */
        if (outAccount.getAmount().compareTo(transferAccountDTO.getAmount()) < 0) {
            throw new BusinessException(ErrorEnum.BALANCE_NOT_ENOUGH);
        }
    }

    /**
     * 出账处理
     *
     * @param transferAccountDTO 转账信息
     */
    private void outAccount(TransferAccountDTO transferAccountDTO) {
        int rowCount = accountRepository.modifyAmount(transferAccountDTO.getOutUserId(), transferAccountDTO.getOutAccountType(), transferAccountDTO.getAmount().negate(), DateUtil.now());
        if (rowCount != 1) {
            throw new BusinessException(ErrorEnum.TRANSFER_FAILED);
        }
    }

    /**
     * 入账处理
     *
     * @param transferAccountDTO 转账信息
     */
    private void inAccount(TransferAccountDTO transferAccountDTO) {
        int rowCount = accountRepository.modifyAmount(transferAccountDTO.getInUserId(), transferAccountDTO.getInAccountType(), transferAccountDTO.getAmount(), DateUtil.now());
        if (rowCount != 1) {
            throw new BusinessException(ErrorEnum.TRANSFER_FAILED);
        }
    }

    /**
     * 保存出账记录
     *
     * @param transferAccountDTO 转账信息
     */
    private void saveOutAccountLog(TransferAccountDTO transferAccountDTO) {
        Account account = findAccount(transferAccountDTO.getOutUserId(), transferAccountDTO.getOutAccountType());
        saveAccountLog(transferAccountDTO, account, transferAccountDTO.getAmount().negate());
    }

    /**
     * 保存入账记录
     *
     * @param transferAccountDTO 转账信息
     */
    private void saveInAccountLog(TransferAccountDTO transferAccountDTO) {
        Account account = findAccount(transferAccountDTO.getInUserId(), transferAccountDTO.getInAccountType());
        saveAccountLog(transferAccountDTO, account, transferAccountDTO.getAmount());
    }

    /**
     * 保存记录
     *
     * @param transferAccountDTO 转账信息
     * @param account            账户信息
     * @param amount             转账数额
     */
    private void saveAccountLog(TransferAccountDTO transferAccountDTO, Account account, BigDecimal amount) {

        LogAccount logAccount = new LogAccount();

        logAccount.setTransferType(transferAccountDTO.getTransferType());
        logAccount.setTransferMemoZh(transferAccountDTO.getTransferMemoZh());
        logAccount.setTransferMemoEn(transferAccountDTO.getTransferMemoEn());
        logAccount.setUserId(transferAccountDTO.getInUserId());
        logAccount.setAccountId(account.getId());
        logAccount.setAccountType(account.getAccountType());
        logAccount.setAmount(amount);
        logAccount.setBalance(account.getAmount());
        logAccount.setRelationType(RelationTypeEnum.ORDER.getCode());
        logAccount.setRelationId(transferAccountDTO.getRelationId());
        logAccount.setRelationType(transferAccountDTO.getRelationType());
        logAccount.setStatus(StatusEnum.VALID.getCode());
        logAccount.setCreatedTime(DateUtil.now());

        logAccountRepository.save(logAccount);
    }

    /**
     * 编辑账户记录VO
     *
     * @param logAccount 账户记录
     * @return 账户记录VO
     */
    private AccountLogVO editAccountLogVO(LogAccount logAccount) {

        AccountLogVO accountLogVO = new AccountLogVO();

        /* 1.设置转账类型分组 */
        Integer transferTypeGroup = 0;

        // 支付相关：投资项目、退出投资、释放锁定
        if (ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.PAY_ORDER.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.PAY_ORDER.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.CANCEL_ORDER.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.RETURN.getCode())) {

            transferTypeGroup = TransferTypeGroupEnum.PAY.getCode();
        }
        // 收益相关：项目收益、大盘奖励
        else if (ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.INCOME.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.REWARD.getCode())) {

            transferTypeGroup = TransferTypeGroupEnum.INCOME.getCode();
        }
        // 充值相关：充值
        else if (ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.TOP_UP.getCode())) {

            transferTypeGroup = TransferTypeGroupEnum.TOP_UP.getCode();
        }
        // 提币相关：提币申请、提币申请-手续费、提币失败、提币失败-手续费
        else if (ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.WITHDRAWAL_APPLICATION.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.WITHDRAWAL_APPLICATION_FEE.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.WITHDRAWAL_FAILURE.getCode()) ||
                ObjectUtil.equals(logAccount.getTransferType(), TransferTypeEnum.WITHDRAWAL_FAILURE_FEE.getCode())) {

            transferTypeGroup = TransferTypeGroupEnum.WITHDRAW.getCode();
        }

        /* 2.设置其他信息 */
        accountLogVO.setId(logAccount.getId());
        accountLogVO.setAccountId(logAccount.getAccountId());
        accountLogVO.setAccountType(logAccount.getAccountType());
        accountLogVO.setAccountTypeName(AccountTypeEnum.getNameByCode(logAccount.getAccountType()));
        accountLogVO.setTransferType(logAccount.getTransferType());
        accountLogVO.setTransferTypeName(TransferTypeEnum.getNameByCode(logAccount.getTransferType()));
        accountLogVO.setTransferTypeGroup(transferTypeGroup);
        accountLogVO.setTransferTypeGroupName(TransferTypeGroupEnum.getNameByCode(transferTypeGroup));
        accountLogVO.setTransferMemo(LanguageUtil.getTextByLanguage(logAccount.getTransferMemoZh(), logAccount.getTransferMemoEn()));
        accountLogVO.setAmount(logAccount.getAmount());
        accountLogVO.setBalance(logAccount.getBalance());
        accountLogVO.setCoinKind(Constant.COIN_CPTO);
        accountLogVO.setCreatedTime(logAccount.getCreatedTime());

        return accountLogVO;
    }
}