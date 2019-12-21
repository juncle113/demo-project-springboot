package com.cpto.dapp.service;

import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.pojo.dto.NoticeDTO;
import com.cpto.dapp.pojo.vo.NoticeVO;
import com.cpto.dapp.pojo.vo.PageVO;

import java.sql.Timestamp;

/**
 * 公告Service
 *
 * @author sunli
 * @date 2019/02/18
 */
public interface NoticeService extends BaseService {

    /**
     * 根据id取得公告详情
     *
     * @param noticeId 公告id
     * @return 公告详情
     */
    NoticeVO findNotice(Long noticeId);

    /**
     * 查询满足条件的公告
     *
     * @param searchTime 查询时间
     * @param page       当前页数
     * @param pageSize   每页条数
     * @param noticeType 公告类型
     * @param status     状态
     * @return 公告列表
     */
    PageVO<NoticeVO> searchNotice(Timestamp searchTime, Integer page, Integer pageSize, Integer noticeType, Integer status);

    /**
     * 新增公告
     *
     * @param admin     管理员
     * @param noticeDTO 公告信息
     */
    void addNotice(ManagerAdmin admin, NoticeDTO noticeDTO);

    /**
     * 修改公告
     *
     * @param admin     管理员
     * @param noticeId  公告id
     * @param noticeDTO 公告信息
     */
    void modifyNotice(ManagerAdmin admin, Long noticeId, NoticeDTO noticeDTO);

    /**
     * 删除公告
     *
     * @param admin    管理员
     * @param noticeId 公告id
     */
    void removeNotice(ManagerAdmin admin, Long noticeId);
}