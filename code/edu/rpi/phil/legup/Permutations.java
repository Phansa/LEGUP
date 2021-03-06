package edu.rpi.phil.legup;

import java.awt.Point;
import java.util.Vector;
import edu.rpi.phil.legup.BoardState;
import edu.rpi.phil.legup.PuzzleModule;

public class Permutations
{
	public static void permutationGeneral(BoardState boardState, Vector<Point> cells, Vector<Integer> states, Vector<Integer> stateCounts, Vector<Integer> conditions, int defaultState, boolean allowUnlimitedDefault )
	{
		if(states.size() != stateCounts.size())
			return;
		
		//Remove states that break the condition
		for(int c = 0; c < cells.size( ); ++c)
		{
			if(!conditions.contains( boardState.getCellContents( cells.get( c ).x, cells.get( c ).y )))
			{
				cells.remove( c );
				--c;
			}
		}
		
		int numstates = 0;
		int defaultcount = 0;
		for(int i = 0; i < stateCounts.size( ); ++i)
		{
			if(states.get( i ) == null)
			{
				defaultcount = stateCounts.get( i );
			}
			numstates += stateCounts.get( i );
		}
		if(!allowUnlimitedDefault && cells.size() < numstates)
		{
			if(defaultcount > 0)
			{
				for(int i = 0; i < stateCounts.size( ); ++i)
				{
					if(states.get( i ) == null)
					{
						stateCounts.set( i, cells.size() - numstates - defaultcount );
						break;
					}
				}
			}
			else
			{
				stateCounts.add( cells.size() - numstates );
				states.add( null );
			}
		}
		else if(allowUnlimitedDefault)
		{
			for(int i = 0; i < stateCounts.size( ); ++i)
			{
				if(states.get( i ) == null)
				{
					stateCounts.set(i, cells.size());
					break;
				}
			}
		}
		
		
		permutationGeneral(boardState.addTransitionFrom( ), cells, states, stateCounts,0).deleteState();
		//boardState.arrangeChildren( );
	}
	
	public static void permutationGeneral(BoardState boardState, Vector<Point> cells, Vector<Integer> states, Vector<Integer> stateCounts)
	{
		if(states.size() != stateCounts.size())
			return;
		permutationGeneral(boardState.addTransitionFrom( ), cells, states, stateCounts,0).deleteState();
		//boardState.arrangeChildren( );
	}
	
	private static BoardState permutationGeneral(BoardState boardState, Vector<Point> cells, Vector<Integer> states, Vector<Integer> stateCounts, int current )
	{
		if(current >= cells.size())
		{
			BoardState newboardState = boardState.copy();
			boardState.getParents( ).get( 0 ).addTransitionFrom( newboardState, null );
			return newboardState;
		}
		Integer state;
		for(int i = 0; i < states.size(); ++i)
		{
			if(stateCounts.get(i) > 0)
			{
				state = states.get( i );
				if(state == null)
				{
					boardState.setCellContents( cells.get( current ).x, cells.get( current ).y, boardState.getParents( ).get( 0 ).getCellContents( cells.get( current ).x, cells.get( current ).y ));
					stateCounts.set( i, stateCounts.get( i ) - 1);
					boardState = permutationGeneral(boardState, cells, states, stateCounts, current + 1);
					stateCounts.set( i, stateCounts.get( i ) + 1);
				}
				else
				{
					boardState.setCellContents( cells.get( current ).x, cells.get(current).y, state);
					stateCounts.set( i, stateCounts.get( i ) - 1);
					boardState = permutationGeneral(boardState, cells, states, stateCounts, current + 1);
					stateCounts.set( i, stateCounts.get( i ) + 1);
				}
			}
		}
		return boardState;
	}
	
	public static void permutationRow(BoardState boardState, int row, Vector<Integer> states, Vector<Integer> stateCounts)
	{
		if(states.size() != stateCounts.size())
			return;
		permutationRow(boardState.addTransitionFrom( ), row, states, stateCounts,0).deleteState();
		//boardState.arrangeChildren( );
	}
	
	private static BoardState permutationRow(BoardState boardState, int row, Vector<Integer> states, Vector<Integer> stateCounts, int current)
	{
		if(current >= boardState.getWidth())
		{
			BoardState newboardState = boardState.copy();
			boardState.getParents( ).get( 0 ).addTransitionFrom( newboardState, null );
			return newboardState;
		}
		Integer state;
		for(int i = 0; i < states.size(); ++i)
		{
			if(stateCounts.get(i) > 0)
			{
				state = states.get( i );
				if(state == null)
				{
					boardState.setCellContents( current, row, boardState.getParents( ).get( 0 ).getCellContents( current, row ));
					stateCounts.set( i, stateCounts.get( i ) - 1);
					boardState = permutationRow(boardState, row, states, stateCounts, current + 1);
					stateCounts.set( i, stateCounts.get( i ) + 1);
				}
				else
				{
					boardState.setCellContents( current, row, state);
					stateCounts.set( i, stateCounts.get( i ) - 1);
					boardState = permutationRow(boardState, row, states, stateCounts, current + 1);
					stateCounts.set( i, stateCounts.get( i ) + 1);
				}
			}
		}
		return boardState;
	}
	
