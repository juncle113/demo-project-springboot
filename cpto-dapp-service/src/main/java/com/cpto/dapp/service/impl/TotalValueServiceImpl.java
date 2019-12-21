package com.cpto.dapp.service.impl;

import com.cpto.dapp.common.util.LanguageUtil;
import com.cpto.dapp.common.util.ObjectUtil;
import com.cpto.dapp.domain.SystemTotalValue;
import com.cpto.dapp.enums.ValueTypeEnum;
import com.cpto.dapp.exception.DataNotFoundException;
import com.cpto.dapp.pojo.vo.TotalValueVO;
import com.cpto.dapp.repository.SystemTotalValueRepository;
import com.cpto.dapp.service.TotalValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 价值总量ServiceImpl
 *
 * @author sunli
 * @date 2019/01/09
 */
@Service
public class TotalValueServiceImpl extends BaseServiceImpl implements TotalValueService {

    @Autowired
    private SystemTotalValueRepository systemTotalValueRepository;

    /**
     * 取得价值总量信息
     *
     * @return 价值总量
     */
    @Override
    public TotalValueVO findTotalValue() {

        /* 1.取得设置数据 */
        List<SystemTotalValue> valueSummaryList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.VALUE_SUMMARY.getCode());
        if (ObjectUtil.isEmptyCollection(valueSummaryList)) {
            throw new DataNotFoundException();
        }

        List<SystemTotalValue> totalAllocationList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.TOTAL_ALLOCATION.getCode());
        if (ObjectUtil.isEmptyCollection(totalAllocationList)) {
            throw new DataNotFoundException();
        }

        List<SystemTotalValue> circulationSourceList = systemTotalValueRepository.findByValueTypeOrderBySortNo(ValueTypeEnum.CIRCULATION_SOURCE.getCode());
        if (ObjectUtil.isEmptyCollection(circulationSourceList)) {
            throw new DataNotFoundException();
        }

        /* 2.编辑数据 */
        TotalValueVO totalValueVO = new TotalValueVO();

        // 价值概要
        List<TotalValueVO.ValueSummary> valueSummaryVOList = new ArrayList<>();
        TotalValueVO.ValueSummary valueSummary;
        for (SystemTotalValue systemTotalValue : valueSummaryList) {
            valueSummary = totalValueVO.new ValueSummary();
            valueSummary.setSortNo(systemTotalValue.getSortNo());
            valueSummary.setTitle(LanguageUtil.getTextByLanguage(systemTotalValue.getTitleZh(), systemTotalValue.getTitleEn()));
            valueSummary.setAmount(systemTotalValue.getAmount());
            valueSummaryVOList.add(valueSummary);
        }

        // 总量分配
        List<TotalValueVO.TotalAllocation> totalAllocationVOList = new ArrayList<>();
        TotalValueVO.TotalAllocation totalAllocation;
        for (SystemTotalValue systemTotalValue : totalAllocationList) {
            totalAllocation = totalValueVO.new TotalAllocation();
            totalAllocation.setSortNo(systemTotalValue.getSortNo());
            totalAllocation.setTitle(LanguageUtil.getTextByLanguage(systemTotalValue.getTitleZh(), systemTotalValue.getTitleEn()));
            totalAllocation.setAmount(systemTotalValue.getAmount());
            totalAllocation.setColor(systemTotalValue.getColor());
            totalAllocationVOList.add(totalAllocation);
        }

        // 流通来源
        BigDecimal circulationAmount = BigDecimal.ZERO;
        List<TotalValueVO.CirculationSource> circulationSourceVOList = new ArrayList<>();
        TotalValueVO.CirculationSource circulationSource;
        for (SystemTotalValue systemTotalValue : circulationSourceList) {
            circulationSource = totalValueVO.new CirculationSource();
            circulationSource.setSortNo(systemTotalValue.getSortNo());
            circulationSource.setTitle(LanguageUtil.getTextByLanguage(systemTotalValue.getTitleZh(), systemTotalValue.getTitleEn()));
            circulationSource.setAmount(systemTotalValue.getAmount());
            circulationSource.setColor(systemTotalValue.getColor());
            circulationSourceVOList.add(circulationSource);

            // 计算合计
            circulationAmount = circulationAmount.add(systemTotalValue.getAmount());
        }

        totalValueVO.setValueSummaries(valueSummaryVOList);
        totalValueVO.setTotalAllocations(totalAllocationVOList);
        totalValueVO.setCirculationAmount(circulationAmount);
        totalValueVO.setCirculationSources(circulationSourceVOList);

        return totalValueVO;
    }
}
