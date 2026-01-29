-- ============================================================================
-- 🔐 MYSQL USER MANAGEMENT & PERMISSIONS COMPREHENSIVE GUIDE
-- ============================================================================
-- This script demonstrates complete user lifecycle management in MySQL
-- including creation, privilege assignment, security policies, and revocation
-- ============================================================================
-- 🎯 SECTION 1: USER CREATION & BASIC VERIFICATION
-- ===================================================
-- 👤 Create a new MySQL user named 'onyx' with password authentication
--    Syntax: CREATE USER 'username' IDENTIFIED BY 'password';
--    Note: Without host specification, defaults to '%' (all hosts)
--    Security Tip: Always use strong passwords in production
CREATE USER 'onyx' IDENTIFIED BY 'onyx';

-- 🔍 Verification: List all users in the MySQL user table
--    The mysql.user table contains authentication and account information
--    This query shows all user accounts on the MySQL server
SELECT
  USER
FROM
  mysql.user;

-- ============================================================================
-- 📋 SECTION 2: PRIVILEGE MANAGEMENT - GRANTING ACCESS
-- ============================================================================
-- 🎪 Grant ALL privileges on specific database (university)
--    Syntax: GRANT privileges ON database.table TO 'user'@'host';
--    *.* means all databases and all tables
--    university.* means all tables in 'university' database only
--    Warning: ALL PRIVILEGES gives complete control - use cautiously!
GRANT ALL PRIVILEGES ON university.* TO 'onyx';

-- 🌍 Grant ALL privileges on ALL databases (Global privileges)
--    This gives the user superuser-like access to entire MySQL instance
--    Typically reserved for database administrators only
--    Security Risk: Avoid granting this to regular users
GRANT ALL PRIVILEGES ON *.* TO 'onyx';

-- ============================================================================
-- 🗑️ SECTION 3: USER REMOVAL & SECURITY VERIFICATION
-- ============================================================================
-- ❌ Remove/Drop the user account completely
--    Syntax: DROP USER 'username';
--    Warning: This permanently removes the user and all their privileges
--    Cannot be undone - use with extreme caution!
DROP USER 'onyx';

-- 🔍 Post-deletion verification
--    Check user status, account lock status, and password expiration
--    Useful for security audits and compliance checks
SELECT
  USER,
  account_locked,
  password_expired
FROM
  mysql.user;

-- ============================================================================
-- ⚙️ SECTION 4: ADVANCED USER CREATION WITH SECURITY POLICIES
-- ============================================================================
-- 📅 Check current server time for reference
--    Helps in scheduling password expiration and lock policies
SELECT
  NOW();

-- 🔐 Create user with immediate password expiration
--    Forces user to change password on first login
--    Security Best Practice: Use for temporary or initial accounts
CREATE USER 'onyx' IDENTIFIED BY 'onyx' PASSWORD EXPIRE;

-- 🛡️ Create user with comprehensive security policies:
--    • PASSWORD EXPIRE INTERVAL 25 DAY: Password expires every 25 days
--    • FAILED_LOGIN_ATTEMPTS 2: Account locks after 2 failed attempts
--    • PASSWORD_LOCK_TIME 1: Account locks for 1 day after failed attempts
--    These policies enhance security and prevent brute-force attacks
CREATE USER 'onyx' IDENTIFIED BY 'onyx' PASSWORD EXPIRE INTERVAL 25 DAY FAILED_LOGIN_ATTEMPTS 2 PASSWORD_LOCK_TIME 1;

-- 🔍 Verify user creation and lock status
--    Shows which hosts the user can connect from and account status
SELECT
  USER,
  HOST,
  account_locked
FROM
  mysql.user;

-- ============================================================================
-- 🔒 SECTION 5: ACCOUNT LOCKING & UNLOCKING
-- ============================================================================
-- 🔐 Manually lock a user account
--    Syntax: ALTER USER 'username' ACCOUNT LOCK;
--    Use cases: Suspicious activity, employee termination, maintenance
--    Note: User can still connect but cannot execute queries
ALTER USER 'onyx' ACCOUNT LOCK;

-- 🔍 Verify account lock status
--    account_locked = 'Y' means account is locked
--    account_locked = 'N' means account is active
SELECT
  USER,
  HOST,
  account_locked
FROM
  mysql.user;

-- 🔓 Unlock a previously locked account
--    Syntax: ALTER USER 'username' ACCOUNT UNLOCK;
--    Use after security review or issue resolution
ALTER USER 'onyx' ACCOUNT UNLOCK;

-- ============================================================================
-- ⚖️ SECTION 6: PRIVILEGE REVOCATION & AUDITING
-- ============================================================================
-- Based on MySQL REVOKE statement documentation
-- 🔍 Audit current privileges before revocation
--    Checking specific privileges to understand what to revoke
--    Common privileges to check:
--    • Grant_priv: Can grant privileges to others
--    • Index_priv: Can create/drop indexes
--    • Show_db_priv: Can see all databases
--    • Alter_priv: Can alter tables
--    • Create_priv: Can create databases/tables
--    • Lock_tables_priv: Can lock tables
--    • Execute_priv: Can execute stored procedures
SELECT
  USER,
  `Grant_priv`,
  `Index_priv`,
  `Show_db_priv`,
  `Alter_priv`,
  `Create_priv`,
  `Lock_tables_priv`,
  `Execute_priv`
