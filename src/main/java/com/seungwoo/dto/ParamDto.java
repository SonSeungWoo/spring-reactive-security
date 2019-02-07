package com.seungwoo.dto;

import lombok.Data;

/**
 * Created by Leo.
 * User: ssw
 * Date: 2019-02-07
 * Time: 14:32
 */
@Data
public class ParamDto<T> {

    private String serviceName;
    private String userKey;
    private String languageCode;
    private String currencyCode;
    private String trId;
    private T request;

}
