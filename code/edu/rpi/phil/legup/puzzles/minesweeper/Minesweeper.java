package edu.rpi.phil.legup.puzzles.minesweeper;

import java.util.Map;
import java.util.Vector;

import edu.rpi.phil.legup.BoardImage;
import edu.rpi.phil.legup.BoardState;
import edu.rpi.phil.legup.CaseRule;
import edu.rpi.phil.legup.Contradiction;
import edu.rpi.phil.legup.PuzzleModule;
import edu.rpi.phil.legup.PuzzleRule;
import edu.rpi.phil.legup.Selection;
import edu.rpi.phil.legup.newgui.LEGUP_Gui;
import edu.rpi.phil.legup.puzzles.lightup.RuleEmptyCorners;
import edu.rpi.phil.legup.puzzles.lightup.RuleFinishWithBulbs;
import edu.rpi.phil.legup.puzzles.lightup.RuleFinishWithEmpty;
import edu.rpi.phil.legup.puzzles.lightup.RuleLightBlueInLight;
import edu.rpi.phil.legup.puzzles.lightup.RuleMustLight;
import edu.rpi.phil.legup.puzzles.lightup.RuleWhiteInLight;

public class Minesweeper extends PuzzleModule {

	@Override
	public Map<String, Integer> getSelectableCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> getUnselectableCells() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<PuzzleRule> getRules() {
		Vector <PuzzleRule>ruleList = new Vector <PuzzleRule>();
		ruleList.add(new RuleFinishWithBombs());
		ruleList.add(new RuleFinishWithEmptyMinesweeper());
		return ruleList;
	}

	@Override
	public Vector<Contradiction> getContradictions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<CaseRule> getCaseRules() {
		// TODO Auto-generated method stub
		return null;
	}

}
