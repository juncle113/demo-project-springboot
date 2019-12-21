package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.MessageDTO;
import com.cpto.dapp.pojo.vo.MessageVO;
import com.cpto.dapp.pojo.vo.PageVO;

import java.sql.Timestamp;

/**
 * 消息Service
 *
 * @author sunli
 * @date 2019/02/18
 */
public interface MessageService extends BaseService {

    /**
     * 查询满足条件的消息
     *
     * @param searchTime  查询时间
     * @param page        当前页数
     * @param pageSize    每页条数
     * @param messageType 消息类型
     * @param status      状态
     * @return 消息列表
     */
    PageVO<MessageVO> searchMessage(Timestamp searchTime, Integer page, Integer pageSize, Integer messageType, Integer status);

    /**
     * 取得消息
     *
     * @param messageId 消息id
     * @return 消息
     */
    MessageVO findMessage(Long messageId);

    /**
     * 新增消息
     *
     * @param admin      管理员
     * @param messageDTO 消息信息
     */
    void addMessage(ManagerAdmin admin, MessageDTO messageDTO);

    /**
     * 修改消息
     *
     * @param admin      管理员
     * @param messageId  被修改的消息id
     * @param messageDTO 消息信息
     */
    void modifyMessage(ManagerAdmin admin, Long messageId, MessageDTO messageDTO);

    /**
     * 删除消息
     *
     * @param admin     管理员
     * @param messageId 被删除的消息id
     */
    void removeMessage(ManagerAdmin admin, Long messageId);
}