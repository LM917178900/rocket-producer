package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.services.AzureADService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@SpringBootApplication
public class DemoApplication {
    static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @DeleteMapping("/test")
    public String test(@RequestBody JSONObject jsonObject) {

//        String[] supplierParts = approval.getSupplierParts();
//        String supplierPartsString = approval.getSupplierPartsString();
//        taskService.approve(approval, "");

//        JSONObject jsonObject = JSONObject.parseObject(text);
        Iterator iter = jsonObject.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            sb.append("update ac_res set title_cn='");
            sb.append(entry.getValue().toString());
            sb.append("' where title='");
            sb.append(entry.getKey().toString());
            sb.append("';");
        }

        String str = sb.toString();
        System.out.println(str);
        return str;
    }

    @GetMapping("/token")
    public String token() {

        // Get access token
        String accessToken;
        try {
            accessToken = AzureADService.getAccessToken();
        } catch (ExecutionException | MalformedURLException | RuntimeException ex) {
            // Log error message
            logger.error(ex.getMessage());

            return ex.getMessage();

        } catch (InterruptedException interruptedEx) {
            // Log error message
            logger.error(interruptedEx.getMessage());

            Thread.currentThread().interrupt();
            return interruptedEx.getMessage();
        }
        return accessToken;
    }
}
