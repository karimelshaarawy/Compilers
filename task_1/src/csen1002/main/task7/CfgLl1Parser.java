package csen1002.main.task7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Write your info here
 * 
 * @name Karim Gamal Elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class CfgLl1Parser {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG, the First sets of
	 *            each right-hand side, and the Follow sets of each variable. The
	 *            string representation follows the one in the task description
	 */

	ArrayList<String> variables = new ArrayList<String>();
	ArrayList<String> terminals = new ArrayList<String>();
	HashMap<String, ArrayList<String>> rules = new HashMap<>();
	HashMap<String, ArrayList<String>> follow = new HashMap<>();
	HashMap<String, HashMap<String, String>> parseTable = new HashMap<>();

	public CfgLl1Parser(String input) {
		// TODO Auto-generated constructor stub
		String[] mainParts = input.split("#");
		String[] temp = mainParts[0].split(";");
		for(int i=0;i<temp.length;i++) {
			variables.add(temp[i]);
		}
		String[] temp1 = mainParts[1].split(";");
		for(int i=0;i<temp1.length;i++) {
			terminals.add(temp1[i]);
		}
		String[] parts = mainParts[2].split(";");
		for(int i =0;i<parts.length;i++) {
			String[] halves = parts[i].split("/");
			String[] rulesString = halves[1].split(",");
			ArrayList<String> rulesArray = new ArrayList<>();
			for (int j = 0; j < rulesString.length; j++) {
				rulesArray.add(rulesString[j]);
			}
			rules.put(halves[0], rulesArray);
		}
		String[] followParts = mainParts[4].split(";");
		for(int i =0;i<followParts.length;i++) {
			String[] halves = followParts[i].split("/");
			String[] rulesString = halves[1].split(",");
			ArrayList<String> followArray = new ArrayList<>();
			for (int j = 0; j < rulesString.length; j++) {
				followArray.add(rulesString[j]);
			}
			follow.put(halves[0], followArray);
		}
		String[] firstParts = mainParts[3].split(";");
		for(int i =0;i<firstParts.length;i++) {
			String[] halves = firstParts[i].split("/");
			String[] firstString = halves[1].split(",");
			ArrayList<String> currentRules = rules.get(halves[0]);
			parseTable.put(halves[0], new HashMap<>());
			HashMap<String, String> targetMap = parseTable.get(halves[0]);
			for(int j=0;j<firstString.length;j++) {
				if(firstString[j].equals("e")) {
					ArrayList<String> followTarget = follow.get(halves[0]);
					for(int k=0;k<followTarget.get(0).length();k++) {
						targetMap.put(followTarget.get(0).charAt(k)+"", "e");
					}
				}else {
					for(int k=0;k<firstString[j].length();k++)
						targetMap.put(firstString[j].charAt(k)+"", currentRules.get(j));
				}
			}

		}
	}

	/**
	 * @param input The string to be parsed by the LL(1) CFG.
	 * 
	 * @return A string encoding a left-most derivation.
	 */
	public String parse(String input) {
		// TODO Auto-generated method stub
		ArrayList<String> result = new ArrayList<>();
		result.add("S");
		Stack<String> stack = new Stack<>();
		stack.push("$");
		stack.push("S");
		while(!stack.isEmpty() && !input.equals("")) {
			String top = stack.pop();
			if(terminals.contains(top)) {
				if(top.equals(input.charAt(0)+""))
					input = input.substring(1, input.length());
				else {
					result.add("ERROR");
					break;
				}
			}else {
				if(variables.contains(top)) {
					String target = parseTable.get(top).get(input.charAt(0)+"");
					if(target == null) {
						result.add("ERROR");
						break;
					}
					if(!target.equals("e")) {
					for(int k= target.length()-1;k>=0;k--) {
						stack.push(target.charAt(k)+"");
					}
					String tobeSub = result.get(result.size()-1);
					for(int i=0;i<tobeSub.length();i++) {
						if(variables.contains(tobeSub.charAt(i)+"")) {
							String newString = tobeSub.substring(0,i)+ target+ tobeSub.substring(i+1,tobeSub.length());
							result.add(newString);
							break;
						}
					}
				}else {
					String tobeSub = result.get(result.size()-1);
					for(int i=0;i<tobeSub.length();i++) {
						if(variables.contains(tobeSub.charAt(i)+"")) {
							String newString = tobeSub.substring(0,i)+ tobeSub.substring(i+1,tobeSub.length());
							result.add(newString);
							break;
						}
					}
				}
			}
			}
			if(stack.peek().equals("$")&& input.equals(""))
				break;
			else if(stack.peek().equals("$")) {
				result.add("ERROR");
				break;
			}else if(input.equals("")) {
				result.add("ERROR");
				break;
			}
		}
		
		String resultString ="";
		for(int i=0;i<result.size();i++)
			resultString+= result.get(i)+";";
		
		return resultString.substring(0,resultString.length()-1);
	}
	public static void main(String[] args) {
		CfgLl1Parser parser = new CfgLl1Parser("S;T#a;c;i#S/iST,e;T/cS,a#S/i,e;T/c,a#S/$ca;T/$ca");
		System.out.print(parser.parse("iiac"));
	}

}
