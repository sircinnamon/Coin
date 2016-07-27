import java.util.LinkedList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
public class Coin
{
	//dailyprogrammer challenge 277 Intermediate - Fake Coins
	public static char[] coins; //indexes of these arrays correspond to each other
	public static String[] rules;
	public static int[] weights;
	public static void main(String[] args)
	{
		//take a filename
		String filename = args[0];
		String[] lines = new String[0];
		try{lines = read(filename);}catch(IOException e){e.printStackTrace();System.exit(1);}
		String coinList = "";
		for(String s : lines)
		{
			coinList = coinList + s.split(" [^ ]+$")[0].replace(" ","");
		}
		coinList = prune(coinList);
		//System.out.println(coinList);
		coins = new char[coinList.length()];
		rules = new String[coinList.length()];
		weights = new int[coinList.length()];
		for(int i = 0; i<coins.length;i++)
		{
			coins[i]=coinList.charAt(i);
			rules[i]="";
			weights[i]=1;
		}
		populateRules(lines,coins,rules);
		//System.out.println(Arrays.toString(rules).replace(",","\n"));
		while(weights[0] < 2 && !testAllRules(coins,rules,weights))
		{
			incrementWeight(weights);
		}
		boolean answer = testAllRules(coins,rules,weights);
		if(!answer){System.out.println("Contradiction in data.");}
		else{System.out.println("Possible solution:");}
		System.out.println(Arrays.toString(coins));
		System.out.println(Arrays.toString(weights));
	}

	public static boolean testAllRules(char[] coins, String[] rules, int[] weights)
	{
		for(int i = 0; i < coins.length; i++)
		{
			String ruleset = rules[i];
			for(String r : ruleset.split(" "))
			{
				//System.out.println(r);
				if(r.matches("[^a-zA-Z]*")){}
				else if(testRule(coins,coins[i],r,weights)==false){return false;}
			}
		}
		return true;

	}
	public static boolean testRule(char[] coins, char coin, String rule, int[] weights)
	{
		char operation = rule.charAt(0);
		int w = weight(""+coin,coins,weights);
		rule = rule.substring(1);
		int difference;
		if(rule.contains("-")){difference = weight(rule.split("-")[0],coins,weights)-weight(rule.split("-")[1],coins,weights);}
		else{difference = weight(rule,coins,weights);}

		if(operation == '>' && (w > difference)){return true;}
		if(operation == '<' && (w < difference)){return true;}
		if(operation == '=' && (w == difference)){return true;}
		return false;
	}
	public static String[] populateRules(String[] lines, char[] coins, String[] rules)
	{
		String left;
		String right;
		char balance;
		for(String l : lines)
		{
			left = l.split(" ")[0];
			right = l.split(" ")[1];
			if(l.split(" ")[2].equals("left")){balance = '>';}
			else if(l.split(" ")[2].equals("right")){balance = '<';}
			else{balance = '=';}

			for(char c : left.toCharArray())
			{
				int index = indexOf(c, coins);
				rules[index] = rules[index]+" "+balance+right+((left.length()==1)?"":("-"+left.replace(""+c,"")));
			}

			if(balance == '>'){balance='<';}
			else if(balance == '<'){balance='>';}
			for(char c : right.toCharArray())
			{
				int index = indexOf(c, coins);
				rules[index] = rules[index]+" "+balance+left+((right.length()==1)?"":("-"+right.replace(""+c,"")));
			}
		}
		return rules;
	}
	public static String[] read(String file) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		LinkedList<String> list = new LinkedList<String>();
		String x = br.readLine();
		while(x != null)
		{
			list.add(x);
			x = br.readLine();
		}
		return list.toArray(new String[list.size()]);
	}
	public static String prune(String s)
	{
		//remove duplicate chars
		String p = "";
		for(char c : s.toCharArray())
		{
			if(!p.contains(""+c)){p = p + c;}
		}
		return p;
	}
	public static int indexOf(char c, char[] chars)
	{
		int i = 0;
		while(i<chars.length && chars[i]!=c){i++;}
		return i;
	}
	public static int weight(String coins, char[] chars, int[] weights)
	{
		int w = 0;
		for(char c : coins.toCharArray())
		{
			w = w + weights[indexOf(c,chars)];
		}
		return w;
	}
	public static void incrementWeight(int[] weight)
	{
		for(int i = weight.length-1; i >= 0; i--)
		{
			if(weight[i]==1){weight[i]=0;break;}
			else if(weight[i]==0 && i == 0){weight[0]=2;}
			else{weight[i]=1;}
		}
	}
}