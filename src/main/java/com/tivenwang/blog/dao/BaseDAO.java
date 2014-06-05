/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tivenwang.blog.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.tivenwang.blog.config.GlobalConfig;

/**
 * 
 * @author Pecan 
 * 类说明：
 */
public class BaseDAO extends HibernateDaoSupport{

    /*protected Session session = null;
    protected Transaction transaction = null;*/
    private static Logger log = Logger.getLogger(BaseDAO.class);

    public BaseDAO() {
    }

	/**
	 * 为父类HibernateDaoSupport注入sessionFactory的值
	 * 
	 * @param sessionFactory
	 */
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
    
    
    /**
     * save object
     *
     * @param object
     * @return
     */
    public boolean save(Object object) {
    	 try {
			getHibernateTemplate().save(object);
		} catch (DataAccessException e) {
			log.error(GlobalConfig.EMPTY_STRING, e);
			return false;
		}
 		return true;
    }

    /**
     * saveOrUpdate object
     *
     * @param object
     * @return
     */
    public boolean saveOrUpdate(Object object) {
		getHibernateTemplate().saveOrUpdate(object);
		return true;
	}

    /**
     * update object
     *
     * @param object
     * @return
     */
    public boolean update(Object object) {
		getHibernateTemplate().update(object);
		return true;
	}

    /**
     * delete object
     *
     * @param object
     * @return
     */
    public boolean delete(Object object) {
		getHibernateTemplate().delete(object);
		return true;

	}

   /**
     * 获取符合条件的对象集合
     *
     * @param hsql
     * @return
     */
    @Transactional
    protected List<?> getObjects(String hsql) {
        return super.getHibernateTemplate().find(hsql);
    }

