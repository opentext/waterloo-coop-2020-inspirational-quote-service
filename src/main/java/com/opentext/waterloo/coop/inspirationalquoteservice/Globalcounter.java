package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;
import java.util.Optional;

public class Globalcounter implements NumberOfCallsCounter{
    private static int numberOfCalls = 0;
    public int IncrementCounter(){
        numberOfCalls +=1;
    };

    public static int getNumberOfCalls() {
        return numberOfCalls;
    }
}
