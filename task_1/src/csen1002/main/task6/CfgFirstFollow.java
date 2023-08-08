package csen1002.main.task6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



/**
 * Write your info here
 * 
 * @name karim gamal elshaarawy
 * @id 46-15042
 * @labNumber 17
 */

public class CfgFirstFollow {

	/**
	 * Constructs a Context Free Grammar
	 * 
	 * @param cfg A formatted string representation of the CFG. The string
	 *            representation follows the one in the task description
	 */
	
	ArrayList<String> variables = new ArrayList<String>();
	ArrayList<String> terminals = new ArrayList<String>();
	HashMap<String, ArrayList<String>> rules = new HashMap<>();
	HashMap<String, ArrayList<String>> first = new HashMap<>();

	public CfgFirstFollow(String cfg) {
		// TODO Auto-generated constructor stub
		String[] mainParts = cfg.split("#");
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
	}

	/**
	 * Calculates the First Set of each variable in the CFG.
	 * 
	 * @return A string representation of the First of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String first() {
		// TODO Auto-generated method stub
		for(int i=0;i<terminals.size();i++) {
		 ArrayList<String> newTerminal = new ArrayList<>();
		 newTerminal.add(terminals.get(i));
		 first.put(terminals.get(i), newTerminal);
		}
		for (int i = 0; i < variables.size(); i++) {
			first.put(variables.get(i), new ArrayList<String>());
		}
		boolean change = true;
		while (change) {
			change = false;
			for(Map.Entry<String, ArrayList<String>> set :
	             rules.entrySet()) {
				String key = set.getKey();
				ArrayList<String> value = set.getValue();
				for(int i=0;i<value.size();i++) {
					String rs = value.get(i);
					ArrayList<String> list = first.get(key);
					if(rs.equals("e")) {
						if(!list.contains("e")) {
							change = true;
							list.add("e");
							first.put(key, list);
							
						}
					}else {
						boolean empty = true;
						for(int j=0;j<rs.length();j++){
							if(!first.get(rs.charAt(j)+"").contains("e"))
								empty = false;
					}
						if(empty) {
							if(!list.contains("e")) {
								list.add("e");
								first.put(key, list);
							}
						}
					}
					
					for(int k=0;k<rs.length();k++) {
						if(!rs.equals("e")) {
						ArrayList<String> original = first.get(key);
						ArrayList<String> toBeAdded = first.get(rs.charAt(k)+"");
						Boolean result = merge(original, toBeAdded);
						if(result == true)
							change = true;
						first.put(key, original);
						if(!first.get(rs.charAt(k)+"").contains("e"))
							break;
						
					}}
					
				}
			};
			
		}
		String res = "";
		for (int i = 0; i < variables.size(); i++) {
			ArrayList<String> targetArrayList = first.get(variables.get(i));
			Collections.sort(targetArrayList);
			res+= variables.get(i)+"/";
			for(int j= 0;j<targetArrayList.size();j++)
				res+= targetArrayList.get(j);
			res+=";";
			
		}
		
		return res.substring(0,res.length()-1);
	}
	
	public static boolean merge(ArrayList<String> original,ArrayList<String> toBeAdded) {
		boolean flag = false;
		for(int i=0;i<toBeAdded.size();i++) {
			if(!toBeAdded.get(i).equals("e")&& ! original.contains(toBeAdded.get(i))) {
				original.add(toBeAdded.get(i));
				flag = true;
			}
		}
		return flag;

	}

	/**
	 * Calculates the Follow Set of each variable in the CFG.
	 * 
	 * @return A string representation of the Follow of each variable in the CFG,
	 *         formatted as specified in the task description.
	 */
	public String follow() {
		// TODO Auto-generated method stub
		this.first();
		HashMap<String, ArrayList<String>> follow = new HashMap<>();
		ArrayList<String> sArrayList = new ArrayList<>();
		sArrayList.add("$");
		follow.put("S", sArrayList);
		for(int i=1;i< variables.size();i++) {
			follow.put(variables.get(i), new ArrayList<String>());
		}
		boolean change = true;
		while (change) {
			change = false;
			for(Map.Entry<String, ArrayList<String>> set :
	             rules.entrySet()) {
				String key = set.getKey();
				ArrayList<String> value = set.getValue();
				for(int i=0;i<value.size();i++) {
					String rs = value.get(i);
					if(!rs.equals("e")) {
						for (int j = 0; j < rs.length(); j++) {
							boolean flag = true;
							if(variables.contains(rs.charAt(j)+"")) {
							for (int k = j+1; k < rs.length(); k++) {
								ArrayList<String> list = follow.get(rs.charAt(j)+"");
								Boolean result = merge(list, first.get(rs.charAt(k)+""));
								if(result)
									change = true;
								follow.put(rs.charAt(j)+"", list);
								if(!first.get(rs.charAt(k)+"").contains("e")) {
									flag = false;
									break;
								}

							}
							if(flag) {
								ArrayList<String> list = follow.get(rs.charAt(j)+"");
								boolean res = merge(list, follow.get(key));
								if(res)
									change = true;
							}
							
						}
					}
						}
				}
			}
			
		}
		String res = "";
		for (int i = 0; i < variables.size(); i++) {
			ArrayList<String> targetArrayList = follow.get(variables.get(i));
			Collections.sort(targetArrayList);
			res+= variables.get(i)+"/";
			for(int j= 0;j<targetArrayList.size();j++)
				res+= targetArrayList.get(j);
			res+=";";
			
		}
		
		return res.substring(0,res.length()-1);
		
		
	}
	public static void main(String[] args) {
		CfgFirstFollow cfgFirstFollow= new CfgFirstFollow("S;Z;I;P;B;J;W#b;f;i;m;n;p;s#S/PZb,S,iBbB;Z/II,If,P;I/B,JZPP,SPnJS,SWsI,bBPb,iB;P/JWWfP,S,Ss,e;B/e,pBPBb,sSP;J/BmPZ,Z,iP;W/bZ,mPnWb,pWBfB");
		System.out.print(cfgFirstFollow.first());
	}

}
