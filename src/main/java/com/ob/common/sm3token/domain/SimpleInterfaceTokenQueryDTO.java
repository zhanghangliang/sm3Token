package com.ob.common.sm3token.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SimpleInterfaceTokenQueryDTO extends SimpleInterfaceQueryDTO {

    public SimpleInterfaceTokenQueryDTO(String authToken, String timeStamp, String data) {
        this.head = new HashMap<>();
        this.head.put("auth_token", authToken);
        this.head.put("TimeStamp", timeStamp);
        setData(data);
    }

    private Map<String, String> head;
}
