package com.hifive.bururung.global.exception.errorcode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) {
        if(response.isCommitted()){
            return;
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        try {
            response.getWriter().write("{\"message\":\"Internal Server Error\"}");
            response.flushBuffer();
        } catch (Exception ex) {
            log.error("Error writing error response", ex);
        }
    }
}

