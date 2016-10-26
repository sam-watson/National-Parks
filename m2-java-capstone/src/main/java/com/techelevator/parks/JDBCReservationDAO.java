package com.techelevator.parks;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate template;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public int setReservation(Reservation res) {
		String sqlInsertReservationIntoDB = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?,?,?,?)";
		template.update(sqlInsertReservationIntoDB, res.getSiteId(), res.getName(), res.getStart(), res.getEnd());
		return findReservation(res);
	}
	
	@Override
	public int findReservation(Reservation res) {
		String sqlSelectMatchingReservation = "SELECT * FROM reservation WHERE site_id=? AND name=? AND from_date=? AND to_date=?";
		SqlRowSet row = template.queryForRowSet(sqlSelectMatchingReservation, res.getSiteId(), res.getName(), res.getStart(), res.getEnd());
		if (row.next()) {
			return row.getInt("reservation_id");
		} else {
			return -1;
		}
	}

}
