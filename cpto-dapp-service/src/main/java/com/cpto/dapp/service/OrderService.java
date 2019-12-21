package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.OrderInfo;
import com.cpto.dapp.domain.User;
import com.cpto.dapp.pojo.dto.OrderDTO;
import com.cpto.dapp.pojo.vo.OrderVO;
import com.cpto.dapp.pojo.vo.PageVO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 订单Service
 *
 * @author sunli
 * @date 2019/01/16
 */
public interface OrderService extends BaseService {

    /**
     * 创建订单
     *
     * @param user     用户
     * @param orderDTO 创建订单信息
     */
    void addOrder(User user, OrderDTO orderDTO);

    /**
     * 查询用户全部订单列表
     *
     * @param user 用户
     * @return 订单列表
     */
    List<OrderVO> findOrderListByUser(User user);

    /**
     * 查询项目全部订单列表
     *
     * @param projectId  项目id
     * @param statusList 状态
     * @return 订单列表
     */
    List<OrderInfo> findOrderListByProject(Long projectId, List<Integer> statusList);

    /**
     * 查询满足条件的订单
     *
     * @param searchTime      查询时间
     * @param page            当前页数
     * @param pageSize        每页条数
     * @param id              订单id
     * @param userId          用户id
     * @param userName        用户名
     * @param projectNo       项目编号
     * @param projectName     项目名称
     * @param status          状态
     * @param remark          备注
     * @param fromCreatedTime 创建开始时间
     * @param toCreatedTime   创建结束时间
     * @return 订单列表
     */
    PageVO<OrderVO> searchOrder(Timestamp searchTime,
                                Integer page,
                                Integer pageSize,
                                Long id,
                                Long userId,
                                String userName,
                                String projectNo,
                                String projectName,
                                Integer status,
                                String remark,
                                String fromCreatedTime,
                                String toCreatedTime);

    /**
     * 退出投资
     *
     * @param user    用户
     * @param orderId 订单id
     */
    void cancelOrder(User user, Long orderId);

    /**
     * 保存订单信息
     *
     * @param orderInfo 订单信息
     */
    void saveOrder(OrderInfo orderInfo);

    /**
     * 项目失败时，作废投资订单
     *
     * @param admin     管理员
     * @param projectId 项目id
     */
    void invalidateOrderByProjectFailure(ManagerAdmin admin, Long projectId);

    /**
     * 项目成功时，更新投资订单
     *
     * @param admin     管理员
     * @param projectId 项目id
     */
    void updateOrderByProjectSuccess(ManagerAdmin admin, Long projectId);

    /**
     * 统计投资某项目的全部订单数量
     *
     * @param projectId 项目id
     * @return 项目订单数量
     */
    Integer countOrderByProject(Long projectId);

    /**
     * 统计参与该项目的投资人数
     *
     * @param projectId 项目id
     * @return 投资人数
     */
    Integer countJoinNumberByProject(Long projectId);

    /**
     * 统计该项目的投资数额合计
     *
     * @param projectId 项目id
     * @return 投资数额合计
     */
    BigDecimal sumPayAmountByProject(Long projectId);

    /**
     * 完成订单（订单锁仓释放时间到期后，释放投资）
     */
    void completeOrder();
}