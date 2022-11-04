package com.cly.config;

import com.cly.web.RedisKeyUtils;
import com.cly.web.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 做登录拦截验证的过滤器
 */
@Configuration
public class LoginFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 做登录放行判断 针对某些接口不登录可放行
     * 其余接口做登录检查 更新登录 token 时间
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String path = exchange.getRequest().getPath().value();

        // 查看路径是否存在 路径不存在 返回 404
        if (StringUtils.isEmpty(path)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NOT_FOUND);
            return response.setComplete();
        }

        // 判断是否属于是调用 api
        if (path.matches("^/log/api/.*$")) {
            return chain.filter(exchange);
        }

        // 判断是否属于是登录或者退出接口 或者是注册接口
        if (path.matches("^/\\b(log|front)\\b/admin/\\b(admin|user)\\b/\\b(log.*|registry)\\b$")) {
            return chain.filter(exchange);
        }

        // 判断是否是前台用户查看商品信息
        String plat = headers.get("plat").get(0);
        if (!"END".equals(plat) && path.matches("^/front/warehouse/goods/\\b(pageFindGoods|getGoodsDetailsById)\\b/.*$")) {
            return chain.filter(exchange);
        }

        // 判断是否携带 token, 没有 token 说明没有登录信息
        List<String> list = headers.get("Authorization");
        if (list != null && list.size() != 0) {

            // 获取 token 用户 id 信息
            String token = list.get(0);
            Long id = TokenUtils.getId(token);
            if (!ObjectUtils.isEmpty(id)) {

                // 平台查看 前台后台登录所使用不同
                String adminKey = null;
                if ("END".equals(plat)) {
                    adminKey = RedisKeyUtils.createAdminKey(id);
                    // 通过用户 id 在 redis 缓存当中查找用户数据
                    Object obj = redisTemplate.opsForValue().get(adminKey);
                    if (!ObjectUtils.isEmpty(obj)) {

                        // 用户信息存在 通过 刷新 token
                        redisTemplate.expire(adminKey, RedisKeyUtils.ADMIN_EXP_TIME, RedisKeyUtils.ADMIN_EXP_TIME_UTIL);
                        return chain.filter(exchange);
                    }
                } else {
                    adminKey = RedisKeyUtils.createUserKey(id);
                    Object obj = redisTemplate.opsForValue().get(adminKey);
                    if (!ObjectUtils.isEmpty(obj)) {
                        redisTemplate.expire(adminKey, RedisKeyUtils.USER_EXP_TIME, RedisKeyUtils.USER_EXP_TIME_UTIL);
                        return chain.filter(exchange);
                    }
                }
            }
        }

        // 没有登录 403
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 排序操作
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }


//    /**
//     * 做登录放行判断 针对某些接口不登录可放行
//     * 其余接口做登录检查 更新登录 token 时间
//     *
//     * @param exchange
//     * @param chain
//     * @return
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        String path = exchange.getRequest().getPath().value();
//
//        // 查看路径是否存在 路径不存在 返回 404
//        if (StringUtils.isEmpty(path)) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.NOT_FOUND);
//            return response.setComplete();
//        }
//
//        // 判断是否属于是调用 api
//        if (path.matches("^/log/api/.*$")) {
//            return chain.filter(exchange);
//        }
//
//        // 判断是否属于是登录或者退出接口
//        if (path.matches("^/log/admin/admin/log.*$")) {
//            return chain.filter(exchange);
//        }
//
//        // 判断是否携带 token, 没有 token 说明没有登录信息
//        List<String> list = headers.get("Authorization");
//        if (list != null && list.size() != 0) {
//
//            // 获取 token 用户 id 信息
//            String token = list.get(0);
//            Long id = TokenUtils.getId(token);
//            if (!ObjectUtils.isEmpty(id)) {
//
//                // 平台查看 前台后台登录所使用不同
//                String plat = headers.get("plat").get(0);
//                String adminKey = null;
//                if ("END".equals(plat))
//                    adminKey = RedisKeyUtils.createAdminKey(id);
//                else adminKey = RedisKeyUtils.createUserKey(id);
//
//                // 通过用户 id 在 redis 缓存当中查找用户数据
//                Object obj = redisTemplate.opsForValue().get(adminKey);
//                if (!ObjectUtils.isEmpty(obj)) {
//
//                    // 用户信息存在 通过 刷新 token
//                    redisTemplate.expire(adminKey, RedisKeyUtils.ADMIN_EXP_TIME, RedisKeyUtils.ADMIN_EXP_TIME_UTIL);
//                    return chain.filter(exchange);
//                }
//            }
//        }
//
//        // 没有登录 403
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.FORBIDDEN);
//        return response.setComplete();
//    }
}
