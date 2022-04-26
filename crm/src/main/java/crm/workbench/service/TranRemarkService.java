package crm.workbench.service;

import crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkService {
    List<TranRemark> queryTranRemarkDetailByTranId(String tranId);
}
