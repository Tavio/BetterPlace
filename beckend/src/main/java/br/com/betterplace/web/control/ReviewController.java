package br.com.betterplace.web.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.betterplace.core.model.Neighborhood;
import br.com.betterplace.core.model.Review;
import br.com.betterplace.core.service.NeighborhoodService;
import br.com.betterplace.core.service.ReviewService;
import br.com.betterplace.web.security.BetterPlaceGenericController;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/private/review")
public class ReviewController extends BetterPlaceGenericController {

    @Autowired
    private NeighborhoodService neighborhoodService;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=application/json", produces = "application/json")
    @ApiOperation(value = "")
    public ResponseEntity list(
            @RequestParam(value = "latitude") Float latitude,
            @RequestParam(value = "longitude") Float longitude,
            @RequestParam(value = "neighborhoodName") String neighborhoodName,
            @RequestParam(value = "reviewerName") String reviewerName,
            @RequestParam(value = "reviewerEmail") String reviewerEmail,
            @RequestParam(value = "comment") String comment) {
        
        Neighborhood neighborhood = this.neighborhoodService.getOrCreate(latitude, longitude, neighborhoodName);
        
        Review review = new Review();
        review.setComment(comment);
        review.setReviewerEmail(reviewerEmail);
        review.setReviewerName(reviewerName);
        
        this.reviewService.addComment(neighborhood.getId(), review);
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}