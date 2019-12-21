package com.cpto.dapp.repository;

import com.cpto.dapp.domain.OrderInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单Repository
 *
 * @author sunli
 * @date 2019/01/16
 */
public interface OrderInfoRepository extends BaseRepository<OrderInfo, Long>, JpaSpecificationExecutor<OrderInfo> {

    /**
     * 统计投资某项目的全部订单数量
     *
     * @param projectId 项目id
     * @return 订单数量
     */
    Integer countByProjectId(Long projectId);

    /**
     * 统计参与某项目的投资订单数（人数）
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 投资订单数（人数）
     */
    Integer countByProjectIdAndStatusIn(Long projectId, List<Integer> statusList);

    /**
     * 查询某个项目所有用户的订单列表
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 订单列表
     */
    List<OrderInfo> findByProjectIdAndStatusInOrderByIdDesc(Long projectId, List<Integer> statusList);

    /**
     * 查询某个用户的某个项目的订单信息
     *
     * @param userId     用户id
     * @param projectId  项目id
     * @param statusList 状态
     * @return 订单信息
     */
    List<OrderInfo> findByUserIdAndProjectIdAndStatusInOrderByIdDesc(Long userId, Long projectId, List<Integer> statusList);

    /**
     * 根据用户id查询全部订单列表
     *
     * @param userId 用户id
     * @return 订单列表
     */
    List<OrderInfo> findByUserIdOrderByIdDesc(Long userId);

    /**
     * 根据状态查询订单信息
     *
     * @param statusList 状态
     * @return 订单信息
     */
    List<OrderInfo> findByStatusInOrderByIdDesc(List<Integer> statusList);

    /**
     * 统计该项目的投资数额合计
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 投资数额合计
     */
    @Query(nativeQuery = true, value = "select ifnull(sum(pay_amount), 0) from order_info where project_id = :projectId and status in (:statusList)")
    BigDecimal sumPayAmountByProjectIdAndStatusIn(Long projectId, List<Integer> statusList);
}