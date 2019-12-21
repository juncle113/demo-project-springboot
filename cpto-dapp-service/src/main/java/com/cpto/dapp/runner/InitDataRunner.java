package com.cpto.dapp.runner;

import com.cpto.dapp.common.constant.Constant;
import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.IdUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.constant.SettingsConstant;
import com.cpto.dapp.domain.*;
import com.cpto.dapp.enums.*;
import com.cpto.dapp.repository.*;
import com.cpto.dapp.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据
 *
 * @author sunli
 * @date 2019/01/17
 */
@Component
@Order(value = 1)
public class InitDataRunner implements CommandLineRunner {

    @Autowired
    private WalletService walletService;

    @Autowired
    private SystemVersionRepository systemVersionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private SystemTotalValueRepository systemTotalValueRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private SystemNoticeRepository systemNoticeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectEvaluationRepository projectEvaluationRepository;

    @Autowired
    private ManagerAdminRepository managerAdminRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    @Autowired
    private SystemExchangeRateRepository systemExchangeRateRepository;

    @Value("${spring.profiles.active}")
    private String profilesActive;

    @Override
    public void run(String... args) throws Exception {

        // 管理员
        initAdmin();

        // 版本信息
        initVersion();

        // 系统设置
        initSystemSettings();

        // 价值总量
        initSystemValue();

        // 系统公告
        initSystemNotice();

        // 币种汇率
        initExchangeRate();

        // 设置测试数据
        if (ObjectUtil.notEquals(profilesActive, Constant.PROD)) {

            // 项目
            initProject();

            // 公告
            initNotice();

            // 消息
            initMessage();

            // 用户
            initUser();

            // 账户
            initAccount();

            // 钱包
            initWallet();
        }
    }

    /**
     * 初始化版本信息
     */
    private void initVersion() {

        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());

        Integer deviceType = DeviceTypeEnum.ANDROID.getCode();
        SystemVersion systemVersion = systemVersionRepository.findTopByDeviceTypeAndStatusInOrderByIdDesc(deviceType, statusList);
        if (ObjectUtil.isEmpty(systemVersion)) {
            systemVersion = new SystemVersion();
            systemVersion.setDeviceType(deviceType);
            systemVersion.setNewVersion("1.0.0");
            systemVersion.setMinVersion("1.0.0");
            systemVersion.setDescriptionZh("初始版");
            systemVersion.setDescriptionEn("first version");
            systemVersion.setForceUpdate(false);
            systemVersion.setStatus(StatusEnum.VALID.getCode());
            systemVersion.setCreatedBy(null);
            systemVersion.setCreatedTime(DateUtil.now());
            systemVersionRepository.save(systemVersion);
        }

