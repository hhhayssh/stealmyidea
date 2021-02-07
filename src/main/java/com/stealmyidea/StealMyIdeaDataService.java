package com.stealmyidea;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;

import com.stealmyidea.model.Idea;

public class StealMyIdeaDataService {
	
	private static final Logger log = Logger.getLogger(StealMyIdeaDataService.class);

	/**
	 * 
	 * The object that does the talking to the database.
	 * 
	 */
	protected DataSource dataSource;
	
	protected static final String SELECT_IDEA = "select idea_id, idea_number, idea, idea_date, steal_status, steal_status_description, greatness, description, status, status_date, enter_date, modification_date " + 
												"from idea ";
	
	protected static final String INSERT_IDEA = "insert into idea (idea, idea_date, steal_status, steal_status_description, greatness, description, status, status_date, enter_date) " +
												"values (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	protected static final String UPDATE_IDEA = "update idea " + 
												"set idea = ?, " + 
													"idea_date = ?, " + 
													"steal_status = ?, " + 
													"steal_status_description = ?, " + 
													"greatness = ?, " + 
													"description = ?, " + 
													"modification_date = ?, " +
													"status = ?, " + 
													"status_date = ? " +
												"where idea_id = ? ";
	
	protected static final Map<String, String> SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP = new HashMap<String, String>();
	static {
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_IDEA_ID, "idea_id");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_IDEA_NUMBER, "idea_number");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_IDEA, "idea");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_IDEA_DATE, "idea_date");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_STEAL_STATUS, "steal_status");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_STEAL_STATUS_DESCRIPTION, "steal_status_description");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_GREATNESS, "greatness");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_DESCRIPTION, "description");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_ENTER_DATE, "enter_date");
		SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.put(StealMyIdeaConstants.SORT_FIELD_NAME_MODIFICATION_DATE, "modification_date");
	}
	
	protected static final Map<String, String> SORT_FIELD_DIRECTION_TO_SORT_COLUMN_DIRECTION_MAP = new HashMap<String, String>();
	static {
		SORT_FIELD_DIRECTION_TO_SORT_COLUMN_DIRECTION_MAP.put(StealMyIdeaConstants.SORT_DIRECTION_ASCENDING, "asc");
		SORT_FIELD_DIRECTION_TO_SORT_COLUMN_DIRECTION_MAP.put(StealMyIdeaConstants.SORT_DIRECTION_DESCENDING, "desc");
	}
	
	public StealMyIdeaDataService(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	public Idea getIdea(long ideaId) {
		
		List<Idea> ideas = getIdeas(ideaId, null, null, null, null, null, null, null, null, null, null, null, null, null);
		
		Idea idea = null;
		
		if (ideas != null && ideas.size() > 0) {
			idea = ideas.get(0);
		}
		
		return idea;
	}
	
	public Idea getIdea(String idea) {
		
		List<Idea> ideas = getIdeas(null, null, idea, null, null, null, null, null, null, null, null, null, null, null);
		
		Idea ideaObject = null;
		
		if (ideas != null && ideas.size() > 0) {
			ideaObject = ideas.get(0);
		}
		
		return ideaObject;
	}
	
	public List<Idea> getDefaultIdeas(){
		
		List<Idea> defaultIdeas = getIdeas(null, null, null, null, null, null, null, null, null, null, StealMyIdeaConstants.DEFAULT_SORT_FIELD_NAME,
				StealMyIdeaConstants.DEFAULT_SORT_DIRECTION, StealMyIdeaConstants.DEFAULT_OFFSET, StealMyIdeaConstants.DEFAULT_SIZE);
		
		return defaultIdeas;
	}
	
	public List<Idea> getIdeas(Long ideaId, Long ideaNumber, String idea, String ideaDate, String stealStatus, String stealStatusDescription, 
			String greatness, String description, Date enterDate, Date modificationDate, String sortFieldName, String sortDirection,
			Integer offset, Integer size) {
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet results = null;
		
		List<Idea> ideas = new ArrayList<Idea>();
		
		StringBuilder stringBuilder = new StringBuilder(SELECT_IDEA);
		List<Object> parameters = new ArrayList<Object>();
		
		addParameter(stringBuilder, "idea_id", ideaId, parameters, false);
		addParameter(stringBuilder, "idea_number", ideaNumber, parameters, false);
		addParameter(stringBuilder, "idea", idea, parameters, false);
		addParameter(stringBuilder, "idea_date", ideaDate, parameters, false);
		addParameter(stringBuilder, "steal_status", stealStatus, parameters, false);
		addParameter(stringBuilder, "steal_status_description", stealStatusDescription, parameters, false);
		addParameter(stringBuilder, "greatness", greatness, parameters, false);
		addParameter(stringBuilder, "description", description, parameters, false);
		addParameter(stringBuilder, "enter_date", enterDate, parameters, false);
		addParameter(stringBuilder, "modification_date", modificationDate, parameters, false);
		addParameter(stringBuilder, "status", StealMyIdeaConstants.STATUS_AVAILABLE, parameters, false);
		
		stringBuilder.append(" %s ");
		String query = stringBuilder.toString();
		
		String sortAndPaginationClause = "";
		if (sortFieldName != null && sortDirection != null) {
			String sortColumnName = getSortColumnName(sortFieldName);
			String sortColumnDirection = getSortColumnDirection(sortDirection);
			
			if (sortColumnName != null && sortColumnDirection != null) {
				sortAndPaginationClause = " order by " + sortColumnName + " " + sortColumnDirection;
			}
		}
		
		if (offset != null && size != null) {
			sortAndPaginationClause = " offset " + offset + " limit " + size;
		}
		
		String queryToUse = String.format(query, sortAndPaginationClause);
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(queryToUse);
			
			setParameterValues(statement, parameters);
			
			results = statement.executeQuery();
			
			while (results.next()){
				Idea mappedIdea = mapIdea(results);
				ideas.add(mappedIdea);
			}
		}
		catch (Exception e){
			log.error("Error getting seasons!", e);
			rollback(connection);
		}
		finally {
			close(results, statement, connection);
		}
		
		return ideas;
	}
	
	protected void setParameterValues(PreparedStatement statement, List<Object> parameterValues) throws SQLException {
		
		for (int index = 0; index < parameterValues.size(); index++) {
			int parameterIndex = index + 1;
			Object parameterValue = parameterValues.get(index);
			setParameterValue(statement, parameterIndex, parameterValue);
		}
	}
	
	protected void setParameterValue(PreparedStatement statement, int parameterIndex, Object parameterValue) throws SQLException {
		
		if (parameterValue instanceof String) {
			statement.setString(parameterIndex, (String)parameterValue);
		}
		
		if (parameterValue instanceof Integer) {
			statement.setInt(parameterIndex, (Integer)parameterValue);
		}
		
		if (parameterValue instanceof Long) {
			statement.setLong(parameterIndex, (Long)parameterValue);
		}
		
		if (parameterValue instanceof Timestamp) {
			statement.setTimestamp(parameterIndex, (Timestamp)parameterValue);
		}
	}
	
	public Idea saveIdea(Idea idea) {
		
		Long ideaId = idea.getIdeaId();
		
		Idea savedIdea = null;
		
		if (ideaId == null) {
			int numberOfAffectedRows = insertIdea(idea);
			
			if (numberOfAffectedRows == 1) {
				savedIdea = getIdea(idea.getIdea());
			}
		}
		else {
			int numberOfAffectedRows = updateIdea(idea);
			
			if (numberOfAffectedRows == 1) {
				savedIdea = getIdea(ideaId);
			}
		}
		
		return savedIdea;
	}
	
	protected int insertIdea(Idea idea) {
		
		int numberOfAffectedRows = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			Timestamp currentTimestamp = new Timestamp(new Date().getTime());
			connection = getConnection();
			statement = connection.prepareStatement(INSERT_IDEA);
			statement.setString(1, idea.getIdea());
			statement.setString(2, idea.getIdeaDate());
			statement.setString(3, idea.getStealStatus());
			statement.setString(4, idea.getStealStatusDescription());
			statement.setString(5, idea.getGreatness());
			statement.setString(6, idea.getDescription());
			statement.setString(7, StealMyIdeaConstants.STATUS_AVAILABLE);
			statement.setTimestamp(8, currentTimestamp);
			statement.setTimestamp(9, currentTimestamp);
			
			numberOfAffectedRows = statement.executeUpdate();
			
			connection.commit();
		}
		catch (Exception e){
			numberOfAffectedRows = -1;
			log.error("Error inserting idea!  idea = " + idea, e);
			rollback(connection);
		}
		finally {
			close(statement, connection);
		}
		
		return numberOfAffectedRows;
	}
	
	protected int updateIdea(Idea idea) {
		
		int numberOfAffectedRows = 0;
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			Timestamp currentTimestamp = new Timestamp(new Date().getTime());
			connection = getConnection();
			statement = connection.prepareStatement(UPDATE_IDEA);
			statement.setString(1, idea.getIdea());
			statement.setString(2, idea.getIdeaDate());
			statement.setString(3, idea.getStealStatus());
			statement.setString(4, idea.getStealStatusDescription());
			statement.setString(5, idea.getGreatness());
			statement.setString(6, idea.getDescription());
			statement.setTimestamp(7, new Timestamp(new Date().getTime()));
			statement.setLong(8, idea.getIdeaId());
			statement.setString(9, StealMyIdeaConstants.STATUS_AVAILABLE);
			statement.setTimestamp(10,currentTimestamp);
			
			numberOfAffectedRows = statement.executeUpdate();
			
			connection.commit();
		}
		catch (Exception e){
			numberOfAffectedRows = -1;
			log.error("Error inserting idea!  idea = " + idea, e);
			rollback(connection);
		}
		finally {
			close(statement, connection);
		}
		
		return numberOfAffectedRows;
	}
	
	protected void addParameter(StringBuilder query, String parameterName, Object parameterValue, List<Object> parameters, boolean addIfNull) {
		
		if (parameterValue == null && !addIfNull) {
			return;
		}
		
		if (parameters.size() > 0) {
			query.append(" and ");
		}
		else {
			query.append(" where ");
		}
		
		if (parameterValue != null) {
			query.append(" ").append(parameterName).append(" = ?");
			parameters.add(parameterValue);
		}
		else {
			query.append(" ").append(parameterName).append(" is null ");
		}
	}
	
	protected String getSortColumnName(String sortFieldName) {
		
		String sortColumnName = SORT_FIELD_NAME_TO_SORT_COLUMN_NAME_MAP.get(sortFieldName);
		
		return sortColumnName;
	}
	
	protected String getSortColumnDirection(String sortDirection) {
		
		String sortColumnDirection = SORT_FIELD_DIRECTION_TO_SORT_COLUMN_DIRECTION_MAP.get(sortDirection);
		
		return sortColumnDirection;
	}
	
	protected Idea mapIdea(ResultSet results) throws SQLException {
		
		Idea idea = new Idea();
		
		idea.setIdeaId(results.getLong("idea_id"));
		idea.setIdeaNumber(results.getLong("idea_number"));
		idea.setIdea(results.getString("idea"));
		idea.setIdeaDate(results.getString("idea_date"));
		idea.setStealStatus(results.getString("steal_status"));
		idea.setStealStatusDescription(results.getString("steal_status_description"));
		idea.setDescription(results.getString("description"));
		idea.setStatus(results.getString("status"));
		Timestamp statusDateTimestamp = results.getTimestamp("status_date");
		if (statusDateTimestamp != null) {
			idea.setStatusDate(new Date(statusDateTimestamp.getTime()));
		}
		Timestamp enterDateTimestamp = results.getTimestamp("enter_date");
		if (enterDateTimestamp != null) {
			idea.setEnterDate(new Date(enterDateTimestamp.getTime()));
		}
		Timestamp modificationDateTimestamp = results.getTimestamp("modification_date");
		if (modificationDateTimestamp != null) {
			idea.setModificationDate(new Date(modificationDateTimestamp.getTime()));
		}
		
		return idea;
	}
	
	
	
	/**
	 * 
	 * A convenience function for closing a statement and connection.
	 * 
	 * @param statement
	 * @param connection
	 */
	protected void close(PreparedStatement statement, Connection connection){
		DatabaseUtil.close(null, statement, connection);
	}
	
	/**
	 * 
	 * A convenience function for closing all the stuff for a database query.
	 * 
	 * @param results
	 * @param statement
	 * @param connection
	 */
	protected void close(ResultSet results, PreparedStatement statement, Connection connection){
		DatabaseUtil.close(results, statement, connection);
	}
	
	/**
	 * 
	 * A convenience function for rolling back whatever we were doing with a connection when
	 * there was an error.
	 * 
	 * @param connection
	 */
	protected void rollback(Connection connection){
		DatabaseUtil.rollback(connection);
	}
	
	/**
	 * 
	 * Gets a connection from the data source.
	 * 
	 * @return
	 */
	protected Connection getConnection(){
		
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
		}
		catch (Exception e){
			log.error("Error getting connection!", e);
		}
		
		return connection;
	}
		
	public DataSource getDataSource(){
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

}
