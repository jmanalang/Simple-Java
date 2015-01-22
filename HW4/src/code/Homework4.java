package code;

import java.util.HashMap;

import util.general.CharacterFromFileReader;

public class Homework4 {

	public HashMap <String, Integer> authorFinder (String inputFilePath){
		CharacterFromFileReader reader = new CharacterFromFileReader(inputFilePath);
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		int state = 0;
		String author = "";
		while(reader.hasNext()){
			char c = reader.next();
			switch (state){
				case(0):
					if(c == '{'){
						state = 1;
					}
					break;
				case(1):
					if(c == 'A'){
						state = 2;
					}
					break;
				case(2):
					if(c == '}'){
						state = 3;
					}
					break;
				case(3):	
					if(c == '{'){
						state = 4;
					}
					else{
						author = author + c;
					}
					break;

				case(4):
					if(c == 'a'){
						state = 5;
					}
					else {
						author = author + '{' + c;
						state = 3;
					}
					break;
				case(5):
					if(c == '}'){
						state = 6;
					}
					else {
						author = author + 'a' + c;
						state = 4;
					}
					break;
				case(6):
					if(map.containsKey(author)){
						map.put(author, map.get(author)+1);
					}
					else if (!map.containsKey(author)){
					map.put(author,1);
					}
					else {
					state = 0;
					}
					break;
			}
			
		}
	return map;
	}
	
	
}
