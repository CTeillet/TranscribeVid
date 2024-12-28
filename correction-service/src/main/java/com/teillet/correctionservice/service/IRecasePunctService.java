package com.teillet.correctionservice.service;

import com.teillet.correctionservice.exception.RecasePunctException;
import com.teillet.correctionservice.dto.RecasePunctResult;

public interface IRecasePunctService {
	RecasePunctResult callRecasePuncApi(String text) throws RecasePunctException;
}
