package com.finance.FinancialMotoboy.service.Register;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserRequest;
import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;

public interface UserRegisterHandler {

    boolean supports(DefaultUserRequest request);

    DefaultUserResponse register(DefaultUserRequest request);
}
