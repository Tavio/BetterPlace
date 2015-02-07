package br.com.betterplace.core.service;

import br.com.betterplace.core.model.Neighborhood;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NeighborhoodService extends AbstractService {

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Neighborhood> readAll() {
        return this.dataSources.getSession()
                .createCriteria(Neighborhood.class)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Neighborhood> readByName(String neighborhoodName) {
        return this.dataSources.getSession()
                .createCriteria(Neighborhood.class)
                .add(Restrictions.like("name", neighborhoodName))
                .list();
    }

    @Transactional(readOnly = false)
    public Neighborhood getOrCreate(Float latitude, Float longitude, String neighborhoodName) {
        List<Neighborhood> neighborhoods = this.readByName(neighborhoodName);
        if (neighborhoods == null || neighborhoods.isEmpty()) {
            Neighborhood neighborhood = new Neighborhood();
            neighborhood.setLatitude(latitude);
            neighborhood.setLongitude(longitude);
            neighborhood.setName(neighborhoodName);
            super.dataSources.getSession().save(neighborhood);
            neighborhoods.add(neighborhood);
        }
        return neighborhoods.get(0);
    }
}