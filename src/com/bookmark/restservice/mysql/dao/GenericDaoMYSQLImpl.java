package com.bookmark.restservice.mysql.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.NullableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="xxxDao" class="com.nemada.gescarga.dao.hibernate.GenericDaoImpl"&gt;
 *          &lt;constructor-arg value="com.nemada.gescarga.model.Xxx"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 *
 */
@SuppressWarnings("unchecked")
public class GenericDaoMYSQLImpl<T, PK extends Serializable> implements GenericDao<T, PK>, Serializable {

    @Autowired
    protected SessionFactory mySQLSessionFactory;
    private Class<T> _persistentClass;

    /**
     * Constructor that takes in a class to see which type of entity to persist
     * 
     * @param _persistentClass the class type you'd like to persist
     */
    public GenericDaoMYSQLImpl(Class<T> persistentClass) {
        this._persistentClass = persistentClass;
    }

    /**
     * Method to return model object by specifying model ID property into
     * current Hibernate Session
     *
     * @param id the primary key of model object
     * @return T model object
     * @throws DataAccessException when there are any data access errors
     */
    public T get(PK id) throws DataAccessException {
        return (T) mySQLSessionFactory.getCurrentSession().get(this._persistentClass, id);
    }

    /**
     * Method to load model object by specifying model ID property into
     * current Hibernate Session
     *
     * @param id the primary key of model object
     * @return T the model object
     * @throws DataAccessException when there are any data access errors
     */
    public T load(PK id) throws DataAccessException {
        return (T) mySQLSessionFactory.getCurrentSession().load(this._persistentClass, id);
    }

    /**
     * Method to return collection of model objects for given persistance DAO object
     * by using current Hibernate Session.
     *
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> getAll() throws DataAccessException {
        return mySQLSessionFactory.getCurrentSession().createCriteria(this._persistentClass).list();
    }

    /**
     * Method to model object with existing object in the current Hibernate Session.
     * @param object the model object to be merged
     * @return T the model object
     * @throws DataAccessException when there are any data access errors
     */
    public T merge(T object) throws DataAccessException {
        /**
         *  Note: Hibernate3's merge operation does not reassociate the object
         * with the current Hibernate Session. Instead, it will always copy the
         * state over to a registered representation of the entity. In case of a
         * new entity, it will register a copy as well, but will not update the
         * id of the passed-in object. To still update the ids of the original
         * objects too, we need to register Spring's
         * IdTransferringMergeEventListener on our SessionFactory.
         */
        return (T) mySQLSessionFactory.getCurrentSession().merge(object);
    }

    /**
     *  Method to save the model object into DB with the current
     *  Hibernate Session.
     * @param object the model object to be merged
     * @return PK the primary key of model object
     * @throws DataAccessException when there are any data access errors
     */
    public PK save(T object) throws DataAccessException {
        return (PK) mySQLSessionFactory.getCurrentSession().save(object);
    }
    /**
     *  Method to save the model object into DB with the current
     *  Hibernate Sessione and refresh.
     * @param object the model object to be merged
     * @return PK the primary key of model object
     * @throws DataAccessException when there are any data access errors
     */
    public PK saveAndRefresh(T object) throws DataAccessException {
    	PK pk = (PK) mySQLSessionFactory.getCurrentSession().save(object);
    	mySQLSessionFactory.getCurrentSession().flush();
    	mySQLSessionFactory.getCurrentSession().refresh(object);
        return pk;
    }
    
    /**
     *  Method to save all the model objects from Collection List into DB with the current
     *  Hibernate Session.
     * @param objects<T> the collection of model objects to be saved
     */
    public void saveAll(List<T> objects) {
        for (T t : objects) {
            mySQLSessionFactory.getCurrentSession().save(t);
        }
    }

    /**
     *  Method to update  the model object into DB with the current
     *  Hibernate Session.
     * @param objects<T> the model object to be updated
     * @throws DataAccessException when there are any data access errors
     */
    public void update(T object) throws DataAccessException {
        mySQLSessionFactory.getCurrentSession().update(object);
    }

