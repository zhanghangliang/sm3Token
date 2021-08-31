package com.ob.common.sm3token.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class EncryptSendQueryDTO extends EncryptQueryDTO{

    private String url;
}
