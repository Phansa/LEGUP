package edu.rpi.phil.legup.puzzles.minesweeper;

import javax.swing.ImageIcon;

import edu.rpi.phil.legup.PuzzleRule;

public class RuleFinishWithEmptyMinesweeper extends PuzzleRule{

	private static final long serialVersionUID = 464884358611362848L;

	@Override
	public String getImageName() {
		return "images/minesweeper/rules/FinishWithEmpty.png"; 
	}
	
	protected RuleFinishWithEmptyMinesweeper()
	 {
		setName("Finish with empty");
		description = "The remaining unknowns around a block must be empty if the number is satisfied.";
	 }

}
