package com.example.lpukipathshala.Cart;

import com.example.lpukipathshala.Notification.MyResponse;
import com.example.lpukipathshala.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers
            (
                    {
                            "Content-Type:application/json",
                            "Authorization:key=AAAACRbOeeY:APA91bHo1SiBqLMTj8e3wQBuM-YnEnOmx2HFZTgojEGCWzBtLdKp6trZZFfwG88TgmOBPW6pdz2b7ECHyUesL26pn7SlrJYQ1kFQsPFSERgB-xfKZ82eVBs57F-E7_xgTp7Nv03z5Buf"
                    }
            )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
