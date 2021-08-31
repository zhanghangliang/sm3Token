package com.ob.common.sm3token.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ob.common.sm3token.base.DataException;
import com.ob.common.sm3token.config.AuthTokenFactory;
import com.ob.common.sm3token.domain.EncryptResultDTO;
import com.ob.common.sm3token.domain.EncryptSendQueryDTO;
import com.ob.common.sm3token.domain.SimpleInterfaceTokenQueryDTO;
import com.ob.common.sm3token.utils.SM3Util;
import com.ob.common.sm3token.utils.SM4Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenManager {

    @Autowired
    private AuthTokenFactory authTokenFactory;

    // 测试
//    public static String BASE_URL = "http://124.161.168.237:8098";
    // 生产
    public static String BASE_URL = "http://124.161.168.237:8099";

    public String getToken() {
        HttpRequest post = HttpUtil.createPost(BASE_URL + "/QiantoonService/AppInterFace/GetToken");
        Map<String, String> body = new HashMap() {{
            put("appid", "TFSMYPat2020");
            put("appsecret", "225B02F0C2BD8143");
        }};
        post.body(JSONUtil.toJsonStr(body));
        HttpResponse execute = post.execute();
        if ("0".equals(JSONUtil.getByPath(JSONUtil.parse(execute.body()), "$.head.resultcode"))) {
            String data = (String) JSONUtil.getByPath(JSONUtil.parse(execute.body()), "$.data");
            String timestamp = (String) JSONUtil.getByPath(JSONUtil.parse(execute.body()), "$.head.Timestamp");

            String secretKey = SM3Util.GetSm3Str("adminAppID" + timestamp);
            String sm4DecryptECB = SM4Util.getSm4DecryptECB(data, secretKey);
            return (String) JSONUtil.getByPath(JSONUtil.parse(sm4DecryptECB), "$.Token");
        } else {
            throw new DataException("请求Token失败", execute.body());
        }
    }

    public EncryptResultDTO encrypt(HashMap<String, String> encryptDTO) {
        String timestamp = (System.currentTimeMillis() / 1000) + "";
        String secretKey = SM3Util.GetSm3Str("adminAppID" + timestamp);
        String data = SM4Util.getSm4EncryptECB(JSONUtil.toJsonStr(encryptDTO), secretKey);
        EncryptResultDTO result = new EncryptResultDTO();
        result.setTimestamp(timestamp);
        result.setData(data);
        return result;
    }

    public String encryptSend(EncryptSendQueryDTO encryptSendQueryDTO) {
        // 先加密待发送的内容
        EncryptResultDTO encryptData = encrypt(encryptSendQueryDTO.getData());
        HttpRequest post = HttpUtil.createPost(encryptSendQueryDTO.getUrl());
        // 组装查询结构
        SimpleInterfaceTokenQueryDTO tokenQueryDTO = new SimpleInterfaceTokenQueryDTO(authTokenFactory.getAuthToken(), encryptData.getTimestamp(), encryptData.getData());
        post.body(JSONUtil.toJsonStr(tokenQueryDTO));

        HttpResponse execute = post.execute();
        String body = execute.body();
        if (!JSONUtil.isJsonObj(body)) {
            throw new DataException("请求失败", body);
        }
        if ("0".equals(JSONUtil.getByPath(JSONUtil.parse(body), "$.head.resultcode", "1"))) {
            String data = (String) JSONUtil.getByPath(JSONUtil.parse(body), "$.data");
            String timestamp = (String) JSONUtil.getByPath(JSONUtil.parse(body), "$.head.Timestamp");

            String secretKey = SM3Util.GetSm3Str("adminAppID" + timestamp);
            String sm4DecryptECB = SM4Util.getSm4DecryptECB(data, secretKey);
            if(data==null) {
                throw new DataException("返回内容data为空", body);
            }
            if (JSONUtil.isJsonArray(sm4DecryptECB)) {
                return sm4DecryptECB;
            } else {
                JSONObject jsonObject = JSONUtil.parseObj(sm4DecryptECB);
                jsonObject.putAll(new HashMap<String, String>() {{
                    put("TimeStamp", timestamp);
                }});
                return jsonObject.toString();
            }
        } else {
            throw new DataException("请求失败", body);
        }
    }
}
