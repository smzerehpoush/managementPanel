package com.nrdc.policeHamrah.helper;

import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.ConstantDao;

public class ABISImpl {
    public StandardResponse sendRequest(Object request, String serviceName) throws Exception {
        String servicePath = ConstantDao.getConstant(serviceName);
        String result = CallWebService.callPostService(servicePath, request);
        StandardResponse<String> response = new StandardResponse<>();
        response.setResponse(result);
        return response;
    }
}
