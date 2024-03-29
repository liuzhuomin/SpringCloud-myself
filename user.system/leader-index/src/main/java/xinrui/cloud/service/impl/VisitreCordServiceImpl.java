package xinrui.cloud.service.impl;

import com.google.common.collect.Lists;
import xinrui.cloud.domain.CompanyMessage;
import xinrui.cloud.domain.DifficultCompany;
import xinrui.cloud.domain.VisitreCord;
import xinrui.cloud.domain.dto.DifficultCompanyDto;
import xinrui.cloud.domain.dto.VisitreCordDto;
import xinrui.cloud.service.DifficultService;
import xinrui.cloud.service.OtherGroupService;
import xinrui.cloud.service.VisitreCordService;
import xinrui.cloud.service.LeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <B>Title:</B>VisitreCordServiceImpl</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/10 11:11
 */
@Service
public class VisitreCordServiceImpl extends BaseServiceImpl<VisitreCord> implements VisitreCordService {

    @Autowired
    DifficultService difficultService;

    @Autowired
    OtherGroupService otherGroupService;

    @Autowired
    LeaderService leaderService;

    @Override
    @Transactional
    public VisitreCordDto getVisitreCordSimpleInfo(Long visitreId, Long messageId) {
        VisitreCord byId = findById(visitreId);
        Assert.notNull(byId, "走访记录对象找不到!");
        VisitreCordDto visitreCordDto = VisitreCordDto.copyFrom(byId);
        visitreCordDto.setCompanyName(otherGroupService.findById(byId.getCompanyId()).getName());
        List<DifficultCompany> difficultCompany = byId.getDifficultCompany();
        if (!CollectionUtils.isEmpty(difficultCompany)) {
            List<DifficultCompanyDto> difficultCompanyDtos = Lists.newArrayList();
            List<DifficultCompanyDto> afterDifficultCompanyDtos = Lists.newArrayList();
            for (int i = 0; i < difficultCompany.size(); i++) {
                DifficultCompany difficultCompanyObject = difficultCompany.get(i);
                DifficultCompanyDto difficultByDifficultId = difficultService.getDifficultByDifficultId(difficultCompanyObject.getId());
                if (difficultByDifficultId != null){
                    if(difficultByDifficultId.getVisitreCordStatus()==0) {
                        difficultCompanyDtos.add(difficultByDifficultId);
                    } else {
                        afterDifficultCompanyDtos.add(difficultByDifficultId);
                    }
                }
            }
            visitreCordDto.setDifficultCompany(difficultCompanyDtos);
            visitreCordDto.setAfterDifficultCompany(afterDifficultCompanyDtos);
        }
        String lastUpdate = visitreCordDto.getLastUpdate();
        if(!StringUtils.isEmpty(lastUpdate)){
            CompanyMessage messageType = leaderService.findById(messageId);
            if(messageType.getVisitreType().intValue()==1){
                String visitreDate = visitreCordDto.getVisitreDate();
                visitreCordDto.setLastUpdate(visitreDate);
                visitreCordDto.setVisitreDate(lastUpdate);
            }
        }
        return visitreCordDto;
    }
}
