package csen1002.main.task1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name karim gamal elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class RegExToNfa {

	/**
	 * Constructs an NFA corresponding to a regular expression based on Thompson's
	 * construction
	 * 
	 * @param input The alphabet and the regular expression in postfix notation for
	 *              which the NFA is to be constructed
	 */
	
	String alphapet = "" ;
	String states = "";
	String start = "";
	String end = "";
	String transitions = "";
	ArrayList<String> list ;
	
	public static ArrayList<String> replace(ArrayList<String> list,String removed,String added){
		ArrayList<String> replaced = new ArrayList<String>();
		int size = list.size();
		for(int i=0;i<size;i++) {
			String[] parts = list.remove(0).split(",");
			String toBeAdded = "";
			for(int j = 0;j<parts.length;j++) {
				if(parts[j].equals(removed))
					parts[j]=added;
				toBeAdded+= parts[j]+",";
			}
			replaced.add(toBeAdded.substring(0, toBeAdded.length()-1));
		}
		
		return replaced;
	}
	
	public RegExToNfa(String input) {
		// TODO Auto-generated constructor stub
		
		String[] tokens = input.split("#");
		alphapet = tokens[0];
		String rest = tokens[1];
		
		Stack<String> stack = new Stack<String>();
		list = new ArrayList<String>();
		ArrayList<String> statesList = new ArrayList<String>();
		start = "0";
		int stateCount = 0;
		for(int i=0;i< rest.length();i++) {
			char current = rest.charAt(i);
			if(current>='a' && current <= 'z') {
				String currentString = stateCount+"," + current +","+ (stateCount+1);
				statesList.add(stateCount+"");
				statesList.add((stateCount+1)+"");
				stateCount += 2;
				stack.push(currentString);
				list.add(currentString);
			}else if(current =='.') {
				String second = stack.pop();
				String first = stack.pop();
				String[] firstParts = first.split(",");
				String[] secondParts = second.split(",");
				String firstPart = firstParts[firstParts.length-1];
				String secondPart = secondParts[0];
				String thirdPart = secondParts[2];
				statesList.remove(secondPart);
				list = replace(list,secondPart,firstPart);
//				String result = firstPart + ","+secondParts[1]+","+thirdPart;
//				list.add(result);
//				String toBeRemoved = secondParts[0]+","+secondParts[1]+","+secondParts[2] ;
//				list.remove(toBeRemoved);
				String concatenated = first +",e,"+ secondParts[secondParts.length-1];
				stack.push(concatenated);
			}else if(current =='*') {
				String target = stack.pop();
				String[] targetParts = target.split(",");
				String first = targetParts[0];
				String last = targetParts[targetParts.length-1];
				String add1= stateCount + ",e,"+ first;
				String add2 = last + ",e," + (stateCount+1);
				String add3 = last + ",e," + first;			
				String add4 = stateCount + ",e," + (stateCount+1);
				String toBePushed = stateCount + ",e,"+ target + ",e," + (stateCount+1);
				statesList.add(stateCount+"");
				statesList.add((stateCount+1)+"");
				stateCount +=2;
				list.add(add1);
				list.add(add2);
				list.add(add3);
				list.add(add4);
				stack.push(toBePushed);
				
				
			}else if(current =='|') {
				String second = stack.pop();
				String first = stack.pop();
				String[] firstParts = first.split(",");
				String[] secondParts = second.split(",");
				String firstTerm = firstParts[0];
				String secondTerm = secondParts[0];
				String lastTerm1 = firstParts[firstParts.length-1];
				String lastTerm2 = secondParts[secondParts.length-1];
				String add1= stateCount + ",e,"+ firstTerm;
				String add2= stateCount + ",e,"+ secondTerm;
				String add3 = lastTerm1 + ",e," + (stateCount+1);
				String add4 = lastTerm2 + ",e," + (stateCount+1);
				String toBePushed = stateCount + ",e,"+ first + ",e," + (stateCount+1);
				statesList.add(stateCount+"");
				statesList.add((stateCount+1)+"");
				stateCount+=2;
				list.add(add1);
				list.add(add2);
				list.add(add3);
				list.add(add4);
				stack.push(toBePushed);
				


				
			}
			
			
			
			
			
		}

		String lastStackElement = stack.pop();
		String[] lastElementParts = lastStackElement.split(","); 
		start = lastElementParts[0];
		end = lastElementParts[lastElementParts.length-1];
		Collections.sort(list, new Comparator<String>() {
		    @Override
		    public int compare(String one,String two) {
		    	String[] oneParts = one.split(",");
		    	String[] twoParts = two.split(",");
		    	int oneFirst = Integer.parseInt(oneParts[0]);
		    	int oneLast = Integer.parseInt(oneParts[2]);
		    	int twoFirst = Integer.parseInt(twoParts[0]);
		    	int twoLast = Integer.parseInt(twoParts[2]);
		    	
		    	
		        if(oneFirst==twoFirst) {
		        	if (oneLast>twoLast)
		        		return 1;
		        	else
		        		return -1;
		        	
		        	
		        
		        }else if (oneFirst>twoFirst)
	        		return 1;
	        	else
	        		return -1;
		        }
		    
		});
		for(int i = 0;i<statesList.size();i++) {
			states += statesList.get(i)+";";
		}
		for(int i = 0;i<list.size();i++) {
			transitions += list.get(i)+";";
		}
		
	}

	/**
	 * @return Returns a formatted string representation of the NFA. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = states.substring(0, states.length()-1) + "#" + alphapet + "#" + transitions.substring(0, transitions.length()-1)+ "#"+start+"#"+end;
		return result;
	}
	
	public static void main (String args) {
		System.out.print(false);
	}

}
