package org.example.fourchak.domain.coupon.lock.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NameLockWithDataSource {

    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";

    private final DataSource dataSource;

    public <T> T executeWithLock(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                log.info("LOCK 획득 시작 KEY: " + userLockName + " __ CONNECTION: " + connection);
                getLock(connection, userLockName, timeoutSeconds);
                log.info("LOCK 획득 성공 KEY: " + userLockName + " __ CONNECTION: " + connection);
                return supplier.get();
            } finally {
                releaseLock(connection, userLockName);
                log.info("LOCK 해제 성공 KEY: " + userLockName + " __ CONNECTION: " + connection);
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void getLock(Connection connection, String userLockName, int timeoutSeconds) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_LOCK)) {
            preparedStatement.setString(1, userLockName);
            preparedStatement.setInt(2, timeoutSeconds);

            checkResult(userLockName, preparedStatement, "GetLock_");
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void releaseLock(Connection connection, String userLockName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(RELEASE_LOCK)) {
            preparedStatement.setString(1, userLockName);

            checkResult(userLockName, preparedStatement, "ReleaseLock");
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void checkResult(String userLockName, PreparedStatement ps, String type)
        throws SQLException {
        try (ResultSet resultSet = ps.executeQuery()) {
            if (!resultSet.next()) {
                log.error(
                    "USER LEVEL LOCK 쿼리 결과 값이 없습니다. type = " + type
                        + "_ userLockName = " + userLockName
                        + " _ connection: " + ps.getConnection());
                throw new RuntimeException("USER LEVEL LOCK 쿼리 결과 값이 없습니다.");
            }
            int result = resultSet.getInt(1);
            if (result != 1) {
                log.error(
                    "USER LEVEL LOCK 쿼리 결과 값이 1이 아닙니다. type = " + type
                        + " _ result = " + result + "_ userLockName = " + userLockName
                        + " _ connection: " + ps.getConnection());
                throw new RuntimeException("USER LEVEL LOCK 쿼리 결과 값이 1이 아닙니다.");
            }
        }
    }
}
