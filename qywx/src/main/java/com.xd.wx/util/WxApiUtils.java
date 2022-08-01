package com.xd.wx.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.*;

import static com.xd.wx.util.WxApiConst.*;

/**
 * @author: yangpengwei
 * @time: 2020/12/11 4:29 下午
 * @description 微信 api 调用工具类
 */
@Slf4j
@Component
public class WxApiUtils {

    private static final String CONST_ACCESS_TOKEN_INVALID = "-1";

    private static Map<String, String> map = new HashMap<>(0);

    /**
     * 获取通讯录相关 accessToken, 直接获取新的
     *
     * @param agentId 应用 id
     * @return accessToken
     */
    public static String getNewAccessTokenAgent(String agentId,String wxCorpId, String wxSecret) {
        String accessToken = getAccessToken(wxCorpId, wxSecret);
        map.put("agent-" + agentId, accessToken);
        return accessToken;
    }

    /**
     * 获取 accessToken
     *
     * @param wxCorpId 企业微信 id
     * @param secret   业务相关 secret
     * @return accessToken
     */
    public static String getAccessToken(String wxCorpId, String secret) {
        String accessTokenUrl = API_ACCESS_TOKEN + "?corpid=" + wxCorpId + "&corpsecret=" + secret;
        String respJson = HttpClientUtil.doGet(accessTokenUrl);

        log.error("getAccessToken: " + respJson);

        JSONObject jsonObject = JSON.parseObject(respJson);
        int errCode = jsonObject.getIntValue("errcode");
        if (errCode == 0) {
            return jsonObject.getString("access_token");
        }
        return null;
    }

    /**
     * 获取企业微信自建应用相关 accessToken, 优先从缓存中获取
     *
     * @param agentId 应用 id
     * @return accessToken
     */
    public static String getAccessTokenAgent(String corpId,String secret, String agentId) {
        String keyAccess = "agent-" + agentId;
        String accessToken = map.get(keyAccess);
        if (accessToken == null) {
            accessToken = getNewAccessTokenAgent(agentId,corpId,secret);
        }
        return accessToken;
    }

    /**
     * 获取员工详情
     *
     * @param agentId 应用 id
     * @param code    通过成员授权获取到的code
     * @return 员工列表 json
     */
    public static String requestWxUserIdApi(String corpId,String secret,  String agentId, String code) {
        String url = API_USER_INFO
                + "?access_token=" + getAccessTokenAgent(corpId,secret, agentId)
                + "&code=" + code;
        String key = "UserId";
        String respJson = doGetResult(url, key);
        if (CONST_ACCESS_TOKEN_INVALID.equals(respJson)) {
            url = API_USER_LIST
                    + "?access_token=" + getNewAccessTokenAgent( agentId,corpId,secret)
                    + "&code=" + code;
            respJson = doGetResult(url, key);
        }
        return respJson;
    }

    private static String doGetResult(String url, String key) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        log.debug("url: " + url);
        String respJson = HttpClientUtil.doGet(url);
        log.debug("respJson: " + respJson);
        log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
        return getResultData(respJson, key);
    }

    private static String doPostResult(String url, String key, String requestBody) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
        log.debug("url: " + url);
        log.debug("requestBody: " + requestBody);
        String respJson = HttpClientUtil.doPost(url, requestBody);
        log.debug("respJson: " + respJson);
        log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ");
        return getResultData(respJson, key);
    }

    private static String doPostResult(String url, String key, File requestBody) {
        String respJson = HttpClientUtil.doPost(url, requestBody);
        return getResultData(respJson, key);
    }

    private static String getResultData(String result, String key) {
        log.debug(key + " : " + result);

        JSONObject jsonObject = JSON.parseObject(result);
        int errCode = jsonObject.getIntValue("errcode");
        if (errCode == 42001) {
            return CONST_ACCESS_TOKEN_INVALID;
        }
        if (errCode == 0) {
            if (key == null) {
                return "";
            } else if (key.isEmpty()) {
                return result;
            } else {
                return jsonObject.getString(key);
            }
        }
        return null;
    }

}
