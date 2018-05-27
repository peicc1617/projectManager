package dao;

import java.sql.*;
import java.util.List;

@Deprecated
 public interface IBaseDao<T>{
    //添加
    public T add(T t) throws SQLException, IllegalAccessException;
    //删除
    public void delete(int id) throws SQLException;
    //更新
    public void update(T t) throws Exception;

    //根据id查询
    public T findOne(int id);

    public  List<T> doQuery(String sql,List<Object> params);

    //查询所有
    public List<T> findAll();

}