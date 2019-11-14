package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {
	
	private JdbcTemplate jdbc;
	
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for (Ingredient ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}
	
	private long saveTacoInfo(Taco taco) {
		taco.setCreatedAt(new Date());
		PreparedStatementCreatorFactory preparedStatementCreatorFactory = 
				new PreparedStatementCreatorFactory(
				"insert into Taco (name, createdAt) values(?, ?)", 
				Types.VARCHAR, Types.TIMESTAMP);
		preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
//		System.out.println("==============Look HERE=============\nTaco name:"  + taco.getName() + "\nTimestamp" + taco.getCreatedAt().getTime());
		PreparedStatementCreator psc = preparedStatementCreatorFactory
				.newPreparedStatementCreator(
						Arrays.asList(taco.getName(), 
						new Timestamp(taco.getCreatedAt().getTime())));
		KeyHolder keyholder = new GeneratedKeyHolder();
		int rowsAffected = (int)jdbc.update(psc, keyholder);
		System.out.println("Rows affected: " + rowsAffected);
		System.out.println("Key: " + keyholder.getKey());
		return keyholder.getKey().longValue();
	}
	
	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)", tacoId, ingredient.getId());
	}
}
