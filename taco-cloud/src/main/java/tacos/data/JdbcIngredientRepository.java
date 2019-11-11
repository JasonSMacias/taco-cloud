package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;

@Repository
public class JdbcIngredientRepository implements IngredientRepository {
	private JdbcTemplate jdbc;
	
	@Autowired
	public JdbcIngredientRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		// Java 8 method reference & lambda used here in place of explicit RowMapper reference
		return jdbc.query("select id, name, type from Ingredient", this::mapRowToIngredient);
	}

	@Override
	public Ingredient findOne(String id) {
		// This is an example of an explicit RowMapper reference (no lambdas or use of mapRowToIngredient)
		return jdbc.queryForObject("select id, name, type from Ingredient where id=?", 
				new RowMapper<Ingredient>() {
					public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Ingredient(
								rs.getString("id"),
								rs.getString("name"),
								Ingredient.Type.valueOf(rs.getString("type"))
								);
					};
				},
				id);
	}
	
	private Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException{
		return new Ingredient(
				rs.getString("id"),
				rs.getString("name"),
				Ingredient.Type.valueOf(rs.getString("type")));
	}
	
	@Override
	public Ingredient save(Ingredient ingredient) {
		jdbc.update("insert into Ingredient (id, name, type) values (?, ?, ?)", 
				ingredient.getId(), ingredient.getName(), ingredient.getType().toString());
		return ingredient;
	}
}
