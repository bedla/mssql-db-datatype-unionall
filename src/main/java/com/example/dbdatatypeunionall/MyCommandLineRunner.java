package com.example.dbdatatypeunionall;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

// https://docs.microsoft.com/en-us/sql/t-sql/functions/cast-and-convert-transact-sql?view=sql-server-ver15
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MyCommandLineRunner.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public MyCommandLineRunner(
            NamedParameterJdbcTemplate jdbcTemplate,
            TransactionTemplate transactionTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void run(String... args) {
        loadData();
        newLine();

        convertDatesToOffsetDateTime();
        newLine();

        convertDateAndTimeToOffsetDateTime();
        newLine();

        convertDateAndTimeToEachOther();
        newLine();

        convertDateAndTimeToDateTime();
        newLine();

        convertDateAndTimeToDateTime2();
        newLine();

        convertDateToDateTime();
        newLine();

        convertNumbersToDateTime();
        newLine();

        convertNumbersToFloat();
        newLine();

        convertNumbersToInt();
        newLine();

        convertStrings();
        newLine();

        convertBinary();
        newLine();
    }

    private void convertBinary() {
        log.info(">>> Convert binary <<<");
        log.info(" >> promotes to byte[]/varbinary");

        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_varbinary d1, c_varbinary d2
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_binary, c_binary
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertStrings() {
        log.info(">>> Convert strings <<<");
        log.info(" >> promotes to ntext");

        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_ntext d1, c_ntext d2, c_ntext d3, c_ntext d4, c_ntext d5
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_text, c_nvarchar, c_nchar, c_varchar, c_char
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertNumbersToInt() {
        log.info(">>> Convert numbers to int <<<");
        log.info(" >> promotes to float 0, 3, 1");

        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_bigint d5, c_bigint d6, c_bigint d7, c_bigint d8, c_bigint d9
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_bigint, c_int, c_smallint, c_tinyint, c_bit
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertNumbersToFloat() {
        log.info(">>> Convert numbers to float <<<");
        log.info(" >> promotes to float 0.0, 3.14, 1.0");

        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_float d1, c_float d2, c_float d3, c_float d4, c_float d5, c_float d6, c_float d7, c_float d8, c_float d9
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_real, c_decimal, c_money, c_smallmoney, c_bigint, c_int, c_smallint, c_tinyint, c_bit
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertNumbersToDateTime() {
        log.info(">>> Convert numbers to datetime2 <<<");

        for (var typeColumn : List.of("c_float", "c_real", "c_decimal", "c_money", "c_smallmoney", "c_bigint", "c_int", "c_smallint", "c_tinyint", "c_bit")) {
            try {
                transactionTemplate.executeWithoutResult(status -> {
                    jdbcTemplate.queryForList("""
                            SELECT c_datetime2 d1
                            FROM foo.data_types1
                            UNION ALL
                            SELECT <<type_column>>
                            FROM foo.data_types2
                            """.replace("<<type_column>>", typeColumn), Map.of());
                });
                throw new IllegalStateException("Column " + typeColumn + " can be UNION ALLed with date-time");
            } catch (UncategorizedSQLException e) {
                log.trace("Unable to UNION ALL column {} with date-time", typeColumn, e);
                log.error("Unable to UNION ALL column {} with date-time: {}", typeColumn, ExceptionUtils.getRootCause(e).getMessage());
            }
        }
    }

    private void convertDateAndTimeToDateTime() {
        log.info(">>> Convert Date & Time to datetime data type <<<");
        log.info(" >> date: range 0001-01-01 through 9999-12-31");
        log.info(" >> datetime: Date range - January 1, 1753, through December 31, 9999");
        log.info(" >>           Time range - 00:00:00 through 23:59:59.997");

        try {
            transactionTemplate.executeWithoutResult(status -> {
                jdbcTemplate.queryForList("""
                        SELECT c_datetime d1, c_datetime d2
                        FROM foo.data_types1
                        UNION ALL
                        SELECT c_date, c_time
                        FROM foo.data_types2
                        """, Map.of());
            });
            // if this fails, it means that random data was not chosen out of datetime year range
            throw new IllegalStateException("Unable to UNION ALL columns date & time to datetime data type");
        } catch (UncategorizedSQLException e) {
            log.trace("Unable to UNION ALL columns date & time to datetime data type", e);
            log.error(" >> Unable to UNION ALL columns date & time to datetime data type: {}", ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    private void convertDateAndTimeToDateTime2() {
        log.info(">>> Convert Date & Time to datetime2 data type (bigger values) <<<");
        log.info(" >> promotes date to hour 00:00:00");
        log.info(" >> promotes time to year 1900-01-01");

        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_datetime2 d1, c_datetime2 d2
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_date, c_time
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertDateToDateTime() {
        log.info(">>> Convert Date to datetime data type <<<");
        log.info(" >> date: range 0001-01-01 through 9999-12-31");
        log.info(" >> datetime: Date range - January 1, 1753, through December 31, 9999");
        log.info(" >>           Time range - 00:00:00 through 23:59:59.997");

        try {
            transactionTemplate.executeWithoutResult(status -> {
                jdbcTemplate.queryForList("""
                        SELECT c_datetime d1
                        FROM foo.data_types1
                        UNION ALL
                        SELECT c_date
                        FROM foo.data_types2
                        """, Map.of());
            });
            // if this fails, it means that random data was not chosen out of datetime year range
            throw new IllegalStateException("Columns date & datetime can not be UNION ALLed with each other");
        } catch (UncategorizedSQLException e) {
            log.trace("Unable to UNION ALL columns date & datetime with each other", e);
            log.error(" >> Unable to UNION ALL columns date & datetime with each other: {}", ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    private void convertDateAndTimeToEachOther() {
        log.info(">>> Convert Date & Time to each other <<<");

        try {
            transactionTemplate.executeWithoutResult(status -> {
                jdbcTemplate.queryForList("""
                        SELECT c_date d1
                        FROM foo.data_types1
                        UNION ALL
                        SELECT c_time
                        FROM foo.data_types2
                        """, Map.of());
            });
            throw new IllegalStateException("Columns date & time can not be UNION ALLed with each other");
        } catch (UncategorizedSQLException e) {
            log.trace("Unable to UNION ALL columns date & time with each other", e);
            log.error(" >> Unable to UNION ALL columns date & time with each other: {}", ExceptionUtils.getRootCause(e).getMessage());
        }
    }

    private void convertDateAndTimeToOffsetDateTime() {
        log.info(">>> Convert Date & Time to OffsetDateTime <<<");
        log.info(" >> promotes date to hour 00:00:00 at offset +00:00");
        log.info(" >> promotes time to year 1900-01-01 at offset +00:00");
        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_datetimeoffset d1, c_datetimeoffset d2
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_date, c_time
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void convertDatesToOffsetDateTime() {
        log.info(">>> Convert dates to OffsetDateTime <<<");
        log.info(" >> promotes date-times to c_datetimeoffset at offset +00:00");
        var list = transactionTemplate.execute(status -> {
            return jdbcTemplate.queryForList("""
                    SELECT c_datetimeoffset c_datetime2, c_datetimeoffset c_datetime, c_datetimeoffset c_smalldatetime
                    FROM foo.data_types1
                    UNION ALL
                    SELECT c_datetime2, c_datetime, c_smalldatetime
                    FROM foo.data_types2
                    """, Map.of());
        });
        dumpTable(list);
    }

    private void loadData() {
        transactionTemplate.executeWithoutResult(status -> {
            var count1 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM foo.data_types1", Map.of(), Number.class);
            var count2 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM foo.data_types1", Map.of(), Number.class);
            if (count1.longValue() == 0 && count2.longValue() == 0) {
                log.info("Inserting new random data");
                for (int i = 0; i < 10; i++) {
                    insert("foo.data_types1");
                    insert("foo.data_types2");
                }
            } else if (count1.longValue() > 0 && count2.longValue() > 0) {
                log.info("We already have foo.data_types1 COUNT(*)={} and foo.data_types2 COUNT(*)={}", count1, count2);
            } else {
                throw new IllegalStateException("Both tables has to have data filled, but we got: " +
                        "foo.data_types1 COUNT(*)=" + count1 + " and foo.data_types2 COUNT(*)=" + count2);
            }
        });
    }

    private void dumpTable(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> row = list.get(i);
            System.out.println(i + ".\t" + row);
        }
    }

    private void newLine() {
        log.info("======\n");
    }

    private void insert(String tableName) {
        var values = new HashMap<String, Object>();
        // order is same with data-type precedence
        // https://docs.microsoft.com/en-us/sql/t-sql/data-types/data-type-precedence-transact-sql?view=sql-server-ver15
        values.put("c_datetimeoffset", randomDateTimeOffset());
        values.put("c_datetime2", randomDateTime2());
        values.put("c_datetime", randomDateTime());
        values.put("c_smalldatetime", randomSmallDateTime());
        values.put("c_date", randomDate());
        values.put("c_time", randomTime());
        values.put("c_float", randomFloat());
        values.put("c_real", randomReal());
        values.put("c_decimal", randomDecimal());
        values.put("c_money", randomMoney());
        values.put("c_smallmoney", randomSmallMoney());
        values.put("c_bigint", randomLong(0, Long.MAX_VALUE));
        values.put("c_int", randomInt(0, Integer.MAX_VALUE));
        values.put("c_smallint", randomInt(0, Short.MAX_VALUE));
        values.put("c_tinyint", randomTinyInt());
        values.put("c_bit", randomBool());
        values.put("c_ntext", randomNText());
        values.put("c_text", randomText());
        values.put("c_nvarchar", randomNVarChar());
        values.put("c_nchar", randomNChar());
        values.put("c_varchar", randomVarChar());
        values.put("c_char", randomChar());
        values.put("c_varbinary", randomVarBinary());
        values.put("c_binary", randomBinary());

        jdbcTemplate.update("""
                        INSERT INTO <<table_name>> (
                        c_datetimeoffset,
                        c_datetime2,
                        c_datetime,
                        c_smalldatetime,
                        c_date,
                        c_time,
                        c_float,
                        c_real,
                        c_decimal,
                        c_money,
                        c_smallmoney,
                        c_bigint,
                        c_int,
                        c_smallint,
                        c_tinyint,
                        c_bit,
                        c_ntext,
                        c_text,
                        c_nvarchar,
                        c_nchar,
                        c_varchar,
                        c_char,
                        c_varbinary,
                        c_binary
                        ) VALUES (
                        :c_datetimeoffset,
                        :c_datetime2,
                        :c_datetime,
                        :c_smalldatetime,
                        :c_date,
                        :c_time,
                        :c_float,
                        :c_real,
                        :c_decimal,
                        :c_money,
                        :c_smallmoney,
                        :c_bigint,
                        :c_int,
                        :c_smallint,
                        :c_tinyint,
                        :c_bit,
                        :c_ntext,
                        :c_text,
                        :c_nvarchar,
                        :c_nchar,
                        :c_varchar,
                        :c_char,
                        :c_varbinary,
                        :c_binary
                        )
                        """.replace("<<table_name>>", tableName),
                values);
    }

    private Integer randomTinyInt() {
        var value = randomInt(0, 32);
        return value == null ? null : Math.abs(value);
    }

    private byte[] randomBinary() {
        return random(() -> RandomStringUtils.randomAscii(64, 64).getBytes(StandardCharsets.UTF_8));
    }

    private byte[] randomVarBinary() {
        return random(() -> RandomStringUtils.randomAscii(10, 64).getBytes(StandardCharsets.UTF_8));
    }

    private String randomChar() {
        return randomNChar();
    }

    private String randomVarChar() {
        return randomNVarChar();
    }

    private String randomNChar() {
        return random(() -> RandomStringUtils.randomPrint(1));
    }

    private String randomNVarChar() {
        return randomNText();
    }

    private String randomText() {
        return randomNText();
    }

    private String randomNText() {
        return random(() -> RandomStringUtils.randomAscii(10, 64));
    }

    private Float randomSmallMoney() {
        return randomFloat();
    }

    private Double randomMoney() {
        return randomDecimal();
    }

    private Double randomDecimal() {
        return random(() -> RandomUtils.nextDouble(0, 1_000_000) * (RandomUtils.nextBoolean() ? -1 : 1));
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/float-and-real-transact-sql?view=sql-server-ver15
    private Float randomReal() {
        return randomFloat();
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/float-and-real-transact-sql?view=sql-server-ver15
    private Float randomFloat() {
        return random(() -> RandomUtils.nextFloat(0, 100) * (RandomUtils.nextBoolean() ? -1 : 1));
    }

    private OffsetDateTime randomDateTimeOffset() {
        var dateTime = random(() -> LocalDateTime.of(createRandomDate(1, 9999), createRandomTime()));
        return dateTime == null
                ? null
                : dateTime.atZone(ZoneId.of("Europe/Prague")).toOffsetDateTime();
    }

    private LocalDateTime randomDateTime2() {
        return random(() -> LocalDateTime.of(createRandomDate(1, 9999), createRandomTime()));
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/smalldatetime-transact-sql?view=sql-server-ver15
    private LocalDateTime randomSmallDateTime() {
        return random(() -> LocalDateTime.of(createRandomDate(1900, 2078), createRandomTime()));
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/datetime-transact-sql?view=sql-server-ver15
    private LocalDateTime randomDateTime() {
        return random(() -> LocalDateTime.of(createRandomDate(1753, 9999), createRandomTime()));
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/time-transact-sql?view=sql-server-ver15
    private LocalTime randomTime() {
        return random(this::createRandomTime);
    }

    // https://docs.microsoft.com/en-us/sql/t-sql/data-types/date-transact-sql?view=sql-server-ver15
    private LocalDate randomDate() {
        return random(() -> createRandomDate(1, 9999));
    }

    private LocalDate createRandomDate(int minYear, int maxYear) {
        long min = LocalDate.of(minYear, 1, 1).toEpochDay();
        long max = LocalDate.of(maxYear, 12, 31).toEpochDay();
        long offset = -1 * min;
        long randomValue = RandomUtils.nextLong(offset + min, offset + max);
        return LocalDate.ofEpochDay(randomValue - offset);
    }

    private LocalTime createRandomTime() {
        long min = LocalTime.of(0, 0, 0).toSecondOfDay();
        long max = LocalTime.of(23, 59, 59).toSecondOfDay();
        long randomValue = RandomUtils.nextLong(min, max);
        return LocalTime.ofSecondOfDay(randomValue);
    }

    private Long randomLong(long startInclusive, long endExclusive) {
        return random(() -> RandomUtils.nextLong(startInclusive, endExclusive) * (RandomUtils.nextBoolean() ? -1 : 1));
    }

    private Integer randomInt(int startInclusive, int endExclusive) {
        return random(() -> RandomUtils.nextInt(startInclusive, endExclusive) * (RandomUtils.nextBoolean() ? -1 : 1));
    }

    private Boolean randomBool() {
        return random(() -> RandomUtils.nextBoolean());
    }

    private <T> T random(Supplier<T> valueGetter) {
        return RandomUtils.nextBoolean()
                ? valueGetter.get()
                : null;
    }
}
