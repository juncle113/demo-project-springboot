package com.cpto.dapp.repository;


import com.cpto.dapp.domain.Message;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 消息Repository
 *
 * @author sunli
 * @date 2019/02/18
 */
public interface MessageRepository extends BaseRepository<Message, Long>, JpaSpecificationExecutor<Message> {

}