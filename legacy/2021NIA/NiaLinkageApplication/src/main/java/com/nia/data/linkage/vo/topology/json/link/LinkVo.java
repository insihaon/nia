package com.nia.data.linkage.vo.topology.json.link;

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
public class LinkVo implements Serializable {

    @JsonProperty("link-id")
    private String linkId;
    @JsonProperty("destination")
    private LinkDestinationVo linkDestinationVo;
    @JsonProperty("source")
    private LinkSourceVo linkSourceVo;

    @JsonProperty("link-id")
    public String getLinkId() {
        return linkId;
    }

    @JsonProperty("link-id")
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    @JsonProperty("destination")
    public LinkDestinationVo getLinkDestinationVo() {
        return linkDestinationVo;
    }

    @JsonProperty("destination")
    public void setLinkDestinationVo(LinkDestinationVo linkDestinationVo) {
        this.linkDestinationVo = linkDestinationVo;
    }

    @JsonProperty("source")
    public LinkSourceVo getLinkSourceVo() {
        return linkSourceVo;
    }

    @JsonProperty("source")
    public void setLinkSourceVo(LinkSourceVo linkSourceVo) {
        this.linkSourceVo = linkSourceVo;
    }
}
