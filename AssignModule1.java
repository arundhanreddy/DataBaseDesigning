import java.util.Scanner;
import java.lang.Math;

class Attr
{
	public StringBuffer attributes;
	public int n; //number of attributes.
	public String[] lhs;
	public String[] rhs;
	public String[] nf_list;

	public int k=0;   // no. of F closure functional dependencies.

	public String[] keys; // list of all keys.
	public String keyAttributes; // contains all the key attributes. useful while finding the normal form.
	public String[] attr_cmbns;

	

	public Attr[] decomposed; // this array contains the relations pertaining to the decomposed relation.

	private String sort(String s)
	{
		//System.out.println("input String is " + s);
		char[] c = s.toCharArray();
		char temp;
		int min;
		int length = s.length();
		for(int i=0; i<length; i++)
		{
			for(int j=i+1; j<length; j++)
			{
				if(c[j]<c[i])
				{
					temp = c[i];
					c[i] = c[j];
					c[j] = temp;
				}
			}
		}
		s = new String(c);
		//System.out.println("output String is " + s);
		return s;
	}

	private String removeDuplicates(String t) //removes duplicates with in each FD. eg: if i/p is 'ABAAC', then o/p is 'ABC'.
	{
		StringBuffer temp = new StringBuffer();
		temp.append(t);

		int v = t.length(),g;
		for(int i=0; i<v; i++)
		{
			for(g=i+1; g<v; g++)
			{
				if(temp.charAt(i) == temp.charAt(g))
				{
					temp.deleteCharAt(g);
					v--;
					g--;
				}
			}
		}
		return temp.toString();
	}

	public void removeRedundantFDs(String[] lhs, String[] rhs) // because of this operation, some null spaces will be created.
	{
	
		for(int i=0; i<k; i++)
		{
			try
			{
				if(lhs[i].contains(rhs[i]))
				{
					lhs[i] = null;
					rhs[i] = null;
				}
				else
				{
					for(int j=0; j<k; j++)
					{
						try
						{
							if(i != j)
							{
								if(rhs[i].equals(rhs[j]) && lhs[j].contains(lhs[i]))
								{
									lhs[j] = null;
									rhs[j] = null;
								}
							}
						}
						catch(NullPointerException e)
						{
							;
						}
					}
				}
			}
			catch(NullPointerException e)
			{
				;
			}
		}
	}

	public void removeRedundantFDs(String[] lhs,String[] rhs, int x) // because of this operation, some null spaces will be created.
	{
		for(int i=0; i<k; i++)
		{
			try
			{
				if(!attributes.toString().contains(rhs[i]))
				{
					lhs[i] = null;
					rhs[i] = null;
					if(x ==1)
						nf_list[i] = null;
				}
				else
				{
					for(int j=0; j<lhs[i].length(); j++)
					{
						if(!attributes.toString().contains(""+lhs[i].charAt(j)))
						{
							lhs[i] = null;
							rhs[i] = null;
							if(x==1)
								nf_list[i] = null;
							break;
						}
					}
				}
			}
			catch(NullPointerException e)
			{
				;
			}
		}
	}

	public void removeNullSpaces(String[] lhs, String[] rhs, int x)
	{
		int g;
		for(g=0; g<k; g++) // iterate through array 'lhs' till you find first null and remember that position. 
		{
			if(lhs[g] == null)
			{
				break;
			}
		}
		for(int i = g+1; i<k; i++)
		{
			if(lhs[i]!=null)
			{
				lhs[g] = new String(lhs[i]);
				rhs[g] = new String(rhs[i]);
				if(x == 1)
				{
					nf_list[g] = new String(nf_list[i]);
					nf_list[i] = null;
				}
				lhs[i] = null;
				rhs[i] = null;

				g++;
			}
		}
		k = g; //Updated number of FDs
	}

