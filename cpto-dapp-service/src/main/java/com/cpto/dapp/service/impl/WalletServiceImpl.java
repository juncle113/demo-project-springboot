package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.*;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.constant.SettingsConstant;
import com.cpto.dapp.domain.*;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.exception.AuthorizedException;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.ParameterException;
import com.cpto.dapp.pojo.dto.CheckApplicationDTO;
import com.cpto.dapp.pojo.dto.TransferAccountDTO;
import com.cpto.dapp.pojo.dto.WalletDTO;
import com.cpto.dapp.pojo.dto.WithdrawalApplicationDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.WalletVO;
import com.cpto.dapp.pojo.vo.WithdrawalApplicationVO;
import com.cpto.dapp.repository.SyncBtcChainRecordRepository;
import com.cpto.dapp.repository.SyncEthChainRecordRepository;
import com.cpto.dapp.repository.WalletRepository;
import com.cpto.dapp.repository.WithdrawalApplicationRepository;
import com.cpto.dapp.service.*;
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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 钱包ServiceImpl
 *
 * @author sunli
 * @date 2019/01/15
 */
@Service
public class WalletServiceImpl extends BaseServiceImpl implements WalletService {

    @Autowired
    private BtcChainService btcChainService;

    @Autowired
    private EthChainService ethChainService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WithdrawalApplicationRepository withdrawalApplicationRepository;

    @Autowired
    private SyncBtcChainRecordRepository syncBtcChainRecordRepository;

    @Autowired
    private SyncEthChainRecordRepository syncEthChainRecordRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Value("${custom.chain.btc.withdraw-address}")
    private String chainBtcWithdrawAddress;

    @Value("${custom.chain.eth.withdraw-address}")
    private String chainEthWithdrawAddress;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 初始化钱包
     *
     * @param userId 用户id
     */
    @Override
    public void initWallet(Long userId) {

        // 内部充值账户：BTC
        Long id = IdUtil.generateIdByCurrentTime();

        String address;
        address = btcChainService.createAccount(String.valueOf(id));
        if (ObjectUtil.isEmpty(address)) {
            if (ObjectUtil.notEquals(profilesActive, Constant.PROD)) {
                System.out.println(ErrorEnum.CHAIN_ACCOUNT_CREATE_FAILED.getMessage());
                address = "none";
            } else {
                throw new BusinessException(ErrorEnum.ACTION_FAILED);
            }
        }

        Wallet wallet = new Wallet();
        wallet.setId(id);
        wallet.setUserId(userId);
        wallet.setAddressType(WalletAddressTypeEnum.TOP_UP.getCode());
        wallet.setCoinKind(Constant.COIN_BTC);
        wallet.setName(Constant.COIN_BTC);
        wallet.setMemo("充值");
        wallet.setAddress(address);
        wallet.setStatus(StatusEnum.VALID.getCode());
        wallet.setCreatedTime(DateUtil.now());
        wallet.setCreatedBy(null);
        walletRepository.save(wallet);

        // 内部充值账户：ETH
        id = IdUtil.generateIdByCurrentTime();

        address = ethChainService.createAccount(String.valueOf(id));
        if (ObjectUtil.isEmpty(address)) {
            if (ObjectUtil.notEquals(profilesActive, Constant.PROD)) {
                System.out.println(ErrorEnum.CHAIN_ACCOUNT_CREATE_FAILED.getMessage());
                address = "none";
            } else {
                throw new BusinessException(ErrorEnum.CHAIN_ACCOUNT_CREATE_FAILED);
            }
        }

        wallet = new Wallet();
        wallet.setId(id);
        wallet.setUserId(userId);
        wallet.setAddressType(WalletAddressTypeEnum.TOP_UP.getCode());
        wallet.setCoinKind(Constant.COIN_ETH);
        wallet.setName(Constant.COIN_ETH);
        wallet.setMemo("充值");
        wallet.setAddress(address);
        wallet.setStatus(StatusEnum.VALID.getCode());
        wallet.setCreatedTime(DateUtil.now());
        wallet.setCreatedBy(null);
        walletRepository.save(wallet);
    }

