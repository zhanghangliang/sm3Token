package com.ob.common.sm3token.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class EncryptQueryDTO {

    private HashMap<String, String> data;
}
