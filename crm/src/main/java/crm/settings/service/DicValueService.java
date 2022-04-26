package crm.settings.service;

import crm.settings.domain.DicValue;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DicValueService {
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
