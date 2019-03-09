package com.nrdc.policeHamrah.helper;

import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.model.dao.ConstantDao;

public class ABISImpl {
    public StandardResponse sendRequest(Object request, String serviceName) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        String result = CallWebService.callPostService(servicePath, request);
        StandardResponse<String> response = new StandardResponse<>();
        response.setResponse(result);
        return response;
    }

    public StandardResponse sendRequest(String serviceName, String pathParam) throws Exception {
        String servicePath = ConstantDao.getConstant("ABIS_BASE_PATH") + ConstantDao.getConstant(serviceName);
        String result = CallWebService.callGetService(servicePath + "/" + pathParam);
        StandardResponse<String> response = new StandardResponse<>();
        response.setResponse(result);
        return response;
    }
}
