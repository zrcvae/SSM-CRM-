package crm.workbench.service;

import crm.workbench.domain.FunnelVO;
import crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    void saveCreateTran(Map<String,Object> map);

    Tran queryTranForDetailById(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();
}
