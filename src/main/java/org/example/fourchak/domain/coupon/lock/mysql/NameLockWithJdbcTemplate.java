package org.example.fourchak.domain.coupon.lock.mysql;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NameLockWithJdbcTemplate {

    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public <T> T executeWithLock(
        String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    private void getLock(String userLockName, int timeoutSeconds) {
        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);
        params.put("timeoutSeconds", timeoutSeconds);

        log.info("LOCK 획득: " + userLockName + " TIME: " + timeoutSeconds);

        Integer result = jdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result, userLockName, "GetLock");
    }

    private void releaseLock(String userLockName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);

        log.info("LOCK 해제: " + userLockName);

        Integer result = jdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result, userLockName, "GetLock");
    }

    private void checkResult(Integer result, String userLockName, String type) {
        if (result == null) {
            log.info("USER LEVEL LOCK 쿼리 결과값이 없습니다. type = " + type, " userLockName: ",
                userLockName);
            throw new BaseException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
        if (result != 1) {
            log.info("USER LEVEL LOCK 쿼리 결과값이 1이 아닙니다. type = " + type, " userLockName: ",
                userLockName);
            throw new BaseException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }
}
