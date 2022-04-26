package crm.workbench.service;

import crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