    /**
     * 获取符合条件的对象集合
     * @param <T>
     *
     * @param hsql
     * @return
     */
    protected <T> List<T> getObjects(final String hsql,final int pageNo, final int pageSize) {
        
        return super.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hsql);
				// 设置取出的第一条索引
				query.setFirstResult((pageNo-1)*pageSize);
				// 设置取出得最大的索引
				query.setMaxResults(pageNo*pageSize);
				return (List<T>) query.list();
			}
        	
		});
    }

    /**
     *
     * 直接使用原生的SQL查询
     *
     * @param sql
     * @param cls
     * @return
     */
    protected List<?> getObjectsBySQL(final String sql, final Class<?> cls, final Map<Serializable, Serializable> map) throws Exception{
        List<?> objects = null;
        
        objects=getHibernateTemplate().execute(new HibernateCallback<List<?>>() {

			@Override
			public List<?> doInHibernate(Session session)
					throws HibernateException, SQLException {
				List<?> objects = null;
				if (session != null) {
					Query query;
					if(cls != null){
						query = session.createSQLQuery(sql).addEntity(cls);
					}
					else
					{
						query = session.createSQLQuery(sql);
					}
					if (map!=null&&map.size()!=0) {
						Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
						for (Entry<Serializable, Serializable> entry : entrys) {
							query.setParameter(entry.getKey().toString(), entry.getValue());
						}
					} 
					objects = query.setCacheable(true).list();
				}
				return objects;
			}
		});
        return objects;
    }

    /**
     *
     * 获取符合查询条件的一个对象，注意可以使用聚集
     * @param <T>
     *
     * @param hsql
     * @return
     */
    protected Object getObject(final String hsql) throws Exception{
        Object object = null;
        object=getHibernateTemplate().execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				// TODO Auto-generated method stub
				return session.createQuery(hsql).setCacheable(true).uniqueResult();
			}
		});
        return object;
    }

    /**
     * 根据ID获得指定类型的对象
     *
     * @param cls
     * @param id
     * @return
     */
    protected <T> T getObject(final Class<T> cls, final Integer id) throws Exception{
    	HibernateCallback<T> hibernateCallback=new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session)
					throws HibernateException, SQLException {
				return (T) session.get(cls, id);
			}
		};
    	return getHibernateTemplate().execute(hibernateCallback);
    }

    /**
     * 批量更新或删除
     *
     * @param hsql
     */
    protected Integer execute(final String hsql,final Map<Serializable, Serializable> map) {
    	HibernateCallback<Integer> hibernateCallback=new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hsql);
				if (map != null) {
					for (Serializable key : map.keySet()) {
						query.setParameter((String) key, map.get(key));
					}
				}
				return query.executeUpdate();
			}
		};
    	return getHibernateTemplate().execute(hibernateCallback);
    }

    /**
     * 多条件查询
     *
     * @param hql
     * @param map 包含参数键值对的map
     * @return 
     *//*
    protected <T> List<T> findListByParameter(String hql, Map<Serializable, Serializable> map) throws Exception{
        if (null == hql || "".equals(hql)) {
            return null;
        }
        List objects = null;
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();        
        Query query = session.createQuery(hql);
        
		if (map!=null&&map.size()!=0) {
			Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
			for (Entry<Serializable, Serializable> entry : entrys) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
        objects = query.setCacheable(true).list();
        transaction.commit();
        
        return objects;
    }
    *//**
     * 多条件查询
     * @param <T>
     *
     * @param hql
     * @param map 包含参数键值对的map
     * @return
     */
    protected <T> T findObjectByParameter(final String hql, final Map<Serializable, Serializable> map) throws Exception{
    	HibernateCallback<T> hibernateCallback=new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (map != null) {
					for (Serializable key : map.keySet()) {
						query.setParameter((String) key, map.get(key));
					}
				}
				return (T) query.uniqueResult();
			}
		};
    	return getHibernateTemplate().execute(hibernateCallback);
    }
    
        /**
     * 多条件查询
     *
     * @param hql
     * @param map 包含参数键值对的map
     * @return
     *//*
    protected Object findObjectByParameterSession(String hql, Map<Serializable, Serializable> map, Session mySession) throws Exception{
    	if (null == hql || "".equals(hql)) {
    		return null;
    	}
        Object obj =null;         
    	Query query = mySession.createQuery(hql);
		if (map!=null&&map.size()!=0) {
			Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
			for (Entry<Serializable, Serializable> entry : entrys) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
        obj = query.setCacheable(true).setMaxResults(1).uniqueResult();
    	return obj;
    }
    
    *//**
     * 多条件查询
     *
     * @param hql
     * @param map 包含参数键值对的map
     * @firstResult 从第几条开始
     * @maxResults  选择多少条
     * @return 
     *//*
    protected <T> List<T> findListByParameter(String hql, Map<Serializable, Serializable> map,
                                                int firstResult, int maxResults) throws Exception{
        if (null == hql || "".equals(hql)) {
            return null;
        }
        List objects = null;
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();        
        Query query = session.createQuery(hql);
        
		if (map!=null&&map.size()!=0) {
			Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
			for (Entry<Serializable, Serializable> entry : entrys) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
        objects = query.setCacheable(true).setFirstResult(firstResult).setMaxResults(maxResults).list();
        transaction.commit();
        
        return objects;
    }    

        *//**
     * 多条件查询
     *
     * @param hql
     * @param map 包含参数键值对的map
     * @param session
     * @return 
     *//*
    protected <T> List<T> findListByParameterSession(String hql, Map<Serializable, Serializable> map,Session mySession) throws Exception{
        if (null == hql || "".equals(hql)) {
            return null;
        }
        List objects = null; 
        Query query = mySession.createQuery(hql);
        
        if (map!=null&&map.size()!=0) {
                Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
                for (Entry<Serializable, Serializable> entry : entrys) {
                        query.setParameter(entry.getKey().toString(), entry.getValue());
                }
        }
        objects = query.setCacheable(true).list();
        
        return objects;
    }
    
    
    *//**
     * 多条件查询
     *
     * @param sql
     * @param map 包含参数键值对的map
     * @firstResult 从第几条开始
     * @maxResults  选择多少条
     * @return 
     *//*
    protected <T> List<T> SqlListByParameter(String sql, Map<Serializable, Serializable> map,
                                                Map<Serializable, Serializable> mapType,
                                                int firstResult, int maxResults) throws Exception{
        if (null == sql || "".equals(sql)) {
            return null;
        }
        List objects = null;
        session = HibernateUtil.getSession();
        transaction = session.beginTransaction();        
        SQLQuery query = session.createSQLQuery(sql);                    
  
		if (mapType!=null&&map.size()!=0) {
			Set<Entry<Serializable, Serializable>> entrys = mapType.entrySet();
			for (Entry<Serializable, Serializable> entry : entrys) {
				query.addScalar(entry.getKey().toString(), (Type) entry.getValue());
			}
		}
                
		if (map!=null&&map.size()!=0) {
			Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
			for (Entry<Serializable, Serializable> entry : entrys) {
				query.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
                              
        objects = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                  .setCacheable(true).setFirstResult(firstResult).setMaxResults(maxResults).list();
        transaction.commit();
        
        return objects;
    }      */

	
	/**
	 * 分页查询
	 * @param hql
	 * @param currentPage 当前页
	 * @param pageSize	  页大小
	 * @param objects
	 * @return
	 */
	public List queryByPage(
			final String hql,
			final Integer currentPage,
			final Integer pageSize,
			final Object ...objects){
		return this.getHibernateTemplate().execute(new HibernateCallback<List>() {
			
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createQuery(hql);
				if(pageSize!=null && currentPage!=null){
					query.setMaxResults(pageSize);
					query.setFirstResult((currentPage-1) * pageSize);
				}
				if(objects!=null){
					for (int i = 0; i < objects.length; i++) {
						query.setParameter(i, objects[i]);
					}
				}
				
				return query.list();
			}
		});
	}
	
	/**
	 * 查询全部
	 * @param hql
	 * @param objects
	 * @return
	 */
	public List queryByHQL(String hql,
			Object ...objects){
		return this.queryByPage(hql, null, null, objects);
	}
	
	/**
	 * 单一查询
	 * @param hql
	 * @param objects
	 * @return
	 */
	public Object queryByUnique(String hql,
			Object ...objects){
		List list=this.queryByHQL(hql,objects);
		return list!=null && list.size()>0?list.get(0):null;
		
	}
	/**
	 * 单一查询ById
	 * @param hql
	 * @param objects
	 * @return<T> T getObject(Object src,Class<T> cls){
	 */
	public <T> T queryByIdque(Class<T> cls,String T,Integer id){
		String hql="select u from "+T+" u where u.id =? ";
		List list=this.queryByHQL(hql,id);
		return (T) (list!=null && list.size()>0?list.get(0):null);
		
	}

	/**
	 * 修改
	 * @param hql
	 * @param objects
	 * @return
	 */
	public Object update(final String hql,
			final Object ...objects){
		return this.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createQuery(hql);
				if(objects!=null){
					for (int i = 0; i < objects.length; i++) {
						query.setParameter(i, objects[i]);
					}
				}
				return query.executeUpdate();
			}
		});
	}

	public String pinjieString(Integer sizes,String str,String hql){
		hql+=" and (";
		for (int i = 0; i <sizes; i++) {
			if (i==sizes-1) {
				hql+=str+"=?";
			}else hql+=str+"=? or ";
		}
		hql	+=")";
		return hql;
	}
