package homework3;

import java.util.HashMap;

import util.general.CharacterFromFileReader;

public class Hashmaps {
	public HashMap<String, String> hashh(String inputFile){
		HashMap<String, String> hm = new HashMap<String,String>();
		CharacterFromFileReader cffr = new CharacterFromFileReader(inputFile);
		String a =new String();
		String b = new String();
		a = "";
		b = "";
		while(cffr.hasNext()){
			char c = cffr.next();
			if(c != ':'){
		    a = a+c;
			}
			if(c == ':'){
				break;
			}
		while(cffr.hasNext()){
				char d = cffr.next();
				if(d == '\n'){
					hm.put(a,b);
				}
				else if(d != '\n'){
					b = b+d;
					}
				}
			}	
		
		return hm;	
	}
}
