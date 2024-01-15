package com.codej.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.codej.base.dto.model.Data;
import com.codej.base.dto.response.ErrorResponse;
import com.codej.base.dto.response.ListResponse;
import com.codej.base.dto.response.ResultResponse;
import com.codej.base.dto.response.SingleResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ResponseService {
    private final SqlService sqlService;

    // enum으로 api 요청 결과에 대한 code, message를 정의합니다.
    public enum CommonResponse {
        SUCCESS(20000, "성공하였습니다.");

        int code;
        String message;

        CommonResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public <T> ResultResponse<T> createResultResponse(T data) {
        ResultResponse<T> response = new ResultResponse<>();
        response.setResult(data);
        setSuccessResponse(response);
        return response;
    }


    // 단일건 결과를 처리하는 메소드
    public <T> SingleResponse<T> createSingleResponse(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setResult(data);
        setSuccessResponse(response);
        return response;
    }

    public SingleResponse<Map<String, Object>> createSingleDataResponse(Data data) {
        return createSingleResponse(data.getMap());
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResponse<T> createListResponse(List<T> list) {
        return createListResponse(list, null);
    }

    public <T> ListResponse<T> createListResponse(List<T> in_list, Integer total) {
        List<T> list = in_list;
        ListResponse<T> response = new ListResponse<T>();
        if(list == null) {
            list = new ArrayList<>();
        }
        response.setResult(list);
        response.setTotal(total == null ? list.size() : Math.toIntExact(total));
        setSuccessResponse(response);
        return response;
    }

    // 성공 결과만 처리하는 메소드
    public ResultResponse<Boolean> createSuccessResponse() {
        ResultResponse<Boolean> response = new ResultResponse<Boolean>();
        response.setResult(true);
        setSuccessResponse(response);
        return response;
    }

    public ResultResponse<Boolean> createFailResponse() {
        return createFailResponse(-999, "알 수 없는 오류가 발생하였습니다.", null);
    }

    public ResultResponse<Boolean> createFailResponse(String message) {
        return createFailResponse(-999, "알 수 없는 오류가 발생하였습니다.", message);
    }

    // 실패 결과만 처리하는 메소드
    public ResultResponse<Boolean> createFailResponse(int code, String message) {
        return createFailResponse(code, message, null);
    }

    // 실패 결과만 처리하는 메소드
    public ResultResponse<Boolean> createFailResponse(int code, String message, String detailMessage) {
        ErrorResponse response = new ErrorResponse();
        response.setSql(getSql());
        response.setMessage(message);
        response.setDetailMessage(detailMessage);
        return response;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResponse(ResultResponse<?> response) {
        response.setSql(getSql());
        if (response.getTotal() == null) {
            response.setTotal(response.getResult() == null ? 0 : 1);
        }
    }

    private String getSql() {
        String sql = sqlService.poll();
        return sql;
    }
}
