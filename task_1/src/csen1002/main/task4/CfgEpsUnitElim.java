package csen1002.main.task4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Write your info here
 * 
 * @name karim elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class CfgEpsUnitElim {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	
	String variables = "";
	String terminals = "";
	ArrayList<Transition> rules = new ArrayList<Transition>();
	public CfgEpsUnitElim(String cfg) {
		// TODO Auto-generated constructor stub
		String[] mainParts = cfg.split("#");
		variables = mainParts[0];
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
		boolean eliminated = false;
		ArrayList<String> rs;
		ArrayList<String> removed;
		public Transition(String ls,String[] rsArray) {
			this.ls = ls;
			this.rs = new ArrayList<String>();
			this.removed = new ArrayList<String>();
			for(int i=0;i<rsArray.length;i++)
				rs.add(rsArray[i]);
		}
	}
	/**
	 * @return Returns a formatted string representation of the CFG. The string
	 *         representation follows the one in the task description
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		String result = variables+"#"+terminals+"#";
		for(int i=0;i< rules.size();i++) {
			Transition current = rules.get(i);
			Collections.sort(current.rs);
			String newWord = current.ls+"/";
			for(int j =0;j<current.rs.size();j++)
				newWord+= current.rs.get(j)+",";
			result += newWord.substring(0,newWord.length()-1)+";";
		}
		
		return result.substring(0,result.length()-1);
	}

	/**
	 * Eliminates Epsilon Rules from the grammar
	 */
	public void eliminateEpsilonRules() {
		// TODO Auto-generated method stub
		boolean flag = true;
		while(flag) {
			flag = false;
			for(int i=0;i<rules.size();i++) {
				Transition current = rules.get(i);
				if(current.rs.contains("e")) {
					current.rs.remove("e");
					current.eliminated = true;
					flag = true;
					char target = current.ls.charAt(0);
					/////////////////////
					for(int j=0;j<rules.size();j++) {
						Transition currentTransition = rules.get(j);
						for(int k=0;k<currentTransition.rs.size();k++) {
							String currentWord = currentTransition.rs.get(k);
							for(int m=0;m<currentWord.length();m++) {
								if(currentWord.length()==1&& currentWord.charAt(0)==target) {
									if(currentTransition.eliminated==false)
										currentTransition.rs.add("e");
									break;
								}
								if(currentWord.charAt(m)== target) {
									String newWord = currentWord.substring(0,m)+currentWord.substring(m+1);
									if(!currentTransition.rs.contains(newWord))
										currentTransition.rs.add(newWord);
								}
							}
						}
						
					}
					
				}
			}
		}
	}

	/**
	 * Eliminates Unit Rules from the grammar
	 */
	public void eliminateUnitRules() {
		// TODO Auto-generated method stub
		boolean flag = true;
		while(flag) {
			flag = false;
		for(int i=0;i<rules.size();i++) {
			Transition currentRule = rules.get(i);
			ArrayList<String> currentList = currentRule.rs;
			for(int j=0;j<currentList.size();j++) {
				String currentWord = currentList.get(j);
				if(currentWord.length()==1 && currentWord.charAt(0)>='A'&& currentWord.charAt(0)<='Z' ) {
					flag = true;
					if(!currentRule.removed.contains(currentWord)) {
						currentRule.removed.add(currentWord);
						for (int k=0;k<rules.size();k++) {
							if(rules.get(k).ls.equals(currentWord)) {
//								currentRule.rs.addAll(rules.get(k).rs);
								ArrayList<String> listToBeAdded = rules.get(k).rs;
								for(int l=0;l<listToBeAdded.size();l++) {
									if(!currentList.contains(listToBeAdded.get(l)))
										currentList.add(listToBeAdded.get(l));
								}
								break;
							}
						}
						
					}
					currentList.remove(currentWord);
				}

			}
		}
		}
//		for(int i=0;i<rules.size();i++) {
//			Transition curretTransition = rules.get(i);
//			ArrayList<String> currentList = curretTransition.rs;
//			for(int j=0;j<currentList.size();j++) {
//				String currentWord = currentList.get(j);
//				if(currentWord.length()==1 && currentWord.charAt(0)>='A'&& currentWord.charAt(0)<='Z' ) {
//				currentList.remove(currentWord);
//				}
//		}
//		}
	}

}
	
