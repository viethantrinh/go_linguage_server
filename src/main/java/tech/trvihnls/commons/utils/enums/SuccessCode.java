package tech.trvihnls.commons.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    GENERAL_SUCCESS(1000, HttpStatus.OK, "success"),
    SIGN_IN_SUCCEEDED(1000, HttpStatus.OK, "sign in succeeded"),
    SIGN_UP_SUCCEEDED(1000, HttpStatus.OK, "sign up succeeded"),
    CREATE_PAYMENT_INTENT_SUCCEEDED(1000, HttpStatus.OK, "create payment intent succeeded"),
    CREATE_STRIPE_SUBSCRIPTION_SUCCEEDED(1000, HttpStatus.OK, "create stripe subscription succeed"),
    RETRIEVE_USER_INFO_SUCCEEDED(1000, HttpStatus.OK, "retrieve user's information successfully"),
    UPDATE_USER_INFO_SUCCEEDED(1000, HttpStatus.OK, "update user's information successfully"),
    DELETE_USER_INFO_SUCCEEDED(1000, HttpStatus.OK, "delete user's information successfully"),
    ;


    private final int code;
    private final HttpStatus status;
    private final String message;
}
