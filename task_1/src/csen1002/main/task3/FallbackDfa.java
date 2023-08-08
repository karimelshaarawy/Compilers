package csen1002.main.task3;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Karim Gamal Elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class FallbackDfa {

	/**
	 * Constructs a Fallback DFA
	 * 
	 * @param fdfa A formatted string representation of the Fallback DFA. The string
	 *             representation follows the one in the task description
	 */
	
	String states ="";
	String alphapet = "";
	String start = "";
	ArrayList<String> accept = new ArrayList<String>();
	Transition[] transitions = new Transition[0];
	
	public FallbackDfa(String fdfa) {
		// TODO Auto-generated constructor stub
		
		String[] mainParts = fdfa.split("#");
		alphapet = mainParts[1];
		start = mainParts[3];
		states = mainParts[0];
		String[] transitionPart = mainParts[2].split(";");
		transitions = new Transition[transitionPart.length];
		for(int i=0;i<transitions.length;i++) {
			String[] parts = transitionPart[i].split(",");
			transitions[i] = new Transition(parts[0],parts[1],parts[2]);
		}
		String[] acceptStates = mainParts[4].split(";");
		for(int i=0;i<acceptStates.length;i++)
			accept.add(acceptStates[i]);
		
		
	}

	/**
	 * @param input The string to simulate by the FDFA.
	 * 
	 * @return Returns a formatted string representation of the list of tokens. The
	 *         string representation follows the one in the task description
	 */
	public String run(String input) {
		// TODO Auto-generated method stub
		String result = "" ;
		Stack<String> stack = new Stack<String>();
		stack.push(start);
		int l =0;
		int r = 0;
		boolean flag = true;
		while(flag) {
			String cur = start;
			for(;l<input.length();l++) {
				String target = input.charAt(l) + "";
				for(int i=0;i<transitions.length;i++) {
					Transition currentTransition = transitions[i];
					if(currentTransition.start.equals(cur)&& currentTransition.input.equals(target)) {
						stack.push(currentTransition.end);
						cur = currentTransition.end;
						break;
					}
				}
			}
			if(stack.isEmpty())
				break;
			String end = stack.peek();
			while(!stack.isEmpty()) {
				String currentState = stack.pop();
				l--;
				if(accept.contains(currentState)) {
					l++;
					result+= input.substring(r, l) +","+ currentState +";";
					r = l;
					break;
				}
				if(stack.isEmpty()|| l<r) {
					flag = false;
					result += input.substring(r,input.length())+","+end+";";
					break;
				}
					
			}
			stack.clear();
		}
		
		return result.substring(0,result.length()-1);
	}
	
	static class Transition{
		String start;
		String input;
		String end;
		public Transition(String start,String input,String end) {
			this.start = start;
			this.input = input;
			this.end = end;
		}
		
	}
	
}
