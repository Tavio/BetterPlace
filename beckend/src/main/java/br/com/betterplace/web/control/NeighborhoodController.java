package br.com.betterplace.web.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.betterplace.core.model.Neighborhood;
import br.com.betterplace.core.model.Review;
import br.com.betterplace.core.service.NeighborhoodService;
import br.com.betterplace.core.service.ReviewService;
import br.com.betterplace.web.resource.ReviewResource;
import br.com.betterplace.web.security.BetterPlaceGenericController;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/private/neighborhood")
public class NeighborhoodController extends BetterPlaceGenericController {

    @Autowired
    private NeighborhoodService neighborhoodService;

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/{neighborhoodName}", method = RequestMethod.GET, headers = "Accept=application/json", produces = "application/json")
    @ApiOperation(value = "Models by Manufacturer ID", notes = "Returns models for the given manufacturer.", response = ReviewResource[].class)
    public ResponseEntity<ReviewResource[]> list(@PathVariable(value = "neighborhoodName") String neighborhoodName) {
        List<Neighborhood> neighborhoods = this.neighborhoodService.readByName(neighborhoodName);
        if (neighborhoods == null || neighborhoods.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Neighborhood neighborhood = neighborhoods.get(0);
        List<Review> reviews = this.reviewService.readByNeighborhood(neighborhood.getId());
        return super.getArrayResponseEntity(reviews, ReviewResource.class);
    }
}