FROM
  mysql.user
WHERE
  USER = 'onyx';

-- ❌ Revoke ALL privileges on ALL databases
--    Syntax: REVOKE ALL PRIVILEGES ON *.* FROM 'user'
--    Removes all global privileges but user still exists
--    User retains only USAGE privilege (ability to connect)
REVOKE ALL PRIVILEGES ON *.*
FROM
  'onyx';

-- 🗑️ Complete privilege revocation including GRANT OPTION
--    Syntax: REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'user'
--    GRANT OPTION allows user to grant their privileges to others
--    This is the most comprehensive revocation
REVOKE ALL PRIVILEGES,
GRANT OPTION
FROM
  'onyx';

-- 🔍 Verify privileges after revocation
--    SHOW GRANTS displays current privileges for a user
--    After complete revocation, should only show:
--    "GRANT USAGE ON *.* TO 'onyx'@'%'"
--    USAGE means no privileges, just ability to connect
SHOW GRANTS FOR 'onyx';

-- ============================================================================
-- 📚 REAL-WORLD SCENARIOS & EXAMPLES
-- ============================================================================
/*
🎯 SCENARIO 1: Creating a Read-Only User for Reporting

CREATE USER 'report_user'@'localhost' IDENTIFIED BY 'SecurePass123!';
GRANT SELECT ON university.* TO 'report_user'@'localhost';
-- Verification
SHOW GRANTS FOR 'report_user'@'localhost';

🎯 SCENARIO 2: Application-Specific User with Limited Access

CREATE USER 'app_user'@'192.168.1.%' IDENTIFIED BY 'AppPass456!';
GRANT SELECT, INSERT, UPDATE, DELETE ON university.student TO 'app_user'@'192.168.1.%';
GRANT SELECT ON university.subject TO 'app_user'@'192.168.1.%';
-- Restrict to specific IP range for security

🎯 SCENARIO 3: Temporary Contractor Access with Expiration

CREATE USER 'contractor'@'%' IDENTIFIED BY 'TempPass789!' 
PASSWORD EXPIRE INTERVAL 30 DAY;
GRANT SELECT, INSERT ON university.attendance TO 'contractor'@'%';
-- Schedule user removal after contract ends
-- DROP USER 'contractor'@'%' AFTER DATE_ADD(NOW(), INTERVAL 30 DAY);
 */
-- ============================================================================
-- ⚠️ SECURITY BEST PRACTICES
-- ============================================================================
/*
1. 🔐 Principle of Least Privilege
- Grant only necessary permissions
- Regularly audit and revoke unused privileges

2. 🌐 Host Restrictions
- Always specify host (@'localhost', @'192.168.1.%', @'specific-ip')
- Avoid using '%' (all hosts) in production

3. 🔄 Regular Password Rotation
- Implement password expiration policies
- Use PASSWORD EXPIRE for sensitive accounts

4. 👁️ Account Monitoring
- Regularly review mysql.user table
- Monitor failed login attempts

5. 🧹 Cleanup Unused Accounts
- Remove temporary and unused accounts
- Document all user accounts and purposes

6. 📊 Privilege Documentation
- Maintain record of all granted privileges
- Use SHOW GRANTS regularly for audits
 */
-- ============================================================================
-- 🛠️ TROUBLESHOOTING COMMON ISSUES
-- ============================================================================
/*
Issue 1: "Access denied for user" after privilege changes
Solution: FLUSH PRIVILEGES; to reload grant tables

Issue 2: User can connect but cannot see databases
Solution: Check SHOW DATABASES privilege or grant SELECT on specific databases

Issue 3: REVOKE doesn't seem to work
Solution: Check if privileges were granted at different levels (global, db, table)
Use: SHOW GRANTS FOR 'user'@'host';

Issue 4: Password policies not enforced
Solution: Check validate_password component installation:
INSTALL COMPONENT 'file://component_validate_password';
 */
-- ============================================================================
-- 📈 ADVANCED: ROLE-BASED ACCESS CONTROL (RBAC)
-- ============================================================================
/*
-- Creating roles for better privilege management
CREATE ROLE 'readonly_role';
GRANT SELECT ON university.* TO 'readonly_role';

CREATE ROLE 'data_entry_role';
GRANT SELECT, INSERT, UPDATE ON university.* TO 'data_entry_role';

-- Assign roles to users
GRANT 'readonly_role' TO 'report_user'@'localhost';
GRANT 'data_entry_role' TO 'app_user'@'192.168.1.%';

-- Activate roles for a session
SET ROLE 'readonly_role';

-- Revoke role from user
REVOKE 'readonly_role' FROM 'report_user'@'localhost';
 */
-- ============================================================================
-- 🔍 FINAL VERIFICATION & CLEANUP SCRIPT
-- ============================================================================
-- 🧹 Cleanup all created users (for testing environments only)
/*
DROP USER IF EXISTS 'onyx';
DROP USER IF EXISTS 'report_user'@'localhost';
DROP USER IF EXISTS 'app_user'@'192.168.1.%';
DROP USER IF EXISTS 'contractor'@'%';

-- Final verification
SELECT User, Host FROM mysql.user ORDER BY User;
 */
-- ============================================================================
-- 🎉 END OF COMPREHENSIVE USER MANAGEMENT GUIDE
-- ============================================================================