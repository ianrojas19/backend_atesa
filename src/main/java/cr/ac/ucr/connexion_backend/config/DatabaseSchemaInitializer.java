package cr.ac.ucr.connexion_backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSchemaInitializer {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initialize() {
        addColumnIfMissing("supporter_id", "INT NULL");
        addColumnIfMissing("author_type", "VARCHAR(20) NULL");
        createChatMessagesTableIfMissing();
    }

    private void addColumnIfMissing(String columnName, String definition) {
        Integer tableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_NAME = 'issue_comments'
                """, Integer.class);

        if (tableCount == null || tableCount == 0) {
            return; // Table doesn't exist yet, nothing to alter
        }

        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM INFORMATION_SCHEMA.COLUMNS
                WHERE TABLE_NAME = 'issue_comments'
                AND COLUMN_NAME = ?
                """, Integer.class, columnName);

        if (count != null && count == 0) {
            jdbcTemplate.execute("ALTER TABLE issue_comments ADD " + columnName + " " + definition);
        }
    }

    private void createChatMessagesTableIfMissing() {
        Integer issuesTableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_NAME = 'issues'
                """, Integer.class);

        if (issuesTableCount == null || issuesTableCount == 0) {
            return; // 'issues' table doesn't exist, cannot create foreign key
        }

        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_NAME = 'chat_messages'
                """, Integer.class);

        if (count != null && count == 0) {
            jdbcTemplate.execute("""
                CREATE TABLE chat_messages (
                    id BIGINT IDENTITY(1,1) PRIMARY KEY,
                    issue_id INT NOT NULL,
                    sender_id INT NOT NULL,
                    sender_role VARCHAR(20) NOT NULL,
                    message NVARCHAR(MAX) NOT NULL,
                    timestamp DATETIME2 NOT NULL,
                    CONSTRAINT fk_chat_issue FOREIGN KEY (issue_id) REFERENCES issues(id)
                )
                """);
        }
    }
}