	public static void permutationRow(BoardState boardState, int row, Vector<Integer> states, Vector<Integer> stateCounts, Vector<Integer> conditions)
	{
		if(states.size() != stateCounts.size())
			return;
		permutationRow(boardState.addTransitionFrom( ), row, states, stateCounts,conditions,0).deleteState();
		//boardState.arrangeChildren( );
	}
	
	private static BoardState permutationRow(BoardState boardState, int row, Vector<Integer> states, Vector<Integer> stateCounts, Vector<Integer> conditions, int current)
	{
		if(current >= boardState.getWidth())
		{
			BoardState newboardState = boardState.copy();
			boardState.getParents( ).get( 0 ).addTransitionFrom( newboardState, null );
			return newboardState;
		}
		Integer state;
		if(conditions.contains( boardState.getParents( ).get( 0 ).getCellContents( current, row )))
		{	
			for(int i = 0; i < states.size(); ++i)
			{
				if(stateCounts.get(i) > 0)
				{
					state = states.get( i );
					if(state == null)
					{
						boardState.setCellContents( current, row, boardState.getParents( ).get( 0 ).getCellContents( current, row ));
						stateCounts.set( i, stateCounts.get( i ) - 1);
						boardState = permutationRow(boardState, row, states, stateCounts,conditions, current + 1);
						stateCounts.set( i, stateCounts.get( i ) + 1);
					}
					else
					{
						boardState.setCellContents( current, row, state);
						stateCounts.set( i, stateCounts.get( i ) - 1);
						boardState = permutationRow(boardState, row, states, stateCounts,conditions, current + 1);
						stateCounts.set( i, stateCounts.get( i ) + 1);
					}
				}
			}
		}
		else
		{
			boardState = permutationRow(boardState, row, states, stateCounts,conditions, current + 1);
		}
		return boardState;
	}
	
	public static void permutationCell(BoardState boardState, Point cell, Vector<Integer> states)
	{
		for(int i = 0; i < states.size( ); ++i)
		{
			BoardState newboardState = boardState.copy();
			boardState.addTransitionFrom( newboardState, null );
			newboardState.setCellContents( cell.x, cell.y, states.get( i ) );
		}
		//boardState.arrangeChildren( );
	}
	
	public static void caseContradictionFinder(BoardState boardState, PuzzleModule pm)
	{
		BoardState caseState,temp;
		for(int i = 0; i < boardState.getChildren( ).size( ); ++i)
		{
			caseState = boardState.getChildren( ).get( i );
			temp = caseState.copy( );
			temp.setLocation( new Point(temp.getLocation( ).x, temp.getLocation( ).y + 25 ));
			caseState.addTransitionFrom( temp, null );
			//caseState.arrangeChildren( );
			String valid = null;
			for(int j = 0; j < pm.getContradictions( ).size( ); ++j)
			{
				temp.setJustification( pm.getContradictions( ).get( j ) );
				valid = pm.getContradictions( ).get( j ).checkContradiction( temp );
				if(valid == null)
					break;
			}
			if(valid != null)
				temp.setJustification( null );
		}
	}
	public static int factorial(int n)
	{
		return (n>0)?(n*factorial(n-1)):1;
	}
	
	public static int permutation(int n, int r)
	{
		return factorial(n)/factorial(n-r);
	}
	
	public static int combination(int n, int r)
	{
		return permutation(n,r)/factorial(r);
	}
	
	public static int intPower(int base, int exponent)
	{
		return (exponent>0)?(base*intPower(base,exponent-1)):1;
	}
	
	//returns true iff the array 'arr' has exactly x instances of digit y
	public static boolean has_x_ys(int[] arr, int x, int y)
	{
		int num_ys = 0;
		for(int c1=0;c1<arr.length;c1++)
		{
			if(arr[c1] == y)num_ys++;
		}
		return (x == num_ys);
	}
	
	//modifies perm to contain the next permutation
	//returns false iff there is no next permutation constructible from the inputs 
	public static boolean nextPermutation(int[] perm, int numDefaults)
	{
		if(perm.length == 0)return false;
		PuzzleModule pm = Legup.getInstance().getPuzzleModule();
		int maximumDigit = pm.numAcceptableStates();
		int default_cell_type = pm.getNonunknownBlank();
		do
		{
			perm[0]++;
			for(int c1=0;c1+1<perm.length;c1++)
			{
				if(perm[c1] >= maximumDigit)
				{
					perm[c1]=1;
					perm[c1+1]++;
				}
				
			}
			if(perm[perm.length-1] >= maximumDigit)return false;
		}while(!has_x_ys(perm,numDefaults,default_cell_type) || !has_x_ys(perm,0,0)); 
		return true;
	}
}