        deviceType = DeviceTypeEnum.IOS.getCode();
        systemVersion = systemVersionRepository.findTopByDeviceTypeAndStatusInOrderByIdDesc(deviceType, statusList);
        if (ObjectUtil.isEmpty(systemVersion)) {
            systemVersion = new SystemVersion();
            systemVersion.setDeviceType(deviceType);
            systemVersion.setNewVersion("1.0.0");
            systemVersion.setMinVersion("1.0.0");
            systemVersion.setDescriptionZh("初始版");
            systemVersion.setDescriptionEn("first version");
            systemVersion.setForceUpdate(false);
            systemVersion.setStatus(StatusEnum.VALID.getCode());
            systemVersion.setCreatedBy(null);
            systemVersion.setCreatedTime(DateUtil.now());
            systemVersionRepository.save(systemVersion);
        }
    }

    /**
     * 初始化系统设置
     */
    private void initSystemSettings() {

        String paramKey = SettingsConstant.URL_APP_DOWNLOAD.concat(SettingsConstant.ZH);
        SystemSettings systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("App下载页面（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://pgyer.com/bmVy?lang=zh");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_APP_DOWNLOAD.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("App下载页面（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://pgyer.com/bmVy?lang=en");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_AD_PIC.concat(SettingsConstant.ZH);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("广告图（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_AD_PIC.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("广告图（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_AD.concat(SettingsConstant.ZH);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("广告页跳转网址（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_AD.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("广告页跳转网址（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_HELP.concat(SettingsConstant.ZH);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("帮助页面网址（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://web-static-dapp.cpto.io/help.html?lang=zh");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_HELP.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("帮助页面网址（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://web-static-dapp.cpto.io/help.html?lang=en");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_USER_AGREEMENT.concat(SettingsConstant.ZH);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("用户协议网址（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://web-static-dapp.cpto.io/userAgreement.html?lang=zh");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_USER_AGREEMENT.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("用户协议网址（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("https://web-static-dapp.cpto.io/userAgreement.html?lang=en");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.NUMBER_WITHDRAW_MAX;
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("提币上限");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("100000");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(0L);
            systemSettings.setMaxLimit(9999999L);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.NUMBER_WITHDRAW_MIN;
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("提币下限");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("1000");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(0L);
            systemSettings.setMaxLimit(9999999L);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.NUMBER_BTC_WITHDRAW_FEE_RATE;
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("BTC提币手续费率");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("2000");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(0L);
            systemSettings.setMaxLimit(1L);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.NUMBER_ETH_WITHDRAW_FEE_RATE;
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("ETH提币手续费率");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("1000");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(0L);
            systemSettings.setMaxLimit(1L);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_INVITE_PIC.concat(SettingsConstant.ZH);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("邀请页背景图网址（中文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.URL_INVITE_PIC.concat(SettingsConstant.EN);
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("邀请页背景图网址（英文）");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("http://cpto-dapp-pic.oss-us-west-1.aliyuncs.com/ad_page.png");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }

        paramKey = SettingsConstant.NUMBER_ETH_RECOVERY_AMOUNT;
        systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            systemSettings = new SystemSettings();
            systemSettings.setName("ETH回收额度");
            systemSettings.setParamKey(paramKey);
            systemSettings.setParamValue("1");
            systemSettings.setEditable(true);
            systemSettings.setMinLimit(null);
            systemSettings.setMaxLimit(null);
            systemSettings.setStatus(StatusEnum.VALID.getCode());
            systemSettings.setRemark(null);
            systemSettings.setCreatedBy(null);
            systemSettings.setCreatedTime(DateUtil.now());
            systemSettingsRepository.save(systemSettings);
        }
    }

    /**
     * 初始化用户
     */
    private void initUser() {

        User user;
        String userName = "testsunli01";
        boolean existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("13752448393");
            user.setEmail("64015323@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("ABCD1111");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "testyangzhen01";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("18522359349");
            user.setEmail("228100868@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("ABCD2222");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "testhanfubin01";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("18302223400");
            user.setEmail("1173635320@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("ABCD3333");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "testwangxin01";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("1");
            user.setEmail("2@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("ABCD4444");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "testmadongming01";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("13920889131");
            user.setEmail("1726610038@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("ABCD5555");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "test01";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("13712345678");
            user.setEmail("test01@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("F5Y324K0");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }

        userName = "test02";
        existsByUserName = userRepository.existsByUserName(userName);
        if (!existsByUserName) {
            user = new User();
            user.setId(IdUtil.generateIdByCurrentTime());
            user.setUserName(userName);
            user.setAreaCode("86");
            user.setPhone("13712345679");
            user.setEmail("test02@qq.com");
            user.setPassword(StringUtil.toMd5("88888888"));
            user.setInviteCode("F5Y324K1");
            user.setStatus(StatusEnum.VALID.getCode());
            user.setCreatedBy(null);
            user.setCreatedTime(DateUtil.now());
            userRepository.save(user);
        }
    }

    /**
     * 初始化账户
     */
    private void initAccount() {

        String userName = "testsunli01";
        User user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "testyangzhen01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "testhanfubin01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "testwangxin01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "testmadongming01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "test01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }

        userName = "test02";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Account> accountList = accountRepository.findByUserId(user.getId());
            if (ObjectUtil.isEmptyCollection(accountList)) {
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.LOCKED.getCode()));
                Account account = new Account(user.getId(), AccountTypeEnum.AVAILABLE.getCode());
                account.setAmount(new BigDecimal("10000000"));
                accountRepository.save(account);
                accountRepository.save(new Account(user.getId(), AccountTypeEnum.APPROVED.getCode()));
            }
        }
    }

    /**
     * 初始化钱包
     */
    private void initWallet() {

        List<Wallet> walletList;
        String userName = "testsunli01";
        User user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "testyangzhen01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "testhanfubin01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "testwangxin01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "testmadongming01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "test01";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }

        userName = "test02";
        user = userRepository.findByUserName(userName);
        if (ObjectUtil.isNotEmpty(user)) {
            walletList = walletRepository.findByUserIdAndAddressTypeOrderByIdDesc(user.getId(), WalletAddressTypeEnum.TOP_UP.getCode());
            if (ObjectUtil.isEmptyCollection(walletList)) {
                walletService.initWallet(user.getId());
            }
        }
    }

    /**
     * 初始化项目
     */
    private void initProject() {

        Long projectId = 11L;
        Project project;
        ProjectEvaluation projectEvaluation;
        boolean existsById = projectRepository.existsById(projectId);
        if (!existsById) {

            project = new Project();
            project.setId(projectId);
            project.setNo("2019012011");
            project.setNameZh("[test]特斯拉油田01");
            project.setNameEn("[test]setNameEn01");
            project.setSummaryZh("项目简介");
            project.setSummaryEn("setSummaryEn");
            project.setDescriptionZh("详细描述");
            project.setDescriptionEn("setDescriptionEn");
            project.setRecruitmentEndTime(DateUtil.timestampAddDay(DateUtil.now(), 90));
            project.setTotalAmount(new BigDecimal("10000000"));
            project.setInitiatorZh("链猫科技");
            project.setInitiatorEn("setInitiatorEn");
            project.setInitiatorPayAmount(new BigDecimal("500000"));
            project.setLockDays(180);
            project.setConditionMaxJoinNumber(null);
            project.setConditionMinLockedAmount(null);
            project.setConditionMinPayAmount(null);
            project.setConditionMinRegisterDays(null);
            project.setDeleted(false);
            project.setStatus(ProjectStatusEnum.START.getCode());
            project.setCreatedBy(null);
            project.setCreatedTime(DateUtil.now());
            projectRepository.save(project);

            projectEvaluation = new ProjectEvaluation();
            projectEvaluation.setProjectId(projectId);
            projectEvaluation.setDeleted(false);
            projectEvaluation.setStatus(StatusEnum.VALID.getCode());
            projectEvaluation.setCreatedBy(null);
            projectEvaluation.setCreatedTime(DateUtil.now());
            projectEvaluationRepository.save(projectEvaluation);
        }

        projectId = 12L;
        existsById = projectRepository.existsById(projectId);
        if (!existsById) {

            project = new Project();
            project.setId(projectId);
            project.setNo("2019012012");
            project.setNameZh("[test]特斯拉油田02");
            project.setNameEn("[test]setNameEn02");
            project.setSummaryZh("项目简介");
            project.setSummaryEn("setSummaryEn");
            project.setDescriptionZh("详细描述");
            project.setDescriptionEn("setDescriptionEn");
            project.setRecruitmentEndTime(DateUtil.timestampAddDay(DateUtil.now(), 90));
            project.setStartTime(DateUtil.now());
            project.setStartTime(DateUtil.now());
            project.setTotalAmount(new BigDecimal("20000000"));
            project.setInitiatorZh("链猫科技");
            project.setInitiatorEn("setInitiatorEn");
            project.setInitiatorPayAmount(new BigDecimal("1000000"));
            project.setLockDays(180);
            project.setConditionMaxJoinNumber(200);
            project.setConditionMinLockedAmount(new BigDecimal("6000"));
            project.setConditionMinPayAmount(new BigDecimal("2000"));
            project.setConditionMinRegisterDays(10);
            project.setDeleted(false);
            project.setStatus(ProjectStatusEnum.START.getCode());
            project.setCreatedBy(null);
            project.setCreatedTime(DateUtil.now());
            projectRepository.save(project);

            projectEvaluation = new ProjectEvaluation();
            projectEvaluation.setProjectId(projectId);
            projectEvaluation.setDeleted(false);
            projectEvaluation.setStatus(StatusEnum.VALID.getCode());
            projectEvaluation.setCreatedBy(null);
            projectEvaluation.setCreatedTime(DateUtil.now());
            projectEvaluationRepository.save(projectEvaluation);
        }

        projectId = 21L;
        existsById = projectRepository.existsById(projectId);
        if (!existsById) {

            project = new Project();
            project.setId(projectId);
            project.setNo("2019012021");
            project.setNameZh("[test]特斯拉油田03");
            project.setNameEn("[test]setNameEn03");
            project.setSummaryZh("项目简介");
            project.setSummaryEn("setSummaryEn");
            project.setDescriptionZh("详细描述");
            project.setDescriptionEn("setDescriptionEn");
            project.setRecruitmentEndTime(DateUtil.timestampAddDay(DateUtil.now(), 90));
            project.setTotalAmount(new BigDecimal("30000000"));
            project.setInitiatorZh("链猫科技");
            project.setInitiatorEn("setInitiatorEn");
            project.setInitiatorPayAmount(new BigDecimal("2000000"));
            project.setLockDays(180);
            project.setConditionMaxJoinNumber(300);
            project.setConditionMinLockedAmount(new BigDecimal("9000"));
            project.setConditionMinPayAmount(new BigDecimal("3000"));
            project.setConditionMinRegisterDays(18);
            project.setDeleted(false);
            project.setStatus(ProjectStatusEnum.S1.getCode());
            project.setCreatedBy(null);
            project.setCreatedTime(DateUtil.now());
            projectRepository.save(project);

            projectEvaluation = new ProjectEvaluation();
            projectEvaluation.setProjectId(projectId);
            projectEvaluation.setS1ExpectRange(new BigDecimal("10"));
            projectEvaluation.setS1ExpectReturnZh("S1预计回报");
            projectEvaluation.setS1ExpectReturnEn("setS1ExpectReturnEn");
            projectEvaluation.setS1ReturnZh("S1回报");
            projectEvaluation.setS1ReturnEn("setS1ExpectReturnEn");
            projectEvaluation.setS1Zh("S1评估值");
            projectEvaluation.setS1En("setS1En");
            projectEvaluation.setS1EvaluatorZh("S1评估人");
            projectEvaluation.setS1EvaluatorEn("setS1EvaluatorEn");
            projectEvaluation.setProjectId(projectId);
            projectEvaluation.setDeleted(false);
            projectEvaluation.setStatus(StatusEnum.VALID.getCode());
            projectEvaluation.setCreatedBy(null);
            projectEvaluation.setCreatedTime(DateUtil.now());
            projectEvaluationRepository.save(projectEvaluation);
        }
    }

    /**
     * 初始化价值总量
     */
    private void initSystemValue() {

        SystemTotalValue systemTotalValue;

        List<SystemTotalValue> valueSummaryList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.VALUE_SUMMARY.getCode());
        if (ObjectUtil.isEmptyCollection(valueSummaryList)) {

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.VALUE_SUMMARY.getCode());
            systemTotalValue.setSortNo(1);
            systemTotalValue.setTitleZh("流通cpto总量：{0}枚");
            systemTotalValue.setTitleEn("setTitleEn：{0}");
            systemTotalValue.setAmount(new BigDecimal("12345"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.VALUE_SUMMARY.getCode());
            systemTotalValue.setSortNo(2);
            systemTotalValue.setTitleZh("正在进行项目：{0}组");
            systemTotalValue.setTitleEn("setTitleEn：{0}");
            systemTotalValue.setAmount(new BigDecimal("3"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.VALUE_SUMMARY.getCode());
            systemTotalValue.setSortNo(3);
            systemTotalValue.setTitleZh("完结投资项目日产油量：{0}组");
            systemTotalValue.setTitleEn("setTitleEn：{0}");
            systemTotalValue.setAmount(new BigDecimal("100"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.VALUE_SUMMARY.getCode());
            systemTotalValue.setSortNo(4);
            systemTotalValue.setTitleZh("分红次数：{0}次");
            systemTotalValue.setTitleEn("setTitleEn：{0}");
            systemTotalValue.setAmount(new BigDecimal("35"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.VALUE_SUMMARY.getCode());
            systemTotalValue.setSortNo(5);
            systemTotalValue.setTitleZh("分红总量：{0}枚");
            systemTotalValue.setTitleEn("setTitleEn：{0}");
            systemTotalValue.setAmount(new BigDecimal("2344"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);
        }

        List<SystemTotalValue> totalAllocationList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
        if (ObjectUtil.isEmptyCollection(totalAllocationList)) {

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(1);
            systemTotalValue.setTitleZh("市场流通");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("11"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(2);
            systemTotalValue.setTitleZh("DApp研发");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("1"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(3);
            systemTotalValue.setTitleZh("矿池总量");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("64"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(4);
            systemTotalValue.setTitleZh("运营成本");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("12"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(5);
            systemTotalValue.setTitleZh("创业团队");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("6"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(6);
            systemTotalValue.setTitleZh("学术研究");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("4"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
            systemTotalValue.setSortNo(7);
            systemTotalValue.setTitleZh("交易奖励");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("2"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);
        }

        List<SystemTotalValue> circulationSourceList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
        if (ObjectUtil.isEmptyCollection(circulationSourceList)) {

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
            systemTotalValue.setSortNo(1);
            systemTotalValue.setTitleZh("天使轮");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("2000"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
            systemTotalValue.setSortNo(2);
            systemTotalValue.setTitleZh("私募");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("1000"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
            systemTotalValue.setSortNo(3);
            systemTotalValue.setTitleZh("公开发行");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("2000"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);

            systemTotalValue = new SystemTotalValue();
            systemTotalValue.setValueType(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
            systemTotalValue.setSortNo(4);
            systemTotalValue.setTitleZh("投资挖掘");
            systemTotalValue.setTitleEn("setTitleEn");
            systemTotalValue.setAmount(new BigDecimal("5000"));
            systemTotalValue.setCreatedBy(null);
            systemTotalValue.setCreatedTime(DateUtil.now());
            systemTotalValue.setStatus(StatusEnum.VALID.getCode());
            systemTotalValueRepository.save(systemTotalValue);
        }
    }

    /**
     * 初始化项目公告
     */
    private void initNotice() {

        Notice notice;
        Long projectNoticeId = 1L;
        boolean existsById = noticeRepository.existsById(projectNoticeId);
        if (!existsById) {
            notice = new Notice();
            notice.setCreatedBy(null);
            notice.setTitleZh("[test]项目公告01");
            notice.setTitleEn("[test]setTitleEn");
            notice.setAuthorZh("链猫科技");
            notice.setAuthorEn("setAuthorEn");
            notice.setUrlZh("http://wwww.baidu.com");
            notice.setUrlEn("setUrlEn");
            notice.setContentZh("我们是一个小小的团队");
            notice.setContentEn("setContentEn");
            notice.setStatus(StatusEnum.VALID.getCode());
            notice.setNoticeType(NoticeTypeEnum.PROJECT.getCode());
            notice.setDeleted(false);
            notice.setCreatedBy(null);
            notice.setCreatedTime(DateUtil.now());
            noticeRepository.save(notice);
        }

        projectNoticeId = 2L;
        existsById = noticeRepository.existsById(projectNoticeId);
        if (!existsById) {
            notice = new Notice();
            notice.setId(2L);
            notice.setTitleZh("[test]项目公告02");
            notice.setTitleEn("[test]setTitleEn");
            notice.setAuthorZh("链猫科技");
            notice.setAuthorEn("setAuthorEn");
            notice.setUrlZh(null);
            notice.setUrlEn(null);
            notice.setContentZh("我们是一个小小的团队");
            notice.setContentEn("setContentEn");
            notice.setStatus(StatusEnum.VALID.getCode());
            notice.setNoticeType(NoticeTypeEnum.PROJECT.getCode());
            notice.setDeleted(false);
            notice.setCreatedBy(null);
            notice.setCreatedTime(DateUtil.now());
            noticeRepository.save(notice);
        }

        projectNoticeId = 11L;
        existsById = noticeRepository.existsById(projectNoticeId);
        if (!existsById) {
            notice = new Notice();
            notice.setId(11L);
            notice.setTitleZh("[test]回报公告01");
            notice.setTitleEn("[test]setTitleEn");
            notice.setNo("cpto201901");
            notice.setAuthorZh("链猫科技");
            notice.setAuthorEn("setAuthorEn");
            notice.setUrlZh("http://wwww.baidu.com");
            notice.setUrlEn("setUrlEn");
            notice.setContentZh("我们是一个小小的团队");
            notice.setContentEn("setContentEn");
            notice.setStatus(StatusEnum.VALID.getCode());
            notice.setNoticeType(NoticeTypeEnum.RETURN.getCode());
            notice.setDeleted(false);
            notice.setCreatedBy(null);
            notice.setCreatedTime(DateUtil.now());
            noticeRepository.save(notice);
        }

        projectNoticeId = 12L;
        existsById = noticeRepository.existsById(projectNoticeId);
        if (!existsById) {
            notice = new Notice();
            notice.setId(12L);
            notice.setTitleZh("[test]回报公告02");
            notice.setTitleEn("[test]setTitleEn");
            notice.setNo("cpto201902");
            notice.setAuthorZh("链猫科技");
            notice.setAuthorEn("setAuthorEn");
            notice.setUrlZh(null);
            notice.setUrlEn(null);
            notice.setContentZh("我们是一个小小的团队");
            notice.setContentEn("setContentEn");
            notice.setStatus(StatusEnum.VALID.getCode());
            notice.setNoticeType(NoticeTypeEnum.RETURN.getCode());
            notice.setDeleted(false);
            notice.setCreatedBy(null);
            notice.setCreatedTime(DateUtil.now());
            noticeRepository.save(notice);
        }
    }

    /**
     * 初始化币种汇率
     */
    private void initExchangeRate() {

        SystemExchangeRate systemExchangeRate;
        Long syncExchangeRateId = 1L;
        boolean existsById = systemExchangeRateRepository.existsById(syncExchangeRateId);
        if (!existsById) {
            systemExchangeRate = new SystemExchangeRate();
            systemExchangeRate.setCreatedBy(null);
            systemExchangeRate.setFromCoinKind("CPTO");
            systemExchangeRate.setToCoinKind("USDT");
            systemExchangeRate.setRate(new BigDecimal("0.1"));
            systemExchangeRate.setStatus(StatusEnum.VALID.getCode());
            systemExchangeRate.setCreatedBy(null);
            systemExchangeRate.setCreatedTime(DateUtil.now());
            systemExchangeRateRepository.save(systemExchangeRate);
        }

        syncExchangeRateId = 2L;
        existsById = systemExchangeRateRepository.existsById(syncExchangeRateId);
        if (!existsById) {
            systemExchangeRate = new SystemExchangeRate();
            systemExchangeRate.setId(2L);
            systemExchangeRate.setFromCoinKind("BTC");
            systemExchangeRate.setToCoinKind("USDT");
            systemExchangeRate.setRate(new BigDecimal("5000"));
            systemExchangeRate.setStatus(StatusEnum.VALID.getCode());
            systemExchangeRate.setCreatedBy(null);
            systemExchangeRate.setCreatedTime(DateUtil.now());
            systemExchangeRateRepository.save(systemExchangeRate);
        }

        syncExchangeRateId = 3L;
        existsById = systemExchangeRateRepository.existsById(syncExchangeRateId);
        if (!existsById) {
            systemExchangeRate = new SystemExchangeRate();
            systemExchangeRate.setId(3L);
            systemExchangeRate.setFromCoinKind("ETH");
            systemExchangeRate.setToCoinKind("USDT");
            systemExchangeRate.setRate(new BigDecimal("100"));
            systemExchangeRate.setStatus(StatusEnum.VALID.getCode());
            systemExchangeRate.setCreatedBy(null);
            systemExchangeRate.setCreatedTime(DateUtil.now());
            systemExchangeRateRepository.save(systemExchangeRate);
        }
    }

    /**
     * 初始化消息
     */
    private void initMessage() {

        Message message;
        Long messageId = 1L;
        boolean existsById = messageRepository.existsById(messageId);
        if (!existsById) {
            message = new Message();
            message.setMessageType(MessageTypeEnum.SYSTEM.getCode());
            message.setTitleZh("[test]系统消息");
            message.setTitleEn("[test]setTitleEn");
            message.setContentZh("新的版本上线了");
            message.setContentEn("setContentEn");
            message.setDeleted(false);
            message.setCreatedBy(null);
            message.setCreatedTime(DateUtil.now());
            message.setStatus(StatusEnum.VALID.getCode());
            messageRepository.save(message);
        }

        messageId = 2L;
        existsById = messageRepository.existsById(messageId);
        if (!existsById) {
            message = new Message();
            message.setMessageType(MessageTypeEnum.PAYMENT.getCode());
            message.setTitleZh("[test]投资消息");
            message.setTitleEn("[test]setTitleEn");
            message.setContentZh("勘探投资项目【特斯拉油田01】即将启动");
            message.setContentEn("setContentEn");
            message.setDeleted(false);
            message.setCreatedBy(null);
            message.setCreatedTime(DateUtil.now());
            message.setStatus(StatusEnum.VALID.getCode());
            messageRepository.save(message);
        }
    }

    /**
     * 初始化系统公告
     */
    private void initSystemNotice() {

        SystemNotice systemNotice;
        Long systemNoticeId = 1L;
        boolean existsById = systemNoticeRepository.existsById(systemNoticeId);
        if (!existsById) {
            systemNotice = new SystemNotice();
            systemNotice.setContentZh("我们是一个小小的团队");
            systemNotice.setContentEn("setContentEn");
            systemNotice.setStatus(StatusEnum.VALID.getCode());
            systemNotice.setCreatedBy(null);
            systemNotice.setCreatedTime(DateUtil.now());
            systemNoticeRepository.save(systemNotice);
        }

        systemNoticeId = 2L;
        existsById = systemNoticeRepository.existsById(systemNoticeId);
        if (!existsById) {
            systemNotice = new SystemNotice();
            systemNotice.setContentZh("但是有着大大的梦想");
            systemNotice.setContentEn("setContentEn");
            systemNotice.setStatus(StatusEnum.VALID.getCode());
            systemNotice.setCreatedBy(null);
            systemNotice.setCreatedTime(DateUtil.now());
            systemNoticeRepository.save(systemNotice);
        }

        systemNoticeId = 3L;
        existsById = systemNoticeRepository.existsById(systemNoticeId);
        if (!existsById) {
            systemNotice = new SystemNotice();
            systemNotice.setContentZh("我们想做最酷的产品");
            systemNotice.setContentEn("setContentEn");
            systemNotice.setStatus(StatusEnum.VALID.getCode());
            systemNotice.setCreatedBy(null);
            systemNotice.setCreatedTime(DateUtil.now());
            systemNoticeRepository.save(systemNotice);
        }

        systemNoticeId = 4L;
        existsById = systemNoticeRepository.existsById(systemNoticeId);
        if (!existsById) {
            systemNotice = new SystemNotice();
            systemNotice.setContentZh("想做最有挑战的技术");
            systemNotice.setContentEn("setContentEn");
            systemNotice.setStatus(StatusEnum.VALID.getCode());
            systemNotice.setCreatedBy(null);
            systemNotice.setCreatedTime(DateUtil.now());
            systemNoticeRepository.save(systemNotice);
        }
    }

    /**
     * 初始化管理员
     */
    private void initAdmin() {

        ManagerAdmin managerAdmin;
        Long adminId = 1L;
        boolean existsById = managerAdminRepository.existsById(adminId);
        if (!existsById) {
            managerAdmin = new ManagerAdmin();
            managerAdmin.setId(adminId);
            managerAdmin.setUserName("system");
            managerAdmin.setName("系统");
            managerAdmin.setPassword(StringUtil.toMd5("88888888"));
            managerAdmin.setRoleType(AdminRoleEnum.ROOT.getCode());
            managerAdmin.setDeleted(false);
            managerAdmin.setStatus(StatusEnum.VALID.getCode());
            managerAdmin.setCreatedBy(null);
            managerAdmin.setCreatedTime(DateUtil.now());
            managerAdminRepository.save(managerAdmin);
        }

        adminId = 2L;
        existsById = managerAdminRepository.existsById(adminId);
        if (!existsById) {
            managerAdmin = new ManagerAdmin();
            managerAdmin.setId(adminId);
            managerAdmin.setUserName("root");
            managerAdmin.setName("系统管理员");
            managerAdmin.setPassword(StringUtil.toMd5("88888888"));
            managerAdmin.setRoleType(AdminRoleEnum.ROOT.getCode());
            managerAdmin.setDeleted(false);
            managerAdmin.setStatus(StatusEnum.VALID.getCode());
            managerAdmin.setCreatedBy(null);
            managerAdmin.setCreatedTime(DateUtil.now());
            managerAdminRepository.save(managerAdmin);
        }

        adminId = 3L;
        existsById = managerAdminRepository.existsById(adminId);
        if (!existsById) {
            managerAdmin = new ManagerAdmin();
            managerAdmin.setId(adminId);
            managerAdmin.setUserName("superadmin");
            managerAdmin.setName("超级管理员");
            managerAdmin.setPassword(StringUtil.toMd5("88888888"));
            managerAdmin.setRoleType(AdminRoleEnum.SUPER_ADMIN.getCode());
            managerAdmin.setDeleted(false);
            managerAdmin.setStatus(StatusEnum.VALID.getCode());
            managerAdmin.setCreatedBy(null);
            managerAdmin.setCreatedTime(DateUtil.now());
            managerAdminRepository.save(managerAdmin);
        }

        adminId = 4L;
        existsById = managerAdminRepository.existsById(adminId);
        if (!existsById) {
            managerAdmin = new ManagerAdmin();
            managerAdmin.setId(adminId);
            managerAdmin.setUserName("admin01");
            managerAdmin.setName("管理员01");
            managerAdmin.setPassword(StringUtil.toMd5("88888888"));
            managerAdmin.setRoleType(AdminRoleEnum.ADMIN.getCode());
            managerAdmin.setDeleted(false);
            managerAdmin.setStatus(StatusEnum.VALID.getCode());
            managerAdmin.setCreatedBy(null);
            managerAdmin.setCreatedTime(DateUtil.now());
            managerAdminRepository.save(managerAdmin);
        }

        adminId = 5L;
        existsById = managerAdminRepository.existsById(adminId);
        if (!existsById) {
            managerAdmin = new ManagerAdmin();
            managerAdmin.setId(adminId);
            managerAdmin.setUserName("admin02");
            managerAdmin.setName("管理员02");
            managerAdmin.setPassword(StringUtil.toMd5("88888888"));
            managerAdmin.setRoleType(AdminRoleEnum.ADMIN.getCode());
            managerAdmin.setDeleted(false);
            managerAdmin.setStatus(StatusEnum.VALID.getCode());
            managerAdmin.setCreatedBy(null);
            managerAdmin.setCreatedTime(DateUtil.now());
            managerAdminRepository.save(managerAdmin);
        }
    }


}
