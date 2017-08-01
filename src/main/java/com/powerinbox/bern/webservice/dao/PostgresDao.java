package com.powerinbox.bern.webservice.dao;

import com.powerinbox.bern.webservice.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Repository
public class PostgresDao {

    final static Logger logger = LoggerFactory.getLogger(PostgresDao.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Resource(name = "postgresDataSource")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * SQL to upsert country information
     * If timestamp, country combination exists,
     * increase count and value by the amounts in current
     * object. Otherwise insert.
     *
     * @return
     */
    private String upsertCountrySql() {
        String sql = "INSERT INTO country_table(time_val, country, count_val, value_val) "
                + "values (:timestamp, :country, :count, :value) ON CONFLICT(time_val, country) "
                + "do update set (value_val, count_val) = (country_table.value_val + excluded.value_val, country_table.count_val + excluded.count_val);";
        return sql;
    }

    public void upsertCountry(CountryInfo countryInfo) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(countryInfo);
        try {
            jdbcTemplate.update(upsertCountrySql(), namedParams);
        } catch (DataAccessException e) {
            // catch first exception and retry insert/update operation
            logger.warn("DataAccessException trying to insert/update: " + countryInfo.toString(), e.getMessage());
            try {
                logger.warn(("Retrying"));
                jdbcTemplate.update(upsertCountrySql(), namedParams);
            } catch (DataAccessException e1) {
                logger.error("error insert/updating", e);
            }
        }
    }

    private String upsertItemSql() {
        String sql = "INSERT INTO item_table(time_val, item_id, count_val, value_val) "
                + "values (:timestamp, :itemId, :count, :value) ON CONFLICT (time_val, item_id) "
                + "do update set (value_val, count_val) = (item_table.value_val + excluded.value_val, item_table.count_val + excluded.count_val);";
        return sql;
    }

    public void upsertItem(ItemInfo itemInfo) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(itemInfo);
        try {
            jdbcTemplate.update(upsertItemSql(), namedParams);
        } catch (DataAccessException e) {
            logger.warn("DataAccessException trying to insert/update: " + itemInfo.toString(), e.getMessage());
            try {
                logger.warn(("Retrying"));
                jdbcTemplate.update(upsertItemSql(), namedParams);
            } catch (DataAccessException e1) {
                logger.error("error insert/updating", e);
            }
        }
    }

    private String upsertDeviceSql() {
        String sql = "INSERT INTO device_table(time_val, device, count_val, value_val) "
                + "values (:timestamp, :device, :count, :value) ON CONFLICT (time_val, device) "
                + "do update set (value_val, count_val) = (device_table.value_val + excluded.value_val, device_table.count_val + excluded.count_val);";
        return sql;
    }

    public void upsertDevice(DeviceInfo deviceInfo) {
        SqlParameterSource namedParams = new BeanPropertySqlParameterSource(deviceInfo);
        try {
            jdbcTemplate.update(upsertDeviceSql(), namedParams);
        } catch (DataAccessException e) {
            logger.warn("DataAccessException trying to insert/update: " + deviceInfo.toString(), e.getMessage());
            try {
                logger.warn(("Retrying"));
                jdbcTemplate.update(upsertDeviceSql(), namedParams);
            } catch (DataAccessException e1) {
                logger.error("error insert/updating", e);
            }
        }
    }

    /**
     * SELECT statement to get total counts and values by country
     *
     * @param startTime
     * @param endTime
     * @return String sql
     */
    private String queryCountrySql(long startTime, long endTime) {
        String sql = "SELECT country, SUM(value_val), SUM(count_val) "
                + "FROM country_table "
                + "WHERE time_val >= " + String.valueOf(startTime) + " AND time_val < " + String.valueOf(endTime) + " "
                + "GROUP BY country;";
        return sql;
    }

    public List<ResponseObject> getCountryResults(QueryObject queryObject) {
        List<ResponseObject> results = new ArrayList<>();
        logger.debug("querying for country info");
        // uses CountryResponseObjectRowMapper to map each row to Response Object
        results.addAll(jdbcTemplate.query(queryCountrySql(queryObject.getStartUnix(), queryObject.getEndUnix()), new CountryResponseObjectRowMapper()));
        return results;
    }

    /**
     * SELECT statement to get total counts and values by item_id
     *
     * @param startTime
     * @param endTime
     * @return String
     */
    private String queryItemSql(long startTime, long endTime) {
        String sql = "SELECT item_id, SUM(value_val), SUM(count_val) "
                + "FROM item_table "
                + "WHERE time_val >= " + String.valueOf(startTime) + " AND time_val < " + String.valueOf(endTime) + " "
                + "GROUP BY item_id;";
        return sql;
    }

    public List<ResponseObject> getItemResults(QueryObject queryObject) {
        List<ResponseObject> results = new ArrayList<>();
        logger.debug("querying for item info");
        results.addAll(jdbcTemplate.query(queryItemSql(queryObject.getStartUnix(), queryObject.getEndUnix()), new ItemResponseObjectRowMapper()));
        return results;

    }

    /**
     * SELECT statement to get total counts and values by device
     *
     * @param startTime
     * @param endTime
     * @return String
     */
    private String queryDeviceSql(long startTime, long endTime) {
        String sql = "SELECT device, SUM(value_val), SUM(count_val) "
                + "FROM device_table "
                + "WHERE time_val >= " + String.valueOf(startTime) + " AND time_val < " + String.valueOf(endTime) + " "
                + "GROUP BY device;";
        return sql;
    }

    public List<ResponseObject> getDeviceResults(QueryObject queryObject) {
        List<ResponseObject> results = new ArrayList<>();
        logger.debug("querying for device info");
        results.addAll(jdbcTemplate.query(queryDeviceSql(queryObject.getStartUnix(), queryObject.getEndUnix()), new DeviceResponseObjectRowMapper()));
        return results;

    }
}
