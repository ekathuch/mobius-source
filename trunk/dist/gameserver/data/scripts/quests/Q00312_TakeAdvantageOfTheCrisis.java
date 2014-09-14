/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

import org.apache.commons.lang3.ArrayUtils;

public class Q00312_TakeAdvantageOfTheCrisis extends Quest implements ScriptFile
{
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	private static final int FILAUR = 30535;
	private static final int MINERAL_FRAGMENT = 14875;
	private static final int DROP_CHANCE = 40;
	private static final int[] MINE_MOBS = new int[]
	{
		22678,
		22679,
		22680,
		22681,
		22682,
		22683,
		22684,
		22685,
		22686,
		22687,
		22688,
		22689,
		22690
	};
	
	public Q00312_TakeAdvantageOfTheCrisis()
	{
		super(false);
		addStartNpc(FILAUR);
		addKillId(MINE_MOBS);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		
		if (event.equalsIgnoreCase("30535-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30535-09.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
		}
		else
		{
			int id = 0;
			
			try
			{
				id = Integer.parseInt(event);
			}
			catch (Exception e)
			{
				// empty catch clause
			}
			
			if (id > 0)
			{
				int count = 0;
				
				switch (id)
				{
					case 9487:
						count = 366;
						break;
					
					case 9488:
						count = 229;
						break;
					
					case 9489:
						count = 183;
						break;
					
					case 9490:
					case 9491:
						count = 122;
						break;
					
					case 9497:
						count = 129;
						break;
					
					case 9625:
						count = 667;
						break;
					
					case 9626:
						count = 1000;
						break;
					
					case 9628:
					case 9629:
						count = 24;
						break;
					
					case 9630:
						count = 36;
						break;
				}
				
				if (count > 0)
				{
					if (st.getQuestItemsCount(MINERAL_FRAGMENT) >= count)
					{
						st.giveItems(id, 1);
						st.takeItems(MINERAL_FRAGMENT, count);
						st.playSound(SOUND_MIDDLE);
						return "30535-16.htm";
					}
					
					return "30535-15.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getId();
		int id = st.getState();
		int cond = st.getCond();
		
		if (npcId == FILAUR)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 80)
				{
					htmltext = "30535-01.htm";
				}
				else
				{
					htmltext = "30535-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (id == STARTED)
			{
				if (st.getQuestItemsCount(MINERAL_FRAGMENT) >= 1)
				{
					htmltext = "30535-10.htm";
				}
				else
				{
					htmltext = "30535-07.htm";
				}
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getId();
		int cond = st.getCond();
		
		if ((cond == 1) && ArrayUtils.contains(MINE_MOBS, npcId))
		{
			if (Rnd.chance(DROP_CHANCE))
			{
				st.giveItems(MINERAL_FRAGMENT, (int) Config.RATE_QUESTS_REWARD * 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		
		return null;
	}
}