	public void fdClosure(String[] lhs, String[] rhs)  // calculating extra FDs by repeatedly applying pseudo-transitive property.(iR6)
	{
		int i,j;
		for(i=0; i<k; i++)
		{ 
			for(j=0; j<k; j++)
			{
				if(i!=j)
				{
					if(lhs[j].contains(rhs[i])) // i&j
					{

						String templhs = sort(removeDuplicates(lhs[j].replace(rhs[i],lhs[i])));

						lhs[k] = new String(templhs);
						rhs[k] = new String(rhs[j]);
						k++;
					}
					if(lhs[i].contains(rhs[j])) // j&i
					{
						String templhs = sort(removeDuplicates(lhs[i].replace(rhs[j],lhs[j])));

						lhs[k] = new String(templhs);
						rhs[k] = new String(rhs[i]);
						k++;
					}
				}
				removeRedundantFDs(lhs,rhs);
				removeNullSpaces(lhs,rhs,0);
			}
		}
	}

	public void fillFDs(String[] arr)
	{
		int i=0;
		for(; k<(5*n); k++)
		{
			int y = 0;
			if(!arr[i].contains("->"))
			{
				break;
			}
			else
			{
				String[] temps = arr[i].split("->");

				temps[0] = sort(temps[0]);  // sorts the inputted string. eg: i/p = ADCB, o/p = ABCD.
				y = temps[1].length();
				for(int j=0; j<y; j++)
				{ 
					lhs[k+j] = new String(temps[0]);
					rhs[k+j] = new String(temps[1].substring(j,j+1));
					//System.out.println(lhs[k+j] + " " + rhs[k+j]);
				}
			}
			k = k+y-1;
			i++;
		}
		//System.out.println(k);
	}

	public void printFDs(String[] lhs,String[] rhs,int x)
	{
		for(int i=0; i<k; i++)
		{
			try
			{
				System.out.print("	" + lhs[i]+"->"+rhs[i]);
				if(x == 1)
				{
					System.out.println(" "+nf_list[i]);
				}
				else
				{
					System.out.println("");
				}
			}
			catch(NullPointerException e)
			{
				; // do nothing just let it continue. 
			}
		}
		return;
	}

	public void fillAttrCmbns()
	{
		for(int w=1; w<n; w++)
		{
			b = new StringBuffer();
			nChooseP(w,0);
		}
	}
	public int r=0; // useful while storing the buffer in attr_cmbns.
	private void store(StringBuffer c)
	{
		attr_cmbns[r] = c.toString();
		r++;
	}

	public StringBuffer b = new StringBuffer(); // useful while writing the combinations.
	public void nChooseP(int p,int i)   // here p means "choose all possible combinations of substrings of length 'p' from attributes list."
										//  i means whether we have to choose ith attribute or not.
	{
		if(p!=1)
	    {
	        b.append(attributes.charAt(i));
	        nChooseP(p-1,i+1);
	        b.deleteCharAt(b.length()-1);
	    }
	    else if(p==1)
	    {
	        int u;
	        for(u=i; u<n; u++)
	        {
	            b.append(attributes.charAt(u));
	            store(b);
	            b.deleteCharAt(b.length()-1);
	        }
	        return;
	    }
	    if(i<=n-p)
	    {
	        nChooseP(p,i+1);
	    }
	    else if(i == n-p)
	    {
	        int u;
	        for(u=i;u<n;u++)
	        {
	            b.append(attributes.charAt(u));
	        }
	        store(b);
	        for(int x=i; x<n; x++)
	        {
	        	b.deleteCharAt(b.length()-1);
	        }
	        return;
	    }
	}

	public int h = 0; //useful while filling key list.
	private String getCombination()
	{
		if(h == Math.pow(2,n)-2) {return "zz";} // done with all combinations.

		boolean r=true;
		String comb = new String(attr_cmbns[h]);
		for(int i=0; i<l; i++)
		{
			String key = new String(keys[i]);
			if(comb.contains(key))
			{
				r = false; // => the String comb is not eligible for becoming a minimal super key.
				break;
			}
		}
		if(r)
		{
			h++;
			return comb;
		}
		else
		{
			h++;
			comb = new String(getCombination());
			return comb;
		}
	}

