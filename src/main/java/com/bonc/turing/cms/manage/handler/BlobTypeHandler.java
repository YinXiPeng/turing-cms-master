package com.bonc.turing.cms.manage.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * description:
 *
 * @author lxh
 * @date 2020/4/27 19:55
 */
@MappedJdbcTypes(JdbcType.BLOB)
@MappedTypes(value = {String[].class})
public class BlobTypeHandler<T> extends BaseTypeHandler<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobTypeHandler.class);

    private static ObjectMapper objectMapper;
    private Class<T> type;

    static {
        objectMapper = new ObjectMapper();
    }

    public BlobTypeHandler(Class<T> type) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("BlobTypeHandler(" + type + ")");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int columnIndex, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(columnIndex, toBytes(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getBytes(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getBytes(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getBytes(columnIndex));
    }

    private T parse(byte[] bytes) {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bin);
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(ois)) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private byte[] toBytes(Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(oos)) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bos.toByteArray();
    }

}
