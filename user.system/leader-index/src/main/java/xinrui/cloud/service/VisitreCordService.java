package xinrui.cloud.service;

import xinrui.cloud.domain.VisitreCord;
import xinrui.cloud.domain.dto.VisitreCordDto;

/**
 * 获取走访记录相关的接口
 */
public interface VisitreCordService extends BaseService<VisitreCord> {
    /**
     * 获取走访记录详情
     * @param visitreId 走访记录id
     * @param messageId
     * @return
     */
    VisitreCordDto  getVisitreCordSimpleInfo(Long visitreId, Long messageId);
}
