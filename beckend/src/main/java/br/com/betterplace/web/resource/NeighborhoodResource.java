package br.com.betterplace.web.resource;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@ApiModel
public class NeighborhoodResource implements Serializable {

    @ApiModelProperty
    private Integer id;

    @ApiModelProperty
    private String name;

    @ApiModelProperty
    private BigDecimal latitude;

    @ApiModelProperty
    private BigDecimal longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}