    /**
     *  Method to delete the model object into DB with the current
     *  Hibernate Session.
     * @param objects<T> the model object to be deleted
     * @throws DataAccessException when there are any data access errors
     */
    public void delete(T object) throws DataAccessException {
        mySQLSessionFactory.getCurrentSession().delete(object);
    }

    /**
     *  Method to return list of collection objects based on
     *  serach criteria through T exampleEntity. The exampleEntity should be
     *  other than the "ID" property of model class. Method uses current Hibernate Session.
     * @param exampleEntity<T> the model object to be find. The exampleEntity object values have to be set by setter methods before calling this method.
     *  The "ID" property values should not be set by that time. If so, it may return collection as null
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findByExample(final T exampleEntity) throws DataAccessException {
        Assert.notNull(exampleEntity, "Example entity must not be null");
        Criteria criteria = mySQLSessionFactory.getCurrentSession().createCriteria(this._persistentClass);
        return criteria.add(Example.create(exampleEntity)).list();
    }

    /**
     *  Method to return list of collection objects based on
     *  serach criteria through T exampleEntity. Method havet he capability to return chunks of collection.
     *  The exampleEntity should be other than the "ID" property of model class. Method uses current Hibernate Session.
     * @param exampleEntity<T> the model object to be find. The exampleEntity object values have to be set by setter methods before calling this method.
     *  The "ID" property values should not be set by that time. If so, it may return collection as null
     * @param firstResult the integer value start from which object
     * @param maxResults the integer value maximum object to be retuned
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findByExample(final T exampleEntity, final int firstResult, final int maxResults) throws DataAccessException {
        Assert.notNull(exampleEntity, "Example entity must not be null");
        Criteria criteria = mySQLSessionFactory.getCurrentSession().createCriteria(this._persistentClass);
        if (firstResult >= 0) {
            criteria.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            criteria.setMaxResults(maxResults);
        }
        return criteria.add(Example.create(exampleEntity)).list();
    }

    /**
     *  Method to return single model object based on
     *  serach criteria through T exampleEntity. The exampleEntity should be
     *  other than the "ID" property of model class. Method uses current Hibernate Session.
     * @param exampleEntity<T> the model object to be find. The exampleEntity object values have to be set by setter methods before calling this method.
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public T getByExample(final T exampleEntity) throws DataAccessException {
        Assert.notNull(exampleEntity, "Example entity must not be null");
        Criteria criteria = mySQLSessionFactory.getCurrentSession().createCriteria(this._persistentClass);
        criteria.setMaxResults(1);
        return (T) criteria.add(Example.create(exampleEntity)).uniqueResult();
    }

    /**
     *  Method to return model object count based on
     *  serach criteria through T exampleEntity. The exampleEntity should be
     *  other than the "ID" property of model class. Method uses current Hibernate Session.
     * @param exampleEntity<T> the model object to be find. The exampleEntity object values have to be set by setter methods before calling this method.
     * @return Integer the model objects count
     * @throws DataAccessException when there are any data access errors
     */
    public Integer countByExample(final T exampleEntity) throws DataAccessException {
        Assert.notNull(exampleEntity, "Example entity must not be null");
        Criteria criteria = mySQLSessionFactory.getCurrentSession().createCriteria(this._persistentClass);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Example.create(exampleEntity));
        return (Integer) criteria.uniqueResult();
    }

    /**
     * Method to return collection of model objects with supplied native sqlQueryString
     * by using current Hibernate Session.
     * @param sqlQueryString the native SQL Query
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findBySQLQuery(String sqlQueryString) throws DataAccessException {
        return mySQLSessionFactory.getCurrentSession().createSQLQuery(sqlQueryString).list();
    }
    
    /**
     * Method to return collection of model objects with supplied native sqlQueryString
     * by using current Hibernate Session.
     * @param sqlQueryString the native SQL Query
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findBySQLQuery(String sqlQueryString, String alias1, String alias2, Class<T> alias1Class,Class<T> alias2Class) throws DataAccessException {
        return mySQLSessionFactory.getCurrentSession().createSQLQuery(sqlQueryString).addEntity(alias1,alias1Class).addEntity(alias2,alias2Class).list();
    }
   
    /**
     * Method to return collection of model objects on chucks basis by suppling
     * of sqlQueryStringm firstResult and maxxResult by using current Hibernate Session.
     * @param sqlQueryString the native SQL Query
     * @param firstResult the integer value start from which object
     * @param maxResults the integer value maximum object to be retuned
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findBySQLQuery(String sqlQueryString, final int firstResult, final int maxResults) throws DataAccessException {
        SQLQuery sqlQuery = mySQLSessionFactory.getCurrentSession().createSQLQuery(sqlQueryString);
        if (firstResult >= 0) {
            sqlQuery.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            sqlQuery.setMaxResults(maxResults);
        }
        return sqlQuery.list();
    }

    /**
     * Method to return single model object with supplied native sqlQueryString
     * by using current Hibernate Session.
     * @param sqlQueryString the native SQL query
     * @return T the model object
     * @throws DataAccessException when there are any data access errors
     */
    public T getBySQLQuery(String sqlQueryString) throws DataAccessException {
        SQLQuery sqlQuery = mySQLSessionFactory.getCurrentSession().createSQLQuery(sqlQueryString);
        sqlQuery.setMaxResults(1);
        return (T) sqlQuery.uniqueResult();
    }

    /**
     * Method to return count of model objects with supplied native sqlQueryString
     * by using current Hibernate Session.
     * @param sqlQueryString the native SQL query
     * @return Integer the count of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Integer countBySQLQuery(String sqlQueryString) throws DataAccessException {
        SQLQuery sqlQuery = mySQLSessionFactory.getCurrentSession().createSQLQuery(sqlQueryString);
        sqlQuery.setMaxResults(1);
        return (Integer) sqlQuery.uniqueResult();
    }

    /**
     * Method to return collection of model objects with supplied HSQL query string
     * by using current Hibernate Session. This is a universal query across DB vendors.
     * @param sqlQueryString the native HSQL query
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findByHSQLQuery(String hsqlQueryString) throws DataAccessException {
    	
        return mySQLSessionFactory.getCurrentSession().createQuery(hsqlQueryString).list();
    }
    
    /**
     * Method to return collection of model objects with supplied HSQL query string
     * by using current Hibernate Session. This is a universal query across DB vendors.
     * @param sqlQueryString the native HSQL query
     * @return Collection<T> the collection of model objects
     * @throws DataAccessException when there are any data access errors
     */
    public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String,NullableType> scalar, ResultTransformer transformer) throws DataAccessException {
    	
        SQLQuery query = mySQLSessionFactory.getCurrentSession().createSQLQuery(hsqlQueryString);
        Set<String> sclarSet = scalar.keySet();
        for(String key: sclarSet){
        	query.addScalar(key, scalar.get(key));
        }
        query.setResultTransformer(transformer);
        return query.list();
    }
    
    @Override
    public Collection<T> findByHSQLQuery(String hsqlQueryString,
      Map<String, Object> map) {
     return mySQLSessionFactory.getCurrentSession().createQuery(hsqlQueryString)
       .setProperties(map).list();

    }
    
    @Override
    public int deleteByHSQLQuery(String hsqlQueryString, Map<String, Object> map) {
     return mySQLSessionFactory.getCurrentSession().createQuery(hsqlQueryString)
       .setProperties(map).executeUpdate();
    }
    
    @Override
    public Object getValueByHSQLQuery(String hsqlQueryString, Map<String, Object> map) {
     return (Object) mySQLSessionFactory.getCurrentSession()
       .createQuery(hsqlQueryString).setProperties(map).setMaxResults(1).uniqueResult();
    }
    
    public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String,NullableType> scalar, final int firstResult, final int maxResults ) throws DataAccessException {
 	   Query query = mySQLSessionFactory.getCurrentSession().createSQLQuery(hsqlQueryString).setProperties(scalar);
        if (firstResult >= 0) {
     	   query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
     	   query.setMaxResults(maxResults);
        }
     return  query.list();
 }

	@Override
	public Integer updateBySQLQuery(String sqlQueryString) {
		SQLQuery sqlQuery = mySQLSessionFactory.getCurrentSession().createSQLQuery(
				sqlQueryString);
		sqlQuery.setMaxResults(1);
		return sqlQuery.executeUpdate();
	}
    
}