	public void printKeys()
	{
		for(int i=0; i<n; i++)
		{
			try
			{
				String d = new String(keys[i]);
				System.out.print("	");
				System.out.print(i+1);
				System.out.println(". " + d);
			}
			catch(NullPointerException e)
			{
				; // do nothing just let it continue. 
			}
		}
		return;
	}
	public int l = 0; //number of keys found till now.
	public void fillKeyList()
	{
		String comb = new String(getCombination()); // this will return a combination.Eg: if AB is a key then this will not return ABC,ABD... beacause they are not minimal keys anymore.
		while(!comb.equals("zz"))
		{
			String buffer = new String(attributes);  // here buffer is of type String and not StringBuffer.
			for(int i=0; i<k; i++)
			{
				if(comb.contains(lhs[i]))
				{
					buffer = new String(buffer.replaceFirst(rhs[i],""));
				}
			}
			buffer = new String(buffer.replaceFirst(comb,""));//every key determines itself.
			if(buffer.isEmpty()) // this implies our comb have implied all attributes and thus this is a valid key.
			{
				keys[l] = new String(comb);
				l++;
			}
			comb = new String(getCombination());
		}
		StringBuffer temp = new StringBuffer();
		for(int i=0; i<l; i++)
		{
			temp = temp.append(keys[i]);
		}
		keyAttributes = removeDuplicates(temp.toString());
	}

	/*public void printAttrCombns()
	{
		for(int i=0; i<(Math.pow(2,n)-2); i++)
		{
			try
			{
				System.out.print(i+1);
				System.out.println(". " + attr_cmbns[i]);
			}
			catch(NullPointerException e)
			{
				; // do nothing just let it continue. 
			}
		}
		return;
	}*/

	public boolean isaKey(String s)
	{
		for(int i=0; i<l; i++)
		{
			if(s.equals(keys[i])) return true;
		}
		return false;
	}

	private boolean isAKeyAttribute(String s)
	{
		for(int i=0; i<s.length(); i++)
		{
			if(!keyAttributes.contains(""+s.charAt(i))) {return false;}
		}
		return true;
	}
	public void fillNormalFormList()
	{
		for(int i=0; i<k; i++)
		{
			if(isaKey(lhs[i]))
			{
				nf_list[i] = "BC";
			}
			else if (isAKeyAttribute(lhs[i]))
			{
				if(isaKey(rhs[i])){nf_list[i] = "3";}
				else if (isAKeyAttribute(rhs[i])) {nf_list[i] = "3";}
				else {nf_list[i] = "1";}
			}
			else
			{
				if(isaKey(rhs[i])){nf_list[i] = "3";}
				else if(isAKeyAttribute(rhs[i])) {nf_list[i] = "3";}
				else {nf_list[i] = "2";}
			}
		} 
	}
	public StringBuffer nfOfDecomposed = new StringBuffer(""); 
	public String getNF() // this method uses list_Nf  to determine in which Nf the relation is in. and prints that relation.
	{
		char temp = 'B';
		char t = '\0';

		for(int i=0; i<k; i++)
		{
			if(nf_list[i].charAt(0) < temp)
			{
				temp = nf_list[i].charAt(0);
			}
		}
		// if(temp == 'B')
		// 	t = 'C';
		return Character.toString(temp);
	}

	private int p = 1;//no of tables in decomposed rel. = 1(default).

