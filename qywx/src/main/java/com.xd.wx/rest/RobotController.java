package com.xd.wx.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xd.wx.util.WxBotUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 企业微信群机器人推送消息案例代码
 * author: liuxiaodong
 * date: 2022-05-09
 */
@RestController
@RequestMapping("api")
public class RobotController {

    @Autowired
    private WxBotUtils weChatBotUtils;

    @PostMapping("testSendText")
    public void testSendText() throws Exception {
        weChatBotUtils.sendTextMsg("我是风你是沙");
        // 发送MarkDown消息
        String markdownMsg =
                "测试：您的会议室已经预定，稍后会同步到`邮箱` \n" +
                        "    >**事项详情** \n" +
                        "    >事　项：<font color=\"info\">开会</font> \n" +
                        "    >组织者：@admin \n" +
                        "    >参与者：@admin \n" +
                        "    >\n" +
                        "    >会议室：<font color=\"info\">广州TIT 1楼 301</font>\n" +
                        "    >日　期：<font color=\"warning\">2022年4月22日</font>\n" +
                        "    >时　间：<font color=\"comment\">上午9:00-11:00</font>\n" +
                        "    >\n" +
                        "    >请准时参加会议。\n";

        weChatBotUtils.sendMarKDownMsg(markdownMsg);
    }

    @PostMapping("testSendImg")
    public void testSendImg() throws Exception {
        weChatBotUtils.sendImgMsg("D:\\baby\\微信图片_20220226130437.jpg");
    }

    @PostMapping("testSendNews")
    public void testSendNews() throws Exception {
        JSONArray array = new JSONArray();
        JSONObject article = new JSONObject();
        article.put("title","中秋节礼品领取");
        article.put("description","今年中秋节公司有豪礼相送");
        article.put("url","www.qq.com");
        article.put("picurl","http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png");
        array.add(article);

        weChatBotUtils.sendNewsMsg(array);
    }

    @PostMapping("testSendFile")
    public void testSendFile() throws Exception {
        // 发送text类型消息
        weChatBotUtils.sendFileMsg("C:\\Users\\hp\\Downloads\\2022-04-22 11_19_43-school_jw_worker.xlsx");
        weChatBotUtils.sendFileMsg("D:\\baby\\微信图片_20220226130500.jpg");
    }

    @PostMapping("testSendCard")
    public void testSendCard() throws Exception {
        JSONObject source = new JSONObject();
        source.put("icon_url","https://wework.qpic.cn/wwpic/252813_jOfDHtcISzuodLa_1629280209/0");
        source.put("desc","企业微信");
        source.put("desc_color",0);

        JSONObject mainTitle = new JSONObject();
        mainTitle.put("title","欢迎使用企业微信");
        mainTitle.put("desc","您的好友正在邀请您加入企业微信");

        JSONObject emphasisContent = new JSONObject();
        emphasisContent.put("title",100);
        emphasisContent.put("desc","数据含义");

        JSONObject quoteArea = new JSONObject();
        quoteArea.put("type","1");
        quoteArea.put("url","https://work.weixin.qq.com/?from=openApi");
        quoteArea.put("appid","APPID");
        quoteArea.put("pagepath","PAGEPATH");
        quoteArea.put("title","引用文本标题");
        quoteArea.put("quote_text","Jack：企业微信真的很好用~\\nBalian：超级好的一款软件！");

        String subTitle = "下载企业微信还能抢红包";

        JSONArray horizontalContentList = new JSONArray();
        JSONObject content1 = new JSONObject();
        content1.put("keyname","邀请人");
        content1.put("value","张三");
        JSONObject content2 = new JSONObject();
        content2.put("keyname","企微官网");
        content2.put("value","点击访问");
        content2.put("type",1);
        content2.put("url","https://work.weixin.qq.com/?from=openApi");
        JSONObject content3 = new JSONObject();
        content3.put("keyname","企微下载");
        content3.put("value","企业微信.apk");
        content3.put("type",2);
        content3.put("media_id","3x7CJqcMQvnI1bGdeW0jYZJIqf_d08dOqFQqR2vyNr_aIo7yFETaqhjHol9veqI83");
        horizontalContentList.add(content1);
        horizontalContentList.add(content2);
        horizontalContentList.add(content3);

        JSONArray jumpList = new JSONArray();
        JSONObject jump1 = new JSONObject();
        jump1.put("type",1);
        jump1.put("url","https://work.weixin.qq.com/?from=openApi");
        jump1.put("title","企业微信官网");
        jumpList.add(jump1);

        JSONObject cardAction = new JSONObject();
        cardAction.put("type",1);
        cardAction.put("url","https://work.weixin.qq.com/?from=openApi");

        weChatBotUtils.sendTemplateCardMsg(source,mainTitle,emphasisContent,quoteArea,subTitle,horizontalContentList,jumpList,cardAction);
    }

}
