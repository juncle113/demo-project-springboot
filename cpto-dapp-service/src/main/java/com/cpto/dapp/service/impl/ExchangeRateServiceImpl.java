package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.DateUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.constant.ManagerLogConstant;
import com.cpto.dapp.domain.ManagerAdmin;
import com.cpto.dapp.domain.SystemExchangeRate;
import com.cpto.dapp.enums.StatusEnum;
import com.cpto.dapp.exception.DataExpiredException;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.dto.ExchangeRateDTO;
import com.cpto.dapp.pojo.vo.ExchangeRateVO;
import com.cpto.dapp.repository.SystemExchangeRateRepository;
import com.cpto.dapp.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 币种汇率ServiceImpl
 *
 * @author sunli
 * @date 2019/03/05
 */
@Service
public class ExchangeRateServiceImpl extends BaseServiceImpl implements ExchangeRateService {

    @Autowired
    private SystemExchangeRateRepository systemExchangeRateRepository;

    /**
     * 根据币种查询汇率信息
     *
     * @param fromCoinKind 源币种
     * @param toCoinKind   目标币种
     * @return 币种汇率信息
     */
    @Override
    public SystemExchangeRate findExchangeRateByCoin(String fromCoinKind, String toCoinKind) {

        SystemExchangeRate systemExchangeRate = systemExchangeRateRepository.findByFromCoinKindAndToCoinKind(fromCoinKind, toCoinKind);
        if (ObjectUtil.isEmpty(systemExchangeRate)) {
            throw new DataNotFoundException();
        }

        return systemExchangeRate;
    }

    /**
     * 根据币种查询汇率信息
     *
     * @param fromCoinKind 源币种
     * @param toCoinKind   目标币种
     * @return 币种汇率信息
     */
    @Override
    public ExchangeRateVO findExchangeVORateByCoin(String fromCoinKind, String toCoinKind) {
        return editExchangeRateVO(findExchangeRateByCoin(fromCoinKind, toCoinKind));
    }

    /**
     * 查询汇率信息列表
     *
     * @return 币种汇率信息列表
     */
    @Override
    public List<ExchangeRateVO> findExchangeRateList() {

        List<ExchangeRateVO> exchangeRateVOList = new ArrayList<>();

        List<SystemExchangeRate> systemExchangeRateList = systemExchangeRateRepository.findAll();
        for (SystemExchangeRate systemExchangeRate : systemExchangeRateList) {
            exchangeRateVOList.add(editExchangeRateVO(systemExchangeRate));
        }

        return exchangeRateVOList;
    }

    /**
     * 根据币种汇率id查询汇率信息
     *
     * @param exchangeRateId 币种汇率id
     * @return 币种汇率信息
     */
    @Override
    public ExchangeRateVO findExchangeRate(Long exchangeRateId) {
        SystemExchangeRate systemExchangeRate = systemExchangeRateRepository.findNotNullById(exchangeRateId);
        return editExchangeRateVO(systemExchangeRate);
    }

    /**
     * 修改币种汇率
     *
     * @param admin           管理员
     * @param exchangeRateId  币种汇率id
     * @param exchangeRateDTO 币种汇率信息
     */
    @Override
    public void modifyExchangeRate(ManagerAdmin admin, Long exchangeRateId, ExchangeRateDTO exchangeRateDTO) {

        /* 1.取得被修改的内容 */
        SystemExchangeRate systemExchangeRate = systemExchangeRateRepository.findNotNullById(exchangeRateId);

        /* 2.检查最后修改时间，避免查询信息被修改过 */
        if (ObjectUtil.notEquals(exchangeRateDTO.getModifiedTime(), systemExchangeRate.getModifiedTime())) {
            throw new DataExpiredException();
        }

        /* 3.设置修改内容 */
        systemExchangeRate.setRate(exchangeRateDTO.getRate());
        systemExchangeRate.setStatus(exchangeRateDTO.getStatus());
        systemExchangeRate.setRemark(exchangeRateDTO.getRemark());
        systemExchangeRate.setModifiedBy(admin);
        systemExchangeRate.setModifiedTime(DateUtil.now());
        systemExchangeRateRepository.save(systemExchangeRate);

        /* 4.记录日志 */
        managerLogService.saveManagerLog(admin, ManagerLogConstant.MODIFY_EXCHANGE_RATE, systemExchangeRate.getId());
    }

    /**
     * 编辑汇率VO
     *
     * @param systemExchangeRate 汇率信息
     * @return 汇率VO
     */
    private ExchangeRateVO editExchangeRateVO(SystemExchangeRate systemExchangeRate) {

        ExchangeRateVO exchangeRateVO = new ExchangeRateVO();

        exchangeRateVO.setId(systemExchangeRate.getId());
        exchangeRateVO.setFromCoinKind(systemExchangeRate.getFromCoinKind());
        exchangeRateVO.setToCoinKind(systemExchangeRate.getToCoinKind());
        exchangeRateVO.setRate(systemExchangeRate.getRate());
        exchangeRateVO.setRemark(systemExchangeRate.getRemark());
        exchangeRateVO.setStatus(systemExchangeRate.getStatus());
        exchangeRateVO.setStatusName(StatusEnum.getNameByCode(systemExchangeRate.getStatus()));
        exchangeRateVO.setCreatedBy(ObjectUtil.isNotEmpty(systemExchangeRate.getCreatedBy()) ? systemExchangeRate.getCreatedBy().getName() : null);
        exchangeRateVO.setCreatedTime(systemExchangeRate.getCreatedTime());
        exchangeRateVO.setModifiedBy(ObjectUtil.isNotEmpty(systemExchangeRate.getCreatedBy()) ? systemExchangeRate.getCreatedBy().getName() : null);
        exchangeRateVO.setModifiedTime(systemExchangeRate.getModifiedTime());

        return exchangeRateVO;
    }
}