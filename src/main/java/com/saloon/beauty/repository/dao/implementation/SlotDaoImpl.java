package com.saloon.beauty.repository.dao.implementation;

import com.saloon.beauty.repository.DBUtils;
import com.saloon.beauty.repository.DaoException;
import com.saloon.beauty.repository.dao.SlotDao;
import com.saloon.beauty.repository.dao.UserDao;
import com.saloon.beauty.repository.entity.Slot;
import com.saloon.beauty.repository.entity.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementing of SlotDao for working with a SQL server
 */
public class SlotDaoImpl implements SlotDao {

    private static final Logger LOG = LogManager.getLogger(UserDao.class);

    private Connection connection;

    public SlotDaoImpl(Connection connection) {
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSlotSearchResultCount(long masterId, Status status, long userId, long procedureId,
                                         LocalDate minDate, LocalDate maxDate,
                                         LocalTime minTime, LocalTime maxTime,
                                         boolean feedbackIsPresent) {
        try {
            PreparedStatement statement = getPreparedAllSlotStatement(masterId, status, userId, procedureId, minDate, maxDate,
                    minTime, maxTime, feedbackIsPresent, Integer.MAX_VALUE, 0, true);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            String errorText = "Can't get slots count in search result. Cause: " + e.getMessage();
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Slot> getAllSlotParameterized(long masterId, Status status, long userId, long procedureId,
                                              LocalDate minDate, LocalDate maxDate,
                                              LocalTime minTime, LocalTime maxTime,
                                              boolean feedbackIsPresent,
                                              int limit, int offset) {

        List<Slot> slots = new ArrayList<>();

        try {
            PreparedStatement statement = getPreparedAllSlotStatement(masterId, status, userId, procedureId, minDate, maxDate,
                    minTime, maxTime, feedbackIsPresent, limit, offset, false);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                slots.add(getSlotFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get slots list from DB. Cause: " + e.getMessage();
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return slots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Slot> getSlotByStatusFeedbackRequest(boolean status) {

        List<Slot> slots = new ArrayList<>();

        try {
            PreparedStatement statement = connection
                    .prepareStatement(DBQueries.GET_SLOT_BY_FEEDBACK_REQUEST_STATUS_QUERY);
            statement.setBoolean(1, status);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                slots.add(getSlotFromResultRow(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorText = "Can't get slots list from DB by feedback request status . Cause: " + e.getMessage();
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
        return slots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSlotStatus(long id, Status status, long userId) {
        try {
            PreparedStatement updateStatement = connection.prepareStatement(DBQueries.UPDATE_SLOT_STATUS_QUERY);
            updateStatement.setString(1, status.name());
            if (userId == 0) {
                updateStatement.setNull(2, Types.INTEGER);
            } else {
                updateStatement.setLong(2, userId);
            }
            updateStatement.setLong(3, id);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update slot status. User id: %s. Cause: %s", id, e.getMessage());
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFeedbackRequestStatus(long slotId, boolean status) {
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(DBQueries.UPDATE_SLOT_FEEDBACK_REQUEST_STATUS_QUERY);
            updateStatement.setBoolean(1, status);
            updateStatement.setLong(2, slotId);

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update slot feedback request status: %s. Slot id: %s Cause: %s", status, slotId, e.getMessage());
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Slot> get(long id) {
        Optional<Slot> resultOptional = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(DBQueries.GET_SLOT_QUERY);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                resultOptional = Optional.of(getSlotFromResultRow(resultSet));
            }

            resultSet.close();

        } catch (SQLException e) {
            String errorText = String.format("Can't get an slot by id: %s. Cause: %s.", id, e.getMessage());
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
        return resultOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Slot> getAll() {
        List<Slot> users = new ArrayList<>();

        try {
            PreparedStatement selectStatement = connection
                    .prepareStatement(DBQueries.GET_ALL_SLOT_QUERY);

            ResultSet resultSet = selectStatement.executeQuery();

            while (resultSet.next()) {
                users.add(getSlotFromResultRow(resultSet));
            }

            resultSet.close();

        } catch (SQLException e) {
            String errorText = "Can't get slots list from DB. Cause: " + e.getMessage();
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }

        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long save(Slot slot) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(DBQueries.SAVE_SLOT_QUERY, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setDate(1, Date.valueOf(slot.getDate()));
            insertStatement.setTime(2, Time.valueOf(slot.getStartTime()));
            insertStatement.setTime(3, Time.valueOf(slot.getEndTime()));
            insertStatement.setLong(4, slot.getMaster());
            if (slot.getUser() == 0) {
                insertStatement.setNull(5, Types.INTEGER);
            } else {
                insertStatement.setLong(5, slot.getUser());
            }
            insertStatement.setString(6, slot.getStatus().name());
            insertStatement.setLong(7, slot.getProcedure());

            insertStatement.executeUpdate();

            return DBUtils.getIdFromStatement(insertStatement);

        } catch (SQLException e) {
            if (DBUtils.isTryingToInsertDuplicate(e)) {
                return -1;
            } else {
                String errorText = String.format("Can't save slot: %s. Cause: %s", slot, e.getMessage());
                LOG.error(errorText, e);
                throw new DaoException(errorText, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Slot slot) {
        try {
            PreparedStatement updateStatement = connection
                    .prepareStatement(DBQueries.UPDATE_SLOT_INFO_QUERY);
            updateStatement.setDate(1, Date.valueOf(slot.getDate()));
            updateStatement.setTime(2, Time.valueOf(slot.getStartTime()));
            updateStatement.setTime(3, Time.valueOf(slot.getEndTime()));
            updateStatement.setLong(4, slot.getMaster());
            if(slot.getUser()!=0) {
                updateStatement.setLong(5, slot.getUser());
            }else{
                updateStatement.setNull(5, Types.INTEGER);
            }
            updateStatement.setString(6, slot.getStatus().name());
            updateStatement.setLong(7, slot.getProcedure());
            updateStatement.setBoolean(8, slot.isFeedbackRequest());
            updateStatement.setLong(9, slot.getId());

            updateStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't update slot: %s. Cause: %s", slot, e.getMessage());
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Slot slot) {
        try {
            PreparedStatement deleteStatement = connection
                    .prepareStatement(DBQueries.DELETE_SLOT_QUERY);
            deleteStatement.setLong(1, slot.getId());

            deleteStatement.execute();

        } catch (SQLException e) {
            String errorText = String.format("Can't delete slot: %s. Cause: %s", slot, e.getMessage());
            LOG.error(errorText, e);
            throw new DaoException(errorText, e);
        }
    }

    /**
     * Gives {@code PreparedStatement} depending on data
     * existence in every of methods argument
     *
     * @param masterId     - the master'd ID
     * @param status       - the slot status
     * @param minDate      - minimum boundary of searching by date
     * @param maxDate      - maximum boundary of searching by date
     * @param minTime      - minimum boundary of searching by time
     * @param maxTime      - maximum boundary of searching by time
     * @param feedbackIsPresent - does slot has feedback
     * @param limit        - the number of slots returned
     * @param offset       - the number of slots returned
     * @param rowsCounting - defines type of result {@code Statement}.
     *                       If {@code rowsCounting} is {true} statement
     *                       will return count of all target slots.
     *                       It will return only limited count of slots otherwise.
     * @return - statement for getting slots information from DB
     */
    private PreparedStatement getPreparedAllSlotStatement(long masterId, Status status, long userId, long procedureId,
                                                          LocalDate minDate, LocalDate maxDate,
                                                          LocalTime minTime, LocalTime maxTime,
                                                          boolean feedbackIsPresent,
                                                          int limit, int offset, boolean rowsCounting) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder();
        boolean anotherParameter = false;

        if (rowsCounting) {
            queryBuilder.append(DBQueries.ALL_SLOTS_COUNT_QUERY_HEAD_PART);
        } else {
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_HEAD_PART);
        }

        if (feedbackIsPresent){
            queryBuilder.append(" ").append(DBQueries.ALL_SLOTS_QUERY_FEEDBACK_PART);
        }

        if (masterId > 0 || status != null || userId > 0 || procedureId > 0 || minDate != null ||
                maxDate != null || minTime != null || maxTime != null) {
            queryBuilder.append(" WHERE");
        }

        if (masterId > 0) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_MASTER_PART);

        }

        if (status != null) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_STATUS_PART);
        }

        if (userId > 0) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_USER_PART);

        }

        if(procedureId>0){
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_PROCEDURE_PART);
        }

        if (minDate != null) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_MIN_DATE_PART);
        }
        if (maxDate != null) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_MAX_DATE_PART);
        }

        if (minTime != null) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            anotherParameter = true;
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_MIN_TIME_PART);
        }
        if (maxTime != null) {
            if (anotherParameter) {
                queryBuilder.append(" AND ");
            } else {
                queryBuilder.append(" ");
            }
            queryBuilder.append(DBQueries.ALL_SLOTS_QUERY_MAX_TIME_PART);
        }

        if (rowsCounting) {
            queryBuilder.append(" ").append(DBQueries.ALL_SLOTS_COUNT_QUERY_TAIL_PART);
        } else {
            queryBuilder.append(" ").append(DBQueries.ALL_SLOTS_QUERY_TAIL_PART);
        }

        PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

        int parameterIndex = 1;
        if (masterId > 0) {
            statement.setLong(parameterIndex++, masterId);
        }

        if (status != null) {
            statement.setString(parameterIndex++, status.name());
        }

        if(userId>0){
            statement.setLong(parameterIndex++, userId);
        }

        if(procedureId>0){
            statement.setLong(parameterIndex++, procedureId);
        }

        if (minDate != null) {
            statement.setDate(parameterIndex++, Date.valueOf(minDate));
        }
        if (maxDate != null) {
            statement.setDate(parameterIndex++, Date.valueOf(maxDate));
        }

        if (minTime != null) {
            statement.setTime(parameterIndex++, Time.valueOf(minTime));
        }
        if (maxTime != null) {
            statement.setTime(parameterIndex++, Time.valueOf(maxTime));
        }

        if (!rowsCounting) {
            statement.setInt(parameterIndex++, limit);
            statement.setInt(parameterIndex, offset);
        }


        return statement;
    }



    /**
     * Creates an slot from given {@code ResultSet}
     *
     * @param resultSet - {@code ResultSet} with slot data
     * @return - slot was passed into
     * @throws SQLException if the columnLabels is not valid;
     *                      if a database access error occurs or result set is closed
     */
    Slot getSlotFromResultRow(ResultSet resultSet) throws SQLException {
        return Slot.builder()
                .id(resultSet.getLong("slot_id"))
                .date(resultSet.getObject("date", LocalDate.class))
                .startTime(resultSet.getObject("start_time", LocalTime.class))
                .endTime(resultSet.getObject("end_time", LocalTime.class))
                .master(resultSet.getLong("master"))
                .user(resultSet.getLong("user"))
                .status(Status.valueOf(resultSet.getString("status")))
                .procedure(resultSet.getLong("procedure"))
                .feedbackRequest(resultSet.getBoolean("feedback_request"))
                .build();
    }
}
