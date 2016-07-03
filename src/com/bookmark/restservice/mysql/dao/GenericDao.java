package com.bookmark.restservice.mysql.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.NullableType;
import org.springframework.dao.DataAccessException;

/**
 * Generic DAO (Data Access Object) with common methods to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) DAO's for your
 * domain objects.
 *
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public interface GenericDao<T, PK extends Serializable> {

	/**
	 * Return the persistent instance of the given entity class with the given identifier,
	 * or null if there is no such persistent instance.
	 *
	 * @param id an identifier
	 * @return a persistent instance or null
	 */	
	public T get(PK id) throws DataAccessException;
	
	/**
	 * Return the persistent instance of the given entity class with the given identifier,
	 * assuming that the instance exists.
	 *
	 * @param id a valid identifier of an existing persistent instance of the class
	 * @return the persistent instance
	 */
	public T load(PK id) throws DataAccessException;
	
	/**
	 * Return all persistent instances.
	 *
	 * @return The list of persistent instances
	 */
	public Collection<T> getAll() throws DataAccessException;
	
	/**
	 * Copy the state of the given object onto the persistent object with the same
	 * identifier.
	 *
	 * @param object a detached instance with state to be copied
	 * @return an updated persistent instance
	 */
	public T merge(T object) throws DataAccessException;
	
	/**
	 * Persist the given transient instance, first assigning a generated identifier.
	 *
	 * @param object a transient instance of a persistent class
	 * @return the generated identifier
	 */
	public PK save(T object) throws DataAccessException;
	/**
	 * Persist the given transient instance, first assigning a generated identifier and refresh.
	 *
	 * @param object a transient instance of a persistent class
	 * @return the generated identifier
	 */
	public PK saveAndRefresh(T object) throws DataAccessException;
	/**
	 * Persist the given transient instance, first assigning a generated identifier.
	 *
	 * @param objects an list of transients instance of a persistent class
	 */
	public void saveAll(List<T> objects) throws DataAccessException;
	
	/**
	 * Update the persistent instance with the identifier of the given detached
	 * instance.
	 *
	 * @param object a detached instance containing updated state
	 */
	public void update(T object) throws DataAccessException;

	/**
	 * Remove a persistent instance from the datastore.
	 *
	 * @param object the instance to be removed
	 */
	public void delete(T object) throws DataAccessException;
	
	/**
	 * Execute a query based on a given example entity object.
	 * 
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 */
	public Collection<T> findByExample(final T exampleEntity) throws DataAccessException;
	
	/**
	 * Execute a query based on a given example entity object.
	 * 
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @param firstResult the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 * @see org.hibernate.Criteria#setFirstResult(int)
	 * @see org.hibernate.Criteria#setMaxResults(int)
	 */
	public Collection<T> findByExample(final T exampleEntity, final int firstResult, final int maxResults) throws DataAccessException;
	
	/**
	 * Execute a query based on a given example entity object.
	 * 
	 * @param exampleEntity an instance of the desired entity, serving as example for "query-by-example"
	 * @return a single instance that matches the query, or null if the query returns no results.
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 * @see org.hibernate.criterion.Example#create(Object)
	 */
	public T getByExample(final T exampleEntity) throws DataAccessException;
	
	public Integer countByExample(final T exampleEntity) throws DataAccessException;
	
	/** 
	 * Execute a query based on the given SQL query string.
	 * 
	 * @param sqlQueryString a SQL query 
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public Collection<T> findBySQLQuery(String sqlQueryString) throws DataAccessException;

	
	/** 
	 * Execute a query based on the given SQL query string.
	 * 
	 * @param sqlQueryString a SQL query 
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public Collection<T> findBySQLQuery(String sqlQueryString, String alias1, String alias2, Class<T> alias1Class,Class<T> alias2Class) throws DataAccessException;
	
	/**
	 * Execute a query based on the given SQL query string.
	 * 
	 * @param sqlQueryString a SQL query
	 * @param firstResult the index of the first result object to be retrieved (numbered from 0)
	 * @param maxResults the maximum number of result objects to retrieve (or <=0 for no limit)
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public Collection<T> findBySQLQuery(String sqlQueryString, final int firstResult, final int maxResults) throws DataAccessException;
	
	/**
	 * Execute a query based on the given SQL query string.
	 * 
	 * @param sqlQueryString a SQL query
	 * @return a single instance that matches the query, or null if the query returns no results.
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public T getBySQLQuery(String sqlQueryString) throws DataAccessException;
	
	public Integer countBySQLQuery(String sqlQueryString) throws DataAccessException;

        /**
	 * Execute a query based on the given HSQL query string.
	 *
	 * @param hsqlQueryString a HSQL query
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public Collection<T> findByHSQLQuery(String hsqlQueryString) throws DataAccessException;
	
    /**
	 * Execute a query based on the given HSQL query string.
	 *
	 * @param hsqlQueryString a HSQL query
	 * @return a {@link List} containing 0 or more persistent instances
	 * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
	 */
	public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String,NullableType> scalar, ResultTransformer transformer) throws DataAccessException;

	public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String, Object> map);
	
	public int deleteByHSQLQuery(String hsqlQueryString, Map<String, Object> map);

	public Object getValueByHSQLQuery(String hsqlQueryString, Map<String, Object> map);
	public Integer updateBySQLQuery(String sqlQueryString);
	
	public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String,NullableType> scalar, final int firstResult, final int maxResults ) throws DataAccessException;
	
}
