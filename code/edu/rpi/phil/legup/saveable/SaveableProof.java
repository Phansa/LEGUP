package edu.rpi.phil.legup.saveable;

//import java.beans.XMLDecoder;
//import java.beans.XMLEncoder;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;

import javax.swing.JOptionPane;

import edu.rpi.phil.legup.BoardState;
import edu.rpi.phil.legup.Legup;
import edu.rpi.phil.legup.newgui.TreePanel;

public class SaveableProof
{
	public SaveableProofState[] states;
	public Vector<SaveableProofTransition> transitions;
	public PrintWriter out;
	public File file;
	private SaveableProof(BoardState state)
	{
		states = new SaveableProofState[state.calcID()];
		transitions = new Vector<SaveableProofTransition>();
		//state.makeSaveableProof(states, transitions);
	}
	
	/*private BoardState toBoardState()
	{
		BoardState[] bstates = new BoardState[states.length];
		for(int i = 0; i < states.length; ++i)
		{
			bstates[i] = BoardState.fromSaveableProofState(states[i]);
		}
		
		for(SaveableProofTransition t : transitions)
		{
			bstates[t.id1].addTransitionFrom(bstates[t.id2], t.justification, t.isCaseRule);
		}
		
		bstates[0].recalculateLocation();
		
		return bstates[0];
	}*/
	public SaveableProof(){}
/*LOAD XML	
	//XML functions
	
	public static BoardState loadProof(String filename)
    {
		SaveableProof rv = null;
    	
    	try
    	{
	    	XMLDecoder d = new XMLDecoder(
	                new BufferedInputStream(
	                		//new GZIPInputStream( //For compression
	                    new FileInputStream(filename)));
			Object result = d.readObject();
			d.close();
			
			rv = (SaveableProof)result;
    	}
    	catch (Exception e)
    	{
    		JOptionPane.showMessageDialog(null,"Error loading: " + e.toString());
    		rv = null;
    	}
    	
    	BoardState state = rv.toBoardState(); 
    	
    	if(state == null)
    		JOptionPane.showMessageDialog(null,"Board Load Failed");
    		
    	return state;
    }
*/
//NORMAL LOAD
	public static BoardState loadProof(String filename) throws IOException
	{
		System.out.println("Loading...\n");
		BoardState state;
		Scanner scan = new Scanner(new File(filename));
		String str;
		String tmp;
		String name;
		String[] tmp_str_array;
		int in;
		Vector <Integer> labels = new Vector<Integer>();
		//iterate through file
		//name
		tmp = scan.next();
		if(!tmp.equals("Name:"))System.out.println("Potentially invalid input at Name.");
		System.out.println("Name: " + (name = scan.nextLine()));
		//puzzle
		tmp = scan.next();
		if(!tmp.equals("Puzzle:"))System.out.println("Potentially invalid input at Puzzle.");
		str = scan.next();
		System.out.println("Puzzle: " + str);
		//state.setPuzzleName("Battleship");
		//System.out.println("stupid loadme is working\n");
		
		//height
		tmp = scan.next();
		if(!tmp.equals("Height:"))System.out.println("Potentially invalid input at Height.");
		in = scan.nextInt();
		System.out.println("Height: " + in);
		//width
		tmp = scan.next();
		if(!tmp.equals("Width:"))System.out.println("Potentially invalid input at Width.");
		//initialize new board while grabbing width
		state = new BoardState(in, scan.nextInt());
		System.out.println("Width: " + state.getWidth());
		//now that board exists, set name and offset
		state.setPuzzleName(str);
		state.setOffset(new Point(0,0));
		state.setLocation(new Point(0,0));
		
		Legup.getInstance().loadPuzzleModule(state.getPuzzleName());
		
		//top labels
		tmp = scan.next();
		if(!tmp.equals("topLabels:"))System.out.println("Potentially invalid input at topLabels.");
		System.out.println("topLabels:");
		//System.out.println(str);
		str = scan.next();
		while (!str.equals("bottomLabels:"))
		{
			in = Integer.parseInt(str);
			System.out.print(in + " ");
			labels.add(in);
			str = scan.next();
		}
		System.out.print("\n");
		state.setTopLabels(new int[labels.size()]);
		for (int x = 0; x < labels.size(); x++)
			state.getTopLabels()[x] = labels.get(x);
		labels.clear();
		
		//bottom labels
		System.out.println("bottomLabels:");
		str = scan.next();
		while (!str.equals("leftLabels:"))
		{
			in = Integer.parseInt(str);
			System.out.print(in + " ");
			labels.add(in);
			str = scan.next();
		}
		System.out.print("\n");
		state.setBottomLabels(new int[labels.size()]);
		for (int x = 0; x < labels.size(); x++)
			state.getBottomLabels()[x] = labels.get(x);
		labels.clear();
		
		//left labels
		System.out.println("leftLabels:");
		str = scan.next();
		while (!str.equals("rightLabels:"))
		{
			in = Integer.parseInt(str);
			System.out.print(in + " ");
			labels.add(in);
			str = scan.next();
		}
		System.out.print("\n");
		state.setLeftLabels(new int[labels.size()]);
		for (int x = 0; x < labels.size(); x++)
			state.getLeftLabels()[x] = labels.get(x);
		labels.clear();
		
		//right labels
		System.out.println("rightLabels:");
		str = scan.next();
		while (!str.equals("hintCells:"))
		{
			in = Integer.parseInt(str);
			System.out.print(in + " ");
			labels.add(in);
			str = scan.next();
		}
		System.out.print("\n");
		state.setRightLabels(new int[labels.size()]);
		for (int x = 0; x < labels.size(); x++)
			state.getRightLabels()[x] = labels.get(x);
		labels.clear();
		
		//hint cells;
		System.out.println("hintCells:");
		//processing of "hintCells" should go here, I don't know what those are, so I'm not
		//yet removing this from the format - Avi
		
		//extra data
		//str = scan.next();
		
		//board
		tmp = scan.next();
		if(!tmp.equals("Board:"))System.out.println("Potentially invalid input at Board.");
		System.out.println("Board:");
		for (int i = 0; i < state.getHeight(); i++)
		{
			for (int j = 0; j < state.getWidth(); j++)
			{
				state.getBoardCells()[i][j] = scan.nextInt();
				System.out.print(state.getBoardCells()[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.println("board cells loaded...");
		System.out.flush();
		
		//transitions
		str = scan.next();
		BoardState currentstate = state;
		while (scan.hasNext() != scan.hasNextInt())
		{
			str = scan.nextLine();
			//JOptionPane.showMessageDialog(null,str);
			System.out.println(str);
			if (str.equals("newState:"))
			{
				//add new child to parent
				//currentstate.getTransitionsFrom().add(new BoardState(state));
				if(currentstate == null)System.out.println("currentstate == null");
				currentstate.addTransitionFromWithNoMovement();
				
				//add parent to child
				//currentstate.getTransitionsFrom().lastElement().getTransitionsTo().add(currentstate);

				//step in to child
				currentstate = currentstate.getTransitionsFrom().lastElement();
				
				//set offset equal to the stored value
				tmp = scan.nextLine();
				System.out.println("Offset: "+tmp);
				tmp_str_array = tmp.split(" ");
				currentstate.setOffsetRaw(new Point(Integer.valueOf(tmp_str_array[0]).intValue(),Integer.valueOf(tmp_str_array[1]).intValue()));
				
				//add justification to child
				//System.out.println(scan.nextLine());
				tmp = scan.nextLine();
				System.out.println("Justification: "+tmp);
				currentstate.setJustification(Legup.getInstance().getPuzzleModule().getRuleByName(tmp));
				System.out.println("test");
				//add case rule to child
				tmp = scan.nextLine();
				System.out.println("CaseRule: "+tmp);
				currentstate.setCaseRuleJustification(Legup.getInstance().getPuzzleModule().getCaseRuleByName(tmp));
				
				//is this a transition?
				tmp = scan.nextLine();
				System.out.println("Transition: "+tmp);
				currentstate.setModifiableState(Boolean.valueOf(tmp).booleanValue());
				
				//get extraData
				tmp = scan.nextLine();
				System.out.println("extraData: "+tmp);
				tmp_str_array = tmp.split(";");
				for(int c1=0;c1<tmp_str_array.length;c1++)
				{
					System.out.println(tmp_str_array[c1]);
					Object exdat = Legup.getInstance().getPuzzleModule().extraDataFromString(tmp_str_array[c1]); 
					if(exdat != null)currentstate.getExtraData().add(exdat);
				}
				//get extraDataDelta
				tmp = scan.nextLine();
				System.out.println("extraDataDelta: "+tmp);
				tmp_str_array = tmp.split(";");
				for(int c1=0;c1<tmp_str_array.length;c1++)
				{
					System.out.println(tmp_str_array[c1]);
					Object exdat = Legup.getInstance().getPuzzleModule().extraDataFromString(tmp_str_array[c1]);
					if(exdat != null)currentstate.extraDataDelta.add(exdat);
				}
				//add point changes to child (if they exist)
				while(scan.hasNext() == scan.hasNextInt())
				{
					//nested if structure to partially deal with non-triplets
					int tmp_x = 0;
					int tmp_y = 0;
					int tmp_cell = 0;
					if(scan.hasNext() == scan.hasNextInt())tmp_x = scan.nextInt();
					if(scan.hasNext() == scan.hasNextInt())
					{
						tmp_y = scan.nextInt();
						if(scan.hasNext() == scan.hasNextInt())
						{
							tmp_cell = scan.nextInt();
							tmp_cell = ((/*currentstate.isModifiable()*/ false) ? Math.abs(tmp_cell) : -Math.abs(tmp_cell));
							//change board cell at point
							currentstate.getChangedCells().add(new Point(tmp_x,tmp_y));
							currentstate.getBoardCells()[tmp_y][tmp_x] = tmp_cell;
							System.out.println("Changing ("+ tmp_x + "," + tmp_y + ") to " + tmp_cell);
						}
					}
				}
			}
			
			else if (str.equals("endLeaf:"))
			{
				//step out to parent (if we have a parent)
				if (!currentstate.getTransitionsTo().isEmpty())
				{
					System.out.println("numChilds: "+currentstate.getTransitionsFrom().size());
					System.out.println("numParents: "+currentstate.getTransitionsTo().size());
					currentstate = currentstate.getTransitionsTo().lastElement();
				}
			}
			
			//assume str is now the x of the modified cell
			/*else
			{
				//add point to child
				currentstate.getChangedCells().add(new Point(Integer.parseInt(str), scan.nextInt()));
				//change board cell at point
				currentstate.getBoardCells()[currentstate.getChangedCells().lastElement().y][currentstate.getChangedCells().lastElement().x] = scan.nextInt();
			}*/
		}
		int the_hash = scan.nextInt();
		System.out.println("Hash: \""+the_hash+"\"");
		scan.close();
		System.out.println("File Closed...");
		
		//set first state as unmodifiable
		for(int i = 0; i < state.getHeight(); i++)
			for(int j = 0; j < state.getWidth(); j++)
				state.setModifiableCell(j, i, false);
		System.out.println("...Done\n");
		return state;
	}
/*SAVE XML	
    public static boolean saveProof(BoardState state, String filename)
    {	
    	SaveableProof s = new SaveableProof(state);
    	
    	try
    	{
	    	 XMLEncoder e = new XMLEncoder(
	                 new BufferedOutputStream(
	                		 //new GZIPOutputStream( //For compression
	                     new FileOutputStream(filename)));
			e.writeObject(s);
			e.close();
    	}
    	catch (Exception e)
    	{
    		JOptionPane.showMessageDialog(null,"Error saving: " + e.getMessage());
    		return false;
    	}
    	
    	return true;
    }
*/
//NORMAL SAVE
	public static boolean saveProof(BoardState state, String filename) throws IOException
	{
		SaveableProof saveme = new SaveableProof(state);
		saveme.file = new File(filename);
		saveme.out = new PrintWriter(new FileWriter(filename));
		//write "static" state data
		String encrypt_me = "";
		encrypt_me += "Name: ";
		encrypt_me += "Bullwinkle Moose";
		encrypt_me += "\nPuzzle: ";
		encrypt_me += state.getPuzzleName();
		encrypt_me += "\nHeight: ";
		encrypt_me += state.getHeight();
		encrypt_me += "\nWidth: ";
		encrypt_me += state.getWidth();
		encrypt_me += "\ntopLabels:";
		for (int savelabel : state.getTopLabels())
		{
			encrypt_me += ' ';
			encrypt_me += savelabel;
		}
		encrypt_me += "\nbottomLabels:";
		for (int savelabel : state.getBottomLabels())
		{
			encrypt_me += ' ';
			encrypt_me += savelabel;
		}
		encrypt_me += "\nleftLabels:";
		for (int savelabel : state.getLeftLabels())
		{
			encrypt_me += ' ';
			encrypt_me += savelabel;
		}
		encrypt_me += "\nrightLabels:";
		for (int savelabel : state.getRightLabels())
		{
			encrypt_me += ' ';
			encrypt_me += savelabel;
		}
		encrypt_me += "\nhintCells:\n";
		for (Point savehints : state.getHintCells())
		{
			//SAVE HINTCELLS
		}
		encrypt_me += "\nBoard:\n";
		for (int i = 0; i < state.getHeight(); i++)
		{
			for (int j = 0; j < state.getWidth(); j++)
			{
				encrypt_me += state.getBoardCells()[i][j];
				encrypt_me += ' ';
			}
			encrypt_me += '\n';
		}
		//save transitions
		encrypt_me += "Transitions:\n";
		for (BoardState transist_state : state.getTransitionsFrom())
			encrypt_me += saveme.printTransitions(transist_state);
		saveme.out.print(encrypt_me);
		saveme.out.print(encrypt_me.hashCode());
		saveme.out.close();
		return true;
	}
	public String printTransitions(BoardState state)
	{
		String encrypt_me = "";
		encrypt_me += "newState:\n";
		encrypt_me += state.getOffset().x + " " + state.getOffset().y + "\n";
		encrypt_me += state.getJustification();
		encrypt_me += '\n';
		encrypt_me += state.getCaseRuleJustification();
		encrypt_me += '\n';
		/*
		//print offset
		encrypt_me += state.getOffset().x;
		encrypt_me += ' ';
		encrypt_me += state.getOffset().y;
		encrypt_me += '\n';
		*/
		encrypt_me += state.isModifiable();
		encrypt_me += '\n';
		
		for(int c1=0;c1<state.getExtraData().size();c1++)
		{
			encrypt_me += state.getExtraData().get(c1).toString();
			if(c1 != state.getExtraData().size()-1)encrypt_me += ";";
		}
		encrypt_me += "\n";
		for(int c1=0;c1<state.extraDataDelta.size();c1++)
		{
			encrypt_me += state.extraDataDelta.get(c1).toString();
			if(c1 != state.getExtraData().size()-1)encrypt_me += ";";
		}
		encrypt_me += "\n";
		for(Point cell : state.getChangedCells())
		{
			encrypt_me += cell.x;
			encrypt_me += ' ';
			encrypt_me += cell.y;
			encrypt_me += ' ';
			encrypt_me += state.getBoardCells()[cell.y][cell.x];
			encrypt_me += '\n';
		}
		//recurse through children
		for (BoardState transition_state : state.getTransitionsFrom())
			encrypt_me += printTransitions(transition_state);
				
		encrypt_me += "endLeaf:\n";
		
		return encrypt_me;
	}
	public SaveableProofState[] getStates()
	{
		return states;
	}

	public void setStates(SaveableProofState[] states)
	{
		this.states = states;
	}

	public Vector<SaveableProofTransition> getTransitions()
	{
		return transitions;
	}

	public void setTransitions(Vector<SaveableProofTransition> transitions)
	{
		this.transitions = transitions;
	}
}
