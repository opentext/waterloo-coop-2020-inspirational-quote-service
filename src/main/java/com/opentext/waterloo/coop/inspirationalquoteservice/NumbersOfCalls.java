package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.Optional;

@Component
public class NumbersOfCalls {
    private static int totalCounter = 0;
    static Hashtable<String, Integer> ip_address = new Hashtable<String, Integer>();

    protected String fetchClientIpAddrAndUpdate(){
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        updateCounter(ip);
        return ip;
    }

    protected String fetchClientIpAddr(){
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:0:1")) ip = "127.0.0.1";
        return ip;
    }

    public void updateCounter(String ip){
        if (ip_address.containsKey(ip)){
            ip_address.put(ip,ip_address.get(ip)+1);
        }else{
            ip_address.put(ip,1);
        }
        totalCounter +=1;
    }

    public int getNumberOfCallsAndUpdate(){
        return ip_address.get(fetchClientIpAddrAndUpdate());
    }
    public int getNumberOfCalls(){
        return ip_address.get(fetchClientIpAddr());
    }
}
