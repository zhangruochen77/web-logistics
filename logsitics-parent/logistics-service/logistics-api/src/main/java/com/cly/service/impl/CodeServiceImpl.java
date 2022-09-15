package com.cly.service.impl;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.cly.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 服务所在地址
     */
    @Value("${cloopen.serverIp}")
    private String serverIp;

    /**
     * 服务端口
     */
    @Value("${cloopen.serverPort}")
    private String serverPort;

    /**
     * 主账户id
     */
    @Value("${cloopen.accountSId}")
    private String accountSId;

    /**
     * 主账户密钥
     */
    @Value("${cloopen.accountToken}")
    private String accountToken;

    /**
     * 容器 id
     */
    @Value("${cloopen.appId}")
    private String appId;

    private static final Long reSetTime = 3600000L;


    /**
     * 生成验证码 存入 redis 缓存 调用 api 实现
     *
     * @return
     */
    @Override
    public String createCode(String phone) {

        Object codeObj = redisTemplate.opsForValue().get(phone);

        if (!ObjectUtils.isEmpty(codeObj)) {
            // TODO: 2022/9/14 操作过于频繁 如何解决
//            Long expire = redisTemplate.opsForValue().getOperations().getExpire(phone);
//            if (reSetTime > expire) {
//
//            }
            return "操作太过于频繁";
        }

        String code = createRandomCode();
        redisTemplate.opsForValue().set(phone, code, 3L, TimeUnit.HOURS);

        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(serverIp, serverPort);
        sdk.setAccount(accountSId, accountToken);
        sdk.setAppId(appId);
        sdk.setBodyType(BodyType.Type_JSON);

        String templateId = "1";
        String[] info = {code};  // 需要短信发送的信息
        HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, info);

        if ("000000".equals(result.get("statusCode"))) {
            // 正常返回输出data包体信息（map）
            HashMap<String, Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                Object object = data.get(key);
                System.out.println(key + " = " + object);
            }
        } else {
            System.out.println("错误码 = " + result.get("statusCode") + " 错误信息= " + result.get("statusMsg"));
        }

        return "验证码已经发送,请注意查收！";
    }

    /**
     * 创建随机验证码
     *
     * @return 验证码
     */
    // FIXME: 2022/9/14 每次都创建一个 Random 对象 很影响性能 但是 Random 高并发又存在问题 如何优化?
    public String createRandomCode() {
        // TODO: 2022/9/14  new Random(seed) -> seed 是什么意思？
        Random random = new Random();
        StringBuilder builder = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }

        return builder.toString();
    }
}
