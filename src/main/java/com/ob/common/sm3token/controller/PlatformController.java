package com.ob.common.sm3token.controller;

import com.ob.common.sm3token.base.ResponseResult;
import com.ob.common.sm3token.domain.EncryptResultDTO;
import com.ob.common.sm3token.domain.EncryptSendQueryDTO;
import com.ob.common.sm3token.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static com.ob.common.sm3token.service.TokenManager.BASE_URL;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("plat")
@EnableScheduling
public class PlatformController {

    @Autowired
    private TokenManager tokenManager;

    @PostMapping()
    @ResponseBody
    public ResponseResult<EncryptResultDTO> plat(@RequestBody EncryptSendQueryDTO encryptSendQueryDTO) {

        encryptSendQueryDTO.setUrl(BASE_URL + encryptSendQueryDTO.getUrl());
        ResponseResult<EncryptResultDTO> resp = new ResponseResult<>();
        try {
            String result = tokenManager.encryptSend(encryptSendQueryDTO);
            Map<String, Object> dto = new HashMap() {{
                put("result", result);
            }};
            resp.setResult(dto);
        } catch (Exception e) {
            e.printStackTrace();
            resp.putException(e);
        }
        return resp;
    }

}
