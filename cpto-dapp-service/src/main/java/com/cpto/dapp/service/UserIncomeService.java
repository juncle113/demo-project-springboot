package com.cpto.dapp.service;

import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.UserIncomeDetailLogVO;
import com.cpto.dapp.pojo.vo.UserIncomeLogVO;

import java.sql.Timestamp;

/**
 * 用户收益Service
 *
 * @author sunli
 * @date 2019/02/25
 */
public interface UserIncomeService extends BaseService {

    /**
     * 查询用户的各项目收益信息
     *
     * @param userId 用户id
     * @return 收益信息
     */
    UserIncomeLogVO findUserIncomeProjectLogList(Long userId);

    /**
     * 查询用户的某项目详细收益明细信息
     *
     * @param userId     用户id
     * @param projectId  项目id
     * @param searchTime 查询时间
     * @param page       当前页数
     * @param pageSize   每页条数
     * @return 收益信息
     */
    PageVO<UserIncomeDetailLogVO> findUserIncomeDetailLogListByProject(Long userId,
                                                                       Long projectId,
                                                                       Timestamp searchTime,
                                                                       Integer page,
                                                                       Integer pageSize);

    /**
     * 释放收益
     */
    void returnIncome();
}