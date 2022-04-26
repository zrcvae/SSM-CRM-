package crm.workbench.service.impl;

import crm.workbench.domain.TranRemark;
import crm.workbench.mapper.TranRemarkMapper;
import crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("tranRemarkService")
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public List<TranRemark> queryTranRemarkDetailByTranId(String tranId) {
        return tranRemarkMapper.selectTranRemarkForDetailByTranId(tranId);
    }
}
