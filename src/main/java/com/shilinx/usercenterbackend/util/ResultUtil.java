package com.shilinx.usercenterbackend.util;

import com.shilinx.usercenterbackend.common.BaseResponse;

/**
 * @author slx
 * @time 17:49
 */
public class ResponseUtil {

    public <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }
}