	public void decomposeRel() // this maintains 'dependency preserving ness' and 'lossless ness'.
	{
		for(int i=0; i<k; i++)
		{
			if(nf_list[i] == "1" || nf_list[i] == "2") // a set of key attributes is determining a non key attribute. the result is BCNF (mostlikely.)
			{
				int position = decomposed[0].attributes.indexOf(rhs[i]);
				if(position!= -1)
				{
					decomposed[0].attributes.deleteCharAt(position);
				}
				decomposed[0].lhs[i] = null;
				decomposed[0].rhs[i] = null;
				decomposed[0].nf_list[i] = null;

				boolean g = true;
				/*for(int j=1; j<p; j++)
				{
					if(decomposed[j].isaKey(lhs[i]))
					{
						decomposed[j].attributes.append(rhs[i]);

						System.out.println(j);
						System.out.println(decomposed[j].k);
						decomposed[j].lhs[k] = new String(lhs[i]);
						decomposed[j].rhs[k] = new String(rhs[i]);
						decomposed[j].k++;
						g = false;
						break;
					}
				}*/
				if(g)
				{
					decomposed[p] = new Attr();
					decomposed[p].attributes = new StringBuffer();

					decomposed[p].attributes.append(lhs[i]);
					decomposed[p].attributes.append(rhs[i]);

					String[] s = new String[2];
					s[0] = new String(lhs[i] + "->" + rhs[i]);
					s[1] = new String("done");
					decomposed[p].completeFormalities(s,"don't print");

					p++;
				}
			}
			else if(nf_list[i]  == "3") // we can decompose 3NF losslessly only if atleast one non-key attribute is on LHS.
			{
				if(!isAKeyAttribute(lhs[i]))
				{
					for(int j=0; j<lhs[i].length(); j++)
					{
						if(!keyAttributes.contains(""+lhs[i].charAt(j)))
						{
							decomposed[0].attributes.deleteCharAt(j);
							decomposed[0].lhs[i] = null;
							decomposed[0].rhs[i] = null;
							decomposed[0].nf_list[i] = null;
							decomposed[0].removeNullSpaces(decomposed[0].lhs,decomposed[0].rhs,1);
							j--;
						}
					}
					int r = -1; // we are not checking if there is already a table existing with the given key.
					if( r < 0)
					{
						decomposed[p] = new Attr();
						decomposed[p].attributes = new StringBuffer();

						decomposed[p].attributes.append(lhs[i]);
						decomposed[p].attributes.append(rhs[i]);

						String[] s = new String[2];
						s[0] = new String(lhs[i] + "->" + rhs[i]);
						s[1] = new String("done");

						decomposed[p].completeFormalities(s,"don't print");
						p++;
					}
					/*else
					{
						decomposed[r].attributes.append(rhs[i]);

					}*/
					
				}
			}
			else if(nf_list[i] == "BC")
			{
				;// this is maximum.
			}
		}
		decomposed[0].removeNullSpaces(decomposed[0].lhs,decomposed[0].rhs,1);
		decomposed[0].removeRedundantFDs(decomposed[0].lhs,decomposed[0].rhs,1);
		decomposed[0].removeNullSpaces(decomposed[0].lhs,decomposed[0].rhs,1);
	}

	public void printRel()
	{
		for(int i=0; i<p; i++)
		{
			int j = i+1;
			System.out.println(j+ ". " + decomposed[i].attributes);
			System.out.println("	the keys:");
			decomposed[i].printKeys();
			System.out.println("	the FDs:");
			decomposed[i].printFDs(decomposed[i].lhs,decomposed[i].rhs,1);
			System.out.print("	");
			nfOfDecomposed.append(decomposed[i].getNF());
			System.out.println("the relation is in " + nfOfDecomposed.charAt(nfOfDecomposed.length()-1) + "NF");
		}
	}

	public void completeFormalities(String[] xyz,String task)
	{
		n = attributes.length();
		lhs = new String[5*(n)]; 
		rhs = new String[5*(n)];
		keys = new String[n];  //assuming there will not be more than n keys.
		keyAttributes = new String();

		fillFDs(xyz);

		removeRedundantFDs(lhs,rhs);
		removeNullSpaces(lhs,rhs,0);

		fdClosure(lhs,rhs);

		removeRedundantFDs(lhs,rhs);
		removeNullSpaces(lhs,rhs,0);


		attr_cmbns = new String[(int)(Math.pow(2,n)-2)]; // contains all 2**n-2 combinations of attributes.
		fillAttrCmbns();

		//System.out.println("here are all combinations:");
		//printAttrCombns();

		fillKeyList();
		
		if(task.equals("print"))
		{
			System.out.println("here are all minimal keys:");
			printKeys();
		}
		
		nf_list = new  String[k]; // a list that shows which FD is in which NF.
		fillNormalFormList();

		if(task.equals("print")) 
		{
			System.out.println("here is F closure with corresponding NF:");
			printFDs(lhs,rhs,1);
		}

		//System.out.println("here are keyAttributes:");
		//System.out.println(keyAttributes);

		if(task.equals("print"))
		{
			String g = new String(getNF());
			System.out.println("the relation is in " + g + "NF");
		}
	}

