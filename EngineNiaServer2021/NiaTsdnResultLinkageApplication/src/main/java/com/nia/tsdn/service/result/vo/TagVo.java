package com.nia.tsdn.service.result.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class TagVo implements Serializable {

    @JsonProperty("tag-index")
    private String tagIndex;
    @JsonProperty("tag-type")
    private String tagType;
    @JsonProperty("tag")
    private String tag;

    @JsonProperty("tag-index")
    public String getTagIndex() {
        return tagIndex;
    }

    @JsonProperty("tag-index")
    public void setTagIndex(String tagIndex) {
        this.tagIndex = tagIndex;
    }

    @JsonProperty("tag-type")
    public String getTagType() {
        return tagType;
    }

    @JsonProperty("tag-type")
    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @JsonProperty("tag")
    public String getTag() {
        return tag;
    }

    @JsonProperty("tag")
    public void setTag(String tag) {
        this.tag = tag;
    }
}
