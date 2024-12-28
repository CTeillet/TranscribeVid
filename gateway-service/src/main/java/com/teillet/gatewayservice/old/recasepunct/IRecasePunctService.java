package com.teillet.gatewayservice.old.recasepunct;

import com.teillet.gatewayservice.old.recasepunct.dto.RecasePunctResult;

public interface IRecasePunctService {
	RecasePunctResult callRecasePuncApi(String text) throws RecasePunctException;
}
