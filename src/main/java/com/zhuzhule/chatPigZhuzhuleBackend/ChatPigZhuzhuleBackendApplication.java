package com.zhuzhule.chatPigZhuzhuleBackend;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.Result;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.UserInput;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxResource;
import com.zhuzhule.chatPigZhuzhuleBackend.domain.WxUserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import okhttp3.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@RestController
@Configuration
public class ChatPigZhuzhuleBackendApplication {
    private static final Logger logger = LoggerFactory.getLogger(ChatPigZhuzhuleBackendApplication.class);

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().
            newBuilder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .readTimeout(55, TimeUnit.SECONDS)
            .writeTimeout(55, TimeUnit.MILLISECONDS).build();

    @Value("${config.secretKey}")
    public  String BAIDU_SECRET_KEY;

    @Value("${config.apiKey}")
    public  String BAIDU_API_KEY;

    @Value("${wxlogin.app-id}")
    public  String WX_APP_ID;

    @Value("${wxlogin.app-secret}")
    public  String WX_APP_SECRET;




    public static void main(String[] args) {
        SpringApplication.run(ChatPigZhuzhuleBackendApplication.class, args);
    }





    @RequestMapping(value = "/api")
    public Result getTestData(@org.springframework.web.bind.annotation.RequestBody UserInput userMsg,HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        System.out.println("-------------------------------------------start-------------------------------------------");
        logger.info("用户{}>>正在提问",((WxResource) session.getAttribute("userStorage")).getNickname());
        System.out.println("user question:" + userMsg.getUserMsg());
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                "    \"temperature\": 0.95,\n" +
                "    \"top_p\": 0.8,\n" +
                "    \"penalty_score\": 1,\n" +
                "    \"enable_system_memory\": false,\n" +
                "    \"disable_search\": false,\n" +
                "    \"enable_citation\": false,\n" +

//                "    \"max_output_tokens\":100,\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \""+userMsg.getUserMsg()+"\"\n" +
//                "            \"content\": \""+userMsg.getUserMsg()+",回答前面加\'猪猪乐说:\'\"\n" +
                "        } \n" +
                "\n" +
                "        \n" +
                "    ]\n" +
                "}");

        String accecssToken = this.getAccessToken();
        Request requestToApi = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + accecssToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response responseFromApi = HTTP_CLIENT.newCall(requestToApi).execute();
        String result_data = JSON.parseObject(responseFromApi.body().string()).toJSONString();
        Result res = JSON.parseObject(result_data, Result.class);
        System.out.println("AI answer:");
        System.out.println(res.getResult());
        return  res;
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
     String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + BAIDU_API_KEY
                + "&client_secret=" + BAIDU_SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}
