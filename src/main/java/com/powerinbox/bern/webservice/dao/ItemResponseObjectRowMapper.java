package com.powerinbox.bern.webservice.dao;

import com.powerinbox.bern.webservice.model.ResponseObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemResponseObjectRowMapper implements RowMapper<ResponseObject> {

    @Override
    public ResponseObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResponseObject object = new ResponseObject();
        object.setItemId(rs.getInt("item_id"));
        object.setValue(rs.getBigDecimal(2));
        object.setCount(rs.getInt(3));

        return object;
    }
}
