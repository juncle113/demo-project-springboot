package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.domain.SystemNotice;
import com.cpto.dapp.domain.SystemVersion;
import com.cpto.dapp.enums.DeviceTypeEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.vo.SystemNoticeVO;
import com.cpto.dapp.pojo.vo.VersionVO;
import com.cpto.dapp.repository.SystemNoticeRepository;
import com.cpto.dapp.repository.SystemVersionRepository;
import com.cpto.dapp.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统信息ServiceImpl
 *
 * @author sunli
 * @date 2019/01/10
 */
@Service
public class SystemInfoServiceImpl extends BaseServiceImpl implements SystemInfoService {

    @Autowired
    private SystemVersionRepository systemVersionRepository;

    @Autowired
    private SystemNoticeRepository systemNoticeRepository;

    /**
     * 检查版本
     *
     * @param deviceType     设备类型
     * @param currentVersion 客户端当前版本号
     * @return 最新版本信息
     */
    @Override
    public VersionVO checkVersion(Integer deviceType, String currentVersion) {

        VersionVO versionVO = new VersionVO();

        /* 1.根据设备类型查询最新版本信息 */
        List<Integer> statusList = new ArrayList<>();
        statusList.add(StatusEnum.VALID.getCode());

        SystemVersion systemVersion = systemVersionRepository.findTopByDeviceTypeAndStatusInOrderByIdDesc(deviceType, statusList);
        if (ObjectUtil.isEmpty(systemVersion)) {
            throw new DataNotFoundException();
        }

        /* 2.将版本号名称转换为便于比较的版本号数值 */
        Integer currentVersionCode = versionNameToCode(currentVersion);
        Integer newVersionCode = versionNameToCode(systemVersion.getNewVersion());
        Integer minVersionCode = versionNameToCode(systemVersion.getMinVersion());

        /* 3.判断是否需要更新 */
        // 当前版本 < 最新版本的场合，有更新
        if (currentVersionCode < newVersionCode) {
            versionVO.setHasUpdate(true);
        } else {
            versionVO.setHasUpdate(false);
        }

        // 当前版本 < 支持最小版本的场合，需要强制更新
        if (currentVersionCode < minVersionCode) {
            versionVO.setForceUpdate(true);
        } else {
            // 不小于支持最小版本的场合，根据数据库中是否强制更新来设置
            versionVO.setForceUpdate(systemVersion.getForceUpdate());
        }

        /* 4.返回最新版本信息 */
        versionVO.setDeviceType(systemVersion.getDeviceType());
        versionVO.setDeviceTypeName(DeviceTypeEnum.getNameByCode(systemVersion.getDeviceType()));
        versionVO.setNewVersion(systemVersion.getNewVersion());
        versionVO.setMinVersion(systemVersion.getMinVersion());
        versionVO.setDescription(LanguageUtil.getTextByLanguage(systemVersion.getDescriptionZh(), systemVersion.getDescriptionEn()));
        versionVO.setUpdateTime(systemVersion.getModifiedTime());

        return versionVO;
    }

    /**
     * 取得系统公告
     *
     * @return 系统公告
     */
    @Override
    public List<SystemNoticeVO> findSystemNoticeList() {

        /* 1.查询系统公告 */
        List<SystemNotice> systemNoticeList = systemNoticeRepository.findAll();

        /* 2.设置系统公告VO */
        List<SystemNoticeVO> systemNoticeVOList = new ArrayList<>();
        SystemNoticeVO systemNoticeVO;
        for (SystemNotice systemNotice : systemNoticeList) {
            systemNoticeVO = new SystemNoticeVO();
            systemNoticeVO.setContent(LanguageUtil.getTextByLanguage(systemNotice.getContentZh(), systemNotice.getContentEn()));
            systemNoticeVOList.add(systemNoticeVO);
        }

        return systemNoticeVOList;
    }

    /**
     * 将版本号名称转换为版本号数值
     * 去掉分隔符后补0（从格式 xx.xx.xx 转换为 xxxxxx ）
     *
     * @param versionName 版本号名称
     * @return 版本号数值
     */
    private Integer versionNameToCode(String versionName) {

        String[] versionArray = versionName.split("\\.");

        String main = versionArray[0];
        String sub = String.format("%02d", Integer.valueOf(versionArray[1]));
        String modify = String.format("%02d", Integer.valueOf(versionArray[2]));

        return Integer.valueOf(main.concat(sub).concat(modify));
    }
}