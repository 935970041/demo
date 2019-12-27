package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description: 数据库操作工具类封装1.0.0
 * @date:2018年8月16日 上午9:42:41
 * @author:ZhangZY
 */
@Component("com.example.demo.utils.DBUtils")
public class DBUtils {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 组装返回的数据
    public String assembleData(int count) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[2];
        String msg = "";
        String methodSub = stack.getMethodName().substring(0, 2);
        JSONObject jsonObject = new JSONObject();
        if (count > 0) {
            if (methodSub.equals("in")) {
                msg = "添加成功";
            }
            if (methodSub.equals("up")) {
                msg = "修改成功";
            }
            if (methodSub.equals("de")) {
                msg = "删除成功";
            }
            jsonObject.put("success", true);
        } else {
            if (methodSub.equals("in")) {
                msg = "添加失败";
            }
            if (methodSub.equals("up")) {
                msg = "修改失败";
            }
            if (methodSub.equals("de")) {
                msg = "删除失败";
            }
            jsonObject.put("success", false);
        }
        jsonObject.put("msg", msg);
        return jsonObject.toString();

    }

    // 获取Id
    public String generateSysId() {
        String sql = " select DATE_FORMAT(NOW(6), '%y%m%d%H%i%S%f') as id  ";
        Map<String, Object> idString = jdbcTemplate.queryForMap(sql.toString());
        return idString.get("id").toString();

    }

    // 插入
    public <T> String insertPo(T t) {
        return assembleData(au(t, getInsertSql(t)));
    }

    // 批量插入,将批量插入的sql语句封装完毕进行传入
    // 如果单纯的多条insert,如果数据量大，那么会比较慢
    public int insertPoBatch(String sql) {
        return jdbcTemplate.update(sql);
    }

    // 修改po,默认是使用id进行修改,如果确实是不想显示某一项的值,那就将该属性的值赋值成""进来
    public <T> String updatePo(T t) {
        return updatePo(t, null);
    }

    // 根据传入的hashMap中的键找到数据库对应的字段进行修改
    public <T> String updatePo(T t, HashMap<String, Object> map) {
        return assembleData(au(t, getUpdateSql(t, map)));
    }

    // 根据单一Id删除
    public <T> String deletePo(T t) {
        return deletePo(t, null);
    }

    // 批量删除
    public <T> String deletePo(T t, List<String> ids) {
        return assembleData(jdbcTemplate.update(getDeleteSql(t, ids)));
    }

    // 查询单个po,使用id进行查询
    public <T> T queryPoById(T t) {
        String tableName = queryTableName(t);
        Method method = null;
        try {
            method = t.getClass().getMethod(getGetter("id"));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        Object id = null;
        try {
            id = method.invoke(t);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String sql = "select * from " + tableName + " where  id=" + "\'" + id + "\'";
        return JsonUtils.MapToBean(t, jdbcTemplate.queryForMap(sql));
    }

    // 查询单个po,使用id进行查询
    public <T> Map<String, Object> queryMapById(T t) {
        String tableName = queryTableName(t);
        Method method = null;
        try {
            method = t.getClass().getMethod(getGetter("id"));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        Object id = null;
        try {
            id = method.invoke(t);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        String sql = "select * from " + tableName + " where  id=" + "\'" + id + "\'";
        return jdbcTemplate.queryForMap(sql);
    }

    // 根据参数查询多个值
    public <T> List<T> queryListBean(T t) {
        List<Object> listValues = new ArrayList<>();
        String sql = getQuerySql(t);
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                // 填坑
                if (fields[i].get(t) != null) {
                    String fieldName = fields[i].getName();
                    if ("serialVersionUID".equals(fields[i].getName())) {
                        continue;
                    }
                    Method method = t.getClass().getMethod(getGetter(fieldName));
                    Object obj = method.invoke(t);
                    listValues.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object[] objects = new Object[listValues.size()];
        listValues.toArray(objects);
        return JsonUtils.ListMapToListBean(t, jdbcTemplate.queryForList(sql, objects));
    }

    // 根据参数查询多个值
    public <T> List<Map<String, Object>> queryListMap(T t) {
        List<Object> listValues = new ArrayList<>();
        String sql = getQuerySql(t);
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                // 填坑
                if (fields[i].get(t) != null) {
                    String fieldName = fields[i].getName();
                    if ("serialVersionUID".equals(fields[i].getName())) {
                        continue;
                    }
                    Method method = t.getClass().getMethod(getGetter(fieldName));
                    Object obj = method.invoke(t);
                    listValues.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] objects = new Object[listValues.size()];
        listValues.toArray(objects);
        return jdbcTemplate.queryForList(sql, objects);
    }

    // 进行单表增改的统一操作
    private <T> int au(T t, String sql) {
        Field[] fields = t.getClass().getDeclaredFields();
        return jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int c = 1;
                // 填坑
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    // 填坑
                    try {
                        if (fields[i].get(t) != null) {
                            String fieldName = fields[i].getName();
                            if ("serialVersionUID".equals(fields[i].getName())) {
                                continue;
                            }
                            Method method = null;
                            try {
                                method = t.getClass().getMethod(getGetter(fieldName));
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();

                            } catch (SecurityException e) {
                                e.printStackTrace();

                            }
                            Object obj = method.invoke(t);
                            ps.setObject(c, obj);
                            fields[i].setAccessible(false);
                            c++;
                        }
                    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
                            | SecurityException e) {
                        e.printStackTrace();

                    }
                }
            }
        });
    }

    // 获取添加的sql,传入一个 类进来,那么解析的顺序就是一样的
    private <T> String getInsertSql(T t) {
        String tableName = queryTableName(t);
        StringBuilder sql = new StringBuilder("insert into " + tableName + " ( ");
        StringBuilder param = new StringBuilder();
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                // 值为空的属性不会拼接到sql语句上面
                if (fields[i].get(t) != null && !"serialVersionUID".equals(fields[i].getName())) {
                    sql.append(fields[i].getName() + ",");
                    param.append("?,");
                }
            }
            sql = new StringBuilder(sql.substring(0, sql.length() - 1)).append(" ) values( ").append(param);
            sql = sql.deleteCharAt(sql.length() - 1).append(")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql.toString();
    }

    // 获取修改的sql语句
    private <T> String getUpdateSql(T t, HashMap<String, Object> map) {
        String tableName = queryTableName(t);
        StringBuilder sql = new StringBuilder("update " + tableName + " set ");
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(t) != null && !"serialVersionUID".equals(fields[i].getName())) {
                    sql.append(fields[i].getName() + "=?,");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql.delete(sql.length() - 1, sql.length());
        if (map == null || map.size() == 0) {
            Object idValue = null;
            try {
                Method method = t.getClass().getMethod(getGetter("id"));
                idValue = method.invoke(t);
                sql.append(" where id =" + "\'" + idValue + "\'");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            sql.append(" where ");
            map.forEach((k, v) -> {
                sql.append(k + "=" + "\'" + v + "\'" + " and ");
            });
            sql.delete(sql.length() - 4, sql.length());
        }
        return sql.toString();
    }

    // 获取删除的sql语句
    private <T> String getDeleteSql(T t, List<String> ids) {
        String tableName = queryTableName(t);
        StringBuilder sql = new StringBuilder("delete from " + tableName + " where id ");
        if (ids == null || ids.size() == 0) {
            Object idValue = null;
            try {
                Method method = t.getClass().getMethod(getGetter("id"));
                idValue = method.invoke(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sql.append("=" + idValue);
        } else {
            sql.append("in ( ");
            for (int i = 0; i < ids.size(); i++) {
                sql.append("\'" + ids.get(i) + "\'" + ",");
            }
            sql.delete(sql.length() - 1, sql.length());
            sql.append(" )");
        }
        return sql.toString();
    }

    // 获取查询的sql语句
    private <T> String getQuerySql(T t) {
        String tableName = queryTableName(t);
        String sql = "select * from " + tableName + "  where 1=1 ";
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(t) != null && !"serialVersionUID".equals(fields[i].getName())) {
                    sql += " and " + fields[i].getName() + "=?";
                }
            }
            sql = sql.substring(0, sql.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sql;
    }

    // 根据类获取对应的表名称
    private <T> String queryTableName(T t) {
        String simpleName = t.getClass().getSimpleName();
        StringBuilder s = new StringBuilder();
        simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);

        char[] ch = simpleName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                s.append("_");
                s.append(Character.toLowerCase(ch[i]));
            } else {
                s.append(ch[i]);
            }

        }
        return s.toString();
    }

    // 根据传入的属性名字获取对应的get方法
    private String getGetter(String fieldName) {
        // 传入属性名 拼接get方法
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

    }

}