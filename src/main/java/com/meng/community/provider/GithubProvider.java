package com.meng.community.provider;

import com.alibaba.fastjson.JSON;
import com.meng.community.dto.GithubUser;

import com.meng.community.dto.AccessTokenDTO;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Meng
 * @date 2019/7/24
 */
@Component
public class GithubProvider {
    /**
     * 使用OKHttp获取token
     * @param accessTokenDTO 传过去的参数
     * @return 返回的token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType=MediaType.get("application/json; charaset=utf-8");
        OkHttpClient client=new OkHttpClient();

        RequestBody body=RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request=new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try(Response response=client.newCall(request).execute()){
            String string=response.body().string();
            String token=string.split("&")[0].split("=")[1];
            System.out.println(token);
            return token;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据token得到登录用户的信息
     * @param accessToken 得到的token
     * @return 返回用户信息
     */
    public  GithubUser getUser(String accessToken){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url("http://api.github.com/user?access_token="+accessToken).build();
        try{
            Response response =client.newCall(request).execute();
            String string =response.body().string();
            GithubUser githubser= JSON.parseObject(string,GithubUser.class);
            return githubser;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
