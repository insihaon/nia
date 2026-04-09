package com.nia.data.linkage.vo.topology.json.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class ServiceNodeTagDataVo implements Serializable {

    @JsonProperty("tag-index")
    private int tagIndex;
    @JsonProperty("tag-type")
    private String tagType;
    @JsonProperty("tag")
    private String tag;

    @JsonProperty("tag-index")
    public int getTagIndex() {
        return tagIndex;
    }

    @JsonProperty("tag-index")
    public void setTagIndex(int tagIndex) {
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
