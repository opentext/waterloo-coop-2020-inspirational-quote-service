package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.Optional;

public class IndividualCounter implements NumberOfCallsCounter{
    private static int individualNumbers = 0;
    static Hashtable<String, Integer> ip_address = new Hashtable<String, Integer>();
    @Override
    public String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:1")) ip = "127.0.0.1";
//        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3,"Illegal IP:" + ip);
        return ip;
    }
    String ip = fetchClientIpAddr();
    public static void setIp_address(Hashtable<String, Integer> ip_address, String ip, int individualNumbers) {
        IndividualCounter.ip_address = ip_address;
        ip_address.put(ip, individualNumbers);
    }
}
