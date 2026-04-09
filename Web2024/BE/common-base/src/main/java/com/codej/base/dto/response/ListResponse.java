package com.codej.base.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponse<T> extends ResultResponse<List<T>> {
    public ListResponse() {
    }
}
