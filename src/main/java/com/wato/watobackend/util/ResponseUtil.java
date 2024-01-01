package com.wato.watobackend.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wato.watobackend.exception.constant.Error;
import com.wato.watobackend.response.ApiErrorResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {

    public static void responseError(HttpServletResponse response, Error error) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        ApiErrorResponse errorResponse = new ApiErrorResponse(error);

        try {
            out.print(new ObjectMapper().writeValueAsString(errorResponse));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

