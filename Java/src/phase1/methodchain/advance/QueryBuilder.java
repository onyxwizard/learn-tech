package phase1.methodchain.advance;

import java.util.ArrayList;
import java.util.List;

/**
 * 🎯 Fluent SQL Query Builder (Advanced Method Chaining)
 * Supports: select(...).from(...).where(...).gt(...).orderBy(...).limit(...)
 * Generates: SELECT name, email FROM users WHERE age > 18 ORDER BY name LIMIT 10
 */
public class QueryBuilder {

    private final List<String> tokens = new ArrayList<>();

    // ———————— MAIN CHAIN ————————
    public QueryBuilder select(String... columns) {
        tokens.add("SELECT");
        tokens.add(String.join(", ", columns));
        return this;
    }

    public QueryBuilder from(String table) {
        tokens.add("FROM");
        tokens.add(table);
        return this;
    }

    // 🧩 Condition Builder: where("age").gt(18) → returns *this*, not a new type
    // (Simpler than full nested builder for this scope)
    public QueryBuilder where(String column) {
        tokens.add("WHERE");
        tokens.add(column);
        return this;
    }

    public QueryBuilder gt(int value) {
        tokens.add(">");
        tokens.add(String.valueOf(value));
        return this;
    }

    public QueryBuilder eq(int value) {
        tokens.add("=");
        tokens.add(String.valueOf(value));
        return this;
    }

    public QueryBuilder orderBy(String column) {
        tokens.add("ORDER BY");
        tokens.add(column);
        return this;
    }

    public QueryBuilder limit(int n) {
        tokens.add("LIMIT");
        tokens.add(String.valueOf(n));
        return this;
    }

    // ✅ Natural SQL formatting
    @Override
    public String toString() {
        return String.join(" ", tokens);
    }

    // 🧪 Demo
    public static void main(String[] args) {
        String sql = new QueryBuilder()
            .select("name", "email")
            .from("users")
            .where("age").gt(18)
            .orderBy("name")
            .limit(10)
            .toString();

        System.out.println(sql);
        // ✅ Output: SELECT name, email FROM users WHERE age > 18 ORDER BY name LIMIT 10
    }
}