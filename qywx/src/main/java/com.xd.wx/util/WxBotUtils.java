package com.xd.wx.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信群机器人API操作工具类
 */
@Component
public class WxBotUtils {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    // 配置的群机器人Webhook地址
    //private String botUrl;
    @Value("${wechatbot.botUrl}")
    private String botUrl;

    /**
     * 发送文字消息
     *
     * @param msg 需要发送的消息
     * @return
     * @throws Exception
     */
    public String sendTextMsg(String msg) throws Exception {
        JSONObject text = new JSONObject();
        text.put("content", msg);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "text");
        reqBody.put("text", text);
        reqBody.put("safe", 0);

        return callWeChatBot(reqBody.toString());
    }

    public String sendNewsMsg(JSONArray articles) throws Exception {
        JSONObject news = new JSONObject();
        news.put("articles", articles);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "news");
        reqBody.put("news", news);

        return callWeChatBot(reqBody.toString());
    }

    public String sendTemplateCardMsg(JSONObject source,JSONObject mainTitle,JSONObject emphasisContent,JSONObject quoteArea,String subTitle,JSONArray horizontalContentList,JSONArray jumpList,JSONObject cardAction) throws Exception {
        JSONObject card = new JSONObject();
        card.put("card_type", "text_notice");
        card.put("source", source);
        card.put("main_title", mainTitle);
        card.put("emphasis_content", emphasisContent);
        card.put("quote_area", quoteArea);
        card.put("sub_title_text", subTitle);
        card.put("horizontal_content_list", horizontalContentList);
        card.put("jump_list", jumpList);
        card.put("card_action", cardAction);

        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "template_card");
        reqBody.put("template_card", card);

        return callWeChatBot(reqBody.toString());
    }

    /**
     * 发送图片消息，需要对图片进行base64编码并计算图片的md5值
     * 注：图片（base64编码前）最大不能超过2M，支持JPG,PNG格式
     * @param path 需要发送的图片路径
     * @return
     * @throws Exception
     */
    public String sendImgMsg(String path) throws Exception {

        String base64 = "";
        String md5 = "";

        // 获取Base64编码
        try {
            FileInputStream inputStream = new FileInputStream(path);
            byte[] bs = new byte[inputStream.available()];
            inputStream.read(bs);
            base64 = Base64.getEncoder().encodeToString(bs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取md5值
        try {
            FileInputStream inputStream = new FileInputStream(path);
            byte[] buf = new byte[inputStream.available()];
            inputStream.read(buf);
            md5 = DigestUtils.md5Hex(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject image = new JSONObject();
        image.put("base64", base64);
        image.put("md5", md5);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "image");
        reqBody.put("image", image);
        reqBody.put("safe", 0);

        return callWeChatBot(reqBody.toString());
    }

    /**
     * 发送MarKDown消息
     *
     * @param msg 需要发送的消息
     * @return
     * @throws Exception
     */
    public String sendMarKDownMsg(String msg) throws Exception {
        JSONObject markdown = new JSONObject();
        markdown.put("content", msg);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "markdown");
        reqBody.put("markdown", markdown);
        reqBody.put("safe", 0);

        return callWeChatBot(reqBody.toString());
    }

    /**
     * 发送文件消息，需要先将文件上传到企业微信临时素材，再根据获取的media_id调用群机器人
     *
     * @param path 需要发送的文件路径
     * @return
     * @throws Exception
     */
    public String sendFileMsg(String path) throws Exception {
        File file = new File(path);

        // 上传到临时素材
        String key = botUrl.substring(botUrl.indexOf("key="));
        System.out.println(key);
        String mediaUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/upload_media?type=file&"+key;
        log.info("将文件" + path+ "上传到临时素材：" + mediaUrl);
        //String respMsg = okHttp(requestBody, mediaUrl);
        String respMsg = HttpClientUtil.doPostFile(mediaUrl,file);
        // 获取临时素材id
        JSONObject result = JSONObject.parseObject(respMsg);
        String media_id = result.getString("media_id");
        System.out.println("media_id:"+media_id);
        JSONObject fileJson = new JSONObject();
        fileJson.put("media_id", media_id);
        JSONObject reqBody = new JSONObject();
        reqBody.put("msgtype", "file");
        reqBody.put("file", fileJson);
        reqBody.put("safe", 0);

        // 调用群机器人发送消息
        return callWeChatBot(reqBody.toString());
    }

    /**
     * 调用群机器人
     *
     * @param reqBody 接口请求参数
     * @throws Exception 可能有IO异常
     */
    public String callWeChatBot(String reqBody) throws Exception {
        log.info("请求参数：" + reqBody);
        // 构造RequestBody对象，用来携带要提交的数据；需要指定MediaType，用于描述请求/响应 body 的内容类型
        MediaType contentType = MediaType.parseMediaType("application/json; charset=utf-8");

        // 调用群机器人
        String respMsg = HttpClientUtil.doPost(botUrl,reqBody);

        if ("0".equals(respMsg.substring(11, 12))) {
            log.info("向群发送消息成功！");
        } else {
            log.info("请求失败！");
            // 发送错误信息到群
            sendTextMsg("群机器人推送消息失败，错误信息：\n" + respMsg);
        }
        return respMsg;
    }

}

