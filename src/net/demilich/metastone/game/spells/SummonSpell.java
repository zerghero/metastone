package net.demilich.metastone.game.spells;

import java.util.Map;

import net.demilich.metastone.game.GameContext;
import net.demilich.metastone.game.Player;
import net.demilich.metastone.game.cards.CardCatalogue;
import net.demilich.metastone.game.cards.MinionCard;
import net.demilich.metastone.game.entities.Entity;
import net.demilich.metastone.game.entities.minions.Minion;
import net.demilich.metastone.game.entities.minions.RelativeToSource;
import net.demilich.metastone.game.spells.desc.SpellArg;
import net.demilich.metastone.game.spells.desc.SpellDesc;
import net.demilich.metastone.game.targeting.EntityReference;

public class SummonSpell extends Spell {
	
	public static SpellDesc create(MinionCard... minionCards) {
		return create(TargetPlayer.SELF, minionCards);
	}
	
	public static SpellDesc create(RelativeToSource relativeBoardPosition, MinionCard... minionCards) {
		return create(TargetPlayer.SELF, relativeBoardPosition, minionCards);
	}
	
	public static SpellDesc create(String minionCard) {
		Map<SpellArg, Object> arguments = SpellDesc.build(SummonSpell.class);
		arguments.put(SpellArg.CARD, minionCard);
		arguments.put(SpellArg.TARGET, EntityReference.NONE);
		return new SpellDesc(arguments);
	}
	
	public static SpellDesc create(TargetPlayer targetPlayer, MinionCard... minionCards) {
		return create(targetPlayer, null, minionCards);
	}

	public static SpellDesc create(TargetPlayer targetPlayer, RelativeToSource relativeBoardPosition, MinionCard... minionCards) {
		Map<SpellArg, Object> arguments = SpellDesc.build(SummonSpell.class);
		arguments.put(SpellArg.CARD, minionCards);
		arguments.put(SpellArg.TARGET, EntityReference.NONE);
		if (relativeBoardPosition != null) {
			arguments.put(SpellArg.BOARD_POSITION_RELATIVE, relativeBoardPosition);
		}
		return new SpellDesc(arguments);
	}

	private int getBoardPosition(GameContext context, Player player, SpellDesc desc, Entity source) {
		final int UNDEFINED = -1;
		int boardPosition = desc.contains(SpellArg.BOARD_POSITION_ABSOLUTE) ? desc.getInt(SpellArg.BOARD_POSITION_ABSOLUTE) : -1;
		if (boardPosition != UNDEFINED) {
			return boardPosition;
		}
		RelativeToSource relativeBoardPosition = (RelativeToSource) desc.get(SpellArg.BOARD_POSITION_RELATIVE);
		if (relativeBoardPosition == null) {
			return UNDEFINED;
		}
		
		int sourcePosition = context.getBoardPosition((Minion) source);
		if (sourcePosition == UNDEFINED) {
			return UNDEFINED;
		}
		switch (relativeBoardPosition) {
		case LEFT:
			return sourcePosition;
		case RIGHT:
			return sourcePosition + 1;
		default:
			return UNDEFINED;
		}
	}
	
	@Override
	protected void onCast(GameContext context, Player player, SpellDesc desc, Entity source, Entity target) {
		String[] cardNames = null;
		if (desc.contains(SpellArg.CARDS)) {
			cardNames = (String[]) desc.get(SpellArg.CARDS);
		} else {
			cardNames = new String[1];
			cardNames[0] = (String) desc.get(SpellArg.CARD);
		}
		MinionCard[] minionCards = new MinionCard[cardNames.length];
		for (int i = 0; i < minionCards.length; i++) {
			minionCards[i] = (MinionCard) CardCatalogue.getCardById(cardNames[i]);
		}
		int boardPosition = getBoardPosition(context, player, desc, source);
		for (MinionCard minionCard : minionCards) {
			context.getLogic().summon(player.getId(), minionCard.summon(), null, boardPosition, false);
		}
	}

}
