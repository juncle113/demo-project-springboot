package com.cpto.dapp.repository;

import com.cpto.dapp.domain.SystemSettings;

import java.util.List;

/**
 * 系统设置Repository
 *
 * @author sunli
 * @date 2019/02/21
 */
public interface SystemSettingsRepository extends BaseRepository<SystemSettings, Long> {

    /**
     * 根据系统参数的key查询系统参数
     *
     * @param paramKey key
     * @return 系统参数
     */
    SystemSettings findByParamKey(String paramKey);

    /**
     * 取得可设置的系统参数列表
     *
     * @return 系统参数列表
     */
    List<SystemSettings> findByEditableTrue();
}