package com.powerinbox.bern.webservice.dao;

import com.powerinbox.bern.webservice.model.ResponseObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceResponseObjectRowMapper implements RowMapper<ResponseObject> {

    @Override
    public ResponseObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        ResponseObject object = new ResponseObject();
        object.setDevice(rs.getString("device"));
        object.setValue(rs.getBigDecimal(2));
        object.setCount(rs.getInt(3));

        return object;
    }

}
