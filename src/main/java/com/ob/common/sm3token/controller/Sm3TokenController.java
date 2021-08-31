package com.ob.common.sm3token.controller;

import com.ob.common.sm3token.base.ResponseResult;
import com.ob.common.sm3token.domain.EncryptQueryDTO;
import com.ob.common.sm3token.domain.EncryptResultDTO;
import com.ob.common.sm3token.domain.EncryptSendQueryDTO;
import com.ob.common.sm3token.service.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.System.exit;

@CrossOrigin(origins = "*")
@Controller
@RequestMapping("sm3Token")
@EnableScheduling
public class Sm3TokenController {

    private static final Logger LOG = Logger.getLogger(Sm3TokenController.class.getName());

    @Autowired
    private TokenManager tokenManager;

    @PostMapping()
    @ResponseBody
    public ResponseResult<String> getSm3Token() {
        ResponseResult<String> res = new ResponseResult<>();
        try {
            String token = tokenManager.getToken();
            res.setData(token);
        } catch (Exception e) {
            e.printStackTrace();
            res.putException(e);
        }
        return res;
    }

    @PostMapping("encrypt")
    @ResponseBody
    public ResponseResult<EncryptResultDTO> encrypt(@RequestBody EncryptQueryDTO encryptQueryDTO) {
        ResponseResult<EncryptResultDTO> res = new ResponseResult<>();
        try {
            EncryptResultDTO encryptResultDTO = tokenManager.encrypt(encryptQueryDTO.getData());
            Map<String, Object> dto = new HashMap() {{
                put("encryptResultDTO", encryptResultDTO);
            }};
            res.setResult(dto);
        } catch (Exception e) {
            e.printStackTrace();
            res.putException(e);
        }
        return res;
    }

    @PostMapping("encryptSend")
    @ResponseBody
    public ResponseResult<EncryptResultDTO> encryptSend(@RequestBody EncryptSendQueryDTO encryptSendQueryDTO) {
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

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void init() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = "2021-9-30"; //假设 设定日期是  2018-11-11
            String webUrl = "https://www.baidu.com";
            URLConnection conn = new URL(webUrl).openConnection();
            conn.connect();
            long dateL = conn.getDate();
            Date today = new Date(dateL);
            Date dateD = sdf.parse(date);
            boolean flag = dateD.getTime() >= today.getTime();
            if (!flag) {
                LOG.info("到期");
                exit(0);
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
