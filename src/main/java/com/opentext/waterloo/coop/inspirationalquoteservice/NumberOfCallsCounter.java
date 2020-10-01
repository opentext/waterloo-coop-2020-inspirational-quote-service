package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.Optional;

public interface NumberOfCallsCounter {
    String fetchClientIpAddr();
}
