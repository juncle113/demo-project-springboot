package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Message;
import com.cpto.dapp.enums.MessageTypeEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.pojo.dto.MessageDTO;
import com.cpto.dapp.pojo.vo.MessageVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.repository.MessageRepository;
import com.cpto.dapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 消息ServiceImpl
 *
 * @author sunli
 * @date 2019/02/18
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * 查询满足条件的消息
     *
     * @param searchTime  查询时间
     * @param messageType 消息类型
     * @param status      状态
     * @return 消息列表
     */
    @Override
    public PageVO<MessageVO> searchMessage(Timestamp searchTime, Integer page, Integer pageSize, Integer messageType, Integer status) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<Message> specification = getSQLWhere(searchTime, messageType, status);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<Message> messagePage = messageRepository.findAll(specification, pageable);

        List<MessageVO> messageVOList = new ArrayList<>();
        for (Message message : messagePage) {
            messageVOList.add(editMessageVO(message));
        }

        PageVO<MessageVO> messagePageVO = new PageVO();
        messagePageVO.setRows(messageVOList);
        messagePageVO.setTotal(messagePage.getTotalElements());
        messagePageVO.setTotalPage(messagePage.getTotalPages());
        messagePageVO.setHasNext(messagePage.hasNext());
        messagePageVO.setSearchTime(searchTime);

        return messagePageVO;
    }

    /**
     * 取得消息
     *
     * @param messageId 消息id
     * @return 消息
     */
    @Override
    public MessageVO findMessage(Long messageId) {

        /* 1.查询系统公告 */
        Message message = messageRepository.findNotNullById(messageId);

        /* 2.设置系统公告VO */
        return editMessageVO(message);
    }

    /**
     * 新增消息
     *
     * @param admin      管理员
     * @param messageDTO 消息信息
     */
    @Override
    public void addMessage(ManagerAdmin admin, MessageDTO messageDTO) {

        /* 1.新增消息 */
        Message message = new Message();

        message.setMessageType(messageDTO.getMessageType());
        message.setTitleZh(messageDTO.getTitleZh());
        message.setTitleEn(messageDTO.getTitleEn());
        message.setContentZh(messageDTO.getContentZh());
        message.setContentEn(messageDTO.getContentEn());
        message.setDeleted(false);
        message.setStatus(StatusEnum.VALID.getCode());
        message.setRemark(messageDTO.getRemark());
        message.setCreatedBy(admin);
        message.setCreatedTime(DateUtil.now());

        message = messageRepository.save(message);

        /* 2.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_MESSAGE, message.getId());
    }

    /**
     * 修改消息
     *
     * @param admin      管理员
     * @param messageId  被修改的消息id
     * @param messageDTO 消息信息
     */
    @Override
    public void modifyMessage(ManagerAdmin admin, Long messageId, MessageDTO messageDTO) {

        /* 1.查询消息 */
        Message message = messageRepository.findNotNullById(messageId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(messageDTO.getModifiedTime(), message.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        message.setTitleZh(messageDTO.getTitleZh());
        message.setTitleEn(messageDTO.getTitleEn());
        message.setContentZh(messageDTO.getContentZh());
        message.setContentEn(messageDTO.getContentEn());
        message.setRemark(messageDTO.getRemark());
        message.setStatus(messageDTO.getStatus());
        message.setModifiedBy(admin);
        message.setModifiedTime(DateUtil.now());

        message = messageRepository.save(message);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_MESSAGE, message.getId());
    }

    /**
     * 删除消息
     *
     * @param admin     管理员
     * @param messageId 被删除的消息id
     */
    @Override
    public void removeMessage(ManagerAdmin admin, Long messageId) {

        /* 1.取得删除信息 */
        Message message = messageRepository.findNotNullById(messageId);

        /* 2.删除消息 */
        message.setDeleted(true);
        message.setStatus(StatusEnum.INVALID.getCode());
        message.setModifiedBy(admin);
        message.setModifiedTime(DateUtil.now());
        messageRepository.save(message);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.REMOVE_MESSAGE, message.getId());
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime  查询时间
     * @param messageType 消息类型
     * @param status      状态
     * @return 动态查询条件
     */
    private Specification<Message> getSQLWhere(Timestamp searchTime, Integer messageType, Integer status) {

        Specification<Message> specification = new Specification<Message>() {
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询消息类型
                if (ObjectUtil.isNotEmpty(messageType)) {
                    predicatesList.add(cb.equal(root.get("messageType"), messageType));
                }

                // 精确查询状态
                if (ObjectUtil.isNotEmpty(status)) {
                    predicatesList.add(cb.equal(root.get("status"), status));
                }

                // 返回生成的条件（条件为并且的关系）
                return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
            }
        };

        return specification;
    }

    /**
     * 编辑消息VO
     *
     * @param message 消息
     * @return 消息VO
     */
    private MessageVO editMessageVO(Message message) {

        MessageVO messageVO = new MessageVO();

        messageVO.setId(message.getId());
        messageVO.setMessageType(message.getMessageType());
        messageVO.setMessageTypeName(MessageTypeEnum.getNameByCode(message.getMessageType()));
        messageVO.setTitle(LanguageUtil.getTextByLanguage(message.getTitleZh(), message.getTitleEn()));
        messageVO.setTitleZh(message.getTitleZh());
        messageVO.setTitleEn(message.getTitleEn());
        messageVO.setContent(LanguageUtil.getTextByLanguage(message.getContentZh(), message.getContentEn()));
        messageVO.setContentZh(message.getContentZh());
        messageVO.setContentEn(message.getContentEn());
        messageVO.setRemark(message.getRemark());
        messageVO.setStatus(message.getStatus());
        messageVO.setStatusName(StatusEnum.getNameByCode(message.getStatus()));
        messageVO.setCreatedBy(ObjectUtil.isNotEmpty(message.getCreatedBy()) ? message.getCreatedBy().getName() : null);
        messageVO.setCreatedTime(message.getCreatedTime());
        messageVO.setModifiedBy(ObjectUtil.isNotEmpty(message.getCreatedBy()) ? message.getCreatedBy().getName() : null);
        messageVO.setModifiedTime(message.getModifiedTime());

        return messageVO;
    }
}