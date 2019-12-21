package com.cpto.dapp.repository;

import com.cpto.dapp.domain.Notice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 公告Repository
 *
 * @author sunli
 * @date 2018/12/18
 */
public interface NoticeRepository extends BaseRepository<Notice, Long>, JpaSpecificationExecutor<Notice> {

}