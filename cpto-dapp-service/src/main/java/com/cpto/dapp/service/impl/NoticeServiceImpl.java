package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.common.util.StringUtil;
import com.cpto.dapp.constant.Constant;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.Notice;
import com.cpto.dapp.enums.NoticeTypeEnum;
import com.cpto.dapp.enums.RelationTypeEnum;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.pojo.dto.NoticeDTO;
import com.cpto.dapp.pojo.vo.NoticeVO;
import com.cpto.dapp.pojo.vo.PageVO;
import com.cpto.dapp.pojo.vo.SourceVO;
import com.cpto.dapp.repository.NoticeRepository;
import com.cpto.dapp.service.NoticeService;
import com.cpto.dapp.service.SourceService;
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
 * 公告ServiceImpl
 *
 * @author sunli
 * @date 2018/12/18
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private SourceService sourceService;

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
    @Override
    public PageVO<NoticeVO> searchNotice(Timestamp searchTime, Integer page, Integer pageSize, Integer noticeType, Integer status) {

        /* 1.生成动态查询条件 */
        // 返回查询时间之前的数据
        if (ObjectUtil.isEmpty(searchTime)) {
            searchTime = DateUtil.now();
        }

        Specification<Notice> specification = getSQLWhere(searchTime, noticeType, status);

        /* 2.设置分页 */
        Sort sort = new Sort(Sort.Direction.DESC, Constant.SORT_KEY_ID);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        /* 3.进行查询 */
        Page<Notice> noticePage = noticeRepository.findAll(specification, pageable);

        List<NoticeVO> noticeVOList = new ArrayList<>();
        for (Notice notice : noticePage) {
            noticeVOList.add(editNoticeVO(notice));
        }

        PageVO<NoticeVO> noticePageVO = new PageVO();
        noticePageVO.setRows(noticeVOList);
        noticePageVO.setTotal(noticePage.getTotalElements());
        noticePageVO.setTotalPage(noticePage.getTotalPages());
        noticePageVO.setHasNext(noticePage.hasNext());
        noticePageVO.setSearchTime(searchTime);

        return noticePageVO;
    }

    /**
     * 根据id取得公告详情
     *
     * @param noticeId 公告id
     * @return 公告详情
     */
    @Override
    public NoticeVO findNotice(Long noticeId) {
        Notice notice = noticeRepository.findNotNullById(noticeId);
        return editNoticeDetailVO(notice);
    }

    /**
     * 新增公告
     *
     * @param admin     管理员
     * @param noticeDTO 新增的公告详情
     */
    @Override
    public void addNotice(ManagerAdmin admin, NoticeDTO noticeDTO) {

        /* 1.设置新增公告详情 */
        Notice notice = new Notice();
        notice.setTitleZh(noticeDTO.getTitleZh());
        notice.setTitleEn(noticeDTO.getTitleEn());
        notice.setAuthorZh(noticeDTO.getAuthorZh());
        notice.setAuthorEn(noticeDTO.getAuthorEn());
        notice.setNo(noticeDTO.getNo());
        notice.setUrlZh(noticeDTO.getUrlZh());
        notice.setUrlEn(noticeDTO.getUrlEn());
        notice.setContentZh(noticeDTO.getContentZh());
        notice.setContentEn(noticeDTO.getContentEn());
        notice.setNoticeType(noticeDTO.getNoticeType());
        notice.setRemark(noticeDTO.getRemark());
        notice.setStatus(StatusEnum.VALID.getCode());
        notice.setDeleted(false);
        notice.setCreatedTime(DateUtil.now());
        notice.setCreatedBy(admin);
        notice = noticeRepository.save(notice);

        /* 2.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.ADD_NOTICE, notice.getId());
    }

    /**
     * 修改公告
     *
     * @param admin     管理员
     * @param noticeId  被修改的公告id
     * @param noticeDTO 公告详情
     */
    @Override
    public void modifyNotice(ManagerAdmin admin, Long noticeId, NoticeDTO noticeDTO) {

        /* 1.取得被修改的内容 */
        Notice notice = noticeRepository.findNotNullById(noticeId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(noticeDTO.getModifiedTime(), notice.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        notice.setTitleZh(noticeDTO.getTitleZh());
        notice.setTitleEn(noticeDTO.getTitleEn());
        notice.setAuthorZh(noticeDTO.getAuthorZh());
        notice.setAuthorEn(noticeDTO.getAuthorEn());
        notice.setNo(noticeDTO.getNo());
        notice.setUrlZh(noticeDTO.getUrlZh());
        notice.setUrlEn(noticeDTO.getUrlEn());
        notice.setContentZh(noticeDTO.getContentZh());
        notice.setContentEn(noticeDTO.getContentEn());
        notice.setNoticeType(noticeDTO.getNoticeType());
        notice.setRemark(noticeDTO.getRemark());
        notice.setStatus(noticeDTO.getStatus());
        notice.setModifiedTime(DateUtil.now());
        notice.setModifiedBy(admin);
        noticeRepository.save(notice);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_NOTICE, notice.getId());
    }

    /**
     * 删除公告
     *
     * @param admin    管理员
     * @param noticeId 被删除的公告id
     */
    @Override
    public void removeNotice(ManagerAdmin admin, Long noticeId) {

        /* 1.检查删除信息 */
        Notice notice = noticeRepository.findNotNullById(noticeId);

        /* 2.删除公告 */
        notice.setDeleted(true);
        notice.setStatus(StatusEnum.INVALID.getCode());
        notice.setModifiedBy(admin);
        notice.setModifiedTime(DateUtil.now());
        noticeRepository.save(notice);

        /* 3.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.REMOVE_NOTICE, notice.getId());
    }

    /**
     * 生成动态查询条件
     *
     * @param searchTime 查询时间
     * @param noticeType 公告类型
     * @param status     状态
     * @return 动态查询条件
     */
    private Specification<Notice> getSQLWhere(Timestamp searchTime, Integer noticeType, Integer status) {

        Specification<Notice> specification = new Specification<Notice>() {
            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicatesList = new LinkedList<>();

                // 返回查询时间之前的数据
                if (ObjectUtil.isNotEmpty(searchTime)) {
                    predicatesList.add(cb.lessThanOrEqualTo(root.get("createdTime"), searchTime));
                }

                // 精确查询公告类型
                if (ObjectUtil.isNotEmpty(noticeType)) {
                    predicatesList.add(cb.equal(root.get("noticeType"), noticeType));
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
     * 编辑公告VO
     *
     * @param notice 公告
     * @return 公告VO
     */
    private NoticeVO editNoticeVO(Notice notice) {

        NoticeVO noticeVO = new NoticeVO();

        noticeVO.setId(notice.getId());
        noticeVO.setTitle(LanguageUtil.getTextByLanguage(notice.getTitleZh(), notice.getTitleEn()));
        noticeVO.setTitleZh(notice.getTitleZh());
        noticeVO.setTitleEn(notice.getTitleEn());
        noticeVO.setNo(notice.getNo());
        noticeVO.setAuthor(LanguageUtil.getTextByLanguage(notice.getAuthorZh(), notice.getAuthorEn()));
        noticeVO.setAuthorZh(notice.getAuthorZh());
        noticeVO.setAuthorEn(notice.getAuthorEn());
        noticeVO.setUrl(StringUtil.ellipsis(LanguageUtil.getTextByLanguage(notice.getUrlZh(), notice.getUrlEn()), 100, null));
        noticeVO.setUrlZh(StringUtil.ellipsis(notice.getUrlZh(), 100, null));
        noticeVO.setUrlEn(StringUtil.ellipsis(notice.getUrlEn(), 100, null));
        noticeVO.setContent(StringUtil.ellipsis(LanguageUtil.getTextByLanguage(notice.getContentZh(), notice.getContentEn()), 100, null));
        noticeVO.setContentZh(StringUtil.ellipsis(notice.getContentZh(), 100, null));
        noticeVO.setContentEn(StringUtil.ellipsis(notice.getContentEn(), 100, null));
        noticeVO.setNoticeType(notice.getNoticeType());
        noticeVO.setNoticeTypeName(NoticeTypeEnum.getNameByCode(notice.getNoticeType()));
        noticeVO.setStatus(notice.getStatus());
        noticeVO.setStatusName(StatusEnum.getNameByCode(notice.getStatus()));
        noticeVO.setRemark(notice.getRemark());
        noticeVO.setCreatedBy(ObjectUtil.isNotEmpty(notice.getCreatedBy()) ? notice.getCreatedBy().getName() : null);
        noticeVO.setCreatedTime(notice.getCreatedTime());
        noticeVO.setModifiedBy(ObjectUtil.isNotEmpty(notice.getCreatedBy()) ? notice.getCreatedBy().getName() : null);
        noticeVO.setModifiedTime(notice.getModifiedTime());

        return noticeVO;
    }

    /**
     * 编辑公告详情VO
     *
     * @param notice 公告
     * @return 公告详情VO
     */
    private NoticeVO editNoticeDetailVO(Notice notice) {

        NoticeVO noticeVO = editNoticeVO(notice);

        noticeVO.setContent(LanguageUtil.getTextByLanguage(notice.getContentZh(), notice.getContentEn()));
        noticeVO.setContentZh(notice.getContentZh());
        noticeVO.setContentEn(notice.getContentEn());

        noticeVO.setUrl(LanguageUtil.getTextByLanguage(notice.getUrlZh(), notice.getUrlEn()));
        noticeVO.setUrlZh(notice.getUrlZh());
        noticeVO.setUrlEn(notice.getUrlEn());

        List<SourceVO> sourceVOList = sourceService.findSourceList(RelationTypeEnum.NOTICE.getCode(), notice.getId());
        noticeVO.setSources(sourceVOList);

        return noticeVO;
    }
}