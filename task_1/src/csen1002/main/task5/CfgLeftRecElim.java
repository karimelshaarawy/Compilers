package csen1002.main.task5;

import java.util.ArrayList;
import java.util.Collections;



/**
 * Write your info here
 * 
 * @name Karim Elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class CfgLeftRecElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	
	ArrayList<String> variables = new ArrayList<String>();
	String variablesString ="";
	String terminals = "";
	ArrayList<Transition> rules = new ArrayList<Transition>();
	public CfgLeftRecElim(String cfg) {
		// TODO Auto-generated constructor stub
		String[] mainParts = cfg.split("#");
		variablesString = mainParts[0];
		String[] temp = mainParts[0].split(";");
		for(int i=0;i<temp.length;i++) {
			variables.add(temp[i]);
		}
		terminals = mainParts[1];
		String[] parts = mainParts[2].split(";");
		for(int i =0;i<parts.length;i++) {
			String[] halves = parts[i].split("/");
			Transition newTransition = new Transition(halves[0],halves[1].split(","));
			rules.add(newTransition);
		}
	}
	
	static class Transition{
		String ls;
		ArrayList<String> rs;
		ArrayList<String> removed;
		public Transition(String ls,String[] rsArray) {
			this.ls = ls;
			this.rs = new ArrayList<String>();
			this.removed = new ArrayList<String>();
			for(int i=0;i<rsArray.length;i++)
				rs.add(rsArray[i]);
		}
		public Transition(String ls,ArrayList<String> rs) {
			this.ls=ls;
			this.rs = rs;
		}
	}

	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result ="";
		for(int i=0;i<variables.size();i++)
			result+= variables.get(i)+";";
		result = result.substring(0,result.length()-1)+"#"+terminals+"#";
		for(int i=0;i< rules.size();i++) {
			Transition current = rules.get(i);
			
			String newWord = current.ls+"/";
			for(int j =0;j<current.rs.size();j++)
				newWord+= current.rs.get(j)+",";
			result += newWord.substring(0,newWord.length()-1)+";";
		}
		
		return result.substring(0,result.length()-1);
		
	}

	/**
	 * Eliminates Left Recursion from the grammar
	 */
	public void eliminateLeftRecursion() {
		// TODO Auto-generated method stub
		
		int size = rules.size();
		for(int i=0;i<size;i++) {
			Transition currentTransition = rules.get(i);
			ArrayList<String> currentList = currentTransition.rs;
			for(int j=0;j<currentList.size();j++) {
				String current = currentList.get(j);
				if(current.charAt(0)>='A' && current.charAt(0)<='Z' && variables.indexOf(current.charAt(0)+"")<variables.indexOf(currentTransition.ls)) {
					String suffix = current.substring(1);
					int index = j;
					
					for(int k=0;k<rules.size();k++) {
						if(rules.get(k).ls.equals(current.charAt(0)+"")) {
							ArrayList<String> targetList = rules.get(k).rs;
							for(int m=0;m<targetList.size();m++) {
								currentList.add(index, targetList.get(m)+suffix);
								index++;
							}
							
							
						}
					}
				currentList.remove(current);
				j--;
				}
			}
			boolean flag = false;
			ArrayList<String> alpha = new ArrayList<>();
			ArrayList<String> beta = new ArrayList<>();
			for(int j=0;j<currentList.size();j++) {
				String current = currentList.get(j);
				if(current.charAt(0)==currentTransition.ls.charAt(0)) {
					flag = true;
					alpha.add(current.substring(1));
				}else {
					beta.add(current);
				}
			}
			if(flag) {
				ArrayList<String> newR1 = new ArrayList<>();
				ArrayList<String> newR2 = new ArrayList<>();
				for(int k =0;k<beta.size();k++) {
					newR1.add(beta.get(k)+currentTransition.ls+"'");
				}
				for(int k =0;k<alpha.size();k++) {
					newR2.add(alpha.get(k)+currentTransition.ls+"'");
				}
				newR2.add("e");
				Transition S1 = new Transition(currentTransition.ls, newR1);
				Transition S2 = new Transition(currentTransition.ls+"'", newR2);
				rules.add(i, S1);
				rules.remove(currentTransition);
				rules.add(S2);
				variables.add(currentTransition.ls+"'");
			}
			
		}
	}

}
