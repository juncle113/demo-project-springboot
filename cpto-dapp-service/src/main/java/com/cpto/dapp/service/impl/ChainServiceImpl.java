package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.MathUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.SettingsConstant;
import com.cpto.dapp.domain.SyncBtcChainRecord;
import com.cpto.dapp.domain.SyncEthChainRecord;
import com.cpto.dapp.domain.SystemSettings;
import com.cpto.dapp.domain.Wallet;
import com.cpto.dapp.enums.ErrorEnum;
import com.cpto.dapp.exception.BusinessException;
import com.cpto.dapp.pojo.dto.BtcChainRecordDTO;
import com.cpto.dapp.pojo.dto.EthChainRecordDTO;
import com.cpto.dapp.repository.SyncBtcChainRecordRepository;
import com.cpto.dapp.repository.SyncEthChainRecordRepository;
import com.cpto.dapp.repository.WalletRepository;
import com.cpto.dapp.service.BtcChainService;
import com.cpto.dapp.service.ChainService;
import com.cpto.dapp.service.EthChainService;
import com.cpto.dapp.service.SystemSettingsService;
import com.cpto.dapp.util.ChainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 钱包ServiceImpl
 *
 * @author sunli
 * @date 2019/04/15
 */
@Service
public class ChainServiceImpl extends BaseServiceImpl implements ChainService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SyncBtcChainRecordRepository syncBtcChainRecordRepository;

    @Autowired
    private SyncEthChainRecordRepository syncEthChainRecordRepository;

    @Autowired
    private BtcChainService btcChainService;

    @Autowired
    private EthChainService ethChainService;

    @Autowired
    private SystemSettingsService systemSettingsService;

    @Value("${custom.chain.eth.recovery-address}")
    private String ethRecoveryAddress;

    private static BigDecimal ethRecoveryAmount;

    @Override
    public void syncChainRecord() {

        /* 1.取得回收用系统参数 */
        SystemSettings systemSettings = systemSettingsService.findSystemSettings(SettingsConstant.NUMBER_ETH_RECOVERY_AMOUNT);
        ethRecoveryAmount = new BigDecimal(systemSettings.getParamValue());

        /* 2.同步用户账号转账信息 */
        /* 2.1.取得钱包信息 */
        List<Wallet> walletList = walletRepository.findAll();
        for (Wallet wallet : walletList) {

            /* 2.2.同步BTC记录 */
            if (ObjectUtil.equals(wallet.getCoinKind(), Constant.COIN_BTC)) {
                syncBtcChainRecord(wallet);
            }

            /* 2.3.同步ETH记录 */
            else if (ObjectUtil.equals(wallet.getCoinKind(), Constant.COIN_ETH)) {
                syncEthChainRecord(wallet);
            }
        }

        /* 3.同步系统回收用账号转账信息 */
        /* 3.1.同步ETH记录 */
        Wallet wallet = new Wallet();
        wallet.setAddress(ethRecoveryAddress);
        syncEthChainRecord(wallet);
    }

    private void syncBtcChainRecord(Wallet wallet) {

        /* 1.取得区块转账信息 */
        List<BtcChainRecordDTO> btcChainRecordDTOList = btcChainService.findRecord(String.valueOf(wallet.getId()), 100);

        for (BtcChainRecordDTO btcChainRecordDTO : btcChainRecordDTOList) {

            /* 2.已经同步的场合退出 */
            if (syncBtcChainRecordRepository.existsByTxId(btcChainRecordDTO.getTxid())) {
                break;
            }

            /* 3.该地址记录的转账类型为收款，且区块链确认数为6以上的场合，同步记录 */
            if (ObjectUtil.equals(wallet.getAddress().toLowerCase(), btcChainRecordDTO.getAddress().toLowerCase()) &&
                    "receive".equals(btcChainRecordDTO.getCategory()) &&
                    Long.valueOf(btcChainRecordDTO.getConfirmations()) >= 6) {

                /* 3.1.保存记录 */
                saveBtcChainRecord(wallet, btcChainRecordDTO);
            }
        }
    }

    private void syncEthChainRecord(Wallet wallet) {

        /* 1.取得区块转账信息 */
        List<EthChainRecordDTO> ethChainRecordDTOList = ethChainService.findRecord(wallet.getAddress());

        for (EthChainRecordDTO ethChainRecordDTO : ethChainRecordDTOList) {

            /* 2.已经同步的场合退出 */
            if (syncEthChainRecordRepository.existsByHash(ethChainRecordDTO.getHash())) {
                break;
            }

            /* 3.该地址记录的转账类型为收款，且区块链确认数为6以上的场合，同步记录 */
            if (ObjectUtil.equals(wallet.getAddress().toLowerCase(), ethChainRecordDTO.getTo().toLowerCase()) &&
                    Long.valueOf(ethChainRecordDTO.getConfirmations()) >= 6) {

                /* 3.1.保存记录 */
                saveEthChainRecord(wallet, ethChainRecordDTO);

                /* 3.2.回收用户账号中的ETH到系统账号 */
                recoveryEth(wallet, ethChainRecordDTO);
            }
        }
    }

    private void saveBtcChainRecord(Wallet wallet, BtcChainRecordDTO btcChainRecordDTO) {

        SyncBtcChainRecord syncBtcChainRecord = new SyncBtcChainRecord();

        syncBtcChainRecord.setUserId(wallet.getUserId());
        syncBtcChainRecord.setSyncAddress(wallet.getAddress());
        syncBtcChainRecord.setAccount(btcChainRecordDTO.getAccount());
        syncBtcChainRecord.setAddress((btcChainRecordDTO.getAddress()));
        syncBtcChainRecord.setIsRead(false);
        syncBtcChainRecord.setSyncTime(DateUtil.now());

        syncBtcChainRecord.setAmount(btcChainRecordDTO.getAmount());
        syncBtcChainRecord.setBlockHash((btcChainRecordDTO.getBlockhash()));
        syncBtcChainRecord.setBlockIndex((btcChainRecordDTO.getBlockindex()));
        syncBtcChainRecord.setBlockTime(btcChainRecordDTO.getBlocktime());
        syncBtcChainRecord.setLabel(btcChainRecordDTO.getLabel());
        syncBtcChainRecord.setCategory(btcChainRecordDTO.getCategory());
        syncBtcChainRecord.setTime(btcChainRecordDTO.getTime());
        syncBtcChainRecord.setTimeReceived(btcChainRecordDTO.getTimereceived());
        syncBtcChainRecord.setTxId(btcChainRecordDTO.getTxid());
        syncBtcChainRecord.setWalletConflicts(btcChainRecordDTO.getWalletconflicts().toString());
        syncBtcChainRecord.setVOut(btcChainRecordDTO.getVout());
        syncBtcChainRecord.setConfirmations(btcChainRecordDTO.getConfirmations());
        syncBtcChainRecord.setBip125Replaceable(btcChainRecordDTO.getBip125_replaceable());

        syncBtcChainRecordRepository.save(syncBtcChainRecord);
    }


    private void saveEthChainRecord(Wallet wallet, EthChainRecordDTO ethChainRecordDTO) {

        SyncEthChainRecord syncEthChainRecord = new SyncEthChainRecord();

        syncEthChainRecord.setUserId(wallet.getUserId());
        syncEthChainRecord.setSyncAddress(wallet.getAddress());
        syncEthChainRecord.setIsRead(false);
        syncEthChainRecord.setSyncTime(DateUtil.now());
        syncEthChainRecord.setBlockNumber(ethChainRecordDTO.getBlockNumber());
        syncEthChainRecord.setTimeStamp(ethChainRecordDTO.getTimeStamp());
        syncEthChainRecord.setHash(ethChainRecordDTO.getHash());
        syncEthChainRecord.setNonce(ethChainRecordDTO.getNonce());
        syncEthChainRecord.setTransactionIndex(ethChainRecordDTO.getTransactionIndex());
        syncEthChainRecord.setFromAddress(ethChainRecordDTO.getFrom());
        syncEthChainRecord.setToAddress(ethChainRecordDTO.getTo());
        syncEthChainRecord.setEthValue(ethChainRecordDTO.getValue());
        syncEthChainRecord.setGas(ethChainRecordDTO.getGas());
        syncEthChainRecord.setGasPrice(ethChainRecordDTO.getGasPrice());
        syncEthChainRecord.setIsError(ethChainRecordDTO.getIsError());
        syncEthChainRecord.setTxReceiptStatus(ethChainRecordDTO.getTxreceipt_status());
        syncEthChainRecord.setInput(ethChainRecordDTO.getInput());
        syncEthChainRecord.setContractAddress(ethChainRecordDTO.getContractAddress());
        syncEthChainRecord.setCumulativeGasUsed(ethChainRecordDTO.getCumulativeGasUsed());
        syncEthChainRecord.setGasUsed(ethChainRecordDTO.getGasUsed());
        syncEthChainRecord.setConfirmations(ethChainRecordDTO.getConfirmations());

        syncEthChainRecordRepository.save(syncEthChainRecord);
    }


    private void recoveryEth(Wallet wallet, EthChainRecordDTO ethChainRecordDTO) {

        BigInteger userEthBalanceBigInteger = ethChainService.getBalance(wallet.getAddress());
        BigDecimal userEthBalanceBigDecimal = new BigDecimal(userEthBalanceBigInteger);
        BigDecimal convertedUserEthBalance = MathUtil.divideRoundDown(userEthBalanceBigDecimal, Constant.ETH_CONVERT_DEGREE);

        if (convertedUserEthBalance.compareTo(ethRecoveryAmount) >= 0) {

            BigInteger unlockDuration = BigInteger.valueOf(60L);
            PersonalUnlockAccount personalUnlockAccount;

            try {
                personalUnlockAccount = ChainUtil.admin.personalUnlockAccount(ethChainRecordDTO.getFrom(), ChainUtil.generateEthPassword(String.valueOf(wallet.getId())), unlockDuration).send();
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(ErrorEnum.CHAIN_ACCOUNT_RECOVERY_FAILED);
            }

            if (personalUnlockAccount.accountUnlocked()) {

                BigInteger value = Convert.toWei(userEthBalanceBigDecimal, Convert.Unit.ETHER).toBigInteger();
                // 先汇总到我的钱包
                Transaction transaction = ethChainService.makeTransaction(ethChainRecordDTO.getFrom(), ethRecoveryAddress,
                        null, null, null, value);

                // 不是必须的 可以使用默认值
                BigInteger gasLimit = ethChainService.getTransactionGasLimit(transaction);
                // 不是必须的 缺省值就是正确的值
                BigInteger nonce = ethChainService.getTransactionNonce(ethChainRecordDTO.getFrom());
                // 该值为大部分矿工可接受的gasPrice
                BigInteger gasPrice = Convert.toWei(ChainUtil.defaultGasPrice, Convert.Unit.GWEI).toBigInteger();
                transaction = ethChainService.makeTransaction(ethChainRecordDTO.getFrom(), ethRecoveryAddress,
                        nonce, gasPrice,
                        gasLimit, value);

                try {
                    ChainUtil.web3j.ethSendTransaction(transaction).send();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BusinessException(ErrorEnum.CHAIN_ACCOUNT_RECOVERY_FAILED);
                }
            }
        }
    }
}