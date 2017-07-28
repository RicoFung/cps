package dao;

import org.springframework.stereotype.Repository;

import chok.devwork.BaseDao;
import entity.User;


@Repository
public class UserDao extends BaseDao<User,Long>
{
	@Override
	public Class<User> getEntityClass()
	{
		return User.class;
	}

	public User getByTcCode(String tcCode)
	{
		return this.getSqlSession().selectOne(getStatementName("getByTcCode"), tcCode);
	}
	
	public void updPwd(User po)
	{
		this.getSqlSession().update(getStatementName("updPwd"), po);
	}
}
