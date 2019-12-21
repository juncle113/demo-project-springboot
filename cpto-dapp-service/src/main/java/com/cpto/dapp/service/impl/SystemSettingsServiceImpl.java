package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.constant.SettingsConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.SystemSettings;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.AuthorizedException;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.SystemSettingsDTO;
import com.cpto.dapp.pojo.vo.SystemSettingsVO;
import com.cpto.dapp.repository.SystemSettingsRepository;
import com.cpto.dapp.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统设置ServiceImpl
 *
 * @author sunli
 * @date 2019/02/21
 */
@Service
public class SystemSettingsServiceImpl extends BaseServiceImpl implements SystemSettingsService {

    @Autowired
    private SystemSettingsRepository systemSettingsRepository;

    /**
     * 取得系统参数列表
     *
     * @return 系统参数列表
     */
    @Override
    public List<SystemSettingsVO> findSystemSettingsList() {

        List<SystemSettingsVO> systemSettingsVOList = new ArrayList<>();

        List<SystemSettings> systemSettingsList = systemSettingsRepository.findByEditableTrue();
        for (SystemSettings systemSettings : systemSettingsList) {
            systemSettingsVOList.add(editSystemSettingsVO(systemSettings));
        }

        return systemSettingsVOList;
    }

    /**
     * 取得系统参数
     *
     * @param settingsId 参数id
     * @return 系统参数
     */
    @Override
    public SystemSettingsVO findSystemSettingsVO(Long settingsId) {
        SystemSettings systemSettings = systemSettingsRepository.findNotNullById(settingsId);
        return editSystemSettingsVO(systemSettings);
    }

    /**
     * 取得系统参数
     *
     * @param paramKey 参数key
     * @return 系统参数
     */
    @Override
    public SystemSettingsVO findSystemSettingsVO(String paramKey) {
        return editSystemSettingsVO(findSystemSettings(paramKey));
    }

    /**
     * 取得系统参数
     *
     * @param paramKey 参数key
     * @return 系统参数
     */
    @Override
    public SystemSettings findSystemSettings(String paramKey) {
        SystemSettings systemSettings = systemSettingsRepository.findByParamKey(paramKey);
        if (ObjectUtil.isEmpty(systemSettings)) {
            throw new DataNotFoundException();
        }
        return systemSettings;
    }

    /**
     * 取得系统参数（前端访问）
     *
     * @param paramKeyList 参数key
     * @return 系统参数
     */
    @Override
    public List<SystemSettingsVO> findSystemSettingsByApp(List<String> paramKeyList) {

        List<SystemSettingsVO> systemSettingsVOList = new ArrayList<>();
        SystemSettingsVO systemSettingsVO;
        SystemSettings systemSettings;
        String langParamKey;

        for (String paramKey : paramKeyList) {
            systemSettings = systemSettingsRepository.findByParamKey(paramKey);

            // 第一次根据参数key没有查询到结果的场合，再通过语言区分查询第二次
            if (ObjectUtil.isEmpty(systemSettings)) {
                langParamKey = paramKey.concat(LanguageUtil.getTextByLanguage(SettingsConstant.ZH, SettingsConstant.EN));
                systemSettings = systemSettingsRepository.findByParamKey(langParamKey);

                if (ObjectUtil.isEmpty(systemSettings)) {
                    throw new DataNotFoundException();
                }
            }

            systemSettingsVO = new SystemSettingsVO();
            systemSettingsVO.setName(systemSettings.getName());
            systemSettingsVO.setParamKey(paramKey);
            systemSettingsVO.setParamValue(systemSettings.getParamValue());

            systemSettingsVOList.add(systemSettingsVO);
        }

        return systemSettingsVOList;
    }

    /**
     * 修改系统参数
     *
     * @param admin             管理员
     * @param settingsId        系统参数id
     * @param systemSettingsDTO 系统参数信息
     */
    @Override
    public void modifySystemSettings(ManagerAdmin admin, Long settingsId, SystemSettingsDTO systemSettingsDTO) {

        /* 1.取得被修改数据 */
        SystemSettings systemSettings = systemSettingsRepository.findNotNullById(settingsId);

        /* 2.判断该系统参数是否可以被修改 */
        if (!systemSettings.getEditable()) {
            throw new AuthorizedException();
        }

        /* 3.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(systemSettingsDTO.getModifiedTime(), systemSettings.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 4.设置修改内容 */
        systemSettings.setParamValue(systemSettingsDTO.getParamValue());
        systemSettings.setRemark(systemSettingsDTO.getRemark());
        // systemSettings.setStatus(systemSettingsDTO.getStatus()); 暂不可通过程序修改状态
        systemSettings.setModifiedBy(admin);
        systemSettings.setModifiedTime(DateUtil.now());

        systemSettingsRepository.save(systemSettings);

        /* 5.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_SETTINGS, systemSettings.getId());
    }

    /**
     * 编辑系统参数VO
     *
     * @param systemSettings 系统参数信息
     * @return 系统参数VO
     */
    private SystemSettingsVO editSystemSettingsVO(SystemSettings systemSettings) {

        SystemSettingsVO systemSettingsVO = new SystemSettingsVO();

        systemSettingsVO.setId(systemSettings.getId());
        systemSettingsVO.setName(systemSettings.getName());
        systemSettingsVO.setParamKey(systemSettings.getParamKey());
        systemSettingsVO.setParamValue(systemSettings.getParamValue());
        systemSettingsVO.setEditable(systemSettings.getEditable());
        systemSettingsVO.setMinLimit(systemSettings.getMinLimit());
        systemSettingsVO.setMaxLimit(systemSettings.getMaxLimit());
        systemSettingsVO.setStatus(systemSettings.getStatus());
        systemSettingsVO.setStatusName(StatusEnum.getNameByCode(systemSettings.getStatus()));
        systemSettingsVO.setRemark(systemSettings.getRemark());
        systemSettingsVO.setCreatedBy(ObjectUtil.isNotEmpty(systemSettings.getCreatedBy()) ? systemSettings.getCreatedBy().getName() : null);
        systemSettingsVO.setCreatedTime(systemSettings.getCreatedTime());
        systemSettingsVO.setModifiedBy(ObjectUtil.isNotEmpty(systemSettings.getCreatedBy()) ? systemSettings.getCreatedBy().getName() : null);
        systemSettingsVO.setModifiedTime(systemSettings.getModifiedTime());

        return systemSettingsVO;
    }
}