	public boolean calculateFDClosureFromDecomposedAndCompareWithOriginal()
	{
		String[] decomposedLhs = new String[5*n];
		String[] decomposedRhs = new String[5*n];
		int w = 0;
		for(int i=0; i<n; i++)
		{
			try
			{
				for(int j=0; j<5*decomposed[i].n; j++)
				{
					try
					{
						decomposedLhs[w] = new String(decomposed[i].lhs[j]);
						decomposedRhs[w] = new String(decomposed[i].rhs[j]);
						w++;
					}
					catch(NullPointerException e)
					{
						;
					}
				}
			}
			catch(NullPointerException e)
			{
				break;
			}			
		}
		removeNullSpaces(decomposedLhs,decomposedRhs,0);
		fdClosure(decomposedLhs,decomposedRhs);
		removeRedundantFDs(decomposedLhs,decomposedRhs,0);
		removeNullSpaces(decomposedLhs,decomposedRhs,0);

		System.out.println("		");
		System.out.println("the FDclosure corresponding to the decomposed relations is:");
		printFDs(decomposedLhs,decomposedRhs,0);

		System.out.println("now, comparison in progress");
		int noOfDecomposedFds = 0;
		for(int i=0; i<5*n; i++)
		{
			if(decomposedLhs[i] == null)
			{
				break;
			}
			noOfDecomposedFds++;
		}

		if(noOfDecomposedFds == k)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}

class AssignModule1
{
	public static String[] inputFDs(Scanner s,int w)
	{
		String[] s_array;
		s_array = new String[5*w];
		System.out.print("Enter FDs one by one.(eg: 'ABC->DE').print'DONE' when done entering.:\n");
		for(int i=0; i<(5*w); i++)
		{
			String temp = new String(s.next());//"ABC->CDE";
			s_array[i] = new String(temp);
			if(!temp.contains("->"))
			{
				break;
			}
		}
		return s_array;
	}

	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		Attr obj = new Attr();
		Syatem.out.println("(Since we are testing only till BCNF, multivalued attributes like emp-proj,emp-chidren_names,emp -phonenumbers etc...are not allowed.)\n");
		System.out.println(" enter attributes(Eg: ABCDEFG):");
		obj.attributes = new StringBuffer();
		obj.attributes.append(s.next());	  //contains all attributes.
		obj.n = obj.attributes.length();

		String[] s_array = inputFDs(s,obj.n);
		obj.completeFormalities(s_array,"print"); 			/* 1. computes FD closure
															   2. computes minimal keys.
															   3. computes the Normal form of the relation.
															   4. prints all the above*/

		obj.decomposed = new Attr[(int)(obj.n)];//assuming there will not be more than n tables in an decomposed relation.

		obj.decomposed[0] = new Attr();
		obj.decomposed[0].attributes = new StringBuffer(obj.attributes);
		obj.decomposed[0].completeFormalities(s_array,"don't print");


		obj.decomposeRel();
		System.out.println("the relations obtained after decomposition  are as follows: ");
		obj.printRel();

		//printing the updated overall normal form
		char temporary = obj.nfOfDecomposed.charAt(0);
		char t = '\0';
		for(int i=0; i<obj.nfOfDecomposed.length(); i++)
		{
			if(obj.nfOfDecomposed.charAt(0) < temporary)
			{
				temporary = obj.nfOfDecomposed.charAt(0);
			}
		}
		if(temporary == 'B')
				t = 'C';
		System.out.println("The overall normal form of the decomposed relation is " + temporary+ t + "NF");
		boolean hi = obj.calculateFDClosureFromDecomposedAndCompareWithOriginal();
		if(hi)
		{
			System.out.println("the number of fdclosures are equal and they are same. therefore the decomposition is dependency preserving.");
			System.out.println("decomposition is successfull." );
		}
		else
		{
			System.out.println("there is some error in decomposition. getting different Fd closures. i.e, the proposed decompositon is not dependency preserving.");
		}
	}
}