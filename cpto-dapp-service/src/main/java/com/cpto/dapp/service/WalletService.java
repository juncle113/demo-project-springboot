package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.CheckApplicationDTO;
import com.cpto.dapp.pojo.dto.WalletDTO;
import com.cpto.dapp.pojo.dto.WithdrawalApplicationDTO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.WalletVO;
import com.cpto.dapp.pojo.vo.WithdrawalApplicationVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 钱包Service
 *
 * @author sunli
 * @date 2019/01/15
 */
public interface WalletService extends BaseService {

    /**
     * 初始化钱包
     *
     * @param userId 用户id
     */
    void initWallet(Long userId);

    /**
     * 查询当前用户的钱包列表
     *
     * @param user 用户
     * @param type 钱包地址类型
     * @return 钱包列表
     */
    List<WalletVO> findWalletListByType(User user, Integer type);

    /**
     * 添加钱包信息
     *
     * @param user      用户
     * @param walletDTO 钱包信息
     */
    void addWallet(User user, WalletDTO walletDTO);

    /**
     * 修改钱包信息
     *
     * @param user      用户
     * @param walletId  钱包信息id
     * @param walletDTO 钱包信息
     */
    void modifyWallet(User user, Long walletId, WalletDTO walletDTO);

    /**
     * 删除钱包信息
     *
     * @param user     用户
     * @param walletId 钱包信息id
     */
    void removeWallet(User user, Long walletId);

    /**
     * 充值处理
     */
    void topUp();

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
    WithdrawalApplicationVO confirmWithdrawalApplication(User user, String toCoinKind, String toChainAddress, BigDecimal cptoAmount, String chainMemo);

    /**
     * 提币申请
     *
     * @param user                     用户信息
     * @param withdrawalApplicationDTO 提币信息
     */
    void withdrawalApplication(User user, WithdrawalApplicationDTO withdrawalApplicationDTO);

    /**
     * 根据id取得提币申请详情
     *
     * @param withdrawalApplicationId 提币申请id
     * @return 提币申请详情
     */
    WithdrawalApplicationVO findWithdrawalApplication(Long withdrawalApplicationId);

    /**
     * 审核提币申请
     *
     * @param admin                   管理员
     * @param withdrawalApplicationId 提币申请id
     * @param checkApplicationDTO     审核申请信息
     */
    void checkWithdrawalApplication(ManagerAdmin admin, Long withdrawalApplicationId, CheckApplicationDTO checkApplicationDTO);

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
     * @param toChainAddress  提币地址
     * @param remark          备注
     * @param status          状态
     * @param fromCreatedTime 申请开始时间
     * @param toCreatedTime   申请结束时间
     * @return 反馈列表
     */
    PageVO<WithdrawalApplicationVO> searchWithdrawalApplication(Timestamp searchTime,
                                                                Integer page,
                                                                Integer pageSize,
                                                                Long id,
                                                                Long userId,
                                                                String userName,
                                                                String coinKind,
                                                                String toChainAddress,
                                                                String remark,
                                                                Integer status,
                                                                String fromCreatedTime,
                                                                String toCreatedTime);
}