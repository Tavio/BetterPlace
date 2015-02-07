package br.com.betterplace.web.resource;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel
public class ReviewResource implements Serializable {

    @ApiModelProperty
    private Integer id;

    @ApiModelProperty
    private String reviewerName;

    @ApiModelProperty
    private String reviewerEmail;

    @ApiModelProperty
    private String comment;

    @ApiModelProperty
    private Integer neighborhoodId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getReviewerEmail() {
        return reviewerEmail;
    }

    public void setReviewerEmail(String reviewerEmail) {
        this.reviewerEmail = reviewerEmail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getNeighborhoodId() {
        return neighborhoodId;
    }

    public void setNeighborhoodId(Integer neighborhoodId) {
        this.neighborhoodId = neighborhoodId;
    }
}