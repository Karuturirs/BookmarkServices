package com.bookmark.restservice.mysql.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.NullableType;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bookmark.restservice.mysql.dao.GenericDao;

/**
 * This class serves as the Base class for all other Services - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 *
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *     &lt;bean id="xxxService" class="com.nemada.gescarga.service.impl.GenericServiceImpl"&gt;
 *     	   &lt;constructor-arg ref="xxxDao"/&gt;
 *     &lt;/bean&gt;
 * </pre>
 *
 * @author praveen kumar
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
@Repository
@Transactional
public class GenericServiceImpl<T, PK extends Serializable> implements GenericService<T, PK>,Serializable {

    /**
     * GenericDao instance, set by constructor of this class
     */
    protected GenericDao<T, PK> _genericDao;
    
    /**
     * 
     */
    public GenericServiceImpl(){
    	
    }

    /**
     * Public constructor for creating a new GenericServiceImpl.
     * 
     * @param genericDao the GenericDao to use for persistence
     */
    public GenericServiceImpl(GenericDao<T, PK> genericDao) {
        this._genericDao = genericDao;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T get(PK id) throws DataAccessException {
        return this._genericDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T load(PK id) throws DataAccessException {
        return this._genericDao.load(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Collection<T> getAll() throws DataAccessException {
        return this._genericDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public T merge(T object) throws DataAccessException {
        return this._genericDao.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public PK save(T object) throws DataAccessException {
        return this._genericDao.save(object);
    }
    
    /**
     * {@inheritDoc}
     */
    public PK saveAndRefresh(T object) throws DataAccessException {
        return this._genericDao.save(object);
    }

    /**
     * {@inheritDoc}
     */
    public void saveAll(List<T> objects) throws DataAccessException {
        this._genericDao.saveAll(objects);
    }

    /**
     * {@inheritDoc}
     */
    public void update(T object) throws DataAccessException {
        this._genericDao.update(object);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(T object) throws DataAccessException {
        this._genericDao.delete(object);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Collection<T> findByExample(final T exampleEntity) throws DataAccessException {
        return this._genericDao.findByExample(exampleEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Collection<T> findByExample(final T exampleEntity, final int firstResult, final int maxResults) throws DataAccessException {
        return this._genericDao.findByExample(exampleEntity, firstResult, maxResults);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public T getByExample(final T exampleEntity) throws DataAccessException {
        return this._genericDao.getByExample(exampleEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public Integer countByExample(final T exampleEntity) throws DataAccessException {
        return this._genericDao.countByExample(exampleEntity);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<T> findBySQLQuery(String sqlQueryString) throws DataAccessException { 
        return this._genericDao.findBySQLQuery(sqlQueryString);
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<T> findBySQLQuery(String sqlQueryString, String alias1, String alias2, Class<T> alias1Class,Class<T> alias2Class) throws DataAccessException {
        return this._genericDao.findBySQLQuery(sqlQueryString,alias1,alias2,alias1Class,alias2Class);
    }

    
    
    /**
     * {@inheritDoc}
     */
    public Collection<T> findBySQLQuery(String sqlQueryString, final int firstResult, final int maxResults) throws DataAccessException {
        return this._genericDao.findBySQLQuery(sqlQueryString, firstResult, maxResults);
    }

    /**
     * {@inheritDoc}
     */
    public T getBySQLQuery(String sqlQueryString) throws DataAccessException {
        return this._genericDao.getBySQLQuery(sqlQueryString);
    }

    /**
     * {@inheritDoc}
     */
    public Integer countBySQLQuery(String sqlQueryString) throws DataAccessException {
        return this._genericDao.countBySQLQuery(sqlQueryString);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<T> findByHSQLQuery(String hsqlQueryString) throws DataAccessException {
        return this._genericDao.findByHSQLQuery(hsqlQueryString);
    }
    
    /**
     * {@inheritDoc}
     */
    public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String,NullableType> scalar, ResultTransformer transformer) throws DataAccessException {
        return this._genericDao.findByHSQLQuery(hsqlQueryString,scalar,transformer);
    }
    
    public Collection<T> findByHSQLQuery(String hsqlQueryString,
    		   Map<String, Object> map) throws DataAccessException{
    	return this._genericDao.findByHSQLQuery(hsqlQueryString, map);
    }
    
    @Override
    public int deleteByHSQLQuery(String hsqlQueryString, Map<String, Object> map)
      throws DataAccessException {
     return this._genericDao.deleteByHSQLQuery(hsqlQueryString, map);
    }
    
    @Override
    public Object getValueByHSQLQuery(String hsqlQueryString, Map<String, Object> map)throws DataAccessException {
     return this._genericDao.getValueByHSQLQuery(hsqlQueryString,  map);
    }
    
   
    public Collection<T> findByHSQLQuery(String hsqlQueryString, Map<String, NullableType> scalar,final int firstResult, final int maxResults )throws DataAccessException {
    	return this._genericDao.findByHSQLQuery(hsqlQueryString, scalar, firstResult, maxResults);
    }

	@Override
	public Integer updateBySQLQuery(String sqlQueryString) {
		return this._genericDao.updateBySQLQuery(sqlQueryString);
	}
}
