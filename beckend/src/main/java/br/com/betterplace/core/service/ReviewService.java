package br.com.betterplace.core.service;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.betterplace.core.model.Review;

@Service
public class ReviewService extends AbstractService {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Review> readAll() {
        return this.dataSources.getSession()
                .createCriteria(Review.class)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Review> readByNeighborhood(Integer neighborhoodId) {
        return this.dataSources.getSession()
                .createCriteria(Review.class)
                .add(Restrictions.eq("neighborhoodId", neighborhoodId))
                .list();
    }

    @Transactional(readOnly = false)
    public void addComment(Integer neighborhoodId, Review review) {
        review.setNeighborhoodId(neighborhoodId);
        this.dataSources.getSession()
                .save(review);
    }
}