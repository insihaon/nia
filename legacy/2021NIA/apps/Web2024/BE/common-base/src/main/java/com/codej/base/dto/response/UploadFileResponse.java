package com.codej.base.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 인자를 모두 갖춘 생성자를 자동으로 생성합니다.
public class UploadFileResponse {
    private String name;
    private String downloadUrl;
    private String previewUrl;
    private String type;
    private long size;
}