//	 /**
//    *
//    * 直接使用原生的SQL查询
//    *
//    * @param sql
//    * @param cls
//    * @return
//    */
//   protected List getObjectsBySQL(String sql, Class<?> cls, Map<Serializable, Serializable> map) throws Exception{
//       List objects = null;
//       session = HibernateUtil.getSession();
//       if (session != null) {
//           transaction = session.beginTransaction();
//           Query query;
//           if(cls != null){
//               query = session.createSQLQuery(sql).addEntity(cls);
//           }
//           else
//           {
//               query = session.createSQLQuery(sql);
//           }
//           
//           if (map!=null&&map.size()!=0) {
//                   Set<Entry<Serializable, Serializable>> entrys = map.entrySet();
//                   for (Entry<Serializable, Serializable> entry : entrys) {
//                           query.setParameter(entry.getKey().toString(), entry.getValue());
//                   }
//           } 
//           objects = query.setCacheable(true).list();
//           transaction.commit();
//
//       }
//       return objects;
//   }
   /**
	 * 分页查询
	 * @param sql
	 * @param currentPage 当前页
	 * @param pageSize	  页大小
	 * @param objects
	 * @return
	 */
	public List queryByPageSql(
			final String sql,
			final Class<?> cls,
			final Integer currentPage,
			final Integer pageSize,
			final Object ...objects){
		return this.getHibernateTemplate().executeFind(
				new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query=session.createSQLQuery(sql);
				if(pageSize!=null && currentPage!=null){
					query.setMaxResults(pageSize);
					query.setFirstResult((currentPage-1) * pageSize);
				}
				if(objects!=null){
					for (int i = 0; i < objects.length; i++) {
						query.setParameter(i, objects[i]);
					}
				}
				
				return query.list();
			}
		});
	}
	/**
	 * 查询全部
	 * @param hql
	 * @param objects
	 * @return
	 */
	public List queryBySQL(String sql,Class<?> cls,
			Object ...objects){
		return this.queryByPageSql(sql,cls, null, null, objects);
	}

	/**
	 * 根据参数获取值
	 * @param sql
	 * @param map
	 * @return
	 */
	public <T> List<T> findListByParameter(final String hql, final Map<Serializable, Serializable> map) {
		if (null == hql || "".equals(hql)) {
			return null;
		}
		return this.getHibernateTemplate().execute(new HibernateCallback<List<T>>() {

			@Override
			public List<T> doInHibernate(final Session session) throws HibernateException,
				SQLException {
				final Query query = session.createQuery(hql);
				for (Serializable key : map.keySet()) {
					query.setParameter((String) key, map.get(key));
				}
				return query.list();
			}
		});
	}


}
