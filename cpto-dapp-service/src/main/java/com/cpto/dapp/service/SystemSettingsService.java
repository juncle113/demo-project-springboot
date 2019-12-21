package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.SystemSettings;
import com.cpto.dapp.pojo.dto.SystemSettingsDTO;
import com.cpto.dapp.pojo.vo.SystemSettingsVO;

import java.util.List;

/**
 * 系统设置Service
 *
 * @author sunli
 * @date 2019/02/21
 */
public interface SystemSettingsService extends BaseService {

    /**
     * 取得系统参数列表
     *
     * @return 系统参数列表
     */
    List<SystemSettingsVO> findSystemSettingsList();

    /**
     * 取得系统参数
     *
     * @param paramKey 参数key
     * @return 系统参数
     */
    SystemSettings findSystemSettings(String paramKey);

    /**
     * 取得系统参数
     *
     * @param settingsId 参数id
     * @return 系统参数
     */
    SystemSettingsVO findSystemSettingsVO(Long settingsId);

    /**
     * 取得系统参数
     *
     * @param paramKey 参数key
     * @return 系统参数
     */
    SystemSettingsVO findSystemSettingsVO(String paramKey);

    /**
     * 取得系统参数（前端访问）
     *
     * @param paramKeyList 参数key
     * @return 系统参数
     */
    List<SystemSettingsVO> findSystemSettingsByApp(List<String> paramKeyList);

    /**
     * 修改系统参数
     *
     * @param admin             管理员
     * @param settingsId        系统参数id
     * @param systemSettingsDTO 系统参数信息
     */
    void modifySystemSettings(ManagerAdmin admin, Long settingsId, SystemSettingsDTO systemSettingsDTO);
}