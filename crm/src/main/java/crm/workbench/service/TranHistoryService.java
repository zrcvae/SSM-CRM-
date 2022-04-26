package crm.workbench.service;

import crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {
    List<TranHistory> queryTranHistoryForDetailByTranId(String tranId);
}
