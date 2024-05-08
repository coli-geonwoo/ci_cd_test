package roomescape.repository.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ThemeRowMapper implements RowMapper<Theme> {
    @Override
    public Theme mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
