package com.codej.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.AppDto;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CUnknowException;
import com.codej.base.utils.FileUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
// @RequestMapping(value = "/v1") // 버전에 상관없이 처리
public class MockController extends BaseController {

    @Autowired
    private AppDto appDto;

    @RequestMapping(value = "/mock")
    public ResponseEntity<?> mock(
            @RequestHeader("jsonfilename") String jsonfilename) throws Exception {
        return doResponse(jsonfilename);
    }

    public ResponseEntity<?> doResponse(String jsonfilename) throws Exception {

        if (jsonfilename == null) {
            throw new CServiceIncorrectUse();
        }

        String filePath = getFilePath(jsonfilename);
        if (!FileUtil.existFile(filePath)) {
            filePath = findLikeFile(jsonfilename);
            // throw new CUnknowException(String.format("JSON 파일을 찾을 수 없습니다.(%s)",
            // filePath));
        }

        try {
            JsonObject jsonObject = ((JsonObject) readJsonFile(filePath)).getAsJsonObject("__body");
            return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
        } catch (ClassCastException e) {
            throw new CUnknowException(String.format("JSON 파일이 알 수 없는 형식입니다.(%s)", filePath));
        } catch (Exception e) {
            throw new CUnknowException(e.toString());
        }
    }

    private String findLikeFile(String jsonfilename) throws CUnknowException {
        String dirPath = FileUtil.combine(FileUtil.getCurrentDir(), "json", appDto.getProject());
        List<String> fileList = getJsonFileList(dirPath);
        int index = getLikeFileIndex(fileList, jsonfilename);

        if (index < 0) {
            index = 0;
        }
        if (index >= fileList.size()) {
            index -= 1;
        }

        return FileUtil.combine(FileUtil.getCurrentDir(), "json", appDto.getProject(), fileList.get(index));
    }

    // 디렉토리를 입력하면 해당 디렉토리 파일 중 .json 확장자를 가진 파일명의 리스트를 반환하는 함수
    private List<String> getJsonFileList(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        return Arrays.asList(files).stream().map(File::getName)
                .filter(s -> s.endsWith(".json"))
                .collect(Collectors.toList());
    }

    public static int getLikeFileIndex(List<String> list, String target) {
        // List 정렬
        Collections.sort(list);

        // 정렬된 List에서 target의 인덱스 찾기
        int index = Collections.binarySearch(list, target);

        // target이 없는 경우, 삽입 위치의 음수 인덱스 반환
        if (index < 0) {
            index = -(index + 1);
        }

        String[] parts = target.split("_");
        String svcname = parts[0];

        if (svcname != null && list.get(index).startsWith(svcname)) {
            index = index - 1;
        }

        // target이 있는 경우, 인덱스 반환
        return index;
    }

    // 파일명을 입력하면 해당 파일 경로를 반환하는 함수
    // 현재 폴더의 json 폴더 아래에 해당 파일이 있는지 확인하고 있으면 그 경로를 반환한다.
    private String getFilePath(String fileName) {
        String path = FileUtil.combine(FileUtil.getCurrentDir(), "json", appDto.getProject(), fileName);
        log.info("JsonFile Path={}", path);
        return path;
    }

    private Object readJsonFile(String path) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(path);
        // FileReader reader = new FileReader(file); // 한글 깨짐 때문에 아래와 같이
        // InputStreamReader 를 사용.
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF8");

        JsonParser jsonParser = new JsonParser();
        Object obj = jsonParser.parse(reader);

        // log.info("type={}, value={}", obj.getClass().getName(), obj.toString());
        return obj;
    }
}
