import net.pferdimanzug.hearthstone.analyzer.game.GameTag;
import net.pferdimanzug.hearthstone.analyzer.game.cards.MinionCard;
import net.pferdimanzug.hearthstone.analyzer.game.cards.Rarity;
import net.pferdimanzug.hearthstone.analyzer.game.entities.Entity;
import net.pferdimanzug.hearthstone.analyzer.game.entities.heroes.HeroClass;
import net.pferdimanzug.hearthstone.analyzer.game.entities.minions.Minion;


public class DevMonster extends MinionCard {

	private final int baseAttack;
	private final int baseHp;
	private final Minion minion;

	public DevMonster(int baseAttack, int baseHp, GameTag... tags) {
		super("Dev monster", Rarity.COMMON, HeroClass.ANY, 1);
		this.baseAttack = baseAttack;
		this.baseHp = baseHp;
		this.minion = createMinion(baseAttack, baseHp, tags);
	}

	@Override
	public Minion summon() {
		return minion;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public int getBaseHp() {
		return baseHp;
	}
	
	public Entity getMinion() {
		return minion;
	}

}
