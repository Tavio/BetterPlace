package br.com.betterplace.core.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.betterplace.core.DataSources;

public abstract class AbstractService {

    @Autowired
    protected DataSources dataSources;

    protected Query setPagingParams(Query query, Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageSize != null) {
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query;
    }

    protected Criteria setPagingParams(Criteria criteria, Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageSize != null) {
            criteria.setFirstResult((pageNumber - 1) * pageSize);
            criteria.setMaxResults(pageSize);
        }
        return criteria;
    }

    /**
     * @param criteria
     * @param orderType
     * @param columnOrder
     * @param availableColumns
     * @return true caso os atributos de ordenacao tenham sido aplicados, false caso contrario
     */
    public boolean setOrderParams(Criteria criteria, String orderType, String columnOrder, String[] availableColumns) {
        if (criteria == null || orderType == null || columnOrder == null || availableColumns == null) {
            return false;
        }
        if (orderType != null && !orderType.equalsIgnoreCase("desc") && !orderType.equalsIgnoreCase("asc")) {
            return false;
        }
        if (!Arrays.asList(availableColumns).contains(columnOrder)) {
            return false;
        }
        if (orderType.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(columnOrder));
        } else {
            criteria.addOrder(Order.asc(columnOrder));
        }
        return true;
    }

    @Transactional(readOnly = true)
    protected <T> Boolean exists(Class<T> clazz, Serializable id) {
        Object result = this.dataSources.getSession().createCriteria(clazz)
                .setProjection(Projections.rowCount())
                .add(Restrictions.idEq(id))
                .uniqueResult();
        if (result != null) {
            return Long.class.cast(result) > 0;
        }
        return false;
    }

    @Transactional(readOnly = true)
    protected <T> Boolean existsWithPropertyEquals(Class<T> clazz, String propertyName, Object propertyValue) {
        Object result = this.dataSources.getSession().createCriteria(clazz)
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq(propertyName, propertyValue))
                .uniqueResult();
        if (result != null) {
            return Long.class.cast(result) > 0;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    protected <T> T get(Class<T> klass, Serializable id) {
        return (T) this.dataSources.getSession().get(klass, id);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    protected <T> List<T> list(Class<T> klass) {
        return (List<T>) this.dataSources.getSession().createCriteria(klass).list();
    }

    public void throwServiceExceptionWithMessageForValue(String message, Object... values) {
        throw new ServiceException(String.format(message, values));
    }
}