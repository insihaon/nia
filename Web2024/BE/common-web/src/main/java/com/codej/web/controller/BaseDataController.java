package com.codej.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.codej.base.controller.BaseController;
import com.codej.base.dto.AppDto;
import com.codej.base.dto.DbUser;
import com.codej.base.dto.model.ResultMap;
import com.codej.base.dto.response.ResultResponse;
import com.codej.base.dto.response.SingleResponse;
import com.codej.base.exception.CMybatisException;
import com.codej.base.exception.CServiceException;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CServiceNotImplement;
import com.codej.base.property.GlobalConstants;
import com.codej.base.utils.CommonUtil;
import com.codej.base.utils.CryptUtil;
import com.codej.base.utils.EncryptUtil;
import com.codej.web.security.AuthUserService;
import com.codej.web.service.ResponseService;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseDataController extends BaseController {

    @Autowired
    protected AuthUserService authUserService;

    @Autowired
    protected ResponseService responseService; // 결과를 처리할 Service

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected AppDto appDto;

    protected List<Object> getServices() {
        return new ArrayList<Object>() {
            {
                add(getService());
            }
        };
    }

    protected List<Object> getMappers() {
        return new ArrayList<Object>() {
            {
                add(getMapper());
            }
        };
    }

    protected abstract Object getService();

    protected abstract Object getMapper();

    public Boolean getEncrypt(@RequestBody HashMap<String, Object> body) {
        Boolean encrypt = true;
        try {
           String encryptText = (String) body.get(GlobalConstants.Common.ENCRYPT);
           String decryptText = EncryptUtil.decryptText(encryptText);
           if(decryptText == null) {
                 throw new CServiceIncorrectUse("데이터 형식을 알 수 없습니다.");
           }
           encrypt = Boolean.parseBoolean(CommonUtil.toString(decryptText, "true"));
           
        } catch (CServiceIncorrectUse e) {
           throw e;            
        } catch (Exception e) {
           e.printStackTrace();
        }
        return encrypt;
      }

    protected HashMap<String, Object> nomalizeParam(HashMap<String, Object> param) {
        Boolean encrypt = false;
        try {
            String encryptText = (String) param.get(GlobalConstants.Common.ENCRYPT);
            encrypt = Boolean.parseBoolean(EncryptUtil.decryptText(encryptText));
        } catch (Exception e) {
            return null;
        }

        // if(appDto.isDevProfile() == false && "datahub".equals(appDto.getProject()) &&
        // appDto.getEncrypt() != encrypt) {
        // return null;
        // }

        HashMap<String, Object> data = paramToData(param, encrypt);

        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
        // String lang = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        String lang = request.getHeader(GlobalConstants.Common.N_LANG);
        data.put(GlobalConstants.Common.N_LANG, lang != null ? lang : Locale.KOREAN.getLanguage());
        data.put(GlobalConstants.Common.UID, authUserService.getUserUid());
        data.put(GlobalConstants.Common.ROLES, authUserService.getUserRoles());
        data.put(GlobalConstants.Common.ENCRYPT, encrypt);

        try {
            DbUser user = authUserService.getUser();
            log.info(user.toString());
        } catch (Exception e) {
        }

        return data;
    }

    public Object doRequest(String action, HashMap<String, Object> param, boolean isIncludeResult) throws Exception {

        HashMap<String, Object> data = nomalizeParam(param);

        if (appDto.isDevProfile()) {
            log.debug(CommonUtil.mapToString(data, 300));
        }

        // Object result = serviceMehthod(action, data, isIncludeResult);
        Object result = null;

        if (result == null) {
            String ACTION = action.toUpperCase();
            if (ACTION.startsWith("SELECT") && ACTION.endsWith("LIST")) {
                result = selectList(null, action, data, isIncludeResult);
            } else if (ACTION.startsWith("SELECT")) {
                result = selectOne(null, action, data, isIncludeResult);
            } else if (ACTION.startsWith("UPDATE")) {
                result = modify(null, action, data);
            } else if (ACTION.startsWith("INSERT")) {
                result = modify(null, action, data);
            } else if (ACTION.startsWith("DELETE")) {
                result = modify(null, action, data);
            } else if (ACTION.startsWith("UPSERT")) {
                result = modify(null, action, data);
            } else {
                result = selectOne(null, action, data, isIncludeResult);
            }
        }

        return toResult(result, (Boolean) data.getOrDefault(GlobalConstants.Common.ENCRYPT, false));
    }

    protected Object toResult(Object result, Boolean encrypt) throws CServiceException, Exception {
        encrypt = encrypt != null ? encrypt : false;
        if (result == null) {
            throw new CServiceException("데이터가 null 입니다.");
        }

        if (result instanceof SingleResponse) {
            SingleResponse<?> response = (SingleResponse<?>) result;
            if (!response.isEncrypt()) {
                return SingleResponse.createResult(response, encrypt);
            }
        } else if (result instanceof ResultResponse) {
            ResultResponse<?> response = (ResultResponse<?>) result;
            if (!response.isEncrypt()) {
                return ResultResponse.createResult(response, encrypt);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private HashMap<String, Object> paramToData(HashMap<String, Object> param, Boolean encrypt) {
        HashMap<String, Object> data = CryptUtil.decryptToMap(param, encrypt);

        Iterator<String> keys = request.getParameterMap().keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = request.getParameter(key);
            data.put(key, value);
        }

        return data;
    }

    @SuppressWarnings("unchecked")
    public Object selectList(String command, String sqlId, HashMap<String, Object> param, boolean isIncludeResult)
            throws Exception {

        if (sqlId == null /* || !sqlId.endsWith("List") */) {
            log.error("selectList sqlId is null: {}", param.toString());
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + sqlId);
        }

        param.put("command", command);
        param.put("sqlId", sqlId);

        try {
            long total = -1;
            Integer page = (Integer) param.get("page");
            if (page != null) {
                param.put("TOTAL_COUNT", true);
                List<ResultMap> totalList = (List<ResultMap>) runMehthod(sqlId, param);

                if (totalList == null) {
                    total = 0;
                } else if (totalList.size() == 0) {
                    // totalList 에는 1개의 count 필드가 반드시 있어야 한다.
                    // 없는 경우는 UI에서는 페이징 그리드를 쓰고, mybatis XML TotalCountPrefix 를 정의하지 않은 경우 이다
                    log.error("mybatis XML TotalCountPrefix 를 정의했는 지 확인하라");
                    total = 0;
                } else {
                    total = getCountOfFirstResultMap(totalList, "count");
                }
                param.remove("TOTAL_COUNT");
            }

            Object result = fetchResult(sqlId, param, isIncludeResult, total);
            return result;
        } catch (InvocationTargetException e) {
            String message = String.format("selectList-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.getTargetException().getMessage(), sqlId, param.toString());
            log.error(message);
            throw new CMybatisException(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message, e);
        } catch (Exception e) {
            String message = String.format("selectList-exception=%s\n\nsqlId==%s\n\nparam==%s", e.toString(), sqlId,
                    param.toString());
            log.error(message);
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message,
                    e);
        }
    }

    public long getCountOfFirstResultMap(List<ResultMap> totalList, String countKey) {
        long count = -1;
        Object countObject = totalList.get(0).get(countKey);
        if (countObject instanceof Long) {
            // Long으로 처리하는 로직 (postgres)
            count = (Long) countObject;
        } else if (countObject instanceof BigDecimal) {
            // BigDecimal로 처리하는 로직 (oracle)
            BigDecimal countBigDecimal = (BigDecimal) countObject;
            count = countBigDecimal.longValue();
        } else {
            // countObject가 다른 타입인 경우 처리
        }

        return count;
    }

    public Object selectOne(String command, String sqlId, HashMap<String, Object> param, boolean isIncludeResult)
            throws Exception {
        if (sqlId == null /* || sqlId.endsWith("List") */) {
            log.error("selectOne sqlId is null: {}", param.toString());
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + sqlId);
        }

        param.put("command", command);
        param.put("sqlId", sqlId);

        try {
            Object result = fetchResult(sqlId, param, isIncludeResult);
            return result;
        } catch (InvocationTargetException e) {
            String message = String.format("selectOne-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.getTargetException().getMessage(), sqlId, param.toString());
            // log.error(message);
            throw new CMybatisException(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message, e);
        } catch (Exception e) {
            String message = String.format("selectOne-exception=%s\n\nsqlId==%s\n\nparam==%s", e.toString(), sqlId,
                    param.toString());
            log.error(message);
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message,
                    e);
        }
    }

    // public ResultResponse<?> modify(String command, String sqlId, HashMap<String,
    // Object> param) throws Exception {
    public Object modify(String command, String sqlId, HashMap<String, Object> param) throws Exception {
        if (sqlId == null || sqlId.matches("^(insert|delete|update|upsert)")) {
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + sqlId);
        }

        param.put("command", command);
        param.put("sqlId", sqlId);

        try {
            Boolean commit = (Boolean) param.getOrDefault("commit", true);
            if (commit) {
                Object result = fetchResult(sqlId, param, true);
                return result;
            }
            return 1;
            // return responseService.createSuccessResponse();
        } catch (InvocationTargetException e) {
            // String message = String.format("modify-exception={}, sqlId={}, param={},
            // target={}", e.getTargetException().getMessage(), sqlId, param.toString(),
            // e.getTargetException().getMessage());
            String message = String.format("modify-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.getTargetException().getCause().getMessage(), sqlId, param.toString());
            // log.error(message);
            throw new CMybatisException(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message, e);
        } catch (Exception e) {
            String message = String.format("modify-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.toString() + "\n" + e.getCause(), sqlId, param.toString());
            log.error(message);
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message,
                    e);
        }
    }

    Object runMehthod(String sqlId, HashMap<String, Object> param) throws CServiceException, InvocationTargetException,
            NoSuchMethodException, SecurityException, CServiceIncorrectUse, Exception {
        int count = getMappers().size();
        Exception ex = null;
        Object result = null;
        for (Object mapper : getMappers()) {
            try {
                count--;
                Method thisMethod = mapper.getClass().getDeclaredMethod(sqlId, new Class[] { HashMap.class });
                result = thisMethod.invoke(mapper, new Object[] { param });
                break;
            } catch (InvocationTargetException e) {
                throw e;
            } catch (NoSuchMethodException | SecurityException e) {
                if (ex == null)
                    ex = new CServiceNotImplement(
                            CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), message=", e.getMessage())),
                            e);
            } catch (Exception e) {
                if (ex == null)
                    ex = new CServiceIncorrectUse(
                            CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), message=", e.getMessage())),
                            e);
            }
        }

        if (result != null) {
            return result;
        }

        if (count == 0 && ex != null) {
            log.error("run-exception=%s\n\nsqlId==%s\n\nparam==%s", ex.toString() + "\n" + ex.getCause(), sqlId,
                    param.toString());
            throw new CServiceException("Error in runMehthod", ex);
        }
        return null;
    }

    private Method findMehthod(Class<?> clazz, String sqlId) throws Exception {
        Method thisMethod = null;
        if (clazz != null) {
            try {
                thisMethod = clazz.getDeclaredMethod(sqlId, new Class[] { HashMap.class });
            } catch (NoSuchMethodException | SecurityException e) {
                thisMethod = findMehthod(clazz.getSuperclass(), sqlId);
            }
        }
        return thisMethod;
    }

    private Object runServiceMehthod(String sqlId, HashMap<String, Object> param, boolean isIncludeResult)
            throws Exception {
        try {
            for (Object service : getServices()) {
                Method thisMethod = findMehthod(service.getClass(), sqlId);
                if (thisMethod != null) {
                    Object result = thisMethod.invoke(service, new Object[] { param });
                    return result;
                }
            }
        } catch (InvocationTargetException e) {
            String message = String.format("serviceMehthod-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.getTargetException(), sqlId, param.toString());
            log.error(message);
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message,
                    e);
        } catch (Exception e) {
            String message = String.format("serviceMehthod-exception=%s\n\nsqlId==%s\n\nparam==%s",
                    e.toString() + "\n" + e.getCause(), sqlId, param.toString());
            log.error(message);
            throw new CServiceIncorrectUse(CommonUtil.getStackTrace(String.format("$METHOD($FILE:$LINE), ")) + message,
                    e);
        }

        return null;
    }

    private Object fetchResult(String sqlId, HashMap<String, Object> param, boolean isIncludeResult) throws Exception {
        return fetchResult(sqlId, param, isIncludeResult, -1);
    }

    private Object fetchResult(String sqlId, HashMap<String, Object> param, boolean isIncludeResult, long total)
            throws Exception {
        Object result = runServiceMehthod(sqlId, param, isIncludeResult);
        result = result != null ? result : runMehthod(sqlId, param);
        if (isIncludeResult) {
            return resultToResponse(result, total);
        }
        return result;
    }

    private Object resultToResponse(Object in_result, long total) {
        Object result = in_result;
        if (total >= 0 && result == null) {
            result = new ArrayList<ResultMap>();
        }

        if (result instanceof ResultResponse) {
            return result;
        } else if (result instanceof SingleResponse) {
            return result;
        } else if (result instanceof List) {
            if (total < 0) {
                return responseService.createListResponse((List<?>) result);
            } else {
                List<ResultMap> list = (List<ResultMap>) result;
                if (list.size() > 0 && list.get(0).get("LIST_TOTAL_COUNT") != null) {
                    total = getCountOfFirstResultMap(list, "LIST_TOTAL_COUNT");
                }

                return responseService.createListResponse(list, (int) Math.max(total, list.size()));
            }
        } else if (result instanceof ResultMap) {
            return responseService.createSingleResponse((ResultMap) result);
        } else if (result != null) {
            return responseService.createResultResponse(result);
        }
        return result;
    }
}
