package com.cpto.dapp.repository;


import com.cpto.dapp.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户Repository
 *
 * @author sunli
 * @date 2018/12/29
 */
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户信息
     */
    User findByUserName(String userName);

    /**
     * 根据邀请码查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    User findByInviteCode(String inviteCode);

    /**
     * 根据手机号查询用户信息
     *
     * @param areaCode 手机号归属地代码
     * @param phone    手机号
     * @return 用户信息
     */
    User findByAreaCodeAndPhone(String areaCode, String phone);

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Query(nativeQuery = true, value = "select * from user where phone = :phone or concat(area_code, phone) = :phone")
    List<User> findByPhone(String phone);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User findByEmail(String email);

    /**
     * 检查用户名是否存在
     *
     * @param userName 用户名
     * @return 是否存在
     */
    boolean existsByUserName(String userName);

    /**
     * 检查手机号是否存在
     *
     * @param areaCode 手机号归属地代码
     * @param phone    手机号
     * @return 是否存在
     */
    boolean existsByAreaCodeAndPhone(String areaCode, String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查邀请码是否存在
     *
     * @param inviteCode 邀请码
     * @return 是否存在
     */
    boolean existsByInviteCode(String inviteCode);

    /**
     * 查询该用户邀请次数
     *
     * @param userId 用户id
     * @return 邀请次数
     */
    Integer countByParentId(Long userId);

    /**
     * 查询订阅邮件的用户信息
     *
     * @return 用户信息
     */
    List<User> findByIsSubscribeMailIsTrue();
}