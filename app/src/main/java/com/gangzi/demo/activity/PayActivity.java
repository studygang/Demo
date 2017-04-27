package com.gangzi.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.gangzi.demo.R;
import com.gangzi.demo.pay.AuthResult;
import com.gangzi.demo.pay.OrderInfoUtil2_0;
import com.gangzi.demo.pay.PayResult;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayActivity extends Activity implements View.OnClickListener{

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2017042606991110";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088911876712776";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "chenlei@atguigu.com";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSwA182ED4r9LAGEkpB1+Z6MMQN7pVTcD/lssRD4rTR3er9eM/JsaqtZI2xUGMcL6XU8iXSnKn9wAb/vEeIC008meg+fMXt9u4a8TCPOSz2puFF5MInUcSiHoSVW/fpNdjRZ9XBAFB/TEgvrmiPe2JGMiCwQzpToUl4PbbIhkFrV+kMcZMQwDJ6otM4Voj5A8zSMfq3dS3y17APbdOfZEe+eN9dWC/M0emjDzvKnumfeoiohxkE8iTekfR5i+3rwIkXaeYomIEkOE0hl1Jnp0jAz4VeaRvdi2jJHCRLd2z2S0Dl0LM9RyrQlNyPB5fEh9nN8IyE1wBemWg1KhNU0qXAgMBAAECggEAa6bPPiI1pzjz1U77fEmYbAM/9Qmsex9K0TQBS0anhE4ND+yVA6KiYmBIHj2d1DRw2/nuUG2gpm6feeoPXxYjk4+vTXuORHvvht4nvIWHEBjxtTSR++PEW2gahJCAPQNjrcNJHEAn3AysmWTQMKBVPBkeHi0YF54uNpdM/3uH940XGHBKQXarwfNmvM9EpPPuJ6pN0K48STwHR7NZw7dMXzBvb/13HjPhMQIOEzsH8gMryp5l4J1uC+om3AdPPE7UeQhL3mFUrSseSVARaWuaRCbZ2bPzcAW9iItYuP3IFuzU2twZ3yYQVAGqW2YMsyv/2bz2Tga6ZXIcVQsk+mNeYQKBgQDCUwrPsPogquBw8WWXKcNV9Q/PVs7padf9YjHsi9kxHllC8NWK5Cwn9hHHV03hu8pG3+t+cdIP5MMM13mgYa9ikKprm7ZHAF5ZZm/hHPluFPGRFOdDK2OwA8s0S0uUL+ooBoz9mY6cj+Kkn7Hvk/iybDz8dtrLjU3TBtQ20bRz1QKBgQDBU5VooqY/F8fg+Z9TAPu1qrhmEFHmule9ya4mscRjSmet5uMlmmOLx7hh13Ta8F9CqEeO5Isggst+KrhTl7JjBdyoQszHYX2ixJ5G3/YU1iFc6apFo/Twm1xhi6XrFybTXKuW8+KjJ7IUGK1ehO9Ha4BHMhwghf/Kmk1+mGT2uwKBgQCrqLnjMF40n142xzfL6euRMjMuIGwVixdu0OBzKjLJjhqh5Kdu0xMild+srTWyU7ZjF4krIJbEzXt0C6B4ifJpaovxFRNW8z9lT/VC8dpxYPToCYQlNN4R7bgC0QTJue3qP6cqmwx8xyoZ7Kw9jARkcPMKvl+W8pMt+Flct7DlpQKBgHnSQpbRzzJ4uMICaiwytWovbs0ZwZJDn+B/AtdtM1Rh4V1K3Z2nYM0HCB/XVdJUn6JwSc84IpdU9AfHwakjMQyG+BmrJkEJJNL5VMdSf0139UQIFfyWKVWogJkTF3VBfHy5q4KOoiSvcd6KAk1KNUFq80VpxSGVbq7rdqOsQgrbAoGBAJ17jH6230d2gonV66vLNoUklzyyN3s+S//nHPHbQjuge8lmI+iBQxToTN/hKyoZA8DmcNa5lgWQIbLkqw0XIk1Mme9AXORx96DjzyItFZ44q2HX1CgLLzZ9Ji4x+VCv5pJ2jOGMBfj1unzb/x02PbJA2VoYHkzn1oojtREKzF2/";
    public static final String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSwA182ED4r9LAGEkpB1+Z6MMQN7pVTcD/lssRD4rTR3er9eM/JsaqtZI2xUGMcL6XU8iXSnKn9wAb/vEeIC008meg+fMXt9u4a8TCPOSz2puFF5MInUcSiHoSVW/fpNdjRZ9XBAFB/TEgvrmiPe2JGMiCwQzpToUl4PbbIhkFrV+kMcZMQwDJ6otM4Voj5A8zSMfq3dS3y17APbdOfZEe+eN9dWC/M0emjDzvKnumfeoiohxkE8iTekfR5i+3rwIkXaeYomIEkOE0hl1Jnp0jAz4VeaRvdi2jJHCRLd2z2S0Dl0LM9RyrQlNyPB5fEh9nN8IyE1wBemWg1KhNU0qXAgMBAAECggEAa6bPPiI1pzjz1U77fEmYbAM/9Qmsex9K0TQBS0anhE4ND+yVA6KiYmBIHj2d1DRw2/nuUG2gpm6feeoPXxYjk4+vTXuORHvvht4nvIWHEBjxtTSR++PEW2gahJCAPQNjrcNJHEAn3AysmWTQMKBVPBkeHi0YF54uNpdM/3uH940XGHBKQXarwfNmvM9EpPPuJ6pN0K48STwHR7NZw7dMXzBvb/13HjPhMQIOEzsH8gMryp5l4J1uC+om3AdPPE7UeQhL3mFUrSseSVARaWuaRCbZ2bPzcAW9iItYuP3IFuzU2twZ3yYQVAGqW2YMsyv/2bz2Tga6ZXIcVQsk+mNeYQKBgQDCUwrPsPogquBw8WWXKcNV9Q/PVs7padf9YjHsi9kxHllC8NWK5Cwn9hHHV03hu8pG3+t+cdIP5MMM13mgYa9ikKprm7ZHAF5ZZm/hHPluFPGRFOdDK2OwA8s0S0uUL+ooBoz9mY6cj+Kkn7Hvk/iybDz8dtrLjU3TBtQ20bRz1QKBgQDBU5VooqY/F8fg+Z9TAPu1qrhmEFHmule9ya4mscRjSmet5uMlmmOLx7hh13Ta8F9CqEeO5Isggst+KrhTl7JjBdyoQszHYX2ixJ5G3/YU1iFc6apFo/Twm1xhi6XrFybTXKuW8+KjJ7IUGK1ehO9Ha4BHMhwghf/Kmk1+mGT2uwKBgQCrqLnjMF40n142xzfL6euRMjMuIGwVixdu0OBzKjLJjhqh5Kdu0xMild+srTWyU7ZjF4krIJbEzXt0C6B4ifJpaovxFRNW8z9lT/VC8dpxYPToCYQlNN4R7bgC0QTJue3qP6cqmwx8xyoZ7Kw9jARkcPMKvl+W8pMt+Flct7DlpQKBgHnSQpbRzzJ4uMICaiwytWovbs0ZwZJDn+B/AtdtM1Rh4V1K3Z2nYM0HCB/XVdJUn6JwSc84IpdU9AfHwakjMQyG+BmrJkEJJNL5VMdSf0139UQIFfyWKVWogJkTF3VBfHy5q4KOoiSvcd6KAk1KNUFq80VpxSGVbq7rdqOsQgrbAoGBAJ17jH6230d2gonV66vLNoUklzyyN3s+S//nHPHbQjuge8lmI+iBQxToTN/hKyoZA8DmcNa5lgWQIbLkqw0XIk1Mme9AXORx96DjzyItFZ44q2HX1CgLLzZ9Ji4x+VCv5pJ2jOGMBfj1unzb/x02PbJA2VoYHkzn1oojtREKzF2/";
    //支付宝公钥--公钥要放在服务器上
    public static final String RSA_PUBLIC="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAksANfNhA+K/SwBhJKQdfmejDEDe6VU3A/5bLEQ+K00d3q/XjPybGqrWSNsVBjHC+l1PIl0pyp/cAG/7xHiAtNPJnoPnzF7fbuGvEwjzks9qbhReTCJ1HEoh6ElVv36TXY0WfVwQBQf0xIL65oj3tiRjIgsEM6U6FJeD22yIZBa1fpDHGTEMAyeqLTOFaI+QPM0jH6t3Ut8tewD23Tn2RHvnjfXVgvzNHpow87yp7pn3qIqIcZBPIk3pH0eYvt68CJF2nmKJiBJDhNIZdSZ6dIwM+FXmkb3YtoyRwkS3ds9ktA5dCzPUcq0JTcjweXxIfZzfCMhNcAXploNSoTVNKlwIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @BindView(R.id.bt_pay)
    Button bt_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        bt_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_pay:
                pay();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务
     */
    private void pay() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void authV2(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
       // Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        //intent.putExtras(extras);
       // startActivity(intent);
    }
}