    /**
     * 查询当前用户的钱包列表
     *
     * @param user 用户
     * @param type 钱包地址类型
     */
    @Override
    public List<WalletVO> findWalletListByType(User user, Integer type) {

        List<WalletVO> walletVOList = new ArrayList<>();
        WalletVO walletVO;

        List<Wallet> walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), type);
        for (Wallet wallet : walletList) {

            walletVO = new WalletVO();

            walletVO.setId(wallet.getId());
            walletVO.setWalletType(wallet.getAddressType());
            walletVO.setWalletTypeName(WalletAddressTypeEnum.getNameByCode(wallet.getAddressType()));
            walletVO.setCoinKind(wallet.getCoinKind());
            walletVO.setName(wallet.getName());
            walletVO.setMemo(wallet.getMemo());
            walletVO.setAddress(wallet.getAddress());

            walletVOList.add(walletVO);
        }

        return walletVOList;
    }

    /**
     * 添加钱包信息
     *
     * @param user      用户
     * @param walletDTO 钱包信息
     */
    @Override
    public void addWallet(User user, WalletDTO walletDTO) {

        Wallet wallet = new Wallet();
        wallet.setId(IdUtil.generateIdByCurrentTime());
        wallet.setUserId(user.getId());
        wallet.setAddressType(WalletAddressTypeEnum.WITHDRAW.getCode());
        wallet.setCoinKind(walletDTO.getCoinKind());
        wallet.setName(walletDTO.getName());
        wallet.setMemo(walletDTO.getMemo());
        wallet.setAddress(walletDTO.getAddress());
        wallet.setStatus(StatusEnum.VALID.getCode());
        wallet.setCreatedTime(DateUtil.now());

        walletRepository.save(wallet);
    }

    /**
     * 修改钱包信息
     *
     * @param user      用户
     * @param walletId  钱包信息id
     * @param walletDTO 钱包信息
     */
    @Override
    public void modifyWallet(User user, Long walletId, WalletDTO walletDTO) {

        /* 1.取得被修改钱包信息 */
        Wallet wallet = walletRepository.findNotNullById(walletId);
        if (ObjectUtil.notEquals(wallet.getUserId(), user.getId())) {
            throw new AuthorizedException();
        }

        /* 2.设置修改内容 */
        wallet.setCoinKind(walletDTO.getCoinKind());
        wallet.setName(walletDTO.getName());
        wallet.setMemo(walletDTO.getMemo());
        wallet.setAddress(wallet.getAddress());
        wallet.setModifiedTime(DateUtil.now());

        walletRepository.save(wallet);
    }

    /**
     * 删除钱包信息
     *
     * @param user     用户
     * @param walletId 钱包信息id
     */
    @Override
    public void removeWallet(User user, Long walletId) {

        /* 1.取得删除钱包信息 */
        Wallet wallet = walletRepository.findNotNullById(walletId);
        if (ObjectUtil.notEquals(wallet.getUserId(), user.getId())) {
            throw new AuthorizedException();
        }

        /* 2.删除钱包信息 */
        walletRepository.deleteById(walletId);
    }

    /**
     * 充值处理
     */
    @Override
    public void topUp() {

        /* 1.取得汇率 */
        SystemExchangeRate cptoToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_CPTO, Constant.COIN_USDT);
        SystemExchangeRate btcToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_BTC, Constant.COIN_USDT);
        SystemExchangeRate ethToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_ETH, Constant.COIN_USDT);

        /* 2.BTC的充值 */
        /* 2.1.取得未处理的BTC区块转账记录 */
        BigDecimal usdtAmount;
        BigDecimal cptoAmount;
        TransferAccountDTO transferAccountDTO;

        List<SyncBtcChainRecord> syncBtcChainRecordList = syncBtcChainRecordRepository.findByIsReadFalse();
        for (SyncBtcChainRecord syncBtcChainRecord : syncBtcChainRecordList) {

            cptoAmount = CoinUtil.btcToCpto(new BigDecimal(syncBtcChainRecord.getAmount()), btcToUsdt.getRate(), cptoToUsdt.getRate());

            transferAccountDTO = new TransferAccountDTO();

            // 因同步区块充值记录，所以出账账户设置成null
            transferAccountDTO.setOutUserId(syncBtcChainRecord.getUserId());
            transferAccountDTO.setOutAccountType(null);

            // 设置入账账户信息（用户id和账户类型（锁定账户））
            transferAccountDTO.setInUserId(syncBtcChainRecord.getUserId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

            // 设置转账数额
            transferAccountDTO.setAmount(cptoAmount);

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.TOP_UP.getCode());
            transferAccountDTO.setTransferMemoZh("充值".concat(Constant.COIN_BTC).concat(StringUtil.HALF_SPACE).concat(String.valueOf(syncBtcChainRecord.getAmount())));
            transferAccountDTO.setTransferMemoEn("Top up ".concat(Constant.COIN_BTC).concat(StringUtil.HALF_SPACE).concat(String.valueOf(syncBtcChainRecord.getAmount())));

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(syncBtcChainRecord.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.TOP_UP.getCode());

            // 不存在出账账户
            accountService.transferAccount(transferAccountDTO);

            syncBtcChainRecord.setIsRead(true);
            syncBtcChainRecordRepository.save(syncBtcChainRecord);
        }

        /* 3.ETH的充值 */
        List<SyncEthChainRecord> syncEthChainRecordList = syncEthChainRecordRepository.findByIsReadFalse();
        for (SyncEthChainRecord syncEthChainRecord : syncEthChainRecordList) {

            BigDecimal amount = MathUtil.divideRoundDown(new BigDecimal(syncEthChainRecord.getEthValue()), Constant.ETH_CONVERT_DEGREE);
            cptoAmount = CoinUtil.ethToCpto(amount, ethToUsdt.getRate(), cptoToUsdt.getRate());

            transferAccountDTO = new TransferAccountDTO();

            // 因同步区块充值记录，所以出账账户设置成null
            transferAccountDTO.setOutUserId(syncEthChainRecord.getUserId());
            transferAccountDTO.setOutAccountType(null);

            // 设置入账账户信息（用户id和账户类型（锁定账户））
            transferAccountDTO.setInUserId(syncEthChainRecord.getUserId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

            // 设置转账数额
            transferAccountDTO.setAmount(cptoAmount);

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.TOP_UP.getCode());
            transferAccountDTO.setTransferMemoZh("充值".concat(Constant.COIN_ETH).concat(StringUtil.HALF_SPACE).concat(String.valueOf(syncEthChainRecord.getEthValue())));
            transferAccountDTO.setTransferMemoEn("Top up ".concat(Constant.COIN_ETH).concat(StringUtil.HALF_SPACE).concat(String.valueOf(syncEthChainRecord.getEthValue())));

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(syncEthChainRecord.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.TOP_UP.getCode());

            // 不存在出账账户
            accountService.transferAccount(transferAccountDTO);

            syncEthChainRecord.setIsRead(true);
            syncEthChainRecordRepository.save(syncEthChainRecord);
        }
    }

    /**
     * 确认提币申请
     *
     * @param user           用户信息
     * @param toCoinKind     提币币种
     * @param toChainAddress 转入地址
     * @param cptoAmount     提币数额
     * @param chainMemo      区块链备注
     * @return 提币申请信息
     */
    @Override
    public WithdrawalApplicationVO confirmWithdrawalApplication(User user, String toCoinKind, String toChainAddress, BigDecimal cptoAmount, String chainMemo) {

        /* 1.检查提币上限和下限 */
        SystemSettings withdrawMaxSettings = systemSettingsService.findSystemSettings(SettingsConstant.NUMBER_WITHDRAW_MAX);
        BigDecimal withdrawMax = new BigDecimal(withdrawMaxSettings.getParamValue());
        if (cptoAmount.compareTo(withdrawMax) > 0) {
            throw new BusinessException(ErrorEnum.AMOUNT_ERROR_1);
        }

        SystemSettings withdrawMinSettings = systemSettingsService.findSystemSettings(SettingsConstant.NUMBER_WITHDRAW_MIN);
        BigDecimal withdrawMin = new BigDecimal(withdrawMinSettings.getParamValue());
        if (cptoAmount.compareTo(withdrawMin) < 0) {
            throw new BusinessException(ErrorEnum.AMOUNT_ERROR_2);
        }

        /* 2.检查可用余额 */
        Account availableAccount = accountService.findAccount(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
        BigDecimal cptoBalance = availableAccount.getAmount().subtract(cptoAmount);
        if (cptoBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorEnum.BALANCE_NOT_ENOUGH);
        }

        /* 3.取得币种兑换汇率 */
        SystemExchangeRate cptoToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_CPTO, Constant.COIN_USDT);
        SystemExchangeRate btcToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_BTC, Constant.COIN_USDT);
        SystemExchangeRate ethToUsdt = exchangeRateService.findExchangeRateByCoin(Constant.COIN_ETH, Constant.COIN_USDT);

        BigDecimal rate;
        if (ObjectUtil.equals(toCoinKind, Constant.COIN_BTC)) {
            rate = CoinUtil.cptoToBtcRate(cptoToUsdt.getRate(), btcToUsdt.getRate());
        } else if (ObjectUtil.equals(toCoinKind, Constant.COIN_ETH)) {
            rate = CoinUtil.cptoToEthRate(cptoToUsdt.getRate(), ethToUsdt.getRate());
        } else {
            throw new ParameterException();
        }

        /* 4.计算兑换数额 */
        BigDecimal toAmount = MathUtil.multiplyRoundDown(12, cptoAmount, rate);
        // 提现后兑换数额小于等于0的场合，提现申请失败
        if (toAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ErrorEnum.AMOUNT_ERROR_3);
        }

        /* 5.计算手续费 */
        /* 5.1.取得兑换币种的手续费 */
        SystemSettings feeSettings;
        if (ObjectUtil.equals(toCoinKind, Constant.COIN_BTC)) {
            feeSettings = systemSettingsService.findSystemSettings(SettingsConstant.NUMBER_BTC_WITHDRAW_FEE_RATE);
        } else if (ObjectUtil.equals(toCoinKind, Constant.COIN_ETH)) {
            feeSettings = systemSettingsService.findSystemSettings(SettingsConstant.NUMBER_ETH_WITHDRAW_FEE_RATE);
        } else {
            throw new ParameterException();
        }
        BigDecimal feeRate = new BigDecimal(feeSettings.getParamValue());
        BigDecimal fee = MathUtil.multiplyRoundDown(cptoAmount, feeRate);

        /* 5.2.扣除手续费 */
        // 余额不足的场合，从提币数额中内扣手续费
        if (cptoBalance.compareTo(fee) < 0) {
            toAmount = toAmount.subtract(fee);
        }
        // 以外的场合，从余额中外扣手续费
        else {
            cptoBalance = cptoBalance.subtract(fee);
        }

        /* 6.设置确认提币申请 */
        WithdrawalApplicationVO withdrawalApplicationVO = new WithdrawalApplicationVO();

        withdrawalApplicationVO.setToChainAddress(toChainAddress);
        withdrawalApplicationVO.setChainMemo(chainMemo);
        withdrawalApplicationVO.setCptoAmount(cptoAmount);
        withdrawalApplicationVO.setToCoinKind(toCoinKind);
        withdrawalApplicationVO.setRate(rate);
        withdrawalApplicationVO.setToAmount(toAmount);
        withdrawalApplicationVO.setFeeCoinKind(Constant.COIN_CPTO);
        withdrawalApplicationVO.setFeeRate(feeRate);
        withdrawalApplicationVO.setFee(fee);
        withdrawalApplicationVO.setCptoBalance(cptoBalance);

        return withdrawalApplicationVO;
    }

    /**
     * 提币申请
     *
     * @param user                     用户信息
     * @param withdrawalApplicationDTO 提币信息
     */
    @Override
    public void withdrawalApplication(User user, WithdrawalApplicationDTO withdrawalApplicationDTO) {

        /* 1.确认提币申请 */
        WithdrawalApplicationVO withdrawalApplicationVO = confirmWithdrawalApplication(user,
                withdrawalApplicationDTO.getToCoinKind(),
                withdrawalApplicationDTO.getToChainAddress(),
                withdrawalApplicationDTO.getCptoAmount(),
                withdrawalApplicationDTO.getChainMemo());

        /* 2.生成提币申请记录 */
        WithdrawalApplication withdrawalApplication = new WithdrawalApplication();

        if (ObjectUtil.equals(withdrawalApplicationDTO.getToCoinKind(), Constant.COIN_BTC)) {
            withdrawalApplication.setFromChainAddress(chainBtcWithdrawAddress);
        } else if (ObjectUtil.equals(withdrawalApplicationDTO.getToCoinKind(), Constant.COIN_ETH)) {
            withdrawalApplication.setFromChainAddress(chainEthWithdrawAddress);
        }

        withdrawalApplication.setUser(user);
        withdrawalApplication.setToChainAddress(withdrawalApplicationVO.getToChainAddress());
        withdrawalApplication.setChainMemo(withdrawalApplicationVO.getChainMemo());
        withdrawalApplication.setCptoAmount(withdrawalApplicationVO.getCptoAmount());
        withdrawalApplication.setToCoinKind(withdrawalApplicationVO.getToCoinKind());
        withdrawalApplication.setRate(withdrawalApplicationVO.getRate());
        withdrawalApplication.setToAmount(withdrawalApplicationVO.getToAmount());
        withdrawalApplication.setFeeCoinKind(withdrawalApplicationVO.getFeeCoinKind());
        withdrawalApplication.setFeeRate(withdrawalApplicationVO.getFeeRate());
        withdrawalApplication.setFee(withdrawalApplicationVO.getFee());
        withdrawalApplication.setCptoBalance(withdrawalApplicationVO.getCptoBalance());
        withdrawalApplication.setStatus(ApplicationStatusEnum.UNTREATED.getCode());
        withdrawalApplication.setCreatedTime(DateUtil.now());

        withdrawalApplication = withdrawalApplicationRepository.save(withdrawalApplication);

        /* 3.转账处理（将提币数额从可用账户转到待审核账户） */
        applicationTransfer(withdrawalApplication);
    }

    /**
     * 根据id取得提币申请详情
     *
     * @param withdrawalApplicationId 提币申请id
     * @return 提币申请详情
     */
    @Override
    public WithdrawalApplicationVO findWithdrawalApplication(Long withdrawalApplicationId) {
        WithdrawalApplication withdrawalApplication = withdrawalApplicationRepository.findNotNullById(withdrawalApplicationId);
        return editWithdrawalApplicationVO(withdrawalApplication);
    }

    /**
     * 审核提币申请
     *
     * @param admin                   管理员
     * @param withdrawalApplicationId 提币申请id
     * @param checkApplicationDTO     审核申请信息
     */
    @Override
    public void checkWithdrawalApplication(ManagerAdmin admin, Long withdrawalApplicationId, CheckApplicationDTO checkApplicationDTO) {

        /* 1.取得被修改的内容 */
        WithdrawalApplication withdrawalApplication = withdrawalApplicationRepository.findNotNullById(withdrawalApplicationId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(checkApplicationDTO.getModifiedTime(), withdrawalApplication.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.申请状态为未处理的场合，才可审核 */
        if (ObjectUtil.notEquals(checkApplicationDTO.getStatus(), ApplicationStatusEnum.UNTREATED.getCode())) {
            throw new BusinessException(ErrorEnum.ACTION_FAILED);
        }

        /* 4.设置修改内容 */
        // 审核申请成功的场合
        if (ObjectUtil.equals(checkApplicationDTO.getStatus(), ApplicationStatusEnum.SUCCESS.getCode())) {
            withdrawalApplication.setStatus(ApplicationStatusEnum.SUCCESS.getCode());
        }
        // 审核申请失败的场合
        else if (ObjectUtil.equals(checkApplicationDTO.getStatus(), ApplicationStatusEnum.FAILED.getCode())) {
            withdrawalApplication.setStatus(ApplicationStatusEnum.FAILED.getCode());
        }
        // 以外的场合，参数错误，处理终止
        else {
            throw new ParameterException();
        }

        withdrawalApplication.setRemark(checkApplicationDTO.getRemark());
        withdrawalApplication.setModifiedBy(admin);
        withdrawalApplication.setModifiedTime(DateUtil.now());
        withdrawalApplicationRepository.save(withdrawalApplication);

        /* 5.转账处理 */
        checkTransfer(withdrawalApplication);

        /* 6.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_WITHDRAWAL_APPLICATION, withdrawalApplication.getId());
    }

    /**
     * 查询满足条件的提币申请
     *
     * @param searchTime      查询时间
     * @param page            当前页数
     * @param pageSize        每页条数
     * @param id              提币申请id
     * @param userId          用户id
     * @param userName        用户名
     * @param coinKind        提币币种
     * @param chainAddress    提币地址
     * @param remark          备注
     * @param status          状态
     * @param fromCreatedTime 申请开始时间
     * @param toCreatedTime   申请结束时间
     * @return 反馈列表
     */
    @Override
    public PageVO<WithdrawalApplicationVO> searchWithdrawalApplication(Timestamp searchTime,
                                                                       Integer page,
                                                                       Integer pageSize,
                                                                       Long id,
                                                                       Long userId,
                                                                       String userName,
                                                                       String coinKind,
                                                                       String chainAddress,
                                                                       String remark,
                                                                       Integer status,
                                                                       String fromCreatedTime,
                                                                       String toCreatedTime) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<WithdrawalApplication> specification = getSQLWhere(id, userId, userName, coinKind, chainAddress, remark, status, fromCreatedTime, toCreatedTime);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<WithdrawalApplication> withdrawalApplicationPage = withdrawalApplicationRepository.findAll(specification, pageable);

        List<WithdrawalApplicationVO> withdrawalApplicationVOList = new ArrayList<>();
        for (WithdrawalApplication withdrawalApplication : withdrawalApplicationPage) {
            withdrawalApplicationVOList.add(editWithdrawalApplicationVO(withdrawalApplication));
        }

        PageVO<WithdrawalApplicationVO> withdrawalApplicationPageVO = new PageVO();
        withdrawalApplicationPageVO.setRows(withdrawalApplicationVOList);
        withdrawalApplicationPageVO.setTotal(withdrawalApplicationPage.getTotalElements());
        withdrawalApplicationPageVO.setTotalPage(withdrawalApplicationPage.getTotalPages());
        withdrawalApplicationPageVO.setHasNext(withdrawalApplicationPage.hasNext());
        withdrawalApplicationPageVO.setSearchTime(searchTime);

        return withdrawalApplicationPageVO;
    }

    /**
     * 生成动态查询条件
     *
     * @param id              提币申请id
     * @param userId          用户id
     * @param userName        用户名
     * @param coinKind        提币币种
     * @param chainAddress    提币地址
     * @param remark          备注
     * @param status          状态
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 动态查询条件
     */
    private Specification<WithdrawalApplication> getSQLWhere(Long id,
                                                             Long userId,
                                                             String userName,
                                                             String coinKind,
                                                             String chainAddress,
                                                             String remark,
                                                             Integer status,
                                                             String fromCreatedTime,
                                                             String toCreatedTime) {

        Specification<WithdrawalApplication> specification = new Specification<WithdrawalApplication>() {
            @Override
            public Predicate toPredicate(Root<WithdrawalApplication> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 精确查询反馈id
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

                // 精确查询提币币种
                if (ObjectUtil.isNotEmpty(coinKind)) {
                    predicatesList.add(cb.or(
                            cb.like(cb.lower(root.get("fromCoinKind")), coinKind.toLowerCase()),
                            cb.like(cb.lower(root.get("toCoinKind")), coinKind.toLowerCase())));
                }

                // 精确查询提币地址
                if (ObjectUtil.isNotEmpty(chainAddress)) {
                    predicatesList.add(cb.or(
                            cb.equal(cb.lower(root.get("fromChainAddress")), chainAddress.toLowerCase()),
                            cb.equal(cb.lower(root.get("toChainAddress")), chainAddress.toLowerCase())));
                }

                // 模糊查询备注
                if (ObjectUtil.isNotEmpty(remark)) {
                    predicatesList.add(cb.like(cb.lower(root.get("remark")), "%" + remark.toLowerCase() + "%"));
                }

                // 精确查询状态
                if (ObjectUtil.isNotEmpty(status)) {
                    predicatesList.add(cb.equal(root.get("status"), status));
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
     * 提币申请转账处理
     *
     * @param withdrawalApplication 提币申请信息
     */
    private void applicationTransfer(WithdrawalApplication withdrawalApplication) {

        /* 1.转账提币数额 */
        TransferAccountDTO transferAccountDTO = new TransferAccountDTO();

        transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
        transferAccountDTO.setOutAccountType(AccountTypeEnum.AVAILABLE.getCode());
        transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
        transferAccountDTO.setInAccountType(AccountTypeEnum.APPROVED.getCode());

        // 设置转账数额
        transferAccountDTO.setAmount(withdrawalApplication.getToAmount());

        // 设置转账类型和说明
        transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_APPLICATION.getCode());
        transferAccountDTO.setTransferMemoZh("提币申请".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));
        transferAccountDTO.setTransferMemoEn("Withdrawal Application ".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));

        // 设置业务关联号（收益记录id）
        transferAccountDTO.setRelationId(withdrawalApplication.getUser().getId());
        transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

        accountService.transferAccount(transferAccountDTO);

        /* 2.转账提币手续费 */
        transferAccountDTO = new TransferAccountDTO();

        transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
        transferAccountDTO.setOutAccountType(AccountTypeEnum.AVAILABLE.getCode());
        transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
        transferAccountDTO.setInAccountType(AccountTypeEnum.APPROVED.getCode());

        // 设置转账数额
        transferAccountDTO.setAmount(withdrawalApplication.getFee());

        // 设置转账类型和说明
        transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_APPLICATION_FEE.getCode());
        transferAccountDTO.setTransferMemoZh("提币申请手续费");
        transferAccountDTO.setTransferMemoEn("Withdrawal Application Fee");

        // 设置业务关联号（收益记录id）
        transferAccountDTO.setRelationId(withdrawalApplication.getId());
        transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

        accountService.transferAccount(transferAccountDTO);
    }

    /**
     * 提币审核转账处理
     *
     * @param withdrawalApplication 提币申请信息
     */
    private void checkTransfer(WithdrawalApplication withdrawalApplication) {

        TransferAccountDTO transferAccountDTO;

        /* 1.申请成功的场合 */
        if (ObjectUtil.equals(withdrawalApplication.getStatus(), ApplicationStatusEnum.SUCCESS.getCode())) {

            /* 1.1.转账提币数额 */
            transferAccountDTO = new TransferAccountDTO();

            // 设置出账账户信息
            transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setOutAccountType(AccountTypeEnum.APPROVED.getCode());

            // 设置入账账户信息（提币转出时，设置为空）
            transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setInAccountType(null);

            // 设置转账数额
            transferAccountDTO.setAmount(withdrawalApplication.getCptoAmount());

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_SUCCESSFUL.getCode());
            transferAccountDTO.setTransferMemoZh("提币".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));
            transferAccountDTO.setTransferMemoEn("Withdrawal ".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(withdrawalApplication.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

            accountService.transferAccount(transferAccountDTO);

            /* 1.2.转账提币手续费 */
            transferAccountDTO = new TransferAccountDTO();

            // 设置出账账户信息
            transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setOutAccountType(AccountTypeEnum.APPROVED.getCode());

            // 设置入账账户信息（提币转出时，设置为空）
            transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setInAccountType(null);

            // 设置转账数额
            transferAccountDTO.setAmount(withdrawalApplication.getFee());

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_SUCCESSFUL_FEE.getCode());
            transferAccountDTO.setTransferMemoZh("提币手续费");
            transferAccountDTO.setTransferMemoEn("Withdrawal Fee");

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(withdrawalApplication.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

            accountService.transferAccount(transferAccountDTO);
        }
        /* 2.申请失败的场合 */
        else if (ObjectUtil.equals(withdrawalApplication.getStatus(), ApplicationStatusEnum.FAILED.getCode())) {

            /* 2.1.转账提币数额 */
            transferAccountDTO = new TransferAccountDTO();

            // 设置出账账户信息
            transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setOutAccountType(AccountTypeEnum.APPROVED.getCode());

            // 设置入账账户信息
            transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

            // 设置转账数额
            transferAccountDTO.setAmount(withdrawalApplication.getCptoAmount());

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_FAILURE.getCode());
            transferAccountDTO.setTransferMemoZh("提币失败".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));
            transferAccountDTO.setTransferMemoEn("Withdrawal Failure ".concat(withdrawalApplication.getToCoinKind().concat(StringUtil.HALF_SPACE).concat(String.valueOf(withdrawalApplication.getToAmount()))));

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(withdrawalApplication.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

            accountService.transferAccount(transferAccountDTO);

            /* 2.2.转账提币手续费 */
            transferAccountDTO = new TransferAccountDTO();

            // 设置出账账户信息
            transferAccountDTO.setOutUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setOutAccountType(AccountTypeEnum.APPROVED.getCode());

            // 设置入账账户信息
            transferAccountDTO.setInUserId(withdrawalApplication.getUser().getId());
            transferAccountDTO.setInAccountType(AccountTypeEnum.AVAILABLE.getCode());

            // 设置转账数额
            transferAccountDTO.setAmount(withdrawalApplication.getFee());

            // 设置转账类型和说明
            transferAccountDTO.setTransferType(TransferTypeEnum.WITHDRAWAL_FAILURE_FEE.getCode());
            transferAccountDTO.setTransferMemoZh("提币失败-手续费");
            transferAccountDTO.setTransferMemoEn("Withdrawal Failure Fee");

            // 设置业务关联号（收益记录id）
            transferAccountDTO.setRelationId(withdrawalApplication.getId());
            transferAccountDTO.setRelationType(RelationTypeEnum.WITHDRAWAL_APPLICATION.getCode());

            accountService.transferAccount(transferAccountDTO);
        }
    }

    /**
     * 编辑提币申请VO
     *
     * @param withdrawalApplication 提币申请
     * @return 提币申请VO
     */
    private WithdrawalApplicationVO editWithdrawalApplicationVO(WithdrawalApplication withdrawalApplication) {

        WithdrawalApplicationVO withdrawalApplicationVO = new WithdrawalApplicationVO();

        withdrawalApplicationVO.setId(withdrawalApplication.getId());
        withdrawalApplicationVO.setUserId(withdrawalApplication.getUser().getId());
        withdrawalApplicationVO.setUserName(withdrawalApplication.getUser().getUserName());
        withdrawalApplicationVO.setToCoinKind(withdrawalApplication.getToCoinKind());
        withdrawalApplicationVO.setFromChainAddress(withdrawalApplication.getFromChainAddress());
        withdrawalApplicationVO.setToChainAddress(withdrawalApplication.getToChainAddress());
        withdrawalApplicationVO.setChainMemo(withdrawalApplication.getChainMemo());
        withdrawalApplicationVO.setRate(withdrawalApplication.getRate());
        withdrawalApplicationVO.setCptoAmount(withdrawalApplication.getCptoAmount());
        withdrawalApplicationVO.setToAmount(withdrawalApplication.getToAmount());
        withdrawalApplicationVO.setFee(withdrawalApplication.getFee());
        withdrawalApplicationVO.setStatus(withdrawalApplication.getStatus());
        withdrawalApplicationVO.setStatusName(ApplicationStatusEnum.getNameByCode(withdrawalApplication.getStatus()));
        withdrawalApplicationVO.setRemark(withdrawalApplication.getRemark());
        withdrawalApplicationVO.setCreatedBy(ObjectUtil.isNotEmpty(withdrawalApplication.getCreatedBy()) ? withdrawalApplication.getCreatedBy().getName() : null);
        withdrawalApplicationVO.setCreatedTime(withdrawalApplication.getCreatedTime());
        withdrawalApplicationVO.setModifiedBy(ObjectUtil.isNotEmpty(withdrawalApplication.getCreatedBy()) ? withdrawalApplication.getCreatedBy().getName() : null);
        withdrawalApplicationVO.setModifiedTime(withdrawalApplication.getModifiedTime());

        return withdrawalApplicationVO;
    }
}