package com.example.assistivetouch.ads;


import com.example.assistivetouch.BuildConfig;

public class AddIds {
    public static String getBannerID(){
        String id = "";
        if(BuildConfig.DEBUG){
            id = "ca-app-pub-3940256099942544/6300978111"; // test id
        }else{
            id = "ca-app-pub-6995994501884409/8116156959"; // orignal id
        }
        return  id;
    }
    public static String getInterstialId(){
        String id = "";
        if(BuildConfig.DEBUG){
            id = "ca-app-pub-3940256099942544/1033173712"; // test id
        }else{
            id = "ca-app-pub-6995994501884409/6803075287"; // orignal id
        }
        return  id;
    }
    public static String getNativeId(){
        String id = "";
        if(BuildConfig.DEBUG){
            id = "ca-app-pub-3940256099942544/2247696110"; // test id
        }else{
            id = "ca-app-pub-6995994501884409/5454334595"; // orignal id
        }
        return  id;
    }
    public static String getAppOpenId(){
        String id = "";
        if(BuildConfig.DEBUG){
            id = "ca-app-pub-3940256099942544/3419835294"; // test id
        }else{
            id = "ca-app-pub-6995994501884409/9216651485"; // orignal id
        }
        return  id;
    }